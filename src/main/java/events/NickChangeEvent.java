package events;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class NickChangeEvent extends ListenerAdapter {

	public void onGuildMemberNickChange(GuildMemberUpdateNicknameEvent event)
	{
		//disregard normal users, only include members
		String prevNick = event.getOldNickname();
		String newNick = event.getNewNickname();
		
		TextChannel textChannel = event.getGuild().getTextChannelsByName("main",true).get(0);
		System.out.println(textChannel);
		textChannel.sendMessage(prevNick + " changed their name to " + newNick).queue();
		
	}
	
}
