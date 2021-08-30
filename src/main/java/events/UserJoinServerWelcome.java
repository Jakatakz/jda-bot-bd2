package events;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserJoinServerWelcome extends ListenerAdapter 
{
	//private static User newUser;
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		//welcome message
		try
		{
			Thread.sleep(3000);
		
		event.getGuild().getDefaultChannel().sendMessage("fk u " + event.getUser().getAsMention()).queue();
		//event.getMember().getDefaultChannel().sendMessage("fk u").queue();
		}
		catch (InterruptedException ex)
		{
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
		
		//add user to users table in database
	}
}