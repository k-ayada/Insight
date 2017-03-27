package pub.ayada.insight.runner;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pub.ayada.dataStructures.queues.CQueue;
import pub.ayada.insight.core.Component;
import pub.ayada.insight.core.ds.Record;
import pub.ayada.insight.core.stats.ExeStats;

public class XMLProcess {
	static Logger L = LoggerFactory.getLogger(XMLProcess.class);
	ExeStats ExeStats = new ExeStats();
	HashMap<String, HashMap<String, CQueue<Record>>> Queues = new HashMap<String, HashMap<String, CQueue<Record>>>();
	NodeList nList;
	ArrayList<Thread> thList;
	

	XMLProcess(String configFile) throws ParserConfigurationException, SAXException, IOException {
		File xmlFile = new File(configFile);
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		this.nList = doc.getElementsByTagName("COMPONENT");
		initEvenVars(doc.getElementsByTagName("GLOBALS").item(0));
	}

	private void initEvenVars(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) node;
			NodeList children = eElement.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
					String chld = children.item(i).getNodeName();
					L.info(String.format("%-15s: %s",chld,children.item(i).getTextContent()));
					switch (chld.toUpperCase()) {
					case "LOG_INTERVEL":
						this.ExeStats.setReportAfter(Long.parseLong(children.item(i).getTextContent()));
						break;
					case "TEMP_DIR":
						System.setProperty("java.io.tmpdir", children.item(i).getTextContent());
						break;
					}
				}
			}
		}
	}
	public void start() throws Exception {
		for (int i = 0; i < this.nList.getLength(); i++) {
			String pkg = this.nList.item(i).getAttributes().getNamedItem("ROOT_PKG").getTextContent().trim();
			Class<?> classJAXB = Class.forName(pkg + ".cfg.JAXB");
			Class<?> classComp = Class.forName(pkg + ".Component");

			NodeList childList = this.nList.item(i).getChildNodes();

			for (int j = 0; j < childList.getLength(); j++) {
				Node childNode = childList.item(j);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					//print(i, childNode, classJAXB);
					startCOMPONENT(childNode, classJAXB, classComp);
				}
			}
		}
		
		this.ExeStats.getStepThreads().keySet().forEach( key -> {
			Thread t = this.ExeStats.getStepThread(key); 
			try {
				if (t.isAlive())
					t.join();
			} catch (InterruptedException e) {	
				interruputChildren();
			}
		});
	}

	private void interruputChildren() {
		this.ExeStats.getStepThreads().keySet().forEach(key -> {
			Thread t = this.ExeStats.getStepThread(key);
			if (t.isAlive())
				t.interrupt();

		});

	}

	private void startCOMPONENT(Node node, Class<?> classJAXB, Class<?> classComp) throws Exception {

		JAXBContext jaxbContext = JAXBContext.newInstance(classJAXB);
		Unmarshaller um = jaxbContext.createUnmarshaller();
		Component comp = (Component) classJAXB.cast(um.unmarshal(node));
		this.ExeStats.addStepStats(comp.getComponentName());
		
		ArrayList<String> parents = comp.getParentNames();
		
		boolean ignore= false;
		for (String parent : parents) {
			if (this.ExeStats.getStepStats(parent).getStepStatus() < 0){
				ignore =true;
				break;
			}
		}
		if (ignore) {
			L.info("Ignoring the Component: " + comp.getComponentName());
			this.ExeStats.getStepStats(comp.getComponentName()).setStepStatus(-1);
		} else {
			Constructor<?> ctor = classComp.getConstructor(classJAXB, this.ExeStats.getClass(), this.Queues.getClass());
			Object object = null;
			Thread componentThread = null;
			try {
				object = ctor.newInstance(classJAXB.cast(comp), this.ExeStats, this.Queues);
				componentThread = new Thread(Runnable.class.cast(object), comp.getComponentName());

				while (true) {
					boolean sleep = false;
					for (String parent : parents) {
						if (this.ExeStats.getStepThread(parent) != null && this.ExeStats.getStepThread(parent).isAlive()
								&& this.ExeStats.getStepStats(parent).getStepStatus() == 0) {
							sleep = true;
							break;
						}
					}
					if (sleep) {
						Thread.sleep(1000);
					} else {
						break;
					}
				}
			} catch (Exception e) {
				L.error("Failed to create new instance of " + classComp.getName());
				e.getCause().printStackTrace();
			}
			L.info("Strating the Component: " + componentThread.getName());
			componentThread.start();
			this.ExeStats.addStepThread(comp.getComponentName(), componentThread);
		}
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			throw new Exception("Please pass the path to the jobconfig XML as agrument 1");
		}
		XMLProcess mn = new XMLProcess(args[0]);
		mn.start();

	}

	@SuppressWarnings({ "rawtypes", "unused"})
	private static void print(int i, Node Node, Class clazz) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller um = jaxbContext.createUnmarshaller();
		Object JAXB = um.unmarshal(Node);

		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(clazz.cast(JAXB), sw);

		System.out.println(i + ". class:" + clazz.getName() + "\n XML:\n" + sw.toString() + "\n");
	}

}
