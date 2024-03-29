package shank_interpreter;


/**
 * A MathOpNode contains a math operator and has 2 child nodes.
 * 
 * @author Ryan Dugan
 */
public class MathOpNode extends Node {

	/**
	 * An enumeration representing the different math operations
	 */
	public enum operation {
		ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD
	}

	public operation op;
	public Node left;
	public Node right;

	/**
	 * Constructs a new MathOpNode.
	 * 
	 * @param op    The operation of the MathOpNode
	 * @param left  The left child
	 * @param right The right child
	 */
	public MathOpNode(operation op, Node left, Node right) {
		this.op = op;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		String operator;
		if(op.equals(operation.ADD)) {
			operator = "+";
		}
		else if(op.equals(operation.SUBTRACT)) {
			operator = "-";
		}
		else if(op.equals(operation.MULTIPLY)) {
			operator = "*";
		}
		else if(op.equals(operation.DIVIDE)) {
			operator = "/";
		}
		else
			operator = "mod";
		
		return "MathOpNode(" + left.toString() + " " + operator + " " + right.toString() + ")";
	}

}
