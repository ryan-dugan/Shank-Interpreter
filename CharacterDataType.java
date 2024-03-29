package shank_interpreter;


public class CharacterDataType extends InterpreterDataType {

	char value;
	
	public CharacterDataType(char value) {
		this.value = value;
	}
	
	public CharacterDataType() {
		this.value = (Character) null;
	}

	@Override
	public String toString() {
		return ""+ value;
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}
	
	public char getValue() {
		return value;
	}
	
	public void setValue(char value) {
		this.value = value;
	}
	
}
