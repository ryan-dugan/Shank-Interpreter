package shank_interpreter;


import java.util.ArrayList;

import shank_interpreter.RuntimeErrorException;


public class BuiltInStart extends FunctionNode {

	public BuiltInStart (ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/*Start var start
		start = the first index of this array*/
		
		InterpreterDataType[] array;
		int index;
		
		ArrayDataType adt = null;
		adt = (ArrayDataType) list.remove(0);
		
		if(adt == null)
			throw new RuntimeErrorException("Invalid IDT for input array in built-in Start function.");
		
		array = adt.getValue();
		
	}

}
