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
	
	public void AddSoldier(Player player) {
		this.soldiers.add(player); 
	}
	
	public void RemoveSoldier(Player player) {
		this.soldiers.remove(player); 
	}
	
	public boolean IsSoldier(Player player) {
		return this.soldiers.contains(player); 
	}
	
	public void AddTeam(Team team) {
		this.army.add(team); 
	}
	
	public void RemoveTeam(Team team) {
		this.army.remove(team); 
	}
	
	public boolean IsTeam(Team team) {
		return this.army.contains(team); 
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
	
}
