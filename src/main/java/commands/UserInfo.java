package commands;

import java.awt.Color;
import java.time.LocalTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserInfo extends ListenerAdapter 
{
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		String[] message = event.getMessage().getContentRaw().split(" ");
		if (message.length == 1 && message[0].equalsIgnoreCase("!user"))
		{
			event.getChannel().sendMessage("to get a user's info, type !user [name]").queue();
		}
		else if (message.length == 2 && message[0].equalsIgnoreCase("!user"))	//message.length == 2, what if 3? in case of "Bug Trio" this doesnt work with spaces, fix.
		{
			String userName = message[1];	//bug, use !realmembers info not !members
			User user = event.getGuild().getMembersByName(userName, true).get(0).getUser();
			String avatar = user.getAvatarUrl();
			EmbedBuilder eb = new EmbedBuilder();
			
			eb.setTitle(userName + "'s info:");
			eb.setColor(Color.GREEN);
			eb.addField("Name", user.getName(), true);
			eb.addField("Online status: ", event.getGuild().getMembersByName(userName, true).get(0).getOnlineStatus().toString(), true);
			eb.addField("Avatar: ", "The avatar is below, " + event.getMember().getAsMention(), true);
			eb.setImage(avatar);
			eb.setFooter("made @ " + getTime(), event.getGuild().getIconUrl());
			
			event.getChannel().sendMessage(eb.build()).queue();
		}
		
	}
	
	private String getTime()
	{
		LocalTime lt1 = LocalTime.now();
		return lt1.toString();
		
	}
}
