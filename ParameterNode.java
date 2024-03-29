package shank_interpreter;


public class ParameterNode extends Node {

	private VariableReferenceNode var;
	private Node value;
	private boolean varMode = false;
	
	public ParameterNode(Node value) {
		this.value = value;
	}
	
	public ParameterNode(VariableReferenceNode var, boolean varMode) {
		this.var = var;
		this.varMode = true;
	}
	
	public VariableReferenceNode getNode() {
		return var;
	}
	
	@Override
	public String toString() {
		if(varMode == false) {
			return "ParameterNode(" + getValue() + ")";
		}
		else {
			return "ParameterNode(var " + var + ")";
		}
		
	}

	public Node getValue() {
		return value;
	}

}
