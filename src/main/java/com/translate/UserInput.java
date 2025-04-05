package com.translate;
import java.util.Scanner;



/**
 * A class that handles user input from the console.
 */
public class UserInput {
    private String userInput;
    private Scanner scanner;

    /**
     * Constructs a new UserInput object with a Scanner for System.in
     */
    public UserInput() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prompts the user for input and stores the response.
     * 
     * @param prompt The message to display to the user
     */
    public void promptUser(String prompt) {
        System.out.print(prompt);
        userInput = scanner.nextLine();
    }

    /**
     * Returns the user's input that was captured by the most recent promptUser call.
     * 
     * @return The stored user input as a String
     */
    public String getUserInput() {
        return userInput;
    }

    /**
     * Closes the scanner when it's no longer needed.
     * Should be called when finished with this UserInput instance.
     */
    public void close() {
        scanner.close();
    }
}