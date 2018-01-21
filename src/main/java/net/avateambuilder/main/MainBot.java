package net.avateambuilder.main;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class MainBot implements Runnable{
	
	private final String token;
	private final JDA jda;
	private final CommandMap commandMap = new CommandMap(this);
	private boolean running;
	private final Scanner scanner = new Scanner(System.in);
	
	public MainBot() throws LoginException {
		token = "MjkzNDcxMzcwOTUwNDc1Nzc3.DUOPLQ.YK7O9eLi3rp7q3LVe-AzHkFPif8";
		jda = new JDABuilder(AccountType.BOT).setToken(token).buildAsync();
		jda.addEventListener(new BotListener(commandMap));
		System.out.println("bot connecté");
	}
	
	public JDA getJda() {
		return jda;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			if(scanner.hasNextLine()) commandMap.commandConsole(scanner.nextLine());
		}
		
		scanner.close();
		System.out.println("Un MARRON !");
		jda.shutdownNow();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		try {
			MainBot mainBot = new MainBot();
			new Thread(mainBot,"bot").start();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
