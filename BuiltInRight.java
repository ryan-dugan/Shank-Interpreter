package shank_interpreter;


import java.util.ArrayList;

import shank_interpreter.RuntimeErrorException;


public class BuiltInRight extends FunctionNode {
	
	ArrayList<InterpreterDataType> list;
	
	public BuiltInRight(ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/* Right someString, length, var resultString
		ResultString = last length characters of someString*/
		
		String someString;
		int length;
		String resultString;
		
		StringDataType idtSomeString = null;
		IntegerDataType idtLength = null;
		
		idtSomeString = (StringDataType) list.get(0);
		idtLength = (IntegerDataType) list.get(1);
		
		if(idtSomeString == null)
			throw new RuntimeErrorException("IDT not valid for input string in built-in Right function.");
		if(idtLength == null)
			throw new RuntimeErrorException("IDT not valid for length in built-in Right function.");
				
		someString = idtSomeString.getValue();
		length = idtLength.getValue();
		
		resultString = someString.substring(someString.length() - length, someString.length());
		StringDataType idtResultString = new StringDataType(resultString);
		
		list.remove(2);
		list.add(idtResultString);
		
	}
	
}
