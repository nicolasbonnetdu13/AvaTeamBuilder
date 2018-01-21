package net.avateambuilder.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Team {
	List<Player> members;
		
	public Team() {
		this.members = new ArrayList<Player>();
	}
	
	public Team(JSONObject objectJson) {
		this.members = new ArrayList<Player>();

        JSONArray membersJson = objectJson.getJSONArray("Members");
        for (Object object : membersJson) {
			if(object.getClass().equals(JSONObject.class)) {
				Player player = new Player((JSONObject)object);
				this.members.add(player);
			}
		}
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
	
	public boolean IsMember(Player player) {
		return this.members.contains(player); 
	}
	
	public JSONObject ToJson() {
        JSONObject obj = new JSONObject();
        JSONArray membersJson = new JSONArray();
        for (Player player : this.members) {
	        membersJson.put(player.ToJson());
		}
        obj.put("Members", membersJson);
		return obj;
	}
}
