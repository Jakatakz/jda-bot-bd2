package events;

import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TextChannelCreate extends ListenerAdapter {

	public void onTextChannelCreate(TextChannelCreateEvent event)
	{
		event.getGuild().getDefaultChannel().sendMessage("new text channel created: " 
				+ event.getChannel().getName().toUpperCase()).queue();
	}
	
}
