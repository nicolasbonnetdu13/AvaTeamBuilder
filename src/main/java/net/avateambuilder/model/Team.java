package net.avateambuilder.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.avateambuilder.model.Player.Classe;

public class Team {
	private int id;
	List<Player> members;
	int levelMinimum;
	int levelMaximum;
		
	public Team(int id, int levelMinimum, int levelMaximum) {
		this.id = id;
		this.members = new ArrayList<Player>();
		this.levelMinimum = levelMinimum;
		this.levelMaximum = levelMaximum;
	}
	
	public Team(JSONObject objectJson) {
		this.id = objectJson.getInt("Id");
		this.levelMinimum = objectJson.getInt("LevelMinimum");
		this.levelMaximum = objectJson.getInt("LevelMaximum");
		this.members = new ArrayList<Player>();
        JSONArray membersJson = objectJson.getJSONArray("Members");
        for (Object object : membersJson) {
			if(object.getClass().equals(JSONObject.class)) {
				Player player = new Player((JSONObject)object);
				this.members.add(player);
			}
		}
	}
	
	public boolean ContainsClasse(Classe classe) {
		for (Player player : members) {
			if (player.classe == classe) return true;
		}
		return false;
	}
	
	public int getLevelMinimum() {
		return levelMinimum;
	}

	public void setLevelMinimum(int levelMinimum) {
		this.levelMinimum = levelMinimum;
	}

	public int getLevelMaximum() {
		return levelMaximum;
	}

	public void setLevelMaximum(int levelMaximum) {
		this.levelMaximum = levelMaximum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Player> GetMembers() {
		return this.members;
	}
	
	public void AddMember(Player player) {
		this.members.add(player); 
	}
	
	public void RemoveMember(Player player) {
		this.members.remove(player); 
	}
	
	public boolean ContainsMember(Player player) {
		return this.members.contains(player); 
	}
	
	public JSONObject ToJson() {
        JSONObject obj = new JSONObject();
        obj.put("Id", this.id);
        obj.put("LevelMinimum", this.levelMinimum);
        obj.put("LevelMaximum", this.levelMaximum);
        JSONArray membersJson = new JSONArray();
        for (Player player : this.members) {
	        membersJson.put(player.ToJson());
		}
        obj.put("Members", membersJson);
		return obj;
	}
	
	public String FormattedString(){
		
		String message = "";
		message = message + "Equipe "+ this.id + " (" + this.levelMinimum  + " - " + String.valueOf(this.levelMaximum-1) + ") :\n";
		for (Player player : members) {
			message = message +"  - " + player.getPseudo() + " " + player.getClasse().name() + " lvl " + String.valueOf(player.getLevel()) + "\n";
		}
		return message;
	}
}
