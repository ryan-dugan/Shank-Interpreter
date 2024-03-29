package shank_interpreter;


public class RealDataType extends InterpreterDataType {

	private float value, from, to;
	
	public RealDataType(float value) {
		this.value = value;
	}
	
	public RealDataType() {
		this.value = (Float) null;
	}

	public RealDataType(float from, float to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
}
