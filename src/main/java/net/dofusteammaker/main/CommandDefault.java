package net.dofusteammaker.main;

import net.dofusteammaker.main.Joueur;
import net.dofusteammaker.main.Command.ExecutorType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandDefault {
	
	private final MainBot mainBot;
	
	public CommandDefault(MainBot mainBot) {
		this.mainBot = mainBot;
	}

	@Command(name = "stop", type=ExecutorType.CONSOLE)
	private void stop() {
		mainBot.setRunning(false);
	}
	
	@Command(name = "joke", type=ExecutorType.USER)
	private void joke(User user, MessageChannel channel) {
		channel.sendMessage("Qu'est-ce qui est petit, et marron ?").complete();
	}
	
	
}























































