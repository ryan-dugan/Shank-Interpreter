package shank_interpreter;


/**
 * An Integer Node contains an integer value and has one parent Node.
 * 
 * @author Ryan Dugan
 */
public class IntegerNode extends Node {

	private int value;

	/**
	 * Constructs a new Integer Node
	 * 
	 * @param value The integer value of the IntegerNode
	 */
	public IntegerNode(int value) {
		this.value = value;
	}
	
	/**
	 * Returns value of IntegerNode
	 * @return integer value
	 */
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value + "";
	}

}
