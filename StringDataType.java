package shank_interpreter;


public class StringDataType extends InterpreterDataType {

	String value;
	int from, to;
	
	public StringDataType(String value) {
		this.value = value;
	}
	
	public StringDataType() {
		this.value = null;
	}

	public StringDataType(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public String toString() {
		return ""+ value;
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
