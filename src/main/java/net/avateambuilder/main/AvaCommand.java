package net.avateambuilder.main;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.avateambuilder.main.Command.ExecutorType;
import net.avateambuilder.model.Battle;
import net.avateambuilder.model.Player;
import net.avateambuilder.model.Player.Classe;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class AvaCommand {

	static int nbJoueurs = 0;
	static String mainFile = null;
	private final MainBot mainBot;
	private List<Battle> battles;
	
	public AvaCommand(MainBot mainBot) {
		this.mainBot = mainBot;
		this.battles = new ArrayList<Battle>();
	}
	
	//!join pseudo classe lvl
	@Command(name = "join", type = ExecutorType.USER)
	private void join(User user, MessageChannel channel, Message message) { 

		Battle battle = getCurrentBattle();
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
		int level = Integer.parseInt(args[3]);
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
			channel.sendMessage(player.getPseudo() + " " + player.getClasse() + " lvl " + player.getLvl() + " rejoint la bataille !").complete();
		} else {
			channel.sendMessage(player.getPseudo() + ", tu fais déjà parti de la bataille !").complete();
		}
		FileMng.SaveCurrentState(this.battles);

	}
	
	//!leave pseudo classe lvl
	@Command(name = "leave", type = ExecutorType.USER)
	private void leave(User user, MessageChannel channel) { 

		Battle battle = getCurrentBattle();
		if (battle == null) {
			channel.sendMessage("L'Ava est déjà terminé").complete();
		} else {
			Player player = new Player(user.getName());
			battle.RemoveSoldier(player);
				
			channel.sendMessage(player.getPseudo() + " a quitté la bataille ! :wave:")
					.complete();
			FileMng.SaveCurrentState(this.battles);
		}
	}

	private Battle getCurrentBattle() {
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

	@Command(name = "startAva", type = ExecutorType.USER)
	private void startAva(User user, MessageChannel channel, Message message) {

		String[] args = message.getContentDisplay().split(" ");

		String name = args[1];
		Battle battle = new Battle(name);
		this.battles.add(battle);
		channel.sendMessage("Demarrage de l'Ava: " + battle.getName()).complete();
		FileMng.SaveCurrentState(this.battles);
	}

	@Command(name = "stopAva", type = ExecutorType.USER)
	private void stopAva(User user, MessageChannel channel, Message message) {
		 
		channel.sendMessage("Fin de l'AvA. bien joué à tous !").complete();
		mainFile = null;
		nbJoueurs = 0;
	}

}
