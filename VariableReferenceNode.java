package shank_interpreter;


public class VariableReferenceNode extends StatementNode {

	private String name;
	private Node index = null;

	public VariableReferenceNode(String name) {
		this.name = name;
	}
	
	public VariableReferenceNode(String name, Node index) {
		this.name = name;
		this.index = index;
	}
	
	@Override
	public String toString() {
		if(index == null)
			return "" + getName();
		else
			return "" + getName() + "[" + index.toString() + "]";
	}

	public String getName() {
		return name;
	}

		
	
}
