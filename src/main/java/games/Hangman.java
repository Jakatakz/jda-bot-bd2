package games;

import java.util.ArrayList;
import java.util.TreeSet;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Hangman extends ListenerAdapter {

	private final static int wrongGuessesMax = 6; //6 to create the hangman and lose
	private final static int possibleGuesses = 26; //a-z, capitalization doesn't matter
	private final static int wordSizeMax = 25;
	private final static int playGamesMaxQueue = 3;
	private int customWordSize;
	private int defaultWordSize = (int)(Math.random() * wordSizeMax);
	private String[] hangmanStatesArrayStr = hangmanStates();
	private TreeSet<Character> userAnswers = new TreeSet<>();
	private TreeSet<Character> correctAnswers = new TreeSet<>();
	private TreeSet<Character> incorrectAnswers = new TreeSet<>();
	private String[] wordBank = {"ant", "baboon", "badger", "bat", "bear", "beaver", "camel", "cat", "clam", "cobra", "cougar",
	         "coyote", "crow", "deer", "dog", "donkey", "duck", "eagle", "ferret", "fox", "frog", "goat",
	         "goose", "hawk", "lion", "lizard", "llama", "mole", "monkey", "moose", "mouse", "mule", "newt",
	         "otter", "owl", "panda", "parrot", "pigeon", "python", "rabbit", "ram", "rat", "raven",
	         "rhino", "salmon", "seal", "shark", "sheep", "skunk", "sloth", "snake", "spider",
	         "stork", "swan", "tiger", "toad", "trout", "turkey", "turtle", "weasel", "whale", "wolf",
	         "wombat", "zebra"};
	private String hangmanWord = getHangmanWord(wordBank);
	private int falseGuesses = 0;
	
	public Hangman() {
	}
	
	public Hangman(int customWordSize) {
		if (customWordSize <= wordSizeMax || customWordSize >0)
			this.customWordSize = customWordSize;
	}
	
	private String getHangmanWord(String[] wordBank) {
		return wordBank[((int)(Math.random() * wordBank.length))];
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] playHangmanString = event.getMessage().getContentRaw().split("\\s+");
		
		//if (playHangmanString[0].equalsIgnoreCase("!play") && playHangmanString[1].equalsIgnoreCase("Hangman")) {
		if (playHangmanString[0].equalsIgnoreCase("!play")) {	
			System.out.println("playing");
		//Hangman hangmanGame = new Hangman();
			hangmanStartGameIntroduction(event, hangmanStatesArrayStr);
			playHangman(event, hangmanStatesArrayStr, correctAnswers, incorrectAnswers, userAnswers, hangmanWord);
		}
	}
	
	//booleam marker for game in progress so you can reead more messages and not just !play
	
	private void hangmanStartGameIntroduction(GuildMessageReceivedEvent event, String[] hangmanStatesArrayStr) {
		String hangmanDefaultState = hangmanStatesArrayStr[0];
		
		event.getChannel().sendMessage("Hangman Game"
				+ "\n###############\n" 
				+ hangmanDefaultState 
				+"\n###############").queue();
	}
	
	private void playHangman(GuildMessageReceivedEvent event, String[] hangmanStatesArrayStr, TreeSet<Character> correctAnswers,
			TreeSet<Character> incorrectAnswers, TreeSet<Character> userAnswers, String hangmanWord) {
		int guesses = 0;//used for max guesses limitation maybe, not used yet.
		int hangmanstate = 1;
		boolean endState = false;
		
		do {
		event.getChannel().sendMessage("Enter your guesses: \n" + correctAnswers + "\n" + hangmanStatesArrayStr[hangmanstate] + "\n" + incorrectAnswers).queue();
		
		char userGuess = getUserGuess(event);
		System.out.println("GetUserGuess2: " + userGuess);
		boolean userGuessResult = checkUserGuess(userGuess, hangmanWord);
		//updating game, if userGuessResult is false aka wrong answer, then add the incorrect answer to incorrectAnswers treeset and userAnswers, and display the next hangman state
		endState = UpdateGame(userGuess, userGuessResult, userAnswers, incorrectAnswers, correctAnswers, event);
			if (endState = true) {
				//to end game, display hangman state and a message event saying its done
				event.getChannel().sendMessage("you lost\n" + hangmanStatesArrayStr[5]).queue();
			}
			else {
				hangmanstate++;
			}
		} while (endState == false);
		
		System.out.println("false guesses: " + falseGuesses);
	}
		
	
	//contains the possibility to end the game based on too many incorrect answers
	private boolean UpdateGame(char userGuess, boolean userGuessResult, TreeSet<Character> userAnswers, 
			TreeSet<Character> incorrectAnswers, TreeSet<Character> correctAnswers, GuildMessageReceivedEvent event) {
		
		boolean flag = false; // false dont end game, true end game
		if (userGuessResult==false && falseGuesses != wrongGuessesMax) {
			userAnswers.add(userGuess);
			incorrectAnswers.add(userGuess);
			falseGuesses++;
		}
		else if (userGuessResult==false && falseGuesses == wrongGuessesMax) {
			//end game, you lost
			userAnswers.add(userGuess);
			incorrectAnswers.add(userGuess);
			flag = true;
			
		}
		else {
			//userGuessResult must be true
			userAnswers.add(userGuess);
			correctAnswers.add(userGuess);
		}
		return flag;
	}
	
	private char getUserGuess(GuildMessageReceivedEvent event) {
		char userGuess = 'a';
		boolean validChk = true;
		String userGuessString = event.getMessage().getContentRaw();
		Message
		System.out.println("i userGuessString: " + userGuessString);
		do {
		if (userGuessString.length() == 1 && userGuessString.matches("[a-zA-z]")) {
				userGuess = userGuessString.charAt(0);
				validChk = false;
		}
		else
		{
			event.getChannel().sendMessage("just input 1 character, a-z");
		}
		} while (validChk);
		System.out.println("GetUserGuess1: " + userGuess);
		return userGuess;
	}
	
	private boolean checkUserGuess(char userGuess,  String hangmanWord) {
		return (hangmanWord.contains(Character.toString(userGuess)));
	}
	
	private static String[] hangmanStates() {
		String[] hangmanStates =
			 {
			"  +---+\n   |\t  |\n\t      |\n\t      |\n\t\t  |\n\t\t  |\n=========",
			"  +---+\n   |\t  |\n\t      |\n\t      |\n\t\t  |\n\t\t  |\n=========",
			"  +---+\n   o\t  |\n\t      |\n\t      |\n\t\t  |\n\t\t  |\n=========",
			"  +---+\n   |\t  |\n\t      |\n\t      |\n\t\t  |\n\t\t  |\n=========",
		    "  +---+\n   |\t  |\n\t      |\n\t      |\n\t\t  |\n\t\t  |\n=========",
			"  +---+\n   |\t  |\n\t      |\n\t      |\n\t\t  |\n\t\t  |\n=========",
			"  +---+\n   |\t  |\n\t      |\n\t      |\n\t\t  |\n\t\t  |\n========="
			};
		return hangmanStates;
	}
	
	
}
