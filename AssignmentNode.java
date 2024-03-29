package shank_interpreter;


public class AssignmentNode extends StatementNode {

	private VariableReferenceNode variable;
	private Node value;
	
	public AssignmentNode(VariableReferenceNode variable, Node value) {
		this.variable = variable;
		this.value = value;
	}
	
	@Override
	public String toString() {

		return "AssignmentNode(" + getVariable().toString() + " := " + getValue().toString() +")";
	}

	public VariableReferenceNode getVariable() {
		return variable;
	}

	public Node getValue() {
		return value;
	}

}
