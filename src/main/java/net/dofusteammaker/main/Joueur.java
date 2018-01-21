package net.dofusteammaker.main;

public class Joueur {
	int lvl;
	Classes classe;
	String pseudo;
	String userId; //ID discord
	
	public Joueur(String pseudo, Classes classe, int lvl, String userId) {
		this.lvl = lvl;
		this.pseudo = pseudo;
		this.userId = userId;
		this.classe = classe ;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
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
	public Classes getClasse() {
		return classe;
	}

	public void setClasse(Classes classe) {
		this.classe = classe;
	}
	
}
