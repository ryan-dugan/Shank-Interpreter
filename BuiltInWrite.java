package shank_interpreter;

import java.util.*;

public class BuiltInWrite extends FunctionNode {
		
	public BuiltInWrite(ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public BuiltInWrite() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isVariadic() {
		return true;
	}

	public void execute(ArrayList<InterpreterDataType> list) {
		
		/*Write a,b,c { for example – these are variadic }
		Writes the values of a,b and c separated by spaces*/

		for(int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + " ");
		}
		
		
	}

}
