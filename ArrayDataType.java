package shank_interpreter;


import java.util.Arrays;

public class ArrayDataType extends InterpreterDataType {

	private InterpreterDataType[] array;
	int from, to;
	
	public ArrayDataType(InterpreterDataType[] array, int from, int to) {
		this.array = array;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(array);
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}
	
	public InterpreterDataType[] getValue() {
		return array;
	}

}
