package shank_interpreter;


import java.util.ArrayList;

import shank_interpreter.RuntimeErrorException;


public class BuiltInSquareRoot extends FunctionNode {

	public BuiltInSquareRoot(ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/*SquareRoot someFloat, var result
		Result = square root of someFloat*/
		
		float input;
		float result;
		RealDataType rdt = null;
		IntegerDataType intdt = null;
		
		//can accept either a float or an int (done this way to be able to use getValue() function)
		rdt = (RealDataType) list.get(0);
		intdt = (IntegerDataType) list.get(0);
		
		//check for valid IDT and get value
		if(rdt == null && intdt == null)
			throw new RuntimeErrorException("IDT is not valid for built-in SquareRoot function.");
		else if(rdt == null)
			input = (float) intdt.getValue();
		else
			input = rdt.getValue();
		
		result = (float) Math.sqrt(input);
		
		RealDataType resultIDT = new RealDataType(result);
		
		list.remove(1);
		list.add(resultIDT);
		
	}

}
