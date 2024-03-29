package shank_interpreter;


public class BooleanDataType extends InterpreterDataType {

	boolean value;
	
	public BooleanDataType(boolean value) {
		this.value = value;
	}
	
	public BooleanDataType() {
		this.value = (Boolean) null;
	}

	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}

}
