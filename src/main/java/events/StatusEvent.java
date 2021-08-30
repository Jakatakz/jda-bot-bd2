package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.time.LocalTime;

public class StatusEvent extends ListenerAdapter 
{
	private int startTime;
	
	public StatusEvent(int startTime)
	{
		this.startTime = startTime;
	}
	
	
	private LocalTime getOnlineElapsedDuration()
	{
		LocalTime lt2 = LocalTime.now();
		String currentTime = lt2.toString();
		LocalTime time = LocalTime.parse(currentTime);
		int endTime = time.toSecondOfDay();
		long onlineElapsedDuration = endTime - startTime;
		LocalTime duration = LocalTime.ofSecondOfDay(onlineElapsedDuration);
		return duration;
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		String[] messageSent = event.getMessage().getContentRaw().split(" ");
		if(messageSent[0].equalsIgnoreCase("bb") && messageSent[1].equalsIgnoreCase("status"))
		{
			if (!event.getMember().getUser().isBot())
				event.getChannel().sendMessage("online for: " +
						getOnlineElapsedDuration()).queue();
		}
			
	}
	
	
}
