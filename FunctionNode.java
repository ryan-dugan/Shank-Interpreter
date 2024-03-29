package shank_interpreter;


import java.util.*;

public class FunctionNode extends Node {

	/* Functions have a name (string), parameters (collection of VariableNode),
	 * constants and variables (a different collection of VariableNode)
	 * and statements (a collection of StatementNode).
	 */
	
	private String name;
	private List<VariableNode> parameters;
	private List<VariableNode> variables;
	private List<StatementNode> statements;
	private ArrayList<InterpreterDataType> list;
	
	public FunctionNode (String name, List<VariableNode> parameters, List<VariableNode> variables, List<StatementNode> statements) {
		this.name = name;
		this.parameters = parameters;
		this.variables = variables;
		this.statements = statements;
	}
	
	public FunctionNode (ArrayList<InterpreterDataType> list) {
		this.list = list;
	}
	
	public FunctionNode() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public List<VariableNode> getParameters(){
		return parameters;
	}
	
	public List<VariableNode> getVariables(){
		return variables;
	}
	
	public List<StatementNode> getStatements(){
		return statements;
	}
	
	public boolean isVariadic() {
		return false;
	}
	
	@Override
	public String toString() {
		if(name == null)
			return "";
		else
			return "\nFunctionNode (\n\nNAME: " + name + "\n\nPARAMETERS:\n\n\t" + parameters + "\n\nVARIABLES:\n\n\t" + variables+ "\n\nSTATEMENTS:\n\n\t" + statements + ")\n";
	}

}
