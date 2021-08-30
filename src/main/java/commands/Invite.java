package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Invite extends ListenerAdapter 
{
	//queue is for threads, so queue makes it so itll execute as soon as possible
	//.complete() will block any thread until this one is run
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		int time = 300;
		String[] message = event.getMessage().getContentRaw().split(" ");
		if (message[0].equalsIgnoreCase("!invite") && message.length == 1)
		{
			event.getChannel().sendMessage("To invite, do: !invite create").queue();
		}
		else if ((message.length == 2) && message[0].equalsIgnoreCase("!invite") && message[1].equalsIgnoreCase("create"))
		{
			event.getChannel().sendMessage("getting invite...").queue();
			event.getChannel().sendMessage("Invite link: " + event.getChannel().createInvite().setMaxUses(1).setMaxAge(time).complete().getUrl()).queue();
			event.getChannel().sendMessage("invite expires in: " + time / 60 + " minutes (" + time + " seconds)").queue();
		}
		else if ((message.length > 2) && message[0].equalsIgnoreCase("!invite") 
				&& message[1].equalsIgnoreCase("create")
				&& message[2].matches("^([1-9][0-9]{0,5}|10000)$")
				&& Integer.parseInt(message[2]) <= 86400
				&& event.getAuthor().getDiscriminator().contains("0598"))
		{
			int customTime = Integer.parseInt(message[2]);
			event.getChannel().sendMessage("getting invite...").queue();
			event.getChannel().sendMessage("Invite link: " + event.getChannel().createInvite().setMaxUses(1).setMaxAge(customTime).complete().getUrl()).queue();
			event.getChannel().sendMessage("invite expires in: " + customTime / 60 + " minutes (" + customTime + " seconds)").queue();
		}
		else if ((message.length > 2) && message[0].equalsIgnoreCase("!invite") 
				&& message[1].equalsIgnoreCase("create")
				&& message[2].matches("^([1-9][0-9]{0,5}|10000)$")
				&& Integer.parseInt(message[2]) <= 86400
				&& !event.getAuthor().getDiscriminator().contains("0598"))
		{
			event.getChannel().sendMessage("you don't have permission to create custom invites").queue();
		}
		else if ((message.length > 2) && message[0].equalsIgnoreCase("!invite") 
				&& message[1].equalsIgnoreCase("create")
				&& (message[2].matches("\\d+") && Integer.parseInt(message[2]) > 86400)
				&& event.getAuthor().getDiscriminator().contains("0598"))
		{
			event.getChannel().sendMessage("custom time must be 1-86400").queue();
		}
		else if ((message.length > 2) && message[0].equalsIgnoreCase("!invite") 
				&& message[1].equalsIgnoreCase("create")
				&& !message[2].matches("\\d+"))
			event.getChannel().sendMessage("to create custom invite, do: !invite create [number]").queue();
	}
}
