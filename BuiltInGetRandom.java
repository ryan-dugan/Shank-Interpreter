package shank_interpreter;


import java.util.ArrayList;
import java.util.Random;



public class BuiltInGetRandom extends FunctionNode {

	public BuiltInGetRandom(ArrayList<InterpreterDataType> list) {
		super(list);
	}
	
	public void execute(ArrayList<InterpreterDataType> list) throws RuntimeErrorException {
		
		/*GetRandom var resultInteger
		resultInteger = some random integer*/
		
		Random rand = new Random();
		InterpreterDataType idt = list.remove(0);
		
		//check for valid IDT
		if(!(idt instanceof IntegerDataType))
			throw new RuntimeErrorException("IDT is not valid for built-in GetRandom function");
		
		int random = rand.nextInt();
		IntegerDataType result = new IntegerDataType(random);
		
		list.add(result);		
		
	}

}
