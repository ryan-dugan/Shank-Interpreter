package shank_interpreter;


import java.util.ArrayList;

public class WhileNode extends StatementNode {

	private BooleanCompareNode condition;
	private ArrayList<StatementNode> statements;
	
	public WhileNode(BooleanCompareNode condition, ArrayList<StatementNode> statements) {
		this.condition = condition;
		this.statements = statements;
	}
	
	@Override
	public String toString() {
		return "\n\tWhileNode(Condition: " + getCondition().toString() + ")\n\tStatements:\n\t\t" + getStatements() + "\n\t//End WhileNode\n\t";
	}

	public BooleanCompareNode getCondition() {
		return condition;
	}

	public ArrayList<StatementNode> getStatements() {
		return statements;
	}	
	
}
