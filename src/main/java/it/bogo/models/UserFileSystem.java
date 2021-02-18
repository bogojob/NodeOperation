package it.bogo.models;


import java.io.Serializable;
import java.util.List;


public class UserFileSystem implements Serializable {
	private int size;
	private List<Node> nodes;

	public UserFileSystem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	
	


	
}
