package com.Jakatakz.jda_bot_bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import commands.BanWord;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


//contains list of banned words, and get/set methods for the banned words list. changes banned words to *
public class Filter extends ListenerAdapter
{		
	private ArrayList<String> bannedWordsCopy = BanWord.getBannedWordsList();
	static Connection conn = DBConnect.dbConnect();
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		String[] message = event.getMessage().getContentRaw().split(" ");
		
		try 
		{
			if (!event.getAuthor().isBot() && !event.getAuthor().getDiscriminator().contains("0598"))	//if message is from bot or admin (admin is 0598), dont filter it
			{
				populateFirstTimeUsersTable(event);
				isBannedWord(message, bannedWordsCopy, event);
			}
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
			System.out.println("heref1");
		}
					//event.getMessage().editMessage(message[i].replaceAll("[a-zA-z]", "*")).queue();
					//message[i].replaceAll("[a-zA-z]", "*");
				
					
			
		
	}
	
	//get user who said ban word and add a warning up to 3 until timeout'd from channel
	//dont add warning counter for first time, just warning for first offense, time limit until refresh 1 week
	
	public void isBannedWord(String[] message_param, ArrayList<String> bannedWordsCopy_p, GuildMessageReceivedEvent event_p) throws InterruptedException
	{
		for (int i = 0; i < message_param.length; i++)
		{
			for (int k = 0; k < bannedWordsCopy.size(); k++)
			{
				if (message_param[i].contains(bannedWordsCopy.get(k)))
				{ 
					//String offender = event_p.getAuthor().getName();
					//String offenderNick = event_p.getMember().getNickname();
					String discriminator = event_p.getAuthor().getDiscriminator();
					
					//updates mysql users table 
					checkAndAddWarningAndBan(discriminator, event_p);
					
					//long warningsOffender = checkWarningBan(discriminator, event_p);
					//get warning integer and add it to method addWarnings so u can add it to database
					
					//update users warnings
					//addWarnings(discriminator, warningsOffender);
					
					String starBannedWord = (bannedWordsCopy.get(k)).replaceAll(".", "#");
					
					event_p.getChannel().sendMessage(starBannedWord + " is banned").queue(); 
					//Thread.sleep(4000);
					event_p.getMessage().delete().queue();
				}
			}
		}
	}
	
		//adds warnings, bans if exceeds warnings, perma bans are seperate
		private void checkAndAddWarningAndBan(String discriminator, GuildMessageReceivedEvent event_p)
		{
			
			long offenderWarnings = getOffenderWarningCount(discriminator);
			
			try
			{
			Statement stmtOW = conn.createStatement();
				if (offenderWarnings == 5)
				{
					TextChannel notificationsChannel = event_p.getGuild().getTextChannelsByName("notifications", true).get(0);
					notificationsChannel.sendMessage(event_p.getMember().getEffectiveName() + " has been banned " + event_p.getGuild().getOwner().getAsMention()).queue();
					Date date = new Date();
					Timestamp banTimeStamp = new Timestamp(date.getTime());
					event_p.getGuild().ban(event_p.getMember(), 0, " used too many banned words").queue();
					event_p.getChannel().sendMessage(event_p.getMember().getEffectiveName() + "(" + event_p.getMember().getNickname() + ") has been banned").queue();
					
					
					
					String sqlWarning5 = "UPDATE mydiscordusers "
							+ "SET warnings = 6, banned = 'yes', bantimestamp = '" + banTimeStamp
							+ "' WHERE discriminator = '" + discriminator + "';";
					stmtOW.executeUpdate(sqlWarning5);
				}
				//if warnings is ALREADY 2, warn user they now have 3/3 warnings (refreshes in (time left for refresh))
				//warn user theyre about to be banned for (ban time length) 
				else if (offenderWarnings == 4)
				{
					event_p.getChannel().sendMessage(event_p.getAuthor().getAsMention() + " you have 1 warning left before being banned, warnings refresh monday").queue();
					
					String sqlWarning4 = "UPDATE mydiscordusers "
							+ "SET warnings = 5 "
							+ "WHERE discriminator = '" + discriminator + "';";
					stmtOW.executeUpdate(sqlWarning4);
				}
				else if (offenderWarnings >= 0 && offenderWarnings < 4)
				{
					event_p.getChannel().sendMessage(event_p.getAuthor().getAsMention() + " stop using banned words").queue();
					if ( offenderWarnings == 3)
					{
						String sqlWarning3 = "UPDATE mydiscordusers "
								+ "SET warnings = 4 "
								+ "WHERE discriminator = '" + discriminator + "';";
						stmtOW.executeUpdate(sqlWarning3);
					}
					else if ( offenderWarnings == 2)
					{
						String sqlWarning2 = "UPDATE mydiscordusers "
								+ "SET warnings = 3 "
								+ "WHERE discriminator = '" + discriminator + "';";
						stmtOW.executeUpdate(sqlWarning2);
						
					} else if ( offenderWarnings == 1)
					{
						String sqlWarning1 = "UPDATE mydiscordusers "
								+ "SET warnings = 2 "
								+ "WHERE discriminator = '" + discriminator + "';";
						stmtOW.executeUpdate(sqlWarning1);
					} 
					else if (offenderWarnings == 0)
					{
						String sqlWarning0 = "UPDATE mydiscordusers"
								+ " SET warnings = 1 "
								+ "WHERE discriminator = '" + discriminator +"';";
						stmtOW.executeUpdate(sqlWarning0);
						//stmt.executeUpdate(sqlWarning0);
						
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("chk offender warning method");
			}
			}
		
		private long getOffenderWarningCount(String discriminator)
		{
			long offenderWarnings = 0;
			try
			{
			//Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			Statement stmt = conn.createStatement();
			String sql = "SELECT warnings FROM mydiscordusers WHERE discriminator = '" + discriminator + "'";
			ResultSet rst = stmt.executeQuery(sql);
				while (rst.next())
				{
					offenderWarnings = rst.getInt("warnings");
					return offenderWarnings;
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.out.println("hereGetOffenderWarningCount()");
			}
			return offenderWarnings;
		}
		//add warning
//		public void addWarnings(String offender, String offenderNick)
//		{
//			try
//			{
//			//PreparedStatement prepStat = conn.prepareStatement("insert into users (name, nickname, warnings, banned) " 
//			//+ "values (?, ?, ?, ?)");
//			PreparedStatement prepStat = conn.prepareStatement("UPDATE users "
//					+ "SET name varchar(45), CHANGE nickname varchar(45), warnings int(7), banned varchar(3) " 
//				+ "values (?, ?, ?, ?)");
//			prepStat.setString(1, offender);
//			prepStat.setString(2, offenderNick);
//			prepStat.setLong(3, 1);
//			prepStat.setString(4, "no?");
//			prepStat.executeUpdate();
//			System.out.println("added warnings");
//			}
//			catch (Exception ex)
//			{
//				ex.printStackTrace();
//				System.out.println("heref3");
//			}
//		}
	
		
		
	//remove warning
		
		
		
	/*
	 * //add warning public void addWarnings(String discriminator, long
	 * warningsOfOffender) { try { //PreparedStatement prepStat =
	 * conn.prepareStatement("insert into users (name, nickname, warnings, banned) "
	 * //+ "values (?, ?, ?, ?)"); PreparedStatement prepStat =
	 * conn.prepareStatement("UPDATE users " + "SET warnings = ? " +
	 * "WHERE name = ?"); //+ "values (?, ?)"); //prepStat.setString(1, offender);
	 * //prepStat.setString(2, offenderNick);
	 * 
	 * prepStat.setLong(1, warningsOfOffender+1); //need to make checkwarningban
	 * return an integer for warning and give it to this method
	 * 
	 * prepStat.setString(2, offender); //prepStat.setString(4, "no?");
	 * prepStat.executeUpdate(); System.out.println("added warnings"); } catch
	 * (Exception ex) { ex.printStackTrace(); System.out.println("heref3"); } }
	 */
//		
	//if the table is dropped or its an empty set , this repopulates the user table, everythings new, bans removed
	private void populateFirstTimeUsersTable(GuildMessageReceivedEvent event)
	{
		try
		{
			System.out.println("33333333333333333333333333");
			List<Member> memberList = event.getGuild().getMembers();
			memberList.forEach(System.out::println);
			int memberListSize = memberList.size();
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM mydiscordusers";
			ResultSet rst = stmt.executeQuery(sql);
			
			//if first time (empty USER table), populate the user table
			if (!rst.next())
			{
				rst.beforeFirst(); //moves cursor to front 
				System.out.println("empty set");
				String sql2 = "insert into mydiscordusers(name, nickname,warnings,banned,"
						+ "permabanned,discriminator) " + "values (?,?,?,?,?,?)";
				PreparedStatement prepStat = conn.prepareStatement(sql2);
				//conn.setAutoCommit(false);
				for (int i = 0; i < memberListSize; i++)
				{
					prepStat.setString(1, memberList.get(i).getEffectiveName());
					prepStat.setString(2, memberList.get(i).getNickname());
					prepStat.setInt(3, 0);
					prepStat.setString(4, "no");
					prepStat.setString(5,"no");
					prepStat.setString(6, memberList.get(i).getUser().getDiscriminator());
					prepStat.addBatch();
				}
				prepStat.executeBatch();
				//prepStat.executeUpdate();
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			System.out.println("populateFirstTimeUsersTable SQL");
		}
	}
	
}
