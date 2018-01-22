package net.avateambuilder.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Battle {
	private String name;
	private LocalDateTime date;
	List<Team> army;
	List<Player> soldiers;

	public Battle(String name) {
		this.army = new ArrayList<Team>();
		this.soldiers = new ArrayList<Player>();
		this.setDate(LocalDateTime.now());
		this.name = name;
	}
	
	public Battle(JSONObject objectJson) {
		this.army = new ArrayList<Team>();
		this.soldiers = new ArrayList<Player>();
		
		this.name = objectJson.getString("Name");
		String dateString = objectJson.getString("Date");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.date = LocalDateTime.parse(dateString, formatter);

        JSONArray soldiersJson = objectJson.getJSONArray("Soldiers");
        for (Object object : soldiersJson) {
			if(object.getClass().equals(JSONObject.class)) {
				Player player = new Player((JSONObject)object);
				this.soldiers.add(player);
			}
		}

        JSONArray armyJson = objectJson.getJSONArray("Army");
        for (Object object : armyJson) {
			if(object.getClass().equals(JSONObject.class)) {
				Team team = new Team((JSONObject)object);
				this.army.add(team);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public boolean AddSoldier(Player player) {
		boolean contain = ContainsSoldier(player);
		if (!contain) {	
			this.soldiers.add(player);
			AutoAddToTeam(player);
		}
		return !contain;
	}
	
	public void AutoAddToTeam(Player player) {
		for (Team team : army) {
			if (player.level >= team.getLevelMinimum() && 
					player.level < team.getLevelMaximum() && 
					!team.ContainsClasse(player.getClasse()) &&
					team.GetMembers().size() < 5) {
				team.AddMember(player);
				return;
			}
		}
		boolean above190 = player.getLevel() >= 190;
		Team newTeam = new Team(army.size()+1, above190 ? 190 : 0, above190 ? 201 : 190);
		newTeam.AddMember(player);
		this.army.add(newTeam);
	}
	
	public String RemoveSoldier(Player player) {
		String playerName = "";
		if (ContainsSoldier(player)) {
			playerName = this.soldiers.get(this.soldiers.indexOf(player)).getPseudo();
			this.soldiers.remove(player); 
		}
		for (Team team : army) {
			if (team.ContainsMember(player)) {
				team.RemoveMember(player);
			}
		}
		return playerName;
	}
	
	public boolean ContainsSoldier(Player player) {
		return this.soldiers.contains(player); 
	}

	public Team GetTeamForPlayer(Player player) {
		for (Team team : army) {
			if (team.ContainsMember(player)) 
				return team;
		}
		return null;
	}
	
	public JSONObject ToJson() {
        JSONObject obj = new JSONObject();
        obj.put("Name", this.name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = this.date.format(formatter);
        obj.put("Date", formattedDateTime);
        JSONArray soldiersJson = new JSONArray();
        for (Player player : this.soldiers) {
        	soldiersJson.put(player.ToJson());
		}
        obj.put("Soldiers", soldiersJson);
        JSONArray armyJson = new JSONArray();
        for (Team team : this.army) {
        	armyJson.put(team.ToJson());
		}
        obj.put("Army", armyJson);
		return obj;
	}
	
	public String FormattedString(){

		String message = "";
		int armySize = this.army.size();
		message = message + "AvA : " + this.name + "\n";
		for (int i = 0; i < armySize;i++) {
			Team team = army.get(i);
			message = message + team.FormattedString();
			message = message + "\n";
		}
		return message;
	}
}

