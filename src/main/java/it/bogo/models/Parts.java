package it.bogo.models;

import java.io.Serializable;

public class Parts implements Serializable  {

	private String datablock;
	private String IV;
	private String key;
	private String hmac;
	private int blocklength;
	
	public Parts() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDatablock() {
		return datablock;
	}

	public void setDatablock(String datablock) {
		this.datablock = datablock;
	}

	public String getIV() {
		return IV;
	}

	public void setIV(String iV) {
		IV = iV;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

	public int getBlocklength() {
		return blocklength;
	}

	public void setBlocklength(int blocklength) {
		this.blocklength = blocklength;
	}
	
	
	
	
}
