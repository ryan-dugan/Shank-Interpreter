package shank_interpreter;


import java.util.ArrayList;

public class ForNode extends StatementNode {
	
	private VariableReferenceNode iterator;
	private ArrayList<StatementNode> statements;
	private int from;
	private int to;
	
	public ForNode(VariableReferenceNode iterator, ArrayList<StatementNode> statements, int from, int to) {
		this.iterator = iterator;
		this.statements = statements;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public String toString() {
		return "\n\tForNode(\'" + iterator + "\' from " + from + " to " + to + ")\n\t\tStatements:\n\t" + statements + "\n\t//End ForNode\n\t";
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public ArrayList<StatementNode> getStatements() {
		return statements;
	}

	public VariableReferenceNode getIterator() {
		return iterator;
	}
}
