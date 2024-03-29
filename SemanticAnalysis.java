package shank_interpreter;

import java.util.*;

import shank_interpreter.RuntimeErrorException;
import shank_interpreter.SyntaxErrorException;
import shank_interpreter.Token.tokenType;

public class SemanticAnalysis {
	
	public SemanticAnalysis(ProgramNode program) throws SyntaxErrorException {
		checkAssignments(program);
	}
	
	public void checkAssignments(ProgramNode program) throws SyntaxErrorException {
		
		HashMap<String, FunctionNode> functions = program.getHashmap();
		
		for(FunctionNode function : functions.values()) {
			if(function != null)
				analyzeFunction(function);
		}
		
	}
	
	public void analyzeFunction(FunctionNode function) throws SyntaxErrorException {
		
		List<StatementNode> statements = function.getStatements();
		List<VariableNode> variables = function.getVariables();
		HashMap<String, VariableNode> hash = new HashMap<String, VariableNode>();
		
		if(variables == null)
			return;
		
		for(VariableNode variable : variables) {

			hash.put(variable.getName(), variable);
			
		}
		
		checkStatements(statements, hash);

	}
	
	public void checkStatements(List<StatementNode> statements, HashMap<String, VariableNode> hash) throws SyntaxErrorException {
		
		for(StatementNode statement : statements) {
			
			checkStatement(statement, hash);
			
		}
		
	}
	
	public void checkStatement(StatementNode statement, HashMap<String, VariableNode> hash) throws SyntaxErrorException {
		
		if(statement instanceof AssignmentNode) {
			
			AssignmentNode assign = (AssignmentNode) statement;
			
			if(hash.containsKey(assign.getVariable().getName())) {
				
				VariableNode variable = hash.get(assign.getVariable().getName());
				tokenType type = variable.getType();
				Node value = assign.getValue();
				
				if(value instanceof IntegerNode && type != tokenType.INTEGER) {
					throw new SyntaxErrorException("Incompatible types INTEGER and " + type);
				}
				else if(value instanceof RealNode && type != tokenType.REAL) {
					throw new SyntaxErrorException("Incompatible types REAL and " + type);
				}
				else if(value instanceof StringNode && type != tokenType.STRING) {
					throw new SyntaxErrorException("Incompatible types STRING and " + type);
				}
				else if(value instanceof CharacterNode && type != tokenType.CHAR) {
					throw new SyntaxErrorException("Incompatible types STRING and " + type);
				}
				else if(value instanceof StringNode && type != tokenType.STRING) {
					throw new SyntaxErrorException("Incompatible types STRING and " + type);
				}
				else if(type == tokenType.ARRAY) {
					
					tokenType arrayType = variable.getArrayType();
					
					if(value instanceof IntegerNode && arrayType != tokenType.INTEGER) {
						throw new SyntaxErrorException("Incompatible types INTEGER and " + type);
					}
					else if(value instanceof RealNode && arrayType != tokenType.REAL) {
						throw new SyntaxErrorException("Incompatible types REAL and " + type);
					}
					else if(value instanceof StringNode && arrayType != tokenType.STRING) {
						throw new SyntaxErrorException("Incompatible types STRING and " + type);
					}
					else if(value instanceof CharacterNode && arrayType != tokenType.CHAR) {
						throw new SyntaxErrorException("Incompatible types STRING and " + type);
					}
					else if(value instanceof StringNode && arrayType != tokenType.STRING) {
						throw new SyntaxErrorException("Incompatible types STRING and " + type);
					}
				}
				
			}
			
		}
		
		else if(statement instanceof IfNode) {
			
			IfNode node = (IfNode) statement;
			List<StatementNode> list = node.getStatements();
			checkStatements(list, hash);
			
		}
		else if(statement instanceof WhileNode) {
			
			WhileNode node = (WhileNode) statement;
			List<StatementNode> list = node.getStatements();
			checkStatements(list, hash);
			
		}
		else if(statement instanceof RepeatNode) {
			
			RepeatNode node = (RepeatNode) statement;
			List<StatementNode> list = node.getStatements();
			checkStatements(list, hash);
			
		}
		else if(statement instanceof ForNode) {
			
			RepeatNode node = (RepeatNode) statement;
			List<StatementNode> list = node.getStatements();
			checkStatements(list, hash);
			
		}
		
	}

}
