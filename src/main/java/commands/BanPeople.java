package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Jakatakz.jda_bot_bd.DBConnect;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BanPeople extends ListenerAdapter
{
	static Connection conn = DBConnect.dbConnect();
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		String message = event.getMessage().getContentRaw();
		List<String> listOfAllMembersEver = listOfAllMembersEver();
		
		//0598 is bot i think.
		//update table with record in users table
		if (message.startsWith("!ban") && event.getAuthor().getDiscriminator().contains("0598"))
		{
			boolean notAMember = true;
			for (String a: listOfAllMembersEver)
			{
				if (message.contains(a))
						{
							notAMember = false;
							//get userid to unban
							String userIDForBan = getUserIDForBan(a);
					
							//doesn't notify whether user has already been banned?, FIX LATER?
							event.getGuild().ban(userIDForBan, 0).queue();
							//need to update users table with updated record
							try
							{
								Date date = new Date();
								Timestamp banTimeStamp = new Timestamp(date.getTime());
								String sqlUnban = "UPDATE mydiscordusers SET warnings = ? , banned = ? , bantimestamp = ? WHERE userid = ?";
								PreparedStatement prepStat = conn.prepareStatement(sqlUnban);
								prepStat.setInt(1, 6);
								prepStat.setString(2, "yes");
								prepStat.setTimestamp(3, banTimeStamp);
								prepStat.setString(4, userIDForBan);
								prepStat.executeUpdate();
								event.getChannel().sendMessage(a + " has been banned").queue();
							} catch (SQLException e)
							{
								System.out.println("Didn't update mydiscordusers table");
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

public String getUserIDForBan(String a)
{

String sqlUserIDForBan = "SELECT userid FROM mydiscordusers WHERE name = ?";
String userID = "";
try
{
	PreparedStatement prepStat3 = conn.prepareStatement(sqlUserIDForBan);
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
	

