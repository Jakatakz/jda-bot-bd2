package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Help extends ListenerAdapter
{
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		
		String[] message = event.getMessage().getContentRaw().split(" ");
		if (message[0].equalsIgnoreCase("!help") && message.length == 1)
		{
			event.getChannel().sendMessage("COMMANDS\n!calculate [add/sub] [number] [number]\n"
					+ "!invite (creates invite link for this discord)\n"
					+ "!halp\n"
					+ "!invite create\n"
					+ "!ban badger (kick badger from the discord, has a time limit)\n"
					+ "!cban badger (show time until someone can ban badger again)\n\n"
					+ "FOR BADGER:\n=============\n"
					+ "!badgerban on (makes people able to ban badger)\n"
					+ "!badgerban off (makes people unable to ban badger)\n"
					+ "!rban badger (makes it so badger is bannable immediately again)\n============"
					+ "\nother commands coming...").queue();
		}
		
	}
}
