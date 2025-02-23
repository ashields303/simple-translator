import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the console
        Scanner scanner = new Scanner(System.in);
        
        // Loop indefinitely until the user types "exit"
        while (true) {
            // Prompt the user for input
            System.out.print("Enter some text (or type 'exit' to quit): ");
            
            // Read the entire line entered by the user
            String input = scanner.nextLine();
            
            // Check if the user wants to exit (case-insensitive)
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the application...");
                break;
            }
            
            // Print the user input back to the console
            System.out.println("You entered: " + input);
        }
        
        // Close the scanner to free up resources
        scanner.close();
    }
}
