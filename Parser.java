package shank_interpreter;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import shank_interpreter.Token.*;
import shank_interpreter.BooleanCompareNode.*;
import shank_interpreter.MathOpNode.*;

public class Parser {

	private List<Token> tokens;

	/**
	 * Constructs a new Parser with a list of tokens created by Lexer.
	 * 
	 * @param tokens The list of tokens created by Lexer
	 */
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Parses list of tokens created by the lexer.
	 * 
	 * @return The root node of the AST.
	 * 
	 * @throws SyntaxErrorException
	 */
	public ProgramNode parse() throws SyntaxErrorException {

		HashMap<String, FunctionNode> hashmap = new HashMap<String, FunctionNode>();

		FunctionNode node;
		FunctionNode nextNode;

		while ((nextNode = function()) != null) {
			node = nextNode;
			hashmap.put(node.getName(), node);
		}
		
		//Add the built in functions to the function hashmap
		BuiltInRead read = new BuiltInRead();
		hashmap.put("read", read);
		BuiltInWrite write = new BuiltInWrite();
		hashmap.put("write", write);
		
		BuiltInLeft left = null;
		hashmap.put("left", left);
		BuiltInRight right = null;
		hashmap.put("right", right);
		BuiltInSubstring substring = null;
		hashmap.put("substring", substring);
		
		BuiltInSquareRoot sqrt = null;
		hashmap.put("squareRoot", sqrt);
		BuiltInGetRandom random = null;
		hashmap.put("getRandom", random);
		BuiltInIntegerToReal intToReal = null;
		hashmap.put("integerToReal", intToReal);
		BuiltInRealToInteger realToInt = null;
		hashmap.put("realToInteger", realToInt);
		
		BuiltInEnd end = null;
		hashmap.put("end", end);
		BuiltInStart start = null;
		hashmap.put("builtinstart", start);

		return new ProgramNode(hashmap);
	}

	/**
	 * Returns a FunctionNode containing collection of parameters, variables and
	 * constants, and statements that make up the function
	 * 
	 * @return the FunctionNode
	 * @throws SyntaxErrorException
	 */
	public FunctionNode function() throws SyntaxErrorException {

		String name;

		if (peek(0) == null) {
			return null;
		}

		// check if there is a define token
		if (peek(0).getTokenType() == tokenType.DEFINE) {
			matchAndRemove(tokenType.DEFINE);
		} else {
			return null;
		}

		// check if there is an identifier token
		if (peek(0).getTokenType() == tokenType.IDENTIFIER) {
			name = matchAndRemove(tokenType.IDENTIFIER).getValue();
		} else {
			throw new SyntaxErrorException("Expected IDENTIFIER after define, saw " + peek(0).toString());
		}

		// check if there is a left parenthesis
		if (peek(0).getTokenType() == tokenType.LPAREN) {
			matchAndRemove(tokenType.LPAREN);
		} else {
			throw new SyntaxErrorException("Expected '(' after name, saw " + peek(0).toString());
		}

		// get parameters
		ArrayList<VariableNode> parameters = parameterDeclarations();

		// check if there is a right parenthesis
		if (peek(0).getTokenType() == tokenType.RPAREN) {
			matchAndRemove(tokenType.RPAREN);
		} else {
			throw new SyntaxErrorException("Expected ')' after parameter declarations, saw " + peek(0).toString());
		}

		expectsEndOfLine();

		// get variables
		ArrayList<VariableNode> variables = variableDeclarations();

		expectsEndOfLine();

		// get statements
		ArrayList<StatementNode> statements = statements();

		expectsEndOfLine();

		return new FunctionNode(name, parameters, variables, statements);
	}

	/**
	 * Returns a collection of VariableNodes containing the parameters in a given
	 * function.
	 * 
	 * @return ArrayList of VariableNodes
	 * @throws SyntaxErrorException
	 */
	public ArrayList<VariableNode> parameterDeclarations() throws SyntaxErrorException {

		// expects 0 or more variables separated by commas, then a colon and a type,
		// then 0 or more variables separated by commas,
		// then a colon and a type...

		ArrayList<VariableNode> parameters = new ArrayList<VariableNode>();
		VariableNode parameter;

		// arbitrary array size
		String[] names = new String[10];
		int i = 0;

		tokenType type;
		tokenType arrayType = null;
		boolean changeable = false;
		int from = 0;
		int to = 0;

		// check if there are any parameters
		if (peek(0).getTokenType() == tokenType.VAR || peek(0).getTokenType() == tokenType.IDENTIFIER) {

			while (true) {

				if (peek(0).getTokenType() == tokenType.VAR) {
					changeable = true;
					matchAndRemove(tokenType.VAR);
				}

				if (peek(0).getTokenType() == tokenType.IDENTIFIER) {

					names[i] = matchAndRemove(tokenType.IDENTIFIER).getValue();

					// expects either a comma (more variables ahead) or a colon (no more variables
					// of this type)

					if (peek(0).getTokenType() != tokenType.COMMA && peek(0).getTokenType() != tokenType.COLON) {
						throw new SyntaxErrorException(
								"Expected COLON or COMMA after parameter identifier, saw " + peek(0).toString());
					} else if (matchAndRemove(tokenType.COMMA) != null) {
						matchAndRemove(tokenType.COMMA);
						i++;
						// go back and loop to expect another identifier
					} else if (matchAndRemove(tokenType.COLON) != null) {

						// saw colon, now expect type

						// check for type token
						if (peek(0).getTokenType() == tokenType.INTEGER) {
							type = matchAndRemove(tokenType.INTEGER).getTokenType();
						} else if (peek(0).getTokenType() == tokenType.REAL) {
							type = matchAndRemove(tokenType.REAL).getTokenType();
						} else if (peek(0).getTokenType() == tokenType.BOOLEAN) {
							type = matchAndRemove(tokenType.BOOLEAN).getTokenType();
						} else if (peek(0).getTokenType() == tokenType.CHAR) {
							type = matchAndRemove(tokenType.CHAR).getTokenType();
						} else if (peek(0).getTokenType() == tokenType.STRING) {
							type = matchAndRemove(tokenType.STRING).getTokenType();
						} else if (peek(0).getTokenType() == tokenType.ARRAY) {

							type = matchAndRemove(tokenType.ARRAY).getTokenType();

							// ARRAY handling
							if (peek(0).getTokenType() == tokenType.OF) {

								matchAndRemove(tokenType.OF);

								// check for valid array type
								if (peek(0).getTokenType() == tokenType.INTEGER) {
									arrayType = matchAndRemove(tokenType.INTEGER).getTokenType();
								} else if (peek(0).getTokenType() == tokenType.REAL) {
									arrayType = matchAndRemove(tokenType.REAL).getTokenType();
								} else if (peek(0).getTokenType() == tokenType.BOOLEAN) {
									arrayType = matchAndRemove(tokenType.BOOLEAN).getTokenType();
								} else if (peek(0).getTokenType() == tokenType.CHAR) {
									arrayType = matchAndRemove(tokenType.CHAR).getTokenType();
								} else if (peek(0).getTokenType() == tokenType.STRING) {
									arrayType = matchAndRemove(tokenType.STRING).getTokenType();
								} else {
									throw new SyntaxErrorException(
											"Expected valid array type, saw " + peek(0).toString());
								}

							}
							// token was not of
							else {
								throw new SyntaxErrorException("Expected OF after ARRAY, saw " + peek(0).toString());
							}

						}
						// token was not a valid shank type
						else {
							throw new SyntaxErrorException(
									"Expected a valid shank type after COLON, saw " + peek(0).toString());
						}

						// now we have the type, expect semicolon or nothing
						if (peek(0).getTokenType() == tokenType.SEMICOLON) {

							// more variables ahead, add current variable to list and loop back through
							matchAndRemove(tokenType.SEMICOLON);

							// how many identifiers have been found? -> i
							// so we need i number of variable nodes

							for (int j = 0; j <= i; j++) {
								// if parameter is an array call the array VariableNode constructor
								if (type == tokenType.ARRAY) {
									parameter = new VariableNode(names[j], type, changeable, from, to, arrayType);
									parameters.add(parameter);
									changeable = false;
								}
								// else call the regular constructor
								else {
									parameter = new VariableNode(names[j], type, changeable);
									parameters.add(parameter);
									changeable = false;
								}

							}
							i = 0;

						} else if (peek(0).getTokenType() == tokenType.RPAREN) {
							// no more variables in parameters, add current variable to list and break loop
							// do not remove rparen

							for (int j = 0; j <= i; j++) {
								// if parameter is an array call the array VariableNode constructor
								if (type == tokenType.ARRAY) {
									parameter = new VariableNode(names[j], type, changeable, from, to, arrayType);
									parameters.add(parameter);
								}
								// else call the regular constructor
								else {
									parameter = new VariableNode(names[j], type, changeable);
									parameters.add(parameter);
								}

							}
							break;

						} else {
							throw new SyntaxErrorException(
									"Expected RPAREN or SEMICOLON after type, saw " + peek(0).toString());
						}
					}
				}
			}

		}

		return parameters;
	}

	/**
	 * Returns a collection of VariableNodes containing the variables in a given
	 * function.
	 * 
	 * @return ArrayList of VariableNodes
	 * @throws SyntaxErrorException
	 */
	public ArrayList<VariableNode> variableDeclarations() throws SyntaxErrorException {

		ArrayList<VariableNode> variables = new ArrayList<VariableNode>();
		VariableNode variable;

		// arbitrary array size
		String[] names = new String[10];

		// members of VariableNode
		String name;
		int i = 0;
		tokenType type = null;
		Node value;
		boolean changeable = false;
		boolean hasTypeLimit = false;
		boolean realTypeLimit = false;
		float from = 0, to = 0;
		int intFrom = 0, intTo = 0;
		tokenType arrayType = null;

		// while there are constants or variables
		while (peek(0).getTokenType() == tokenType.CONSTANTS || peek(0).getTokenType() == tokenType.VARIABLES) {

			// if constants
			if (peek(0).getTokenType() == tokenType.CONSTANTS) {

				matchAndRemove(tokenType.CONSTANTS);

				while (true) {
					// expect identifier
					if (peek(0).getTokenType() == tokenType.IDENTIFIER) {

						name = matchAndRemove(tokenType.IDENTIFIER).getValue();

						// expect equals sign
						if (peek(0).getTokenType() == tokenType.EQUALS) {

							matchAndRemove(tokenType.EQUALS);

							// expect a valid shank type value
							if (peek(0).getTokenType() == tokenType.NUMBER) {

								boolean negativeMode = false;

								// if number is negative
								while (matchAndRemove(tokenType.MINUS) != null) {
									negativeMode = true;
								}

								// if number is a decimal, return new RealNode
								if (peek(0).getValue().contains(".")) {

									// uses parseFloat to convert string value of the token to a float value
									float fValue = Float.parseFloat(peek(0).getValue());
									matchAndRemove(tokenType.NUMBER);
									// check for sign
									if (negativeMode)
										value = new RealNode(-fValue);
									else
										value = new RealNode(fValue);

									type = tokenType.REAL;
								}
								// else if number is an integer, return new IntegerNode
								else {

									// uses parseInt to convert string value of the token to an int value
									int iValue = Integer.parseInt(peek(0).getValue());
									matchAndRemove(tokenType.NUMBER);
									// check for sign
									if (negativeMode)
										value = new IntegerNode(-iValue);
									else
										value = new IntegerNode(iValue);

									type = tokenType.INTEGER;
								}

							} else if (peek(0).getTokenType() == tokenType.TRUE) {
								value = new BooleanNode(true);
								type = tokenType.BOOLEAN;
								matchAndRemove(tokenType.TRUE);
							} else if (peek(0).getTokenType() == tokenType.FALSE) {
								value = new BooleanNode(false);
								type = tokenType.BOOLEAN;
								matchAndRemove(tokenType.FALSE);
							} else if (peek(0).getTokenType() == tokenType.CHARACTERLITERAL) {
								value = new CharacterNode(peek(0).getValue().charAt(0));
								type = tokenType.CHARACTERLITERAL;
								matchAndRemove(tokenType.CHARACTERLITERAL);
							} else if (peek(0).getTokenType() == tokenType.STRINGLITERAL) {
								value = new StringNode(peek(0).getValue());
								type = tokenType.STRINGLITERAL;
								matchAndRemove(tokenType.STRINGLITERAL);
							} else {
								throw new SyntaxErrorException(
										"[constants]: Expected valid type value after EQUALS, saw "
												+ peek(0).toString());
							}

							// got type and value, now look for endOfLine or comma

							// if endofline, done parsing constants
							if (peek(0).getTokenType() == tokenType.ENDOFLINE) {
								expectsEndOfLine();
								variable = new VariableNode(name, type, value, changeable);
								variables.add(variable);

								break;
							}
							// if no endofline, go back through the loop and look for more constants
							else if (peek(0).getTokenType() == tokenType.COMMA) {
								matchAndRemove(tokenType.COMMA);
								variable = new VariableNode(name, type, value, changeable);
								variables.add(variable);
							} else {
								throw new SyntaxErrorException(
										"[constants]: Expected ENDOFLINE or COMMA after value, saw "
												+ peek(0).toString());
							}

						} else {
							throw new SyntaxErrorException(
									"[constants]: Expected EQUALS after IDENTIFIER, saw " + peek(0).toString());
						}

					} else {
						throw new SyntaxErrorException(
								"[constants]: Expected IDENTIFIER after CONSTANTS, saw " + peek(0).toString());
					}
				} // while

			} // if constants

			// if variables
			else if (peek(0).getTokenType() == tokenType.VARIABLES) {

				changeable = true;
				matchAndRemove(tokenType.VARIABLES);

				// loop through variables
				while (true) {

					if (peek(0).getTokenType() == tokenType.IDENTIFIER) {

						names[i] = matchAndRemove(tokenType.IDENTIFIER).getValue();

						// if comma, more variables ahead
						if (peek(0).getTokenType() == tokenType.COMMA) {
							i++;
							matchAndRemove(tokenType.COMMA);
						}
						// else if colon, done with variables so add them to list
						else if (peek(0).getTokenType() == tokenType.COLON) {

							matchAndRemove(tokenType.COLON);
							// check for type
							if (peek(0).getTokenType() == tokenType.INTEGER) {
								type = matchAndRemove(tokenType.INTEGER).getTokenType();
							} else if (peek(0).getTokenType() == tokenType.REAL) {
								type = matchAndRemove(tokenType.REAL).getTokenType();
							} else if (peek(0).getTokenType() == tokenType.BOOLEAN) {
								type = matchAndRemove(tokenType.BOOLEAN).getTokenType();
							} else if (peek(0).getTokenType() == tokenType.CHAR) {
								type = matchAndRemove(tokenType.CHAR).getTokenType();
							} else if (peek(0).getTokenType() == tokenType.STRING) {
								type = matchAndRemove(tokenType.STRING).getTokenType();
							} else if (peek(0).getTokenType() == tokenType.ARRAY) {
								type = matchAndRemove(tokenType.ARRAY).getTokenType();
							} else {
								throw new SyntaxErrorException(
										"[variables]: Expected variable type, saw " + peek(0).toString());
							}

							// type limit & array handling
							if (type == tokenType.INTEGER || type == tokenType.REAL || type == tokenType.STRING
									|| type == tokenType.ARRAY) {

								if (peek(0).getTokenType() == tokenType.FROM) {

									if (type != tokenType.ARRAY)
										hasTypeLimit = true;

									matchAndRemove(tokenType.FROM);

									// expects a number
									if (peek(0).getTokenType() == tokenType.NUMBER) {

										// if the number is a float
										if (peek(0).getValue().contains(".")) {
											
											realTypeLimit = true;

											// type must be real for a float type limit
											if (type == tokenType.REAL) {
												from = Float.parseFloat(matchAndRemove(tokenType.NUMBER).getValue());
											} else {
												throw new SyntaxErrorException(
														"[variables]: Float FROM not defined for type "
																+ type.toString() + ", saw " + peek(0).toString());
											}

										}
										// else number is an integer
										else {
											intFrom = Integer.parseInt(matchAndRemove(tokenType.NUMBER).getValue());
										}

										// now expects TO
										if (peek(0).getTokenType() == tokenType.TO) {
											matchAndRemove(tokenType.TO);

											if (peek(0).getTokenType() == tokenType.NUMBER) {
												// if the number is a float
												if (peek(0).getValue().contains(".")) {

													// type must be real for a float type limit
													if (type == tokenType.REAL) {
														to = Float.parseFloat(
																matchAndRemove(tokenType.NUMBER).getValue());
													} else {
														throw new SyntaxErrorException(
																"[variables]: Float TO not defined for type "
																		+ type.toString() + ", saw "
																		+ peek(0).toString());
													}

												}
												// else number is an integer
												else {
													intTo = Integer.parseInt(matchAndRemove(tokenType.NUMBER).getValue());
												}

												// check for array type
												if (peek(0).getTokenType() == tokenType.OF) {

													if (type == tokenType.ARRAY) {

														matchAndRemove(tokenType.OF);

														if (peek(0).getTokenType() == tokenType.INTEGER) {
															arrayType = matchAndRemove(tokenType.INTEGER)
																	.getTokenType();
														} else if (peek(0).getTokenType() == tokenType.REAL) {
															arrayType = matchAndRemove(tokenType.REAL).getTokenType();
														} else if (peek(0).getTokenType() == tokenType.BOOLEAN) {
															arrayType = matchAndRemove(tokenType.BOOLEAN)
																	.getTokenType();
														} else if (peek(0).getTokenType() == tokenType.CHAR) {
															arrayType = matchAndRemove(tokenType.CHAR).getTokenType();
														} else if (peek(0).getTokenType() == tokenType.STRING) {
															arrayType = matchAndRemove(tokenType.STRING).getTokenType();
														} else {
															throw new SyntaxErrorException(
																	"[variables]: " + peek(0).toString()
																			+ " is not a valid array type.");
														}

													} else {
														throw new SyntaxErrorException(
																"[variables]: Type is not array, but saw OF token.");
													}

												}

											}
											// token was not a number
											else {
												throw new SyntaxErrorException("[variables]: Type " + peek(0).toString()
														+ " undefined for type TO.");
											}
										}
										// token was not TO
										else {
											throw new SyntaxErrorException(
													"[variables]: Expected TO token, saw " + peek(0).toString());
										}

									}
									// token was not NUMBER
									else {
										throw new SyntaxErrorException("[variables]: Type " + peek(0).toString()
												+ " undefined for type FROM.");
									}

								} // end type limit/array handling
							}

							for (int j = 0; j <= i; j++) {
								// if array
								if (arrayType != null) {
									variable = new VariableNode(names[j], type, changeable, from, to, arrayType);
									variables.add(variable);
								}
								// if variable with type limit
								else if (hasTypeLimit == true) {
									
									if(realTypeLimit) {
										variable = new VariableNode(names[j], type, changeable, from, to);
										variables.add(variable);
									}
									else {
										variable = new VariableNode(names[j], type, changeable, intFrom, intTo);
										variables.add(variable);
									}
								}
								// if variable without type limit
								else {
									variable = new VariableNode(names[j], type, changeable);
									variables.add(variable);
								}
							}
							break;

						} else {
							throw new SyntaxErrorException(
									"[variables]: Expected COLON or COMMA after IDENTIFIER, saw " + peek(0).toString());
						}

					} // if(identifier)
					else {
						throw new SyntaxErrorException(
								"[variables]: Expected IDENTIFIER after VARIABLES, saw " + peek(0).toString());
					}

				} // end while

			} // end outer while

		}

		return variables;

	}

	/**
	 * Returns a collection of StatementNodes containing statements in a given
	 * function.
	 * 
	 * @return ArrayList of StatementNodes
	 * @throws SyntaxErrorException
	 */
	public ArrayList<StatementNode> statements() throws SyntaxErrorException {

		// check for an indent
		if (peek(0).getTokenType() != tokenType.INDENT) {
			throw new SyntaxErrorException("Expected INDENT before statements, saw " + peek(0).toString());
		} else {
			matchAndRemove(tokenType.INDENT);
		}

		expectsEndOfLine();

		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		StatementNode statement = statement();

		// call statement() until it returns null and add statements to the list
		while (statement != null) {
			expectsEndOfLine();
			statements.add(statement);
			statement = statement();
		}

		expectsEndOfLine();

		// check for a dedent
		matchAndRemove(tokenType.DEDENT);

		return statements;
	}

	/**
	 * Returns a Node containing a single statement
	 * 
	 * @return
	 * @throws SyntaxErrorException
	 */
	public StatementNode statement() throws SyntaxErrorException {

		Node statement = null;
		expectsEndOfLine();

		// call each parsing method until we find one
		statement = assignment();
		expectsEndOfLine();
		if (statement != null)
			return (StatementNode) statement;
		
		statement = parseIf();
		expectsEndOfLine();
		if (statement != null)
			return (StatementNode) statement;
		
		statement = parseWhile();
		expectsEndOfLine();
		if (statement != null)
			return (StatementNode) statement;
		
		statement = parseRepeat();
		expectsEndOfLine();
		if (statement != null)
			return (StatementNode) statement;
		
		statement = parseFor();
		expectsEndOfLine();
		if (statement != null)
			return (StatementNode) statement;
		
		statement = parseFunctionCalls();
		expectsEndOfLine();
		if (statement != null)
			return (StatementNode) statement;

		return null;

	}

	public IfNode parseIf() throws SyntaxErrorException {

		IfNode ifNode;
		Node condition = null;
		ArrayList<StatementNode> statements;
		IfNode NextIf = null;
		boolean elseMode = false;

		if (peek(0) == null)
			return null;

		if (peek(0).getTokenType() == tokenType.IF || peek(0).getTokenType() == tokenType.ELSIF
				|| peek(0).getTokenType() == tokenType.ELSE) {

			matchAndRemove(tokenType.IF);
			matchAndRemove(tokenType.ELSIF);

			if (matchAndRemove(tokenType.ELSE) != null) {
				expectsEndOfLine();
				elseMode = true;
			}

			// now expect a condition
			if (elseMode == false)
				condition = boolCompare();

			if (condition != null || elseMode == true) {
				if (peek(0).getTokenType() == tokenType.THEN || elseMode == true) {
					matchAndRemove(tokenType.THEN);
					expectsEndOfLine();

					// now expects indent
					if (peek(0).getTokenType() == tokenType.INDENT) {
						
						statements = statements();

						// expects elsif or else or nothing and creates IfNode with appropriate
						// constructor
						if (peek(0) != null && (peek(0).getTokenType() == tokenType.ELSIF
								|| peek(0).getTokenType() == tokenType.ELSE)) {
							NextIf = parseIf();
							return ifNode = new IfNode((BooleanCompareNode) condition, statements, NextIf);
						} else {
							return ifNode = new IfNode((BooleanCompareNode) condition, statements);
						}

					} else {
						if (elseMode == false)
							throw new SyntaxErrorException("Expected INDENT after THEN, saw " + peek(0).toString());
						else
							throw new SyntaxErrorException("Expected INDENT after ELSE, saw " + peek(0).toString());
					}

				} else {
					throw new SyntaxErrorException("Expected THEN after condition, saw " + peek(0).toString());
				}
			} else {
				throw new SyntaxErrorException("Expected condition, saw " + peek(0).toString());
			}
		} else {
			return null;
		}

	}

	public WhileNode parseWhile() throws SyntaxErrorException {

		WhileNode whileNode;
		Node condition;
		ArrayList<StatementNode> statements;

		if (peek(0) == null)
			return null;

		// expects while
		if (peek(0).getTokenType() == tokenType.WHILE) {
			matchAndRemove(tokenType.WHILE);

			// now expect a condition
			condition = boolCompare();

			if (condition != null) {
				expectsEndOfLine();

				// expects indent
				if (peek(0).getTokenType() == tokenType.INDENT) {

					statements = statements();
					return whileNode = new WhileNode((BooleanCompareNode) condition, statements);

				} else {
					throw new SyntaxErrorException("Expected INDENT after condition, saw " + peek(0).toString());
				}
			} else {
				throw new SyntaxErrorException("Expected condition, saw " + peek(0).toString());
			}
		} else {
			return null;
		}

	}

	public RepeatNode parseRepeat() throws SyntaxErrorException {

		RepeatNode repeatNode;
		BooleanCompareNode condition;
		ArrayList<StatementNode> statements;

		if (peek(0) == null)
			return null;

		// expects repeat
		if (peek(0).getTokenType() == tokenType.REPEAT) {
			matchAndRemove(tokenType.REPEAT);

			// expects until
			if (peek(0).getTokenType() == tokenType.UNTIL) {
				matchAndRemove(tokenType.UNTIL);

				// expects condition
				condition = (BooleanCompareNode) boolCompare();
				if (condition != null) {
					expectsEndOfLine();

					// expects indent
					if (peek(0).getTokenType() == tokenType.INDENT) {

						statements = statements();

						return repeatNode = new RepeatNode(condition, statements);

					} else {
						throw new SyntaxErrorException("Expected INDENT after condition, saw " + peek(0).toString());
					}
				} else {
					throw new SyntaxErrorException("Expected condition, saw " + peek(0).toString());
				}

			} else {
				throw new SyntaxErrorException("Expected UNTIL after REPEAT, saw " + peek(0).toString());
			}
		} else {
			return null;
		}

	}

	public ForNode parseFor() throws SyntaxErrorException {

		ForNode forNode;
		VariableReferenceNode iterator;
		String name;
		ArrayList<StatementNode> statements;
		Node from;
		Node to;

		if (peek(0) == null)
			return null;

		expectsEndOfLine();

		// expects for
		if (peek(0).getTokenType() == tokenType.FOR) {
			matchAndRemove(tokenType.FOR);

			// expects iterator
			if (peek(0).getTokenType() == tokenType.IDENTIFIER) {
				name = matchAndRemove(tokenType.IDENTIFIER).getValue();
				iterator = new VariableReferenceNode(name);

				// expects FROM
				if (peek(0).getTokenType() == tokenType.FROM) {
					matchAndRemove(tokenType.FROM);

					from = expression();
					IntegerNode fromNode = (IntegerNode) from;
					int intFrom = fromNode.getValue();

					if (from != null) {
						
						// expects TO
						if (peek(0).getTokenType() == tokenType.TO) {
							matchAndRemove(tokenType.TO);

							to = expression();
							IntegerNode intNode = (IntegerNode) to;
							int intTo = intNode.getValue();

							if (to != null) {
								expectsEndOfLine();

								// expects indent
								if (peek(0).getTokenType() == tokenType.INDENT) {

									statements = statements();

									return forNode = new ForNode(iterator, statements, intFrom, intTo);

								} else {
									throw new SyntaxErrorException(
											"Expected INDENT after \'for\' statement, saw " + peek(0).toString());
								}
							} else {
								throw new SyntaxErrorException(
										"Upper bound invalid for for-loop.");
							}
						} else {
							throw new SyntaxErrorException("Expected token TO after integer variable, saw " + peek(0).toString());
						}

					} else {
						throw new SyntaxErrorException("Lower bound invalid for for-loop.");
					}
				} else {
					throw new SyntaxErrorException("Expected FROM after IDENTIFIER, saw " + peek(0).toString());
				}

			} else {
				throw new SyntaxErrorException("Expected IDENTIFIER after FOR, saw " + peek(0).toString());
			}

		} else {
			return null;
		}

	}

	public Node parseFunctionCalls() throws SyntaxErrorException {

		String name = null;
		ArrayList<Node> parameters = new ArrayList<Node>();
		Node parameter;
		String paramName;
		Node value;

		if (peek(0) == null)
			return null;

		// expects identifier
		if (peek(0).getTokenType() == tokenType.IDENTIFIER) {

			if(peek(1).getTokenType() != tokenType.IDENTIFIER && peek(1).getTokenType() != tokenType.STRINGLITERAL && peek(1).getTokenType() != tokenType.NUMBER && peek(1).getTokenType() != tokenType.VAR) {
				return null;
			}
			
			name = matchAndRemove(tokenType.IDENTIFIER).getValue();
			
			// look for parameters, loop through to expect multiple parameters separated by
			// comma
			
			while(true) {
				if (peek(0).getTokenType() == tokenType.NUMBER) {
					value = boolCompare();
					parameter = new ParameterNode(value);
					parameters.add(parameter);
				}
				else if (peek(0).getTokenType() == tokenType.IDENTIFIER) {
					paramName = matchAndRemove(tokenType.IDENTIFIER).getValue();
					value = new VariableReferenceNode(paramName);
					parameter = new ParameterNode(value);
					parameters.add(parameter);
				}
				else if (peek(0).getTokenType() == tokenType.STRINGLITERAL) {
					paramName = matchAndRemove(tokenType.STRINGLITERAL).getValue();
					value = new StringNode(paramName);
					parameter = new ParameterNode(value);
					parameters.add(parameter);
				}
				// now look for parameters of type var
				else if (peek(0).getTokenType() == tokenType.VAR) {
					matchAndRemove(tokenType.VAR);
					if (peek(0).getTokenType() == tokenType.IDENTIFIER) {
						paramName = matchAndRemove(tokenType.IDENTIFIER).getValue();
						value = new VariableReferenceNode(paramName);
						parameter = new ParameterNode((VariableReferenceNode) value, true);
						parameters.add(parameter);
					} else {
						throw new SyntaxErrorException("Expected IDENTIFIER after VAR, saw " + peek(0).toString());
					}
				} // end if var
				
				if (matchAndRemove(tokenType.COMMA) != null)
					continue;
				else
					break;
			}
			
			// now we have everything we need for a function call, create FunctionCallNode
			return new FunctionCallNode(name, parameters);
			
		} else {
			return null;
		}

	}

	/**
	 * Returns an AssignmentNode containing a target variable and a value.
	 * 
	 * @return an AssignmentNode
	 * @throws SyntaxErrorException
	 */
	public AssignmentNode assignment() throws SyntaxErrorException {

		VariableReferenceNode variable = null;
		Node value = null;

		if (peek(0) == null)
			return null;

		if (peek(0).getTokenType() == tokenType.IDENTIFIER) {
			String name = peek(0).getValue();
			// if there is an LBRACK indicating an array index
			if (peek(1).getTokenType() == tokenType.LBRACK) {
				matchAndRemove(tokenType.IDENTIFIER);
				matchAndRemove(tokenType.LBRACK);
				// expect an index (number or identifier)
				if (peek(0).getTokenType() == tokenType.NUMBER || peek(0).getTokenType() == tokenType.IDENTIFIER) {
					Node arrayIndex = expression();
					// expect an RBRACK
					if (peek(0).getTokenType() == tokenType.RBRACK) {
						matchAndRemove(tokenType.RBRACK);
						variable = new VariableReferenceNode(name, arrayIndex);
					}
					// if no RBRACK after index, exception
					else {
						throw new SyntaxErrorException("Expected RBRACK after array index, saw " + peek(0).toString());
					}
				}
				// if no index after LBRACK, exception
				else {
					throw new SyntaxErrorException("Undefined array index, saw " + peek(0).toString());
				}
			}
			// else if no array index
			else {
				variable = new VariableReferenceNode(name);
			}

			// now we have variableReferenceNode, look for assignment token
			if (peek(0).getTokenType() == tokenType.ASSIGNMENT || peek(1).getTokenType() == tokenType.ASSIGNMENT) {

				// now that we are sure this is an assignment statement, remove the identifier
				// for variable name
				matchAndRemove(tokenType.IDENTIFIER);
				matchAndRemove(tokenType.ASSIGNMENT);

				// now we call boolCompare to get the value for assignment
				value = boolCompare();

				if (value != null) {
					return new AssignmentNode(variable, value);
				} else {
					throw new SyntaxErrorException("Right side is not a valid expression");
				}

			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	/**
	 * Returns either an expression or a BooleanCompareNode containing a left and
	 * right expression and a comparison operator.
	 * 
	 * @return Node
	 * @throws SyntaxErrorException
	 */
	public Node boolCompare() throws SyntaxErrorException {

		Token comparison = null;
		Node node1 = expression();

		// check for a comparison operator
		if (peek(0).getTokenType() == tokenType.GREATER)
			comparison = matchAndRemove(tokenType.GREATER);
		else if (peek(0).getTokenType() == tokenType.LESS)
			comparison = matchAndRemove(tokenType.LESS);
		else if (peek(0).getTokenType() == tokenType.EQUALS)
			comparison = matchAndRemove(tokenType.EQUALS);
		else if (peek(0).getTokenType() == tokenType.NOTEQUAL)
			comparison = matchAndRemove(tokenType.NOTEQUAL);
		else if (peek(0).getTokenType() == tokenType.LESSOREQUAL)
			comparison = matchAndRemove(tokenType.LESSOREQUAL);
		else if (peek(0).getTokenType() == tokenType.GREATEROREQUAL)
			comparison = matchAndRemove(tokenType.GREATEROREQUAL);

		// if there is none, return expression
		if (comparison == null)
			return node1;

		// if there is a comparison, return BooleanCompareNode
		else {
			Node node2 = expression();

			if (comparison.getTokenType() == tokenType.GREATER)
				return new BooleanCompareNode(boolComparison.GREATER, node1, node2);
			if (comparison.getTokenType() == tokenType.LESS)
				return new BooleanCompareNode(boolComparison.LESS, node1, node2);
			if (comparison.getTokenType() == tokenType.EQUALS)
				return new BooleanCompareNode(boolComparison.EQUALS, node1, node2);
			if (comparison.getTokenType() == tokenType.NOTEQUAL)
				return new BooleanCompareNode(boolComparison.NOTEQUAL, node1, node2);
			if (comparison.getTokenType() == tokenType.GREATEROREQUAL)
				return new BooleanCompareNode(boolComparison.GREATEROREQUAL, node1, node2);
			if (comparison.getTokenType() == tokenType.LESSOREQUAL)
				return new BooleanCompareNode(boolComparison.LESSOREQUAL, node1, node2);
		}

		return null;

	}

	/**
	 * Calls term to create a node of the AST.
	 * 
	 * @return The new node of the AST.
	 * 
	 * @throws SyntaxErrorException
	 */
	public Node expression() throws SyntaxErrorException {

		Node node1 = term();

		while (true) {

			Token operator = null;

			if (peek(0) != null) {
				// looks for + or -
				if (peek(0).getTokenType() == tokenType.PLUS)
					operator = matchAndRemove(tokenType.PLUS);
				else if (peek(0).getTokenType() == tokenType.MINUS)
					operator = matchAndRemove(tokenType.MINUS);
			}

			// when there are no more + or - tokens, loop breaks
			if (operator == null)
				break;

			Node node2 = term();

			// create new MathOpNode
			if (operator.getTokenType() == tokenType.PLUS)
				node1 = new MathOpNode(operation.ADD, node1, node2);
			else if (operator.getTokenType() == tokenType.MINUS)
				node1 = new MathOpNode(operation.SUBTRACT, node1, node2);

		}

		return node1;
	}

	/**
	 * Calls factor to create a node of the AST.
	 * 
	 * @return
	 * 
	 * @throws SyntaxErrorException
	 */
	public Node term() throws SyntaxErrorException {

		Node node1 = factor();

		while (true) {

			Token operator = null;

			if (peek(0) != null) {
				// looks for * or /
				if (peek(0).getTokenType() == tokenType.MULTIPLY)
					operator = matchAndRemove(tokenType.MULTIPLY);
				else if (peek(0).getTokenType() == tokenType.DIVIDE)
					operator = matchAndRemove(tokenType.DIVIDE);
				else if (peek(0).getTokenType() == tokenType.MOD)
					operator = matchAndRemove(tokenType.MOD);
			}

			// when there are no more * or / or mod tokens, loop breaks
			if (operator == null)
				break;

			Node node2 = factor();

			// create new MathOpNode
			if (operator.getTokenType() == tokenType.MULTIPLY)
				node1 = new MathOpNode(operation.MULTIPLY, node1, node2);
			else if (operator.getTokenType() == tokenType.DIVIDE)
				node1 = new MathOpNode(operation.DIVIDE, node1, node2);
			else if (operator.getTokenType() == tokenType.MOD)
				node1 = new MathOpNode(operation.MOD, node1, node2);

		}

		return node1;

	}

	/*
	 * factor() FACTOR = {-} number or lparen EXPRESSION rparen
	 */

	/**
	 * Creates a new IntegerNode or RealNode.
	 * 
	 * @return The new node
	 * 
	 * @throws SyntaxErrorException
	 */
	public Node factor() throws SyntaxErrorException {

		boolean negativeMode = false;
		int i = 0;

		// if there are minus tokens, remove and count them
		while (matchAndRemove(tokenType.MINUS) != null) {
			i++;
		}

		// if # of minus tokens are odd, negative (i.e. --x = x, but ---x = -x)
		if (i % 2 == 1)
			negativeMode = true;

		if (peek(0) != null
				&& (peek(0).getTokenType() == tokenType.NUMBER || peek(0).getTokenType() == tokenType.IDENTIFIER)) {

			// if number is a decimal, return new RealNode
			if (peek(0).getValue().contains(".")) {

				// uses parseFloat to convert string value of the token to a float value
				float value = Float.parseFloat(peek(0).getValue());
				matchAndRemove(tokenType.NUMBER);

				if (negativeMode)
					return new RealNode(-value);
				else
					return new RealNode(value);

			}

			// else if number is an integer, return new IntegerNode
			else if (peek(0).getValue().matches("-?\\d+(\\.\\d+)?")) {
				
				System.out.println("peek(0): " +peek(0));

				// uses parseInt to convert string value of the token to an int value
				int value = Integer.parseInt(peek(0).getValue());
				matchAndRemove(tokenType.NUMBER);

				if (negativeMode)
					return new IntegerNode(-value);
				else
					return new IntegerNode(value);

			}
			// else there is an identifier, return new VariableReferenceNode
			else {
				String name = matchAndRemove(tokenType.IDENTIFIER).getValue();
				// if there is an LBRACK indicating an array index
				if (peek(0).getTokenType() == tokenType.LBRACK) {
					matchAndRemove(tokenType.LBRACK);
					// expect an index (number or identifier)
					if (peek(0).getTokenType() == tokenType.NUMBER || peek(0).getTokenType() == tokenType.IDENTIFIER) {
						Node arrayIndex = expression();
						// expect an RBRACK then create new VariableReferenceNode with array constructor
						if (peek(0).getTokenType() == tokenType.RBRACK) {
							matchAndRemove(tokenType.RBRACK);
							return new VariableReferenceNode(name, arrayIndex);
						}
						// if no RBRACK after index, exception
						else {
							throw new SyntaxErrorException(
									"Expected RBRACK after array index, saw " + peek(0).toString());
						}
					}
					// if no index after LBRACK, exception
					else {
						throw new SyntaxErrorException(peek(0).toString() + " is not a valid array index.");
					}
				}
				// else if no array index
				else {
					return new VariableReferenceNode(name);
				}

			}

		} else if (peek(0) != null && peek(0).getTokenType() == tokenType.LPAREN) {

			matchAndRemove(tokenType.LPAREN);
			Node expression = expression();

			if (expression != null) {

				if (peek(0).getTokenType() == tokenType.RPAREN) {
					matchAndRemove(tokenType.RPAREN);
					return expression;
				} else {
					throw new SyntaxErrorException("Expected RPAREN after expression, saw " + peek(0).toString());
				}

			}

			else {
				throw new SyntaxErrorException("No valid expression found between parenthesis.");
			}

		} else if (peek(0) == null) {
			return null;
		} else {
			throw new SyntaxErrorException("factor(): Expected NUMBER or LPAREN, saw " + peek(0).toString());
		}

	}

	/**
	 * Accepts a tokenType and checks if the next token in the list matches. Removes
	 * and returns the token if so, returns null if not.
	 * 
	 * @param type The tokenType to check.
	 * 
	 * @return The next token in the list or null.
	 */
	private Token matchAndRemove(tokenType type) {

		// if next token matches passed in token type and there are more tokens in the
		// list, remove and return the next token
		if (tokens.size() > 0 && (tokens.get(0)).getTokenType() == type) {
			return tokens.remove(0);
		}
		// else if next token does not match passed in token type or there are no more
		// tokens in the list, return null
		else {
			return null;
		}

	}

	/**
	 * Utilizes matchAndRemove to match and remove one or more ENDOFLINE tokens.
	 * 
	 * @throws SyntaxErrorException If no ENDOFLINE token is found.
	 */
	private void expectsEndOfLine() throws SyntaxErrorException {

		// if next token is ENDOFLINE, token = ENDOFLINE. Otherwise token = null
		Token token = matchAndRemove(tokenType.ENDOFLINE);

		// throw SyntaxErrorException if ENDOFLINE not found
		if (token == null) {
			return;
		}

		// loop through list of tokens and remove any subsequent ENDOFLINE tokens
		while (matchAndRemove(tokenType.ENDOFLINE) != null)
			;
	}

	/**
	 * Accepts an integer and looks ahead that number of tokens and returns that
	 * token. Returns null if there are not enough tokens.
	 * 
	 * @param i The number of tokens to look ahead to.
	 * 
	 * @return The token at the index or null.
	 */
	private Token peek(int i) {

		// if there are enough tokens to fulfill the request, return the token at the
		// index
		if (i >= 0 && i < tokens.size()) {
			return tokens.get(i);
		}
		// else if there are not enough tokens, return null
		else {
			return null;
		}

	}

}
