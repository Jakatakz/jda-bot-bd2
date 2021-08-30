package commands;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.coobird.thumbnailator.Thumbnails;

public class Image extends Command {

	public Image()
	{
		this.name = "image";
		this.arguments = "[width] [height] [image-url] [degrees to rotate(optional)]";
		this.help = "rotate/resize  image links";
	}
	
	@Override
	protected void execute(CommandEvent event) 
	{
		if (event.getArgs().equalsIgnoreCase(""))
		{
			event.reply("no arguments, " + event.getAuthor().getAsMention());
			event.reply("type !halp to know how to use");
		} 
		else
		{
			try 
			{
			String[] args = event.getArgs().split(" ");
			int width = Integer.parseInt(args[0]);
			int height = Integer.parseInt(args[1]);
			URL imageURL = new URL(args[2]);
			int rotateAmount = 0;
			if (args.length == 4)
			{
				rotateAmount = Integer.parseInt(args[3]);
			}
			OutputStream os = new ByteArrayOutputStream();
			Thumbnails.of(imageURL).forceSize(width,  height).rotate(rotateAmount).outputFormat("png").toOutputStream(os);
			byte[] imageInBytes = ((ByteArrayOutputStream) os).toByteArray();
			event.getChannel().sendFile(imageInBytes, "newfile.png").queue();
			event.reply(event.getAuthor().getAsMention() + ", image formatted: width: " + width + ", height: " + height + ", degrees rotate: " + rotateAmount);
			} catch (Exception ex)
			{
				System.out.println("exception happened");
			}
		}
		
	}

}