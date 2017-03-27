package pub.ayada.insight.component;

public class ComponentData {
	private String ComponentID, SourceComponent, DestComponent;

	public ComponentData() {
		this.ComponentID = null;
		this.SourceComponent = null;
		this.DestComponent = null;
	}

	public ComponentData(String componentID, String sourceComponent, String destComponent) {
		super();
		ComponentID = componentID;
		SourceComponent = sourceComponent;
		DestComponent = destComponent;
	}

	public String getComponentID() {
		return ComponentID;
	}

	public void setComponentID(String componentID) {
		ComponentID = componentID;
	}

	public String getSourceComponent() {
		return SourceComponent;
	}

	public void setSourceComponent(String sourceComponent) {
		SourceComponent = sourceComponent;
	}

	public String getDestComponent() {
		return DestComponent;
	}

	public void setDestComponent(String destComponent) {
		DestComponent = destComponent;
	}

}
