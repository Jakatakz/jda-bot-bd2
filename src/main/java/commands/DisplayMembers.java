package commands;

import java.util.List;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;


public class DisplayMembers extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		Guild guild = event.getGuild();
		List<Member> membersInDiscord = guild.getMembers();
		
		String[] message = event.getMessage().getContentRaw().split(" ");
		
		//get users by nickname
		if (message[0].equalsIgnoreCase("!members") && message.length == 1 && event.getAuthor().getId().contains("129725468206170112"))
		{
			for (Member m: membersInDiscord)
			{
				if (m.getNickname() == null)
					event.getChannel().sendMessage("no nickname\n").queue();
				else
					event.getChannel().sendMessage(m.getNickname() + "\n").queue();
//				if (m.getNickname() == null)
//					event.getChannel().sendMessage(m.getUser().getName() + "\n").queue();
//				else
//					event.getChannel().sendMessage(m.getNickname() + "\n").queue();
			}
		}
			//get actual users, no nick
		if (message[0].equalsIgnoreCase("!realMembers") && message.length == 1  && event.getAuthor().getId().contains("129725468206170112"))
		{
			for (Member m: membersInDiscord)
			{
				event.getChannel().sendMessage(m.getUser().getName() + "\n").queue();
				//event.getChannel().sendMessage(m.getEffectiveName() + "\n").queue();
				//event.getChannel().sendMessage(m.getUser().getName() + "\n").queue();
			}
		}
	}
}
