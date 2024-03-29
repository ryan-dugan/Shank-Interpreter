package shank_interpreter;


import shank_interpreter.Token;
import shank_interpreter.Token.*;

public class VariableNode extends Node {

	//name, type, changable, value, from, to
	
	private String name;
	private tokenType type, arrayType;
	private Node value;
	private boolean changeable;
	private float from, to;
	private int intFrom, intTo;
	private boolean hasTypeLimit = false, realTypeLimit = false;
	
	//variables
	public VariableNode(String name, tokenType type, boolean changeable) {
		this.name = name;
		this.type = type;
		this.changeable = changeable;
	}
	
	//constants
	public VariableNode(String name, tokenType type, Node value, boolean changeable) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.changeable = changeable;
	}
	
	//variables with integer type limits
	public VariableNode(String name, tokenType type, boolean changeable, int intFrom, int intTo) {
		this.name = name;
		this.type = type;
		this.changeable = changeable;
		this.intFrom = intFrom;
		this.intTo = intTo;
		hasTypeLimit = true;
	}
	
	//variables with float type limits
	public VariableNode(String name, tokenType type, boolean changeable, float from, float to) {
		this.name = name;
		this.type = type;
		this.changeable = changeable;
		this.from = from;
		this.to = to;
		hasTypeLimit = true;
		realTypeLimit = true;
	}
	
	//arrays
	public VariableNode(String name, tokenType type, boolean changeable, float from, float to, tokenType arrayType) {
		this.name = name;
		this.type = type;
		this.changeable = changeable;
		this.from = from;
		this.to = to;
		this.arrayType = arrayType;
	}
	
	@Override
	public String toString() {

		//constant
		if(this.getValue() != null){
			return "VariableNode(name = \'" + getName() + "\', value = \'" + getValue() + "\', type = \'" + getType().toString() + "\', changeable = " + changeable + ") ";
		}
		//variable with type limit
		else if (hasTypeLimit == true){
			if(realTypeLimit)
				return "VariableNode(name = \'" + getName() + "\', type = \'" + getType().toString() + "\', changeable = " + changeable + ", from = " + from + ", to = " + to +") ";
			else
				return "VariableNode(name = \'" + getName() + "\', type = \'" + getType().toString() + "\', changeable = " + changeable + ", from = " + intFrom + ", to = " + intTo +") ";
		}
		//array
		else if (getType() == tokenType.ARRAY) {
			return "VariableNode(name = \'" + getName() + "\', type = " + getType().toString() + " from " + getFrom() + " to " + to + " of " + getArrayType() + ") ";
		}
		//variable without type limit
		else {
			return "VariableNode(name = \'" + getName() + "\', type = \'" + getType().toString() + "\', changeable = " + changeable + ") ";
		}
		
	}

	public tokenType getType() {
		return type;
	}

	public Node getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public tokenType getArrayType() {
		return arrayType;
	}

	public float getFrom() {
		return from;
	}

	public float getTo() {
		return to;
	}
	
	public int getIntFrom() {
		return intFrom;
	}
	
	public int getIntTo() {
		return intTo;
	}
	
	public boolean getHasTypeLimit() {
		return hasTypeLimit;
	}

	public boolean getRealTypeLimit() {
		return realTypeLimit;
	}
	
}
