package shank_interpreter;




import java.util.*;

import shank_interpreter.RuntimeErrorException;

public class BuiltInRead extends FunctionNode {
		
	public BuiltInRead(ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public BuiltInRead() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isVariadic() {
		return true;
	}

	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/*Read var a, var b, var c { for example – these are variadic }
		Reads (space delimited) values from the user*/
		
		Scanner input = new Scanner(System.in);
		String string;
		int integer;
		float real;
		boolean bool;
		InterpreterDataType idt;
		int size = list.size();
		
		//loop through the list and store the user input back into the parameters that were passed in
		for(int i = 0; i < size; i++) {
			
			idt = list.remove(0);
			
			//check for correct IDT
			if(idt instanceof BooleanDataType) {
				bool = input.nextBoolean();
				BooleanDataType bdt = new BooleanDataType(bool);
				list.add(bdt);
			}
			else if(idt instanceof StringDataType) {
				string = input.next();
				StringDataType sdt = new StringDataType(string);
				list.add(sdt);
			}
			else if(idt instanceof IntegerDataType) {
				integer = input.nextInt();
				IntegerDataType intdt = new IntegerDataType(integer);
				list.add(intdt);
			}
			else if(idt instanceof RealDataType) {
				real = input.nextFloat();
				RealDataType rdt = new RealDataType(real);
				list.add(rdt);
			}
			else
				throw new RuntimeErrorException("IDT is not valid for built-in Read function.");
		}
		
	}

}
