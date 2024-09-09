/*
Short Description:  This program will accept a command line argument that represents a filename.  The file is checked
                    to validate whether or not the file has correct groupings.  It tells the user the file is good
                    if it has valid groupings or the file is bad if it does not have valid groupings
Author:  Brian Wiatrek
Date:  September 9, 2024
*/
import java.io.*;
import java.text.*;
import java.util.ArrayDeque;

public class Main {
    public static void main(String[] args) throws Exception {

        //userInput contains the command line parameter that represents the file name
        String userInput = "";

        //validInput represents whether the input file is valid or not; it's defaulted to true, but set to
        //false in the logic below
        boolean validInput = true;

        //inputLine represents a line of input from the file
        String inputLine;

        //checkFileInput is the reader for the data in the file
        BufferedReader checkFileInput = null;

        //if the number of command line arguments are not one, present error message to the user
        if (args.length != 1) {
            System.out.println("Please provide a valid filename on the command line");
            System.exit(1);
        } else {
            userInput = args[0];
        }

        try {
            File checkFile = new File(userInput);
            checkFileInput = new BufferedReader(new FileReader(checkFile));
        } catch (Exception e) {
            System.out.println("Unable to open file");
            System.exit(1);
        }

        //groupingInput is a Deque Array where I will push opening grouping signs and from where I will remove
        //opening grouping signs when I find the matching closing grouping sign
        ArrayDeque<Character> groupingInput = new ArrayDeque<>();

        //this loop will read through the file until i get to the end of the file or i find invalid input
        while (((inputLine = checkFileInput.readLine()) != null) && (validInput)){
            CharacterIterator inputLineIterator = new StringCharacterIterator(inputLine);

            //this loop will iterate through each character on the line until i get to the last character or
            //i find invalid input
            while ((inputLineIterator.current() != CharacterIterator.DONE) && (validInput)){
                Character testCh = inputLineIterator.current();
                //ignore any character that is not a grouping character
                if (((testCh.equals('{')) || (testCh.equals('(')) || (testCh.equals('['))) ||
                        (testCh.equals('}')) || (testCh.equals(')')) || (testCh.equals(']'))) {

                    //if I get an opening grouping character, push it to the deque
                    if ((testCh.equals('{')) || (testCh.equals('(')) || (testCh.equals('['))) {
                        groupingInput.add(testCh);
                    }

                    //if there is a grouping character at the end of the deque, check closing grouping chars
                    if (groupingInput.peekLast() != null) {

                        //The next three if else if blocks check to see if the end grouping character matches
                        //the opening grouping character at the end of the deque, if it does, remove the last
                        //grouping character.  If it does not, I have found an invalid grouping
                        if ((testCh.equals('}')) && (groupingInput.peekLast().equals('{'))) {
                            groupingInput.removeLast();
                        } else if ((testCh.equals('}')) && (!groupingInput.peekLast().equals('{'))) {
                            validInput = false;
                        }
                        if ((testCh.equals(')')) && (groupingInput.peekLast().equals('('))) {
                            groupingInput.removeLast();
                        } else if ((testCh.equals(')')) && (!groupingInput.peekLast().equals('('))) {
                            validInput = false;
                        }
                        if ((testCh.equals(']')) && (groupingInput.peekLast().equals('['))) {
                            groupingInput.removeLast();
                        } else if ((testCh.equals(']')) && (!groupingInput.peekLast().equals('['))) {
                            validInput = false;
                        }
                    } else {
                        validInput = false;
                    }
                }
                inputLineIterator.next();
            }
        }

        //if i have processed all the characters in the file, and i have not removed all the grouping characters
        //from the deque, then i must have invalid input.
        if (!groupingInput.isEmpty()){
            validInput = false;
        }

        if (validInput) System.out.printf("%s is a good file \n", userInput);
        else System.out.printf("%s is not a good file\n", userInput);

        //System.out.println(groupingInput);
    }
}