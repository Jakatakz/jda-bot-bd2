package commands;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UserInfoCommand extends Command
{
	private EventWaiter waiter;
	
	public UserInfoCommand(EventWaiter waiter)
	{
		super.name = "ui";
		super.help = "Get some information about the user";
		super.aliases = new String[] {"userinfo"};
		super.category = new Category("Members");
		super.cooldown = 10;
		super.arguments = "[name]";
		this.waiter = waiter; 
	}
	
	@Override
	protected void execute(CommandEvent event)
	{
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			event.reply("give name of user for their info: like @Baboon");
			waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor()) 
					&& e.getChannel().equals(event.getChannel()), e -> {
			
			try 
			{
				Member name = e.getMessage().getMentionedMembers().get(0);
				EmbedBuilder eb = new EmbedBuilder()
						.setColor(Color.magenta)
						.setThumbnail("https://images-i.jpimedia.uk/imagefetch/c_fill,f_auto,h_1133,q_auto:eco,w_1700/https://inews.co.uk/wp-content/uploads/2016/09/pepehead.jpg")
						.setAuthor("Information on " + name.getUser().getName(),
								"http://www.google.com", 
								name.getUser().getAvatarUrl())
						.setDescription(name.getUser().getName() 
								+ " joined on " + name.getTimeJoined().format(fmt))
						.addField("Game: " , "unavailable", true)
						.addField("Status: ", name.getOnlineStatus().toString(), true)
						.addField("NickName: ", 
								name.getNickname() == null ? "no nickname" : name.getNickname(),
										true)
						.addField("Roles: ", getRolesString(name.getRoles()), true);
				event.reply(eb.build());
			} catch (IndexOutOfBoundsException ex)
			{
				System.out.println("Exception Occurred");
				ex.printStackTrace();
				event.reply("You need to provide name as a mention.");
			}
					}, 7, TimeUnit.SECONDS, () -> event.reply("no name to search, time expired"));
	}
	
	
	//jdabuilder.enablecache(cacheflag.activity); then use member.getActivities() method maybe
	//doesn't work anymore in this version, no comparable methods
	/*
	 * private String displayGameInfo(Member name) { try { String game =
	 * name.getGame().getName(); return "playing game: " + game; } catch
	 * (NullPointerException ex) { return "no game is being played"; }
	 * 
	 * }
	 */
	
	private String getRolesString(List rolesList)
	{
		String roles;
		if (!rolesList.isEmpty())
		{
			Role tempRole = (Role) rolesList.get(0);
			roles = tempRole.getName();
			for (int i = 1; i < rolesList.size(); i++)
			{
				tempRole = (Role) rolesList.get(i);
				roles = roles + ", " + tempRole.getName();
			}
		}
		else
		{
			roles = "none";
		}
		return roles;
	}
}
