import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordSecurityDemo
{
    public static void main(String[] args)
      {
        System.out.println("   Password Hashing & Strength Demo      ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        //Take password input securely if possible, fallback to Scanner
        String password= getPasswordInput();
        
        if ((password == null) || (password.isEmpty()))
        {
            System.out.println("Error: Password cannot be empty");
            return;
        }

        //Check strength rules
        boolean isStrong = checkPasswordStrength(password);
        System.out.println("\n[+] Strength Evaluation:");
        System.out.println(isStrong ? "    -> Status: STRONG" : "    -> Status: WEAK (Fix: Ensure 8+ chars, uppercase, digit, & symbol)");

        //Hash the password
        String hashedPassword = hashWithSHA256(password);
        System.out.println("\n[+] Computed Hash (SHA-256):");
        System.out.println("    "+ hashedPassword);

        //Print security explanations
        System.out.println("⚠️  CRITICAL SECURITY EDUCATION:");
        System.out.println("1. NEVER STORE PLAIN TEXT PASSWORDS!");
        System.out.println("If a database is leaked, plain text passwords expose users instantly.");
        System.out.println("2. ARCHITECTURAL WARNING:");
        System.out.println("SHA-256 alone is NOT ideal for passwords. It is too fast, allowing attackers\nto brute-force billions of hashes per second. Production\nsystems must use slow key-derivation functions like BCrypt or Argon2.");
    }

    private static String getPasswordInput()
  {
        Console console= System.console();
        if (console!=null) {
            char[] passwordArray = console.readPassword("Enter password for testing:");
            return new String(passwordArray);
        }
        else
        {
            // Fallback for IDE environments where System.console() can return null
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter password to test:");
            return scanner.nextLine();
        }
    }

    private static boolean checkPasswordStrength(String pwd) {
        if (pwd.length() < 8)
          return false;
        
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : pwd.toCharArray())
        {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
      return hasUpper && hasDigit && hasSpecial;
    }

    private static String hashWithSHA256(String input) {
        try
          {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            // Convert byte array into signum representation
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash)
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length()==1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e)
          {
            throw new RuntimeException(e);
          }
    }
}
