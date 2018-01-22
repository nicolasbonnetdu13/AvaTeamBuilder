package net.avateambuilder.main;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
			channel.sendMessage("Il manque des infos pour rejoindre l'ava -> `!join <pseudo> <class> <level>`").complete();
			return;
		}
		String pseudo = args[1];
		String classe = args[2];
		String levelString = args[3];
		if (!levelString.matches("\\d+")) {
			channel.sendMessage("Il manque des infos pour rejoindre l'ava -> `!join <pseudo> <class> <level>`").complete();
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
			channel.sendMessage(player.getPseudo() + " " + player.getClasse() + " lvl " + player.getLevel() + " rejoint la bataille !").complete();
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
				channel.sendMessage(playerName + " a quitté la bataille ! :wave:").complete();
			}
			FileMng.SaveCurrentState(this.battles);
		}
	}

	private Battle getLastBattle() {
		Battle battle = null;
		LocalDateTime today = LocalDateTime.now();
		for (Battle aBattle : this.battles) {
			LocalDateTime date = aBattle.getDate();
			if (date.getYear() == today.getYear() &&
				date.getDayOfYear() == today.getDayOfYear()) {
				battle = aBattle;
			}
		}
		return battle;
	}

	@Command(name = "startAvA", type = ExecutorType.USER)
	private void startAvA(User user, MessageChannel channel, Message message) {
		startAva(user, channel, message);
	}

	@Command(name = "startAva", type = ExecutorType.USER)
	private void startAva(User user, MessageChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");

		String name = args[1];
		Battle battle = new Battle(name);
		Battle lastBattle = getLastBattle();
		if (lastBattle.getName().equals(battle.getName())) {
			channel.sendMessage("L'Ava \"" + battle.getName() + "\" existe déjà").complete();
			return;
		}
		this.battles.add(battle);
		channel.sendMessage("Demarrage de l'Ava: " + battle.getName()).complete();
		FileMng.SaveCurrentState(this.battles);
	}

	@Command(name = "stopAva", type = ExecutorType.USER)
	private void stopAva(User user, MessageChannel channel, Message message) {
		 
		channel.sendMessage("Fin de l'AvA. bien joué à tous !").complete();
	}
	
	@Command(name = "statusAva", type = ExecutorType.USER)
	private void statusAva(User user, MessageChannel channel, Message message) {
		
		Battle battle = getLastBattle();
		String status = "```";
		status = status + battle.FormattedString();
		status = status + "```";
		channel.sendMessage(status).complete();
	}
	
	@Command(name = "myTeam", type = ExecutorType.USER)
	private void myTeam(User user, MessageChannel channel, Message message) {
		
		Battle battle = getLastBattle();
		Team team = battle.GetTeamForPlayer(new Player(user.getName(), ""));
		String status = "```";
		status = status + team.FormattedString();
		status = status + "```";
		channel.sendMessage(status).complete();
	}
	
	@Command(name = "movePlayer", type = ExecutorType.USER)
	private void movePlayer(User user, MessageChannel channel, Message message) {

		Battle battle = getLastBattle();
		String[] args = message.getContentDisplay().split(" ");
		if (args.length < 3) {
			channel.sendMessage("Il manque des infos pour deplacer un joueur -> `!movePlayer <pseudo> <teamId>`").complete();
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
			channel.sendMessage("L'équipe doit être un nombre. `!movePlayer <pseudo> <teamId>`").complete();
			return;
		}
		int teamId = Math.min(Integer.parseInt(teamIdString), battle.NumberOfTeam()+1);
		battle.MovePlayerToTeam(player, teamId);
		channel.sendMessage(player.getPseudo() + " a été déplacé dans l'équipe " + teamId).complete();
		FileMng.SaveCurrentState(this.battles);
	}
}

