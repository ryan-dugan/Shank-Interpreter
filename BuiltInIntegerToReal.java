package shank_interpreter;


import java.util.ArrayList;

import shank_interpreter.RuntimeErrorException;


public class BuiltInIntegerToReal extends FunctionNode {

	public BuiltInIntegerToReal (ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/*IntegerToReal someInteger, var someReal
		someReal = someInteger  (so if someInteger = 5, someReal = 5.0)*/
		
		int value;
		float result;
		
		IntegerDataType input = null;
		input = (IntegerDataType) list.get(0);
		
		//check for valid IDT
		if(input == null)
			throw new RuntimeErrorException("IDT not valid for input integer in built-in function IntegerToReal.");
		
		value = input.getValue();
		result = (float) value;
		RealDataType output = new RealDataType(result);
		
		list.remove(1);
		list.add(output);
		
	}

}
