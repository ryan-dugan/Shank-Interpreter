package shank_interpreter;


public class CharacterNode extends Node {
	
	private char value;
	
	public CharacterNode (char value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CharacterNode(" + value + ")";
	}

	public char getValue() {
		return value;
	}
	
	

}
