package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.Jakatakz.jda_bot_bd.DBConnect;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UnbanPeople extends ListenerAdapter
{
	static Connection conn = DBConnect.dbConnect();
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		//String[] message = event.getMessage().getContentRaw().split(" ");
		String message = event.getMessage().getContentRaw();
		List<String> listOfAllMembersEver = listOfAllMembersEver();
		
		if (message.startsWith("!unban") && event.getAuthor().getDiscriminator().contains("0598"))
				{
					//dont use getMembers(), doesn't show members who've been banned
					//List<Member> memberList = event.getGuild().getMembers();
					
					boolean notAMember = true;
					for (String a: listOfAllMembersEver)
					{
						if (message.contains(a))
								{
									notAMember = false;
									//get userid to unban
									String userIDForUnban = getUserIDForUnban(a);
							
									//doesn't notify whether user has already been unbanned, FIX LATER?
									event.getGuild().unban(userIDForUnban).queue();
									//need to update users table with updated record
									try
									{
										String sqlUnban = "UPDATE mydiscordusers SET warnings = ? , banned = ? , bantimestamp = ? WHERE userid = ?";
										PreparedStatement prepStat = conn.prepareStatement(sqlUnban);
										prepStat.setInt(1, 0);
										prepStat.setString(2, "no");
										prepStat.setTimestamp(3, null);
										prepStat.setString(4, userIDForUnban);
										prepStat.executeUpdate();
										event.getChannel().sendMessage(a + " has been unbanned").queue();
									} catch (SQLException e)
									{
										System.out.println("Didn't update users table");
										// TODO Auto-generated catch block
										e.printStackTrace();
										System.out.println("HERE1");
									}
									}
					}
					if (notAMember == true)
					{
						event.getChannel().sendMessage(" is not a member of this server").queue();
					}
				}
		
	}
	
	public List<String> listOfAllMembersEver()
	{
		String sqlListAllMembersEver = "SELECT name FROM mydiscordusers;";
		List<String> listOfAllMembersEver = new ArrayList<>();
		try
		{
			PreparedStatement prepStat2 = conn.prepareStatement(sqlListAllMembersEver);
			ResultSet rst = prepStat2.executeQuery();
			while (rst.next())
			{
				listOfAllMembersEver.add(rst.getString("name"));
				//System.out.println("added -> " + rst.getString("name"));
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listOfAllMembersEver;
		
	}
	
	public String getUserIDForUnban(String a)
	{
		
		String sqlUserIDForUnban = "SELECT userid FROM mydiscordusers WHERE name = ?";
		String userID = "";
		try
		{
			PreparedStatement prepStat3 = conn.prepareStatement(sqlUserIDForUnban);
			prepStat3.setString(1, a);
			ResultSet rst2 = prepStat3.executeQuery();
			while(rst2.next())
			{
				userID = rst2.getString("userid");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return userID;
	}
	
}
