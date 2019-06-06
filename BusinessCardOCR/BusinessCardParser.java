import java.nio.file.*;
import java.io.File;
import java.util.Scanner;

public class BusinessCardParser implements ContactInfo {
  private String name, number, email;

  /*Creates a ContactInfo object. Iterates through the document and
  sets name, number and email accordingly*/
  public ContactInfo getContactInfo(String document) {
    //Scanner object is initialized
    Scanner doc = new Scanner(document);

    //Scanner object is used to iterate line-by-line through the document
    while(doc.hasNextLine()) {
      String line = doc.nextLine();

      //Checks for the '@' char to find what line is an email address
      if(line.indexOf('@') >= 0) {
        email = line;
      }
      //Checks for the '-' char to find what line is a number
      else if(line.indexOf('-') >= 0) {
        if(!line.contains("Fax") && !line.contains("Pager")) {
          //Uses a regular expression to remove any character that is not a digit
          number = line.replaceAll("\\D", "");
        }
      }
    }

    doc = new Scanner(document);

    /*Makes a second pass through the document to find the name. It uses the last
    name in the email that we found earlier*/
    while(doc.hasNextLine()) {
      String line = doc.nextLine();
      //The index of the last letter of the last name
      int emailIndex = document.indexOf('@') - 1;

      /*Iterates through the line in reverse to check for the last name found
      on the email*/
      for(int i = line.length() - 1; i >= 0; i--) {
        //End of last name
        if(line.charAt(i) == ' ') {
          name = line;
          break;
        }
        //Compares characters from the email and the line
        else if(Character.toLowerCase(line.charAt(i)) != Character.toLowerCase(document.charAt(emailIndex))) {
          break;
        } else {
          emailIndex--;
        }
      }

      if(name != null)
        break;
    }

    doc.close();

    return this;
  }

  //Returns the name//
  public String getName() {
    return name;
  }

  //Returns the phone number
  public String getPhoneNumber() {
    return number;
  }

  //Returns the email address//
  public String getEmailAddress() {
    return email;
  }

  //Main method
  public static void main(String args[]) throws Exception {
    System.out.println("Welcome to the Business Card OCR Parser!\n");

    //Uses a Scanner object to read user input
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter a file name (or type \"Exit\" to end the program): ");
    String choice = scanner.nextLine();

    /*While loop allows the user to parse multiple documents without needing
    to restart the program*/
    while(!choice.equalsIgnoreCase("exit")) {
      File file = new File(choice);

      //Checks if a file exists
      if(file.exists()) {
        //Reads the text file from the chosen file and converts to a string
        String document = new String(Files.readAllBytes(Paths.get(choice)));
        //Creates a BusinessCardParser object
        BusinessCardParser bcp = new BusinessCardParser();
        //Uses the BusinessCardParser object to call getContactInfo. Passes doc as a parameter
        bcp.getContactInfo(document);
        //Prints output
        System.out.println("\nName: " + bcp.getName());
        System.out.println("Phone: " + bcp.getPhoneNumber());
        System.out.println("Email: " + bcp.getEmailAddress());
      } else {
        //Error message
        System.out.println("\nOops, that file does not exist!");
      }

      //Reads next input
      System.out.print("\nEnter a file name (or type \"Exit\" to end the program): ");
      choice = scanner.nextLine();
    }

    System.out.println("\nUntil next time, bye!");
  }
}
