package net.avateambuilder.main;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.avateambuilder.main.Command.ExecutorType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public final class CommandMap {

	private final MainBot mainBot;
	private final Map<String, SimpleCommand> commands = new HashMap<>();
	private final String tag = "!";

	public CommandMap(MainBot mainBot) {
		this.mainBot = mainBot;

		registerCommand(new AvaCommand(mainBot));
	}

	public String getTag() {
		return tag;
	}

	public Collection<SimpleCommand> getCommands() {
		return commands.values();
	}

	public void registerCommands(Object... objects) {
		for (Object object : objects)
			registerCommand(object);
	}

	public void registerCommand(Object object) {
		for (Method method : object.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(Command.class)) {
				Command command = method.getAnnotation(Command.class);
				method.setAccessible(true);
				SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.type(),
						object, method);
				commands.put(command.name(), simpleCommand);
			}
		}
	}

	public void commandConsole(String command) {
		Object[] object = getCommand(command);
		if (object[0] == null || ((SimpleCommand) object[0]).getExecutorType() == ExecutorType.USER) {
			System.out.println("Command not supported");
			return;
		}
		try {
			execute(((SimpleCommand) object[0]), command, (String[]) object[1], null);
		} catch (Exception exception) {
			System.out.println("commandConsole: " + ((SimpleCommand) object[0]).getMethod().getName() + " couldn't be executed");
		}
	}

	public boolean commandUser(User user, String command, Message message) {

		command = command.trim(); // Retire l'espace en d�but de chaine pour une commande sur discord
		Object[] object = getCommand(command);
		if (object[0] == null || ((SimpleCommand) object[0]).getExecutorType() == ExecutorType.CONSOLE) {
			return false;
		}

		try {
			execute(((SimpleCommand) object[0]), command, (String[]) object[1], message);
		} catch (Exception exception) {
			System.out.println("commandUser: " + ((SimpleCommand) object[0]).getMethod().getName() + " couldn't be executed");
			System.out.println("exception: " + exception.getStackTrace());
		}
		return true;
	}

	private Object[] getCommand(String command) {
		System.out.println("COMMANDE :" + command);
		String[] commandSplit = command.split(" ");
		String[] args = new String[commandSplit.length - 1];
		for (int i = 1; i < commandSplit.length; i++) {
			args[i - 1] = commandSplit[i];
		}
		SimpleCommand simpleCommand = commands.get(commandSplit[0]);
		return new Object[] { simpleCommand, args };
	}

	private void execute(SimpleCommand simpleCommand, String command, String[] args, Message message) throws Exception {
		Parameter[] parameters = simpleCommand.getMethod().getParameters();
		Object[] objects = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getType() == String[].class)
				objects[i] = args;
			else if (parameters[i].getType() == User.class)
				objects[i] = message == null ? null : message.getAuthor();
			else if (parameters[i].getType() == TextChannel.class)
				objects[i] = message == null ? null : message.getTextChannel();
			else if (parameters[i].getType() == PrivateChannel.class)
				objects[i] = message == null ? null : message.getPrivateChannel();
			else if (parameters[i].getType() == Guild.class)
				objects[i] = message == null ? null : message.getGuild();
			else if (parameters[i].getType() == String.class)
				objects[i] = command;
			else if (parameters[i].getType() == Message.class)
				objects[i] = message;
			else if (parameters[i].getType() == JDA.class)
				objects[i] = mainBot.getJda();
			else if (parameters[i].getType() == MessageChannel.class)
				objects[i] = message == null ? null : message.getChannel();
		}
		simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
	}
}