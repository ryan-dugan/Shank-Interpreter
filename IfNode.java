package shank_interpreter;


import java.util.ArrayList;

public class IfNode extends StatementNode {

	private BooleanCompareNode condition;
	private ArrayList<StatementNode> statements;
	private IfNode NextIf = null;
	
	public IfNode(BooleanCompareNode condition, ArrayList<StatementNode> statements, IfNode NextIf) {
		this.condition = condition;
		this.statements = statements;
		this.NextIf = NextIf;
	}
	
	public IfNode(BooleanCompareNode condition, ArrayList<StatementNode> statements) {
		this.condition = condition;
		this.statements = statements;
	}
	
	@Override
	public String toString() {
		
		String elsif = "";
		
		if(getNextIf() != null && getNextIf().condition != null)
			elsif = "\n\tELSE IF:";
		
		//standalone if
		//(there is no next if but there is a condition)
		if(getNextIf() == null && getCondition() != null)
			return "\n\tIfNode(" + getCondition().toString() + ")\n\tStatements: " + getStatements();
		
		//elsif
		//(there is a next if and there is a condition)
		else if(getNextIf() != null && getCondition() != null)
			return "\n\tIfNode(" + getCondition().toString() + ")\n\tStatements: " + getStatements() + elsif + "\n\t" + getNextIf();
		
		//else
		//(there is no next if and no condition)
		else
			return "\n\tELSE\n\tStatements: " + getStatements() + "\n";

	}

	public BooleanCompareNode getCondition() {
		return condition;
	}

	public IfNode getNextIf() {
		return NextIf;
	}

	public ArrayList<StatementNode> getStatements() {
		return statements;
	}

}
