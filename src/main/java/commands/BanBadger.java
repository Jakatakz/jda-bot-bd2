package commands;

import java.time.LocalTime;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


//actually just kicks instead of bans, this is intentional for humor sake to call it banning
public class BanBadger extends ListenerAdapter
{
		private long startTime;
		private long banRefreshTime;
		int BadgerBanAllotmentForUsers = 1;
		boolean BadgerBanRefreshTime = false;
		boolean BadgerBanON = false;		//if want ban badger command off, badgerbanON must be false
		
		public void onGuildMessageReceived(GuildMessageReceivedEvent event)
		{
			
			//refresh command for admin to refresh ban so can ban again
			//time limit for ban on badger, don't want him to constantly be getting banned, 24 hr cool down on badger ban
			
			String[] message = event.getMessage().getContentRaw().split(" ");
			checkBanAndTime();
			
			//turn badger ban off
			if (message.length == 2 && message[0].equalsIgnoreCase("!badgerban") && message[1].equalsIgnoreCase("off") && (event.getAuthor().getDiscriminator().contains("0598")
					|| event.getAuthor().getDiscriminator().contains("5713") ))
			{
				BadgerBanON = false;
			}
			
			//turn badger ban on
			if (message.length == 2 && message[0].equalsIgnoreCase("!badgerban") && message[1].equalsIgnoreCase("on") && (event.getAuthor().getDiscriminator().contains("0598")
					|| event.getAuthor().getDiscriminator().contains("5713") ))
			{
				BadgerBanON = true;
			}
			
			//check time left till users can ban badger again
			if (message.length == 2 && message[0].equalsIgnoreCase("!cban") && message[1].equalsIgnoreCase("badger") && BadgerBanON == true)
			{
				long elapsedTime = System.currentTimeMillis() - startTime;
				event.getChannel().sendMessage("elapsed time: " + elapsedTime/1000 + " secoonds").queue();
				event.getChannel().sendMessage("!ban refreshes at " + banRefreshTime/1000 + " seconds").queue();
				System.out.println("elapsedTime: " + elapsedTime/1000 + " seconds?");
				System.out.println("banrefreshtime: " + banRefreshTime/1000 + " seconds left?");
			}
			
			//refresh ban on badger for admin
			if (message.length == 2 && message[0].equalsIgnoreCase("!rban") && message[1].equalsIgnoreCase("badger") && BadgerBanON == true && (event.getAuthor().getDiscriminator().contains("0598") 
					|| event.getAuthor().getDiscriminator().contains("5713") ))
			{
				if (BadgerBanAllotmentForUsers != 1)
				{
					BadgerBanAllotmentForUsers = 1;
					BadgerBanRefreshTime = false;
				}
			}
			//if badger, allow him to ban himself, allow him to turn command off and on
			//if other user besides admin, allow 1 ban every 24 hours (may change), only allow 1 total ban for all users every 24 hours
			if ((message.length == 2 && message[0].equalsIgnoreCase("!ban") && message[1].equalsIgnoreCase("badger") && BadgerBanAllotmentForUsers == 1 && BadgerBanON == true) 
					|| (message.length == 2 && message[0].equalsIgnoreCase("!ban") && message[1].equalsIgnoreCase("badger")  && event.getAuthor().getDiscriminator().contains("0598")))
			{
				User usr = event.getJDA().getUserById("108018188054286336");
				//User usr = event.getJDA().getUserById("609646183291420682");  //for testing
				if (event.getGuild().isMember(usr))
						{
							sendPrivateMessageForBadgerKick(usr, event.getChannel().createInvite().setMaxUses(0).setMaxAge(0).complete().getUrl()
									+ "  \n" + event.getAuthor().getName()
									+ " banned you, I'm sorry badger pls rejoin");
							//event.getGuild().getController().kick("609646183291420682").queue(); //for testing
							//event.getChannel().sendMessage("Bug Trio has been banned").queue();  //for testing
							event.getGuild().kick("108018188054286336").queue();	//kicks badger, not ban
							event.getChannel().sendMessage("Badger has been banned").queue();
						}
				else
					event.getChannel().sendMessage("the poor boys already banned").queue();
				
				BadgerBanAllotmentForUsers--;
				BadgerBanRefreshTime = true;
				startTime = System.currentTimeMillis();
				banRefreshTime = 86400000;
			}
			
		}
		
		
		//pm badger with reinvite link and who banned him
		private void sendPrivateMessageForBadgerKick(User user, String content)
		{
			user.openPrivateChannel().queue( (channel) 
					-> channel.sendMessage(content).queue());
		}
		
		private void checkBanAndTime()
		{
			if (BadgerBanRefreshTime == true)
			{
				long elapsedTime = System.currentTimeMillis() - startTime;
				if (elapsedTime >= banRefreshTime)
				{
					System.out.println("did checkbantime");
					BadgerBanAllotmentForUsers = 1;
					BadgerBanRefreshTime = false;
				}
				
			}
		}

		
		
	}

