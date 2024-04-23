import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler implements Displayable {

    private Scanner scanner;
    private ArrayList<Pattern> commandPatterns;
    private CommandListener commandListener;
    private MessageListener messageListener;

    public InputHandler()
    {
        scanner = new Scanner(System.in);

        // Game command patterns, all others invalid.
        commandPatterns = new ArrayList<>();
        commandPatterns.add(Pattern.compile("^new \\w+ \\w+"));
        commandPatterns.add(Pattern.compile("^reveal \\w+ \\w+"));
        commandPatterns.add(Pattern.compile("^mark \\w+ \\w+"));
        commandPatterns.add(Pattern.compile("^exit"));
    }

    public void registerCommandListener(CommandListener listener)
    {
        commandListener = listener;
    }

    public void registerMessageListener(MessageListener listener)
    {
        messageListener = listener;
    }

    public void readInput()
    {
        String userInput = scanner.nextLine();
        validateInput(userInput);
    }

    private void validateInput(String userInput)
    {
        Matcher matcher;
        String commandString = null;
        for(int i=0; i<commandPatterns.size(); i++)
        {
            Pattern p = commandPatterns.get(i);
            matcher = p.matcher(userInput);
            if(matcher.find())
            {
                commandString = matcher.group();
                break;
            }
        }

        if(commandString != null)
        {
            processInput(commandString);
        }else
        {
            messageListener.HandleMessage("Invalid command '"+userInput+"'.");
        }
    }

    private void processInput(String commandString)
    {
        String[] sequence = commandString.split(" ");
        String command = sequence[0];

        try{
            int val1 = 0;
            int val2 = 0;
            //handles case of 'quit' command
            if(sequence.length > 1)
            {
                val1 = Integer.parseInt(sequence[1]);
                val2 = Integer.parseInt(sequence[2]);
            }
            commandListener.handleCommand(command, val1, val2);
        }catch (Exception e)
        {
            messageListener.HandleMessage("Oops! One of the number arguments was invalid. Please try again.");
        }
    }

    @Override
    public void display() {
        System.out.println("Enter a command:");
        System.out.println("");
    }
}
