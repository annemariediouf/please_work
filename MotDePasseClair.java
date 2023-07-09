import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class MotDePasseClair {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Veuillez saisir votre mot de passe : ");
        String password = scanner.nextLine();

        String hashedPassword = getMD5Hash(password);

        List<MotDePasseClair> crackers = createMotDePasseClair();
        boolean passwordFound = false;

        for (MotDePasseClair cracker : crackers) {
            String crackedPassword = cracker.crackPassword(hashedPassword);

            if (crackedPassword != null) {
                if (cracker instanceof DictionaryMotDePasseClair) {
                    System.out.println("Le mot de passe trouvé par attaque par dictionnaire est : " + crackedPassword);
                } else if (cracker instanceof BruteForceMotDePasseClair) {
                    System.out.println("Le mot de passe trouvé par attaque brute force est : " + crackedPassword);
                } else {
                    System.out.println("Le mot de passe trouvé est : " + crackedPassword);
                }
                passwordFound = true;
            }
        }

        if (!passwordFound) {
            System.out.println("Le mot de passe n'a pas été trouvé.");
        }

        scanner.close();
    }

    // Factory Method
    public static List<MotDePasseClair> createMotDePasseClair() {
        List<MotDePasseClair> crackers = new ArrayList<>();
        crackers.add(new DictionaryMotDePasseClair());
        crackers.add(new BruteForceMotDePasseClair());
        return crackers;
    }

    public abstract String crackPassword(String hashedPassword);

    protected static String getMD5Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class BruteForceMotDePasseClair extends MotDePasseClair {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_LENGTH = 4;

    @Override
    public String crackPassword(String hashValue) {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            StringBuilder password = new StringBuilder(length);
            generatePasswords(password, length, hashValue);
            if (password.length() > 0) {
                return password.toString();
            }
        }
        return null;
    }

    private boolean generatePasswords(StringBuilder password, int length, String hashValue) {
        if (password.length() == length) {
            String hashedPassword = getMD5Hash(password.toString());
            if (hashedPassword.equals(hashValue)) {
                return true;
            }
            return false;
        }

        for (int i = 0; i < CHARACTERS.length(); i++) {
            password.append(CHARACTERS.charAt(i));
            if (generatePasswords(password, length, hashValue)) {
                return true;
            }
            password.setLength(password.length() - 1);
        }

        return false;
    }
}

class DictionaryMotDePasseClair extends MotDePasseClair {
    private static final List<String> DICTIONARY = loadDictionary();

    @Override
    public String crackPassword(String hashValue) {
        return dictionaryAttack(hashValue);
    }

    public String dictionaryAttack(String hashValue) {
        for (String password : DICTIONARY) {
            String hashedPassword = getMD5Hash(password);
            if (hashedPassword.equals(hashValue)) {
                return password;
            }
        }
        return null;
    }

    private static List<String> loadDictionary() {
        List<String> dictionary = new ArrayList<>();
        // Charger le dictionnaire depuis un fichier ou une autre source
        // Ajouter les mots de passe potentiels au dictionnaire

        dictionary.add("pile");
        dictionary.add("lait");
        dictionary.add("main");
        dictionary.add("test");
        dictionary.add("voir");
        dictionary.add("toit");
        dictionary.add("love");

        return dictionary;
    }
}
