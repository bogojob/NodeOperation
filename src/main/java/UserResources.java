/**
 * 
 */


import java.util.List;

import it.bogo.models.Node;

/**
 * @author toor
 *
 */


public class UserResources {
	
	int userid;
	String path;
	int size; 
	static List<Node> nodes;
	public UserResources() {super();}
	public UserResources(int userid, String path, List<Node> nodeList) {
		super();
		this.userid = userid;
		this.path = path;
		this.nodes = nodeList;
	}
	

	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}


	public static List<Node> getNodes() {
		return nodes;
	}


	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}



	

}


