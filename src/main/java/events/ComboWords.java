package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ComboWords extends ListenerAdapter
{
	private int comboCounter = 0;
	String previousPoster = "";
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
	
		
		//for wow combo, i don't think it is possible to edit a users message or the bots message
		//therefore, we count how many times wow or variations of wow were used 
		//and after they arent used alone, either interrupted by other words or not alone the bot will post a combo counter
		//and go on a cooldown before a combo can be activated again
		
		// WOW COMBO COUNTER
		if ((event.getMessage().getContentRaw().equalsIgnoreCase("wow") 
				|| event.getMessage().getContentRaw().equalsIgnoreCase("weow") 
				|| event.getMessage().getContentRaw().equalsIgnoreCase("waow")) 
				&& (!event.getAuthor().isBot()) 
				&& !previousPoster.contains(event.getAuthor().getId()))
				{
					comboCounter++;
					previousPoster = event.getAuthor().getId();	//if previous poster posts wow again, it breaks combo
					System.out.println(previousPoster);
					if (comboCounter >= 2)
					{
						//event.getMessage().delete().queue();
					}
					System.out.println(comboCounter);
					
					
					}
//		else if ((!event.getMessage().getContentRaw().equalsIgnoreCase("wow") 
//				|| !event.getMessage().getContentRaw().equalsIgnoreCase("weow") 
//				|| !event.getMessage().getContentRaw().equalsIgnoreCase("waow")) 
//				&& (!event.getAuthor().isBot()) && comboCounter > 0
//				&& previousPoster.contains(event.getAuthor().getId()))
		else
		{
			//combo breaks
			if (comboCounter == 1)
			{
				comboCounter = 0;
				//ignore
			}
			else if (comboCounter == 2 || comboCounter == 3)
			{
				event.getChannel().sendMessage(":regional_indicator_w: :regional_indicator_o: :regional_indicator_w: x " + comboCounter).queue();  //+1 is for initial wow before incrementing
				comboCounter = 0;
			}
			else if (comboCounter > 3)
			{
				event.getChannel().sendMessage(":regional_indicator_w: :regional_indicator_o: :regional_indicator_w: x " + comboCounter).queue();
				comboCounter = 0;
			}
		}
				}
				
	}


