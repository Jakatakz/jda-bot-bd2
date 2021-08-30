package com.Jakatakz.jda_bot_bd;

import events.AddUserToDB;
import events.CategoryCreate;
import events.ComboWords;
import events.NickChangeEvent;
import events.StatusEvent;
import events.TextChannelCreate;
import events.TotalMessages;
import events.UserJoinServerWelcome;
import games.Hangman;
//import events.UserJoinServerWelcome;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;

import org.apache.log4j.BasicConfigurator;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import commands.BanBadger;
import commands.BanPeople;
import commands.BanWord;
import commands.Calculate;
import commands.DisplayMembers;
import commands.Help;
import commands.Image;
import commands.Invite;
import commands.ServerInfo;
import commands.UnbanPeople;
import commands.UserInfo;
import commands.UserInfoCommand;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	BasicConfigurator.configure();
   
    	//deprecrated
    	//JDA jda1 = new JDABuilder("deleted").build().awaitReady();
     
        EventWaiter waiter = new EventWaiter();
        
        //jda utilities commands
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId("576468205023789078");
        builder.setPrefix("!");
        builder.setHelpWord("halp");
        builder.addCommand(new ServerInfo());
        builder.addCommand(new Image());
        builder.addCommand(new UserInfoCommand(waiter));
        
        //jda utilities commands
        CommandClient client = builder.build();
        jda1.addEventListener(client);
        jda1.addEventListener(waiter);
        
        //time
        LocalTime lt1 = LocalTime.now();
		String currentTime = lt1.toString();
		LocalTime time = LocalTime.parse(currentTime);
		int startTime = time.toSecondOfDay();
		
		//events
        jda1.addEventListener(new StatusEvent(startTime));
        jda1.addEventListener(new CategoryCreate());
        jda1.addEventListener(new NickChangeEvent());
        jda1.addEventListener(new TextChannelCreate());
        jda1.addEventListener(new UserJoinServerWelcome());
        jda1.addEventListener(new AddUserToDB());
        jda1.addEventListener(new ComboWords());
        jda1.addEventListener(new TotalMessages());
        
        //commands
        jda1.addEventListener(new Calculate());
        jda1.addEventListener(new Help());
        jda1.addEventListener(new Invite());
        jda1.addEventListener(new UserInfo());
        jda1.addEventListener(new BanWord());
        jda1.addEventListener(new DisplayMembers());
        jda1.addEventListener(new BanBadger());
        jda1.addEventListener(new UnbanPeople());
        jda1.addEventListener(new BanPeople());
        
        jda1.addEventListener(new Filter());
        
        //games
        jda1.addEventListener(new Hangman());
    }
    
    
}
