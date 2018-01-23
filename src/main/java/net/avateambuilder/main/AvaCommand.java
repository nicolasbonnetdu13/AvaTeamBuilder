package net.avateambuilder.main;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.avateambuilder.main.Command.ExecutorType;
import net.avateambuilder.model.Battle;
import net.avateambuilder.model.Player;
import net.avateambuilder.model.Player.Classe;
import net.avateambuilder.model.Team;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class AvaCommand {

	private final MainBot mainBot;
	private List<Battle> battles;
	
	public AvaCommand(MainBot mainBot) {
		this.mainBot = mainBot;
		this.battles = FileMng.GetLastState();
	}
	
	@Command(name = "join", type = ExecutorType.USER)
	private void join(User user, MessageChannel channel, Message message) { 

		Battle battle = getLastBattle();
		if (battle == null) {
			channel.sendMessage("Pas d'AvA en cours. Revenez plus tard ;)").complete();
			return;
		}
		String[] args = message.getContentDisplay().split(" ");
		if (args.length < 4) {
			channel.sendMessage("Il manque des infos pour rejoindre l'ava :noob: -> `!join <pseudo> <class> <level>`").complete();
			return;
		}
		String pseudo = args[1];
		String classe = args[2];
		String levelString = args[3];
		if (!levelString.matches("\\d+")) {
			channel.sendMessage("Il manque des infos pour rejoindre l'ava :noob: -> `!join <pseudo> <class> <level>`").complete();
			return;
		}
		int level = Math.min(Integer.parseInt(levelString), 200);
		String userId = user.getName();
		Player player = new Player(pseudo, classe, level, userId);
		if (player.getClasse() == Classe.unknown) {
			channel.sendMessage("Je n'ai pas compris ta classe. Voici la liste de classe possible: `eniripsa, "
					+ "iop, sacrieur, huppermage, osamodas, sadida, xelor, cra, steamer, eliotrope, ecaflip, "
					+ "pandawa, sram, ouginak, feca, zobal, roublard, enutrof`").complete();
			return;
		}

		boolean succeed = battle.AddSoldier(player);
		
		if (succeed) {
			channel.sendMessage(player.getPseudo() + " " + player.getClasse() + " lvl " + player.getLevel() + " rejoint la bataille ! :muscle:").complete();
		} else {
			channel.sendMessage(player.getPseudo() + ", tu fais déjà parti de la bataille !").complete();
		}
		FileMng.SaveCurrentState(this.battles);

	}
	
	@Command(name = "leave", type = ExecutorType.USER)
	private void leave(User user, MessageChannel channel) { 

		Battle battle = getLastBattle();
		if (battle == null) {
			channel.sendMessage("L'Ava est déjà terminé").complete();
		} else {
			Player player = new Player(user.getName(), "");
			String playerName = battle.RemoveSoldier(player);
				
			if(!playerName.isEmpty()) {				
				channel.sendMessage(playerName + " a quitté la bataille ! :cry:").complete();
			}
			FileMng.SaveCurrentState(this.battles);
		}
	}

	private Battle getLastBattle() {
		Collections.sort(battles, new Comparator<Battle>() {
			  public int compare(Battle o1, Battle o2) {
			      return o1.getDate().compareTo(o2.getDate());
			  }
			});
		for (Battle battle : battles) {
			if(battle.isOngoing()) return battle;
		}
		return null;
	}

	@Command(name = "startAva", type = ExecutorType.USER)
	private void startAva(User user, MessageChannel channel, Message message) {

		String[] args = message.getContentDisplay().split(" ");

		String name = args[1];
		Battle battle = new Battle(name);
		Battle lastBattle = getLastBattle();
		if (lastBattle != null) {
			channel.sendMessage("Une Ava est déjà en cours !").complete();
			return;
		}
		battle.setOngoing(true);
		this.battles.add(battle);
		channel.sendMessage("Demarrage de l'Ava: " + battle.getName()).complete();
		FileMng.SaveCurrentState(this.battles);
	}

	@Command(name = "stopAva", type = ExecutorType.USER)
	private void stopAva(User user, MessageChannel channel, Message message) {

		Battle battle = getLastBattle();
		if (battle == null) {
			channel.sendMessage("Pas d'AvA en cours.").complete();
			return;
		}
		battle.setOngoing(false);
		channel.sendMessage("Fin de l'AvA. Bien joué à tous ! :muscle:").complete();
		FileMng.SaveCurrentState(this.battles);
	}

	@Command(name = "statusAvA", type = ExecutorType.USER)
	private void statusAvA(User user, MessageChannel channel, Message message) {
		statusAva(user, channel, message);
	}

	@Command(name = "statusava", type = ExecutorType.USER)
	private void statusava(User user, MessageChannel channel, Message message) {
		statusAva(user, channel, message);
	}
	
	@Command(name = "statusAva", type = ExecutorType.USER)
	private void statusAva(User user, MessageChannel channel, Message message) {
		
		Battle battle = getLastBattle();
		if (battle == null) {
			channel.sendMessage("Pas d'AvA en cours.").complete();
			return;
		}
		String status = "```";
		status = status + battle.FormattedString();
		status = status + "```";
		channel.sendMessage(status).complete();
	}
	
	@Command(name = "myteam", type = ExecutorType.USER)
	private void myteam(User user, MessageChannel channel, Message message) {
		myTeam(user, channel, message);
	}
	
	@Command(name = "myTeam", type = ExecutorType.USER)
	private void myTeam(User user, MessageChannel channel, Message message) {
		
		Battle battle = getLastBattle();
		if (battle == null) {
			channel.sendMessage("Pas d'AvA en cours.").complete();
			return;
		}
		Team team = battle.GetTeamForPlayer(new Player(user.getName(), ""));
		String status = "```";
		status = status + team.FormattedString();
		status = status + "```";
		channel.sendMessage(status).complete();
	}
	
	@Command(name = "movePlayer", type = ExecutorType.USER)
	private void movePlayer(User user, MessageChannel channel, Message message) {

		Battle battle = getLastBattle();
		if (battle == null) {
			channel.sendMessage("Pas d'AvA en cours.").complete();
			return;
		}
		String[] args = message.getContentDisplay().split(" ");
		if (args.length < 3) {
			channel.sendMessage("Il manque des infos pour deplacer un joueur :noob: -> `!movePlayer <pseudo> <teamId>`").complete();
			return;
		}
		String pseudo = args[1];
		Player player = battle.GetPlayerWithPseudo(pseudo);
		if (player == null) {
			channel.sendMessage("Ce joueur ne fait pas parti de l'Ava.").complete();
			return;
		}
		String teamIdString = args[2];
		if (!teamIdString.matches("\\d+")) {
			channel.sendMessage("L'équipe doit être un nombre. :noob: `!movePlayer <pseudo> <teamId>`").complete();
			return;
		}
		int teamId = Math.min(Integer.parseInt(teamIdString), battle.NumberOfTeam()+1);
		battle.MovePlayerToTeam(player, teamId);
		channel.sendMessage(player.getPseudo() + " a été déplacé dans l'équipe " + teamId).complete();
		FileMng.SaveCurrentState(this.battles);
	}
 
	@Command(name = "kick", type = ExecutorType.USER)
	private void kick(User user, MessageChannel channel, Message message) {

		Battle battle = getLastBattle();
		if (battle == null) {
			channel.sendMessage("Pas d'AvA en cours.").complete();
			return;
		}
		String[] args = message.getContentDisplay().split(" ");
		if (args.length < 2) {
			channel.sendMessage("Il manque des infos pour expluser un joueur :noob: -> `!kick <pseudo>`").complete();
			return;
		}
		String pseudo = args[1];
		Player player = battle.GetPlayerWithPseudo(pseudo);
		if (player == null) {
			channel.sendMessage("Ce joueur ne fait pas parti de l'Ava.").complete();
			return;
		}
		battle.RemoveSoldier(player);
		channel.sendMessage(player.getPseudo() + " a été explusé de l'Ava :smiling_imp:").complete();
		FileMng.SaveCurrentState(this.battles);
	}
	
	@Command(name = "help", type = ExecutorType.USER)
	private void help(User user, MessageChannel channel, Message message) {
		
		channel.sendMessage("```"
				+ "Commandes disponibles :\n\n"
				+ "!join [personnage] [classe] [lvl]\n -> Rejoindre l'AvA\n\n"
				+ "!leave\n -> Quitter l'AvA\n\n"
				+ "!statusAva\n -> Affiche les équipes de l'AvA en cours\n\n"
				+ "!myTeam\n -> Affiche votre équipe```").complete();
	}
	
}

