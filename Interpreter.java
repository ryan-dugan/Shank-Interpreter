package shank_interpreter;

import java.util.*;

import shank_interpreter.BooleanCompareNode.boolComparison;
import shank_interpreter.MathOpNode.operation;
import shank_interpreter.Token.tokenType;

public class Interpreter {

	HashMap<String, InterpreterDataType> known_variables = new HashMap<String, InterpreterDataType>();
	HashMap<String, FunctionNode> functions;
	
	public HashMap<String, InterpreterDataType> getHashmap(){
		return known_variables;
	}

	/**
	 * Constructs a new Interpreter with the ProgramNode created by the Parser.
	 * Calls the function "start" if it exists, throws an error if it doesn't.
	 * 
	 * @param program
	 * @throws RuntimeErrorException
	 */
	public Interpreter(ProgramNode program) throws RuntimeErrorException {
		
		functions = program.getHashmap();
		if (functions.get("start") != null) {
			FunctionNode start = functions.get("start");
			interpretFunction(start, null);
		} else {
			throw new RuntimeErrorException("No start function defined.");
		}
	}

	public void interpretFunction(FunctionNode function, ArrayList<InterpreterDataType> idts) throws RuntimeErrorException {

		ArrayList<VariableNode> parameters = (ArrayList<VariableNode>) function.getParameters();
		ArrayList<VariableNode> variables = (ArrayList<VariableNode>) function.getVariables();
		ArrayList<StatementNode> statements = (ArrayList<StatementNode>) function.getStatements();
		
		int i = 0;

		// loop over parameters, create new IDTs for them, and add the IDTs to the
		// hashmap
		for (VariableNode parameter : parameters) {

			String name = parameter.getName();
			tokenType type = parameter.getType();
			
			InterpreterDataType idt = idts.get(i);

			if (type == tokenType.INTEGER && idt instanceof IntegerDataType) {
				known_variables.put(name, idt);
			} else if (type == tokenType.REAL && idt instanceof RealDataType) {
				known_variables.put(name, idt);
			} else if (type == tokenType.BOOLEAN && idt instanceof BooleanDataType) {
				known_variables.put(name, idt);
			} else if (type == tokenType.STRING && idt instanceof StringDataType) {
				known_variables.put(name, idt);
			} else if (type == tokenType.CHAR && idt instanceof CharacterDataType) {
				known_variables.put(name, idt);
			} else {
				throw new RuntimeErrorException("Invalid parameter; expected " + type + ", saw " + idt);
			}
			
			i++;
		} // end for loop

		// loop over constants/variables, create new IDTs for them, and add the IDTs to
		// the hashmap
		for (VariableNode variable : variables) {

			String name = variable.getName();
			tokenType type = variable.getType();
			boolean hasTypeLimit = variable.getHasTypeLimit();

			if (type == tokenType.INTEGER) {
				IntegerNode valueNode = (IntegerNode) variable.getValue();

				// check if variable or constant
				if (valueNode == null) { // is a variable (value of variables at declaration is null)

					// check if variable has type limit
					if (hasTypeLimit) {
						int from = variable.getIntFrom();
						int to = variable.getIntTo();
						IntegerDataType integer = new IntegerDataType(from, to);
						known_variables.put(name, integer);
					} else {
						IntegerDataType integer = new IntegerDataType();
						known_variables.put(name, integer);
					}
				} else { // is a constant
					int value = valueNode.getValue();
					IntegerDataType integer = new IntegerDataType(value);
					known_variables.put(name, integer);
				}
			} else if (type == tokenType.REAL) {
				RealNode valueNode = (RealNode) variable.getValue();
				// check if variable or constant
				if (valueNode == null) { // is a variable

					// check if variable has type limit
					if (hasTypeLimit) {
						float from = variable.getFrom();
						float to = variable.getTo();
						RealDataType real = new RealDataType(from, to);
						known_variables.put(name, real);
					} else {
						RealDataType real = new RealDataType();
						known_variables.put(name, real);
					}
				} else { // is a constant
					float value = valueNode.getValue();
					RealDataType real = new RealDataType(value);
					known_variables.put(name, real);
				}
			} else if (type == tokenType.BOOLEAN) {
				BooleanNode valueNode = (BooleanNode) variable.getValue();

				// check if variable or constant
				if (valueNode == null) {
					BooleanDataType bool = new BooleanDataType();
					known_variables.put(name, bool);
				} else {
					boolean value = valueNode.getValue();
					BooleanDataType bool = new BooleanDataType(value);
					known_variables.put(name, bool);
				}
			} else if (type == tokenType.CHARACTERLITERAL) {
				CharacterNode valueNode = (CharacterNode) variable.getValue();

				char value = valueNode.getValue();
				CharacterDataType character = new CharacterDataType(value);
				known_variables.put(name, character);
			} else if (type == tokenType.STRINGLITERAL) {
				StringNode valueNode = (StringNode) variable.getValue();

				String value = valueNode.getValue();
				StringDataType string = new StringDataType(value);
				known_variables.put(name, string);
			} else if (type == tokenType.STRING) {
				StringNode valueNode = (StringNode) variable.getValue();

				if (hasTypeLimit) {
					int from = variable.getIntFrom();
					int to = variable.getIntTo();
					StringDataType string = new StringDataType(from, to);
					known_variables.put(name, string);
				} else {
					StringDataType string = new StringDataType();
					known_variables.put(name, string);
				}
			} else if (type == tokenType.CHAR) {
				CharacterNode valueNode = (CharacterNode) variable.getValue();

				CharacterDataType character = new CharacterDataType();
				known_variables.put(name, character);
			} else if (type == tokenType.ARRAY) {

				tokenType arrayType = variable.getArrayType();
				int from = variable.getIntFrom();
				int to = variable.getIntTo();
				int range = to - from;

				if (arrayType == tokenType.INTEGER) {
					IntegerDataType[] array = new IntegerDataType[range];
					ArrayDataType adt = new ArrayDataType(array, from, to);
					known_variables.put(name, adt);
				} else if (arrayType == tokenType.REAL) {
					RealDataType[] array = new RealDataType[range];
					ArrayDataType adt = new ArrayDataType(array, from, to);
					known_variables.put(name, adt);
				} else if (arrayType == tokenType.BOOLEAN) {
					BooleanDataType[] array = new BooleanDataType[range];
					ArrayDataType adt = new ArrayDataType(array, from, to);
					known_variables.put(name, adt);
				} else if (arrayType == tokenType.CHAR) {
					CharacterDataType[] array = new CharacterDataType[range];
					ArrayDataType adt = new ArrayDataType(array, from, to);
					known_variables.put(name, adt);
				} else if (arrayType == tokenType.STRING) {
					StringDataType[] array = new StringDataType[range];
					ArrayDataType adt = new ArrayDataType(array, from, to);
					known_variables.put(name, adt);
				}

			} // end type checks

		} // end for loop

		interpretBlock(known_variables, statements);

	} // end interpretFunction()

	public void interpretBlock(HashMap<String, InterpreterDataType> known_variables, List<StatementNode> statements)
			throws RuntimeErrorException {

		// loop over statements
		for (StatementNode statement : statements) {

			if (statement instanceof AssignmentNode) {
				AssignmentNode assign = (AssignmentNode) statement;
				interpretAssignment(assign);
			}
			if (statement instanceof IfNode) {
				IfNode node = (IfNode) statement;
				interpretIf(node);
			}
			if (statement instanceof WhileNode) {
				WhileNode whileNode = (WhileNode) statement;
				interpretWhile(whileNode);
			}
			if (statement instanceof RepeatNode) {
				RepeatNode repeat = (RepeatNode) statement;
				interpretRepeat(repeat);
			}
			if (statement instanceof ForNode) {
				ForNode forNode = (ForNode) statement;
				interpretFor(forNode);
			}
			if (statement instanceof FunctionCallNode) {
				FunctionCallNode call = (FunctionCallNode) statement;
				interpretFunctionCall(call);
			}
		}
	}

	public void interpretFunctionCall(FunctionCallNode call) throws RuntimeErrorException {

		String name = call.getName();
		FunctionNode function;
		List<Node> parameters;
		ArrayList<InterpreterDataType> idts = new ArrayList<InterpreterDataType>();

		// locate the function definition
		if (functions.containsKey(name)) {
			function = functions.get(name);
		} else {
			throw new RuntimeErrorException("Invalid function call; Function \'" + name + "\' not found.");
		}

		// make sure the number of parameters match or that the function is variadic
		if (function.isVariadic() || (function.getParameters().size() == call.getParameters().size())) {
			parameters = call.getParameters();

			// evaluate the parameters and add to collection of IDTs
			for (Node parameter : parameters) {
				
				ParameterNode paramNode = (ParameterNode) parameter;
				
				if(paramNode.getNode() instanceof VariableReferenceNode) {
					VariableReferenceNode node = paramNode.getNode();
					idts.add(interpretVariableReference(node, known_variables));
				}
				else {
					parameter = expression(paramNode.getValue());
				}

				if (parameter instanceof IntegerNode) {
					IntegerDataType integer = new IntegerDataType(((IntegerNode) parameter).getValue());
					idts.add(integer);
				} else if (parameter instanceof RealNode) {
					RealDataType real = new RealDataType(((RealNode) parameter).getValue());
					idts.add(real);
				} else if (parameter instanceof StringNode) {
					StringDataType string = new StringDataType(((StringNode) parameter).getValue());
					idts.add(string);
				}

			}

			//check if function is a builtin
			if (function instanceof BuiltInEnd) {
				BuiltInEnd builtin = (BuiltInEnd) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInStart) {
				BuiltInStart builtin = (BuiltInStart) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInRead) {
				BuiltInRead builtin = (BuiltInRead) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInWrite) {
				BuiltInWrite builtin = (BuiltInWrite) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInSubstring) {
				BuiltInSubstring builtin = (BuiltInSubstring) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInLeft) {
				BuiltInLeft builtin = (BuiltInLeft) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInRight) {
				BuiltInRight builtin = (BuiltInRight) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInGetRandom) {
				BuiltInGetRandom builtin = (BuiltInGetRandom) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInIntegerToReal) {
				BuiltInIntegerToReal builtin = (BuiltInIntegerToReal) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInRealToInteger) {
				BuiltInRealToInteger builtin = (BuiltInRealToInteger) function;
				builtin.execute(idts);
			} else if (function instanceof BuiltInSquareRoot) {
				BuiltInSquareRoot builtin = (BuiltInSquareRoot) function;
				builtin.execute(idts);
			}
			//it is not a built in function, so call interpretFunction on it
			else {
				interpretFunction(function, idts);
			}
			
			

		} else {
			throw new RuntimeErrorException("Invalid function call; Incorrect usage (" + function.getParameters().size()
					+ " parameters expected, " + call.getParameters().size() + " parameters found)");
		}

	}

	public void interpretAssignment(AssignmentNode assign) throws RuntimeErrorException {

		VariableReferenceNode variable = assign.getVariable();
		interpretVariableReference(variable, known_variables);		
		Node right = assign.getValue();

		if (right instanceof CharacterNode) {
			CharacterNode rightChar = (CharacterNode) right;
			CharacterDataType character = new CharacterDataType(rightChar.getValue());
			known_variables.remove(variable.getName());
			known_variables.put(variable.getName(), character);
		} else if (right instanceof VariableReferenceNode) {
			VariableReferenceNode rightVar = (VariableReferenceNode) right;
			InterpreterDataType idt = interpretVariableReference(rightVar, known_variables);
			known_variables.remove(variable.getName());
			known_variables.put(variable.getName(), idt);
		} else {
			Node expression = expression(right);
			if (expression instanceof IntegerNode) {
				IntegerNode exp = (IntegerNode) expression;
				IntegerDataType idt = new IntegerDataType(exp.getValue());
				known_variables.remove(variable.getName());
				known_variables.put(variable.getName(), idt);
			} else if (expression instanceof RealNode) {
				RealNode exp = (RealNode) expression;
				RealDataType idt = new RealDataType(exp.getValue());
				known_variables.remove(variable.getName());
				known_variables.put(variable.getName(), idt);
			} else if (expression instanceof StringNode) {
				StringNode exp = (StringNode) expression;
				StringDataType idt = new StringDataType(exp.getValue());
				known_variables.remove(variable.getName());
				known_variables.put(variable.getName(), idt);
			}
		}

	}

	public void interpretFor(ForNode forNode) throws RuntimeErrorException {

		List<StatementNode> statements = forNode.getStatements();
		int from = forNode.getFrom();
		int to = forNode.getTo();
		VariableReferenceNode iterator = forNode.getIterator();

		if (from < to) {
			for (int i = from; i < to; i++) {
				IntegerDataType idt = new IntegerDataType(i);
				known_variables.put(iterator.getName(), idt);
				interpretBlock(known_variables, statements);
				known_variables.remove(idt);
			}
		} else {
			for (int i = to; i > from; i--) {
				IntegerDataType idt = new IntegerDataType(i);
				known_variables.put(iterator.getName(), idt);
				interpretBlock(known_variables, statements);
				known_variables.remove(idt);
			}
		}

	}

	public void interpretRepeat(RepeatNode repeatNode) throws RuntimeErrorException {

		BooleanCompareNode condition = repeatNode.getCondition();
		List<StatementNode> statements = repeatNode.getStatements();

		while (!interpretBooleanCompare(condition)) {
			interpretBlock(known_variables, statements);
		}

	}

	public void interpretWhile(WhileNode whileNode) throws RuntimeErrorException {

		BooleanCompareNode condition = whileNode.getCondition();
		List<StatementNode> statements = whileNode.getStatements();

		while (interpretBooleanCompare(condition)) {
			interpretBlock(known_variables, statements);
		}

	}

	public void interpretIf(IfNode ifNode) throws RuntimeErrorException {

		while (true) {

			BooleanCompareNode condition = ifNode.getCondition();
			IfNode nextIf = ifNode.getNextIf();
			List<StatementNode> statements = ifNode.getStatements();

			if (condition != null && interpretBooleanCompare(condition)) {
				interpretBlock(known_variables, statements);
				break;
			} else if (nextIf != null) {
				ifNode = nextIf;
			} else if (condition == null) {
				interpretBlock(known_variables, statements);
				break;
			}
			else
				break;
		}

	}

	public boolean interpretBooleanCompare(BooleanCompareNode node) throws RuntimeErrorException {

		Node left = node.left;
		Node right = node.right;
		boolComparison op = node.bc;

		Node leftExp = expression(left);
		Node rightExp = expression(right);

		IntegerNode leftInt, rightInt;
		RealNode leftFloat, rightFloat;
		StringNode leftString, rightString;

		// when both integers
		if (leftExp instanceof IntegerNode && rightExp instanceof IntegerNode) {
			leftInt = (IntegerNode) leftExp;
			rightInt = (IntegerNode) rightExp;

			if (op == boolComparison.EQUALS) {
				return leftInt.getValue() == rightInt.getValue();
			}
			if (op == boolComparison.NOTEQUAL) {
				return leftInt.getValue() != rightInt.getValue();
			}
			if (op == boolComparison.GREATER) {
				return leftInt.getValue() > rightInt.getValue();
			}
			if (op == boolComparison.GREATEROREQUAL) {
				return leftInt.getValue() >= rightInt.getValue();
			}
			if (op == boolComparison.LESS) {
				return leftInt.getValue() < rightInt.getValue();
			}
			if (op == boolComparison.LESSOREQUAL) {
				return leftInt.getValue() <= rightInt.getValue();
			}

		}
		// when both floats
		else if (leftExp instanceof RealNode && rightExp instanceof RealNode) {
			leftFloat = (RealNode) leftExp;
			rightFloat = (RealNode) rightExp;

			if (op == boolComparison.EQUALS) {
				return leftFloat.getValue() == rightFloat.getValue();
			}
			if (op == boolComparison.NOTEQUAL) {
				return leftFloat.getValue() != rightFloat.getValue();
			}
			if (op == boolComparison.GREATER) {
				return leftFloat.getValue() > rightFloat.getValue();
			}
			if (op == boolComparison.GREATEROREQUAL) {
				return leftFloat.getValue() >= rightFloat.getValue();
			}
			if (op == boolComparison.LESS) {
				return leftFloat.getValue() < rightFloat.getValue();
			}
			if (op == boolComparison.LESSOREQUAL) {
				return leftFloat.getValue() <= rightFloat.getValue();
			}
		}
		// when left is int and right is float
		else if (leftExp instanceof IntegerNode && rightExp instanceof RealNode) {
			leftInt = (IntegerNode) leftExp;
			rightFloat = (RealNode) rightExp;

			if (op == boolComparison.EQUALS) {
				return leftInt.getValue() == rightFloat.getValue();
			}
			if (op == boolComparison.NOTEQUAL) {
				return leftInt.getValue() != rightFloat.getValue();
			}
			if (op == boolComparison.GREATER) {
				return leftInt.getValue() > rightFloat.getValue();
			}
			if (op == boolComparison.GREATEROREQUAL) {
				return leftInt.getValue() >= rightFloat.getValue();
			}
			if (op == boolComparison.LESS) {
				return leftInt.getValue() < rightFloat.getValue();
			}
			if (op == boolComparison.LESSOREQUAL) {
				return leftInt.getValue() <= rightFloat.getValue();
			}
		}
		// when left is float and right is int
		else if (leftExp instanceof RealNode && rightExp instanceof IntegerNode) {
			leftFloat = (RealNode) leftExp;
			rightInt = (IntegerNode) rightExp;

			if (op == boolComparison.EQUALS) {
				return leftFloat.getValue() == rightInt.getValue();
			}
			if (op == boolComparison.NOTEQUAL) {
				return leftFloat.getValue() != rightInt.getValue();
			}
			if (op == boolComparison.GREATER) {
				return leftFloat.getValue() > rightInt.getValue();
			}
			if (op == boolComparison.GREATEROREQUAL) {
				return leftFloat.getValue() >= rightInt.getValue();
			}
			if (op == boolComparison.LESS) {
				return leftFloat.getValue() < rightInt.getValue();
			}
			if (op == boolComparison.LESSOREQUAL) {
				return leftFloat.getValue() <= rightInt.getValue();
			}
		}
		// when strings
		else if (leftExp instanceof StringNode && rightExp instanceof StringNode) {
			leftString = (StringNode) leftExp;
			rightString = (StringNode) rightExp;

			if (op == boolComparison.EQUALS) {
				return leftString.getValue() == rightString.getValue();
			} else if (op == boolComparison.NOTEQUAL) {
				return leftString.getValue() != rightString.getValue();
			} else
				throw new RuntimeErrorException("Invalid operator " + op + " on type string.");
		} else {
			throw new RuntimeErrorException(
					"Incompatible types " + left + " and " + right + " for operator " + op + ".");
		}
		return false;
	}

	public InterpreterDataType interpretVariableReference(VariableReferenceNode variable,
			HashMap<String, InterpreterDataType> known_variables) throws RuntimeErrorException {

		String name = variable.getName();
		InterpreterDataType idt = null;

		if (known_variables.containsKey(name))
			idt = known_variables.get(name);
		else
			throw new RuntimeErrorException("Invalid variable reference: " + name);

		return idt;

	}

	public InterpreterDataType interpretMathOp(MathOpNode math) throws RuntimeErrorException {

		Node left = math.left;
		Node right = math.right;
		operation op = math.op;

		Node leftExp = expression(left);
		Node rightExp = expression(right);

		if (leftExp instanceof IntegerNode && rightExp instanceof IntegerNode) {
			IntegerNode leftInt = (IntegerNode) leftExp;
			IntegerNode rightInt = (IntegerNode) rightExp;
			int result;

			if (op == operation.ADD) {
				result = leftInt.getValue() + rightInt.getValue();
			} else if (op == operation.SUBTRACT) {
				result = leftInt.getValue() - rightInt.getValue();
			} else if (op == operation.MULTIPLY) {
				result = leftInt.getValue() * rightInt.getValue();
			} else if (op == operation.DIVIDE) {
				result = leftInt.getValue() / rightInt.getValue();
			} else {
				result = leftInt.getValue() % rightInt.getValue();
			}
			IntegerDataType idt = new IntegerDataType(result);
			return idt;
		}
		if (leftExp instanceof RealNode && rightExp instanceof RealNode) {
			RealNode leftFloat = (RealNode) leftExp;
			RealNode rightFloat = (RealNode) rightExp;
			float result;

			if (op == operation.ADD) {
				result = leftFloat.getValue() + rightFloat.getValue();
			} else if (op == operation.SUBTRACT) {
				result = leftFloat.getValue() - rightFloat.getValue();
			} else if (op == operation.MULTIPLY) {
				result = leftFloat.getValue() * rightFloat.getValue();
			} else if (op == operation.DIVIDE) {
				result = leftFloat.getValue() / rightFloat.getValue();
			} else {
				result = leftFloat.getValue() % rightFloat.getValue();
			}
			RealDataType idt = new RealDataType(result);
			return idt;
		}
		if (leftExp instanceof StringNode && rightExp instanceof StringNode) {
			StringNode leftString = (StringNode) leftExp;
			StringNode rightString = (StringNode) rightExp;
			String result;

			if (op == operation.ADD) {
				result = leftString.getValue() + rightString.getValue();
			} else {
				throw new RuntimeErrorException("Invalid operation " + op + " on type string.");
			}

			StringDataType idt = new StringDataType(result);
			return idt;
		} else {
			throw new RuntimeErrorException("Right and left side of math operation are not of the same type. Left: \'" + leftExp
					+ "\', Right: \'" + rightExp + "\'");
		}

	}

	public Node expression(Node node) throws RuntimeErrorException {

		if (node instanceof MathOpNode) {
			MathOpNode math = (MathOpNode) node;
			Node left = math.left, right = math.right;
			Node leftExp = expression(left);
			Node rightExp = expression(right);
			operation op = math.op;

			if (leftExp instanceof IntegerNode && rightExp instanceof IntegerNode) {
				IntegerNode leftInt = (IntegerNode) leftExp;
				IntegerNode rightInt = (IntegerNode) rightExp;
				int result;

				if (op == operation.ADD) {
					result = leftInt.getValue() + rightInt.getValue();
				} else if (op == operation.SUBTRACT) {
					result = leftInt.getValue() - rightInt.getValue();
				} else if (op == operation.MULTIPLY) {
					result = leftInt.getValue() * rightInt.getValue();
				} else if (op == operation.DIVIDE) {
					result = leftInt.getValue() / rightInt.getValue();
				} else {
					result = leftInt.getValue() % rightInt.getValue();
				}
				IntegerNode intNode = new IntegerNode(result);
				return intNode;
			}
			if (leftExp instanceof RealNode && rightExp instanceof RealNode) {
				RealNode leftFloat = (RealNode) leftExp;
				RealNode rightFloat = (RealNode) rightExp;
				float result;

				if (op == operation.ADD) {
					result = leftFloat.getValue() + rightFloat.getValue();
				} else if (op == operation.SUBTRACT) {
					result = leftFloat.getValue() - rightFloat.getValue();
				} else if (op == operation.MULTIPLY) {
					result = leftFloat.getValue() * rightFloat.getValue();
				} else if (op == operation.DIVIDE) {
					result = leftFloat.getValue() / rightFloat.getValue();
				} else {
					result = leftFloat.getValue() % rightFloat.getValue();
				}
				RealNode realNode = new RealNode(result);
				return realNode;
			}
			if (leftExp instanceof StringNode && rightExp instanceof StringNode) {
				StringNode leftString = (StringNode) leftExp;
				StringNode rightString = (StringNode) rightExp;
				String result;

				if (op == operation.ADD) {
					result = leftString.getValue() + rightString.getValue();
				} else {
					throw new RuntimeErrorException("Invalid operation " + op + " on type string.");
				}

				StringNode stringNode = new StringNode(result);
				return stringNode;
			} else {
				throw new RuntimeErrorException("Invalid left and right side of math op. Left: \'" + leftExp
						+ "\', Right: \'" + rightExp + "\'");
			}
		} else if (node instanceof VariableReferenceNode) {
			VariableReferenceNode vrn = (VariableReferenceNode) node;
			InterpreterDataType idt = interpretVariableReference(vrn, known_variables);
			if (idt instanceof IntegerDataType) {
				IntegerDataType integer = (IntegerDataType) idt;
				IntegerNode intNode = new IntegerNode(integer.getValue());
				return intNode;
			} else if (idt instanceof RealDataType) {
				RealDataType real = (RealDataType) idt;
				RealNode realNode = new RealNode(real.getValue());
				return realNode;
			} else if (idt instanceof StringDataType) {
				StringDataType string = (StringDataType) idt;
				StringNode stringNode = new StringNode(string.getValue());
				return stringNode;
			} else
				throw new RuntimeErrorException("Invalid expression for " + node + ".");
		} else if (node instanceof IntegerNode) {
			IntegerNode integer = (IntegerNode) node;
			return integer;
		} else if (node instanceof RealNode) {
			RealNode real = (RealNode) node;
			return real;
		} else if (node instanceof StringNode) {
			StringNode string = (StringNode) node;
			return string;
		} else
			throw new RuntimeErrorException("Invalid expression for " + node + ".");

	}

}
