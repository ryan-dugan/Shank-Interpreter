package shank_interpreter;


import java.util.ArrayList;

public class FunctionCallNode extends StatementNode {

	ArrayList<Node> parameters;
	String name;
	
	public FunctionCallNode(String name, ArrayList<Node> parameters2) {
		this.name = name;
		this.parameters = parameters2;
	}
	
	@Override
	public String toString() {
		return "\n\tFunctionCallNode(Name: " + getName() + ", Parameters: " + getParameters() + ")\n\t";
	}

	public String getName() {
		return name;
	}

	public ArrayList<Node> getParameters() {
		return parameters;
	}

}
