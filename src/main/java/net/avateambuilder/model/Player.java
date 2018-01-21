package net.avateambuilder.model;

import org.json.JSONObject;

public class Player {

	public enum Classe { 
		eniripsa,
		iop,
		sacrieur,
		huppermage,
		osamodas,
		sadida,
		xelor,
		cra,
		steamer,
		eliotrope,
		ecaflip,
		pandawa,
		sram,
		ouginak,
		feca,
		zobal,
		roublard,
		enutrof,
		unknown
	}

	int level;
	Classe classe;
	String pseudo;
	String userId; //ID discord
	
	public Player(String userId) {
		this.userId = userId;
	}
	
	public Player(String pseudo, String classe, int lvl, String userId) {
		this.level = lvl;
		this.pseudo = pseudo;
		this.userId = userId;
		this.classe = ToClasse(classe);
	}
	
	public Player(JSONObject obj) {
		this.userId = obj.getString("UserID");
		this.classe = ToClasse(obj.getString("Classe"));
		this.pseudo = obj.getString("Pseudo");
		this.level = obj.getInt("Level");
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int lvl) {
		this.level = lvl;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}
	
	public Classe ToClasse(String input) {
		Classe classe = Classe.unknown;
		input = input.toLowerCase();
		if (input.equals("eniripsa") || input.equals("eni"))
		{
			classe = Classe.eniripsa;
		}
		if (input.equals("iop"))
		{
			classe = Classe.iop;
		}
		if (input.equals("sacrieur") || input.equals("sacri"))
		{
			classe = Classe.sacrieur;
		}
		if (input.equals("huppermage") || input.equals("hupper"))
		{
			classe = Classe.huppermage;
		}
		if (input.equals("osamodas") || input.equals("osa"))
		{
			classe = Classe.osamodas;
		}
		if (input.equals("sadida") || input.equals("sadi"))
		{
			classe = Classe.sadida;
		}
		if (input.equals("xelor") || input.equals("xel"))
		{
			classe = Classe.xelor;
		}
		if (input.equals("cra"))
		{
			classe = Classe.cra;
		}
		if (input.equals("steamer") || input.equals("steam"))
		{
			classe = Classe.steamer;
		}
		if (input.equals("eliotrope") || input.equals("elio"))
		{
			classe = Classe.eliotrope;
		}
		if (input.equals("ecaflip") || input.equals("eca"))
		{
			classe = Classe.ecaflip;
		}
		if (input.equals("pandawa") || input.equals("panda"))
		{
			classe = Classe.pandawa;
		}
		if (input.equals("sram"))
		{
			classe = Classe.sram;
		}
		if (input.equals("ouginak") || input.equals("ougi"))
		{
			classe = Classe.ouginak;
		}
		if (input.equals("feca"))
		{
			classe = Classe.feca;
		}
		if (input.equals("zobal") || input.equals("zob"))
		{
			classe = Classe.zobal;
		}
		if (input.equals("roublard") || input.equals("roub"))
		{
			classe = Classe.roublard;
		}
		if (input.equals("enutrof") || input.equals("enu"))
		{
			classe = Classe.enutrof;
		}
		return classe;
	}

	@Override
	public boolean equals(Object object){
		return (object.getClass().equals(Player.class)) && this.userId.equals(((Player)object).userId);
	}
	
	public JSONObject ToJson() {
        JSONObject obj = new JSONObject();
        obj.put("UserID", userId);
        obj.put("Classe", classe);
        obj.put("Pseudo", pseudo);
        obj.put("Level", level);
		return obj;
	}
}
