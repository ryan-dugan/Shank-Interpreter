package shank_interpreter;


import java.util.ArrayList;

public class RepeatNode extends StatementNode {
	
	private BooleanCompareNode condition;
	private ArrayList<StatementNode> statements;
	
	public RepeatNode(BooleanCompareNode condition, ArrayList<StatementNode> statements) {
		this.condition = condition;
		this.statements = statements;
	}
	
	@Override
	public String toString() {
		return "\n\tRepeatNode(Repeat until: " + getCondition().toString() + ")\n\tStatements:\n\t" + getStatements() + "//End repeat\n\t";
	}

	public BooleanCompareNode getCondition() {
		return condition;
	}

	public ArrayList<StatementNode> getStatements() {
		return statements;
	}

}
