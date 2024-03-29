package shank_interpreter;


import java.util.ArrayList;

import shank_interpreter.RuntimeErrorException;


public class BuiltInSubstring extends FunctionNode {
	
	ArrayList<InterpreterDataType> list;
	
	public BuiltInSubstring(ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/* Substring someString, index, length, var resultString
		ResultString = length characters from someString, starting at index*/
		
		String someString;
		int index;
		int length;
		String resultString;
		
		StringDataType idtSomeString = null;
		IntegerDataType idtIndex = null;
		IntegerDataType idtLength = null;
		
		idtSomeString = (StringDataType) list.get(0);
		idtIndex = (IntegerDataType) list.get(1);
		idtLength = (IntegerDataType) list.get(2);
		
		if(idtSomeString == null)
			throw new RuntimeErrorException("IDT not valid for input string in built-in Substring function.");
		if(idtIndex == null)
			throw new RuntimeErrorException("IDT not valid for index in built-in Substring function.");
		if(idtLength == null)
			throw new RuntimeErrorException("IDT not valid for length in built-in Substring function.");
				
		someString = idtSomeString.getValue();
		index = idtIndex.getValue();
		length = idtLength.getValue();
		
		resultString = someString.substring(index, length);
		StringDataType idtResultString = new StringDataType(resultString);
		
		list.remove(3);
		list.add(idtResultString);
		
	}
	

}
