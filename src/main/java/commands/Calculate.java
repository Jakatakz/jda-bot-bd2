package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Calculate extends ListenerAdapter 
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		
		String[] message = event.getMessage().getContentRaw().split(" ");
		
		if (message.length == 1 && message[0].equalsIgnoreCase("!calculate"))
		{
			event.getChannel().sendMessage("To use: !calculate [add/sub] [number] [number]").queue();
		}
		else if (message[0].equalsIgnoreCase("!calculate") && message[1].equalsIgnoreCase("add"))
		{
			int num1 = Integer.parseInt(message[2]);
			int num2 = Integer.parseInt(message[3]);
			event.getChannel().sendMessage(num1 + " + " + num2 + " = " + (num1+num2)).queue();
		}
		else if (message[0].equalsIgnoreCase("!calculate") && message[1].equalsIgnoreCase("sub"))
		{
			int num1 = Integer.parseInt(message[2]);
			int num2 = Integer.parseInt(message[3]);
			event.getChannel().sendMessage(num1 + " - " + num2 + " = " + (num1-num2)).queue();
		}
	}
}
