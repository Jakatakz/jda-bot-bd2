package com.Jakatakz.jda_bot_bd;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;



public class trackUserInfo extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		Guild guild = event.getGuild();
		List<Member> membersInDiscord = guild.getMembers();
		
		
	}
	
	//store warnings  >3 is ban for day, refresh after week
	public void addWarning()
	{
		
	}
	
	
	//other information?
	
	
	
}
