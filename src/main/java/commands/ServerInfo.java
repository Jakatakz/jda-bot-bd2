package commands;

import java.awt.Color;
import java.util.Arrays;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class ServerInfo extends Command {

	public ServerInfo()
	{
		this.name = "serverinfo";
		this.aliases = new String[] {"server"};
		this.help = "gives information about the server";
	}

	@Override
	protected void execute(CommandEvent event)
	{
		String[] members = new String[event.getGuild().getMembers().size()];
		for (int i = 0; i < event.getGuild().getMembers().size(); i++)
		{
			members[i] = event.getGuild().getMembers().get(i).getEffectiveName();
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor(event.getGuild().getName());
		eb.setThumbnail("https://i.imgur.com/PqR9IJt.jpg");
		eb.addField("Server Owner: ", event.getGuild().getOwner().getEffectiveName(), true);
		eb.addField("Member Count:", Integer.toString(event.getGuild().getMembers().size()), true);
		eb.setDescription("Members: \n" + Arrays.toString(members) + "\n Invite Link: \n"
				+ "https://discord.gg/wHm2TmV");
		event.getChannel().sendMessage(eb.build()).queue();
	}
	
}
