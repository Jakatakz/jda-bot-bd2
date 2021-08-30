package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.Jakatakz.jda_bot_bd.DBConnect;
import com.Jakatakz.jda_bot_bd.Filter;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BanWord extends ListenerAdapter
{
	
	//private static ArrayList<String> bannedWords = new ArrayList<>();
	static Connection conn1 = DBConnect.dbConnect();
	
	private static ArrayList<String> bannedWords = new ArrayList<>();
	
	public static String getBannedWordsAsString(ArrayList<String> bannedWords)
	{
		String bwlist = "";
		try
		{
			Statement stmt = conn1.createStatement();
			String sql = "SELECT word FROM bannedWords";
			ResultSet rst = stmt.executeQuery(sql);
			while (rst.next())
			{
				bwlist += (rst.getString("word")) + " ";
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("here");
		}
		return bwlist;
	}
	
	public static void getBannedWords(ArrayList<String> bannedWords)
	{
		//System.out.println("1");
		try
		{
			Statement stmt = conn1.createStatement();
			String sql = "SELECT word FROM bannedWords";
			ResultSet rst = stmt.executeQuery(sql);
			while (rst.next())
			{
				if (!bannedWords.contains(rst.getString("word")))
						bannedWords.add(rst.getString("word"));
				else continue;
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("here");
		}
	}
	
	
	private void addBannedWordDB(String message)
	{
		try
		{
		PreparedStatement prepStat = conn1.prepareStatement("insert into BannedWords (word) " + "values (?)");
		prepStat.setString(1, message);
		prepStat.executeUpdate();
		System.out.println("added banned word");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("here");
		}
	}
	
	private void dropBannedWordDB(String message)
	{
		try
		{
		//PreparedStatement prepStat = conn1.prepareStatement("DELETE FROM bannedwords WHERE word = '"+message+"'");
		PreparedStatement prepStat = conn1.prepareStatement("DELETE FROM bannedwords WHERE word = ?");
		prepStat.setString(1, message);
		prepStat.executeUpdate();
		System.out.println("dropped word");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("here");
		}
		
	}
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		getBannedWords(bannedWords);
		String list = getBannedWordsAsString(bannedWords);
		//String list = "";
		String[] message = event.getMessage().getContentRaw().split(" ");
	
		if (!event.getAuthor().isBot() && event.getAuthor().getDiscriminator().contains("0598") )	//ignore bot
		{
			if (message.length == 1 && message[0].equalsIgnoreCase("!banw") 
					|| message.length == 1 && message[0].equalsIgnoreCase("!dropw"))
			{
				event.getChannel().sendMessage("to ban a word, type !banw [word] to drop, type !dropw [word]").queue();
			}
			else if (message.length == 2 && message[0].equalsIgnoreCase("!banw") && message[1].equalsIgnoreCase("list"))
			{
				//getBannedWords(bannedWords);
				
				if (bannedWords.isEmpty())
					event.getChannel().sendMessage("no banned words").queue();
				else
				{
					event.getChannel().sendMessage(list).queue();
				}
			}
			else if (message.length == 2 && message[0].equalsIgnoreCase("!banw") && alreadyBanned(message[1]) == true)
			{
				//getBannedWords(bannedWords);
				event.getChannel().sendMessage("already banned: " + message[1]).queue();
			}
			else if (message.length == 2 && message[0].equalsIgnoreCase("!banw") && alreadyBanned(message[1]) == false)
			{
				//getBannedWords(bannedWords);
				addBannedWordDB(message[1]);
				bannedWords.add(message[1]);
				event.getChannel().sendMessage(message[1] + " added to banned words").queue();
			}
			else if (message.length == 2 && message[0].equalsIgnoreCase("!dropw") && alreadyBanned(message[1]) == true)
			{
				//getBannedWords(bannedWords);
				System.out.println("drop word??");
				//drop word from bannedWord table in database
				dropBannedWordDB(message[1]);
				
				//remove word from bannedWords arraylist
				bannedWords.remove(bannedWords.indexOf(message[1]));
			}
		}
	}
	
	public boolean alreadyBanned(String word)
	{
		boolean status = false;
		for (String a: bannedWords)
		{
			if (a.equalsIgnoreCase(word))
			{
				System.out.println("a: " + a + "\nword: " + word);
				return status = true;
				//System.out.println(status);
			}
			else
				status = false;
		}
		System.out.println("already banned status: "  + status);
		return status;
	}
	
	
	  public static ArrayList<String> getBannedWordsList()
	  { 
		  return bannedWords; 
	  }
	  
	  //?
	  public static String getBannedWordString()
	  {
		  String bannedWordListString = "";
		  for (String a: bannedWords)
				bannedWordListString += a + " ";
		  return bannedWordListString;
	  }
	 
	
	
}
