package net.dofusteammaker.main;


import net.dofusteammaker.main.Command.ExecutorType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class ATMcmd {

	static int nbJoueurs = 0;
	static String mainFile = null;
	private final MainBot mainBot;
	
	public ATMcmd(MainBot mainBot) {
		this.mainBot = mainBot;
	}
	
	//!join pseudo classe lvl
	@Command(name = "join", type = ExecutorType.USER)
	private void join(User user, MessageChannel channel, Message message) { 
		if (mainFile == null) {
			channel.sendMessage("Pas d'AvA en cours. Revenez plus tard ;)").complete();
		} else {
			
			// recupere les arguments
			String[] args = message.getContentDisplay().split(" ");

			// arguments
			String pseudo = args[1];
			Classes classe = Classes.valueOf(args[2]);
			int lvl = Integer.parseInt(args[3]);
			String userId = user.getName();
			Joueur joueur = new Joueur(pseudo, classe, lvl, userId);

			if (FileMng.IsRegistered(mainFile, userId) == false) {
				FileMng.AddPlayer(mainFile, joueur);
				nbJoueurs = nbJoueurs + 1;
			}
				
			channel.sendMessage(joueur.getPseudo() + " " + joueur.getClasse() + " lvl" + joueur.getLvl() + " rejoint la bataille !")
					.complete();
		}
	}

	@Command(name = "startAvA", type = ExecutorType.USER)
	private void startAvA(User user, MessageChannel channel, Message message) {

		String[] args = message.getContentDisplay().split(" ");
		mainFile = args[1];
		FileMng.CreateFile(mainFile);

	}

	@Command(name = "stopAvA", type = ExecutorType.USER)
	private void stopAvA(User user, MessageChannel channel, Message message) {
		 
		channel.sendMessage("Fin de l'AvA. bien joué à tous !").complete();
		mainFile = null;
		nbJoueurs = 0;
	}

}
