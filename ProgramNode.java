package shank_interpreter;


import java.util.*;

public class ProgramNode extends Node {

	
	HashMap<String, FunctionNode> hashmap = new HashMap<String, FunctionNode>();
	
	public ProgramNode(HashMap<String, FunctionNode> hashmap) {
		this.hashmap = hashmap;
	}
	
	@Override
	public String toString() {
		HashMap<String, FunctionNode> hashPrint = getHashmap();
		hashPrint.values().removeAll(Collections.singleton(null));
		String output = "ProgramNode\n" + hashPrint.values();
		return output;
	}

	public HashMap<String, FunctionNode> getHashmap() {
		return hashmap;
	}
	
}
