package pub.ayada.insight.core.ds;

public class Record {

	Object[] columns;

	public Record(int Count) {
		this.columns = new Object[Count];
	}

	public Record(Object[] Columns) {
		this.columns = Columns;
	}
	public Record(Object Column) {
		this(1);
		this.columns[0] = Column;
	}

	/** Return the record (array of columns)
	 * @return record  - Record
	 */
	public Record getRecord() {
		return this;
	}

	/** Updates the Records with new the list of columns passed.
	 * @param Columns - Object[]
	 */
	public void setColumns(Object[] Columns ) {
		this.columns = Columns;
	}

	/** Updates the column value at the specified index
	 * @param Index    - int 
	 * @param NewVal - Object
	 */
	public void setColumn(int Index, Object NewVal) {
		this.columns[Index] = NewVal;
	}

	/** Returns the columns value of the specified index
	 * @param Index - int
	 * @return Column Value - Object
	 */
	public Object getColum(int Index) {
		return this.columns[Index];
	}
    
	/** Returns the String representation of the columns with commaSeparated 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < this.columns.length; i++)
			if (this.columns[i] == null)
				b.append(",");
			else
				b.append(this.columns[i].toString()).append(",");
        b.trimToSize();
		b.setLength(b.length() - 1);
		return b.toString();
	}
}
