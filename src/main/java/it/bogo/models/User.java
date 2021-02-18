package it.bogo.models;


import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class User implements Serializable {

	private String iduser;
	private String mail;
	private String firstname;
	private String lastname;
	private String crv;
	private String iv;
	private String emk;
	private String hak;
	private String rsapub;
	private String rsapriv;
	private String ed25519pub;
	private String ed25519priv;
	private String curve25519pub;
	private String curve25519priv;
	private String cc;
	@JsonInclude(Include.NON_NULL)
	private UserFileSystem userfilesystem;
	

	public User() {
		super();
		this.iduser = getRandomHex();
		//this.mail="pippo@tiscali.it";
		
		//this.filesystem = new HashMap<>();
		//List<Node> nodeList = new ArrayList<>();
		//nodeList.add(new Node(Costants.NODE_TYPE_DIR));
		//filesystem.put("nodes", nodeList);
		

	}

	private String getRandomHex() {
		SecureRandom random = new SecureRandom();
		String s = String.format("%06x", random.nextInt(0x1000000));
		return s;
	}

	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCrv() {
		return crv;
	}

	public void setCrv(String crv) {
		this.crv = crv;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getEmk() {
		return emk;
	}

	public void setEmk(String emk) {
		this.emk = emk;
	}

	public String getHak() {
		return hak;
	}

	public void setHak(String hak) {
		this.hak = hak;
	}

	public String getRsapub() {
		return rsapub;
	}

	public void setRsapub(String rsapub) {
		this.rsapub = rsapub;
	}

	public String getRsapriv() {
		return rsapriv;
	}

	public void setRsapriv(String rsapriv) {
		this.rsapriv = rsapriv;
	}

	public String getEd25519pub() {
		return ed25519pub;
	}

	public void setEd25519pub(String ed25519pub) {
		this.ed25519pub = ed25519pub;
	}

	public String getEd25519priv() {
		return ed25519priv;
	}

	public void setEd25519priv(String ed25519priv) {
		this.ed25519priv = ed25519priv;
	}

	public String getCurve25519pub() {
		return curve25519pub;
	}

	public void setCurve25519pub(String curve25519pub) {
		this.curve25519pub = curve25519pub;
	}

	public String getCurve25519priv() {
		return curve25519priv;
	}

	public void setCurve25519priv(String curve25519priv) {
		this.curve25519priv = curve25519priv;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public UserFileSystem getUserfilesystem() {
		return userfilesystem;
	}

	public void setUserfilesystem(UserFileSystem userfilesystem) {
		this.userfilesystem = userfilesystem;
	}

	
}
