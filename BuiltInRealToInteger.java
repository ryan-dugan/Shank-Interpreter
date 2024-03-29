package shank_interpreter;


import java.util.ArrayList;

import shank_interpreter.RuntimeErrorException;


public class BuiltInRealToInteger extends FunctionNode {

	public BuiltInRealToInteger(ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/*RealToInteger someReal, var someInt
		someInt = truncate someReal (so if someReal = 5.5, someInt = 5)*/
		
		float value;
		int result;
		
		RealDataType input = null;
		input = (RealDataType) list.get(0);
		
		//check for valid IDT
		if(input == null)
			throw new RuntimeErrorException("IDT not valid for input float in built-in function RealToInteger.");
		
		value = input.getValue();
		result = (int) value;
		IntegerDataType output = new IntegerDataType(result);
		
		list.remove(1);
		list.add(output);
		
	}

}
