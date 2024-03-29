package shank_interpreter;


public class BooleanNode extends Node {
	
	private boolean value;

	public BooleanNode (boolean value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "" + value;
	}

	public boolean getValue() {
		return value;
	}
	
	
	
}
