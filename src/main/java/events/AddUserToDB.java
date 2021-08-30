package events;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.Jakatakz.jda_bot_bd.DBConnect;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AddUserToDB extends ListenerAdapter 
{
	static Connection conn = DBConnect.dbConnect();
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		//when user types anything, theyre added to the database
		try
		{
			if (!event.getAuthor().isBot())
			{
			
			String query = " INSERT IGNORE INTO mydiscordusers (name, nickname, userid, warnings, banned, permabanned, discriminator, totalmessages)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?,?)";
			
			PreparedStatement prepStat = conn.prepareStatement(query);
			prepStat.setString(1, event.getMember().getEffectiveName());
			prepStat.setString(2, event.getMember().getNickname());
			prepStat.setString(3, event.getMember().getUser().getId());
			prepStat.setInt(4, 0);
			prepStat.setString(5, "no");
			prepStat.setString(6, "no"); 
			prepStat.setString(7, event.getAuthor().getDiscriminator());
			prepStat.setInt(8, 0);
		
			prepStat.executeUpdate();
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("hereadduserdb");
		}
	}
	
}

