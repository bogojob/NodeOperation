package it.bogo.models;


import java.io.Serializable;
import java.security.SecureRandom;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;




public class Node implements Serializable  {
	private String id;
	private String name;
	private String type ;
	private String mime ;
	private int size;
	@JsonInclude(Include.NON_NULL)
	private List<Node> childs;
	@JsonInclude(Include.NON_NULL)
	private List<String> parts;
	

	
	public Node() {
		super();
		
	}

	public Node(String type) {
		super();
		
		this.setId(getRandomHex());
		this.type = type;
//		if (type == Constants.NODE_TYPE_FILE) {
//			this.filedescription = new FileDescription();
//		}
	}

	public Node(String name, String type, String mime, int size, List<Node> childs, List<String> parts) {
		
		super();
		this.id = getRandomHex();
		this.name = name;
		this.type = type;
		this.mime = mime;
		this.size = size;
		this.childs = childs;
		this.parts = parts;

//		if (optional.length > 0 && type == Constants.NODE_TYPE_FILE) {
//			this.filedescription = optional[0];
//		} else {
//			this.filedescription = new FileDescription();
//		}
	}
	
	
	private String getRandomHex() {
		SecureRandom random = new SecureRandom();
		String s = String.format("%06x", random.nextInt(0x1000000));
		return s;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMime() {
		return mime;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<Node> getChilds() {
		return childs;
	}
	public void setChilds(List<Node> childs) {
		this.childs = childs;
	}
	public List<String> getParts() {
		return parts;
	}
	public void setParts(List<String> parts) {
		this.parts = parts;
	}

	
	
	
}





