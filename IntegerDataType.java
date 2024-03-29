package shank_interpreter;


public class IntegerDataType extends InterpreterDataType {

	private int value;
	private int from, to;
	
	//variable
	public IntegerDataType(int value) {
		this.value = value;
	}
	
	//variable with type limit
	public IntegerDataType(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	//constant
	public IntegerDataType() {
		this.value = 0;
	}

	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}

	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

}
