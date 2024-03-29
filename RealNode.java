package shank_interpreter;


/**
 * A Real Node contains a value and has one parent Node.
 * 
 * @author Ryan Dugan
 *
 */
public class RealNode extends Node {

	private float value;

	/**
	 * Constructs a new RealNode
	 * 
	 * @param value The float value of the RealNode
	 */
	public RealNode(float value) {
		this.value = value;
	}

	/**
	 * Returns value of RealNode
	 * @return float value
	 */
	public float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value + "";
	}
}
