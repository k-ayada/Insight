package pub.ayada.insight.core.ds;

import java.util.ArrayList;

public class RecordMeta {

	private ArrayList<String[]> columns= new ArrayList<String[]>();

	public RecordMeta(int ColumnCount) {
		this.columns = new ArrayList<String[]>(ColumnCount);
		for (int i = 1; i < ColumnCount; i++)
			this.columns.add(i, new String[2]);
	}

	public RecordMeta(ArrayList<String[]> Columns) {
		this.columns = Columns;
	}

	public RecordMeta() {
	}

	/** Inserts the column info at the specified index. </br>If other column info found at that index, the list is adjusted. 
	 * @param Index - int
	 * @param Data  - String[2] [0] - Column Name [1] - Data Type
	 */
	public void addColumn(int Index, String[] Data) {
		if (Index >= this.columns.size() || this.columns.get(Index) == null) {
			this.columns.add(Index, Data);
		} else {
			ArrayList<String[]> tmp = new ArrayList<String[]>(this.columns.size() + 1);
			for (int i = 0; i < Index - 1; i++) {
				tmp.add(this.columns.get(i));
			}
			tmp.add(Index, Data);
			for (int i = Index; i < Index; i++) {
				tmp.add(this.columns.get(i));
			}
			this.columns = tmp;
		}
	}
	/** Inserts the column info at the specified index. </br>If other column info found at that index, the list is adjusted. 
	 * @param Index - int
	 * @param Data  - String[2] [0] - Column Name [1] - Data Type
	 */
	public void addColumn(int Index, String ColumnName, String DataType) {		
		addColumn(Index,new String[] {ColumnName, DataType});
	}	
	/** Inserts the column info at the end of the list. 
	 * @param Data  - String[2]
	 */	
	public void addColumn(String[] Data) {
			this.columns.add( Data);
	}	
	/** Updates the column name at the specified index..
	 * @param Index - int 
	 * @param Name  - String 
	 */	
	public void setColumnName(int Index, String Name) {
		this.columns.get(Index)[0] = Name;
	}
	/** Updates the column's Data Type at the specified index..
	 * @param Index - int 
	 * @param Datatype  - String 
	 */	
	public void setColumnType(int Index, String Datatype) {
		this.columns.get(Index)[1] = Datatype;
	}
	/** Returns the Column Name at the specified index 
	 * @param Index - int 
	 * @return Name  - String 
	 */	
	public String getColumnName(int Index) {
		return this.columns.get(Index)[0];
	}
	/** Returns the Column's Data Type at the specified index 
	 * @param Index - int 
	 * @return Datatype  - String 
	 */	
	public String getColumnType(int Index) {
		return this.columns.get(Index)[1];
	}
	
	/**
	 * Return the index of the column for the input name
	 * @param name
	 * @return index - index of the column (if not found -1)
	 * @throws Exception 
	 */
	public int getIndexOfcolumn(String name) throws Exception {
		int res = -1;
		int i=0;
		try{
			for ( String[] a : this.columns) {
				if (name.equals(a[0])) {
					res = i;
					break;
				}
				i++;
			}
		} catch (Exception e) {
			throw new Exception ("Failed to get the index of the column: " + name, e );
		}
		
		return res;
	}
}
