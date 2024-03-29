package shank_interpreter;


/**
 * A BooleanCompareNode contains a boolean operator and has 2 child nodes.
 * 
 * @author Ryan Dugan
 */
public class BooleanCompareNode extends Node {

	/**
	 * An enumeration representing the different boolean boolComparisons
	 */
	public enum boolComparison {
		GREATER, LESS, EQUALS, NOTEQUAL, GREATEROREQUAL, LESSOREQUAL
	}

	public boolComparison bc;
	public Node left;
	public Node right;

	/**
	 * Constructs a new BooleanCompareNode.
	 * 
	 * @param op    The boolComparison of the BooleanCompareNode
	 * @param left  The left child
	 * @param right The right child
	 */
	public BooleanCompareNode(boolComparison bc, Node left, Node right) {
		this.bc = bc;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		String operator;
		if(bc.equals(boolComparison.GREATER)) {
			operator = ">";
		}
		else if(bc.equals(boolComparison.LESS)) {
			operator = "<";
		}
		else if(bc.equals(boolComparison.EQUALS)) {
			operator = "=";
		}
		else if(bc.equals(boolComparison.NOTEQUAL)) {
			operator = "!=";
		}
		else if(bc.equals(boolComparison.GREATEROREQUAL)) {
			operator = ">=";
		}
		else {
			operator = "<=";
		}

		return "BooleanCompareNode(" + left.toString() + " " + operator + " " + right.toString() + ")";
	}

}
