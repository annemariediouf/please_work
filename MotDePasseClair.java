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
        String motDePasse = scanner.nextLine();

        String motDePasseHache = getMD5Hash(motDePasse);

        List<MotDePasseClair> crackers = createMotDePasseClair();
        boolean motDePasseTrouve = false;

        for (MotDePasseClair cracker : crackers) {
            String motDePasseDechiffre = cracker.casserMotDePasse(motDePasseHache);

            if (motDePasseDechiffre != null) {
                if (cracker instanceof DictionaryMotDePasseClair) {
                    System.out.println("Le mot de passe trouvé par attaque par dictionnaire est : " + motDePasseDechiffre);
                } else if (cracker instanceof BruteForceMotDePasseClair) {
                    System.out.println("Le mot de passe trouvé par attaque brute force est : " + motDePasseDechiffre);
                } else {
                    System.out.println("Le mot de passe trouvé est : " + motDePasseDechiffre);
                }
                motDePasseTrouve = true;
            }
        }

        if (!motDePasseTrouve) {
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

    public abstract String casserMotDePasse(String motDePasseHache);

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
    private static final String CARACTERES = "abcdefghijklmnopqrstuvwxyz";
    private static final int LONGUEUR_MAX = 4;

    @Override
    public String casserMotDePasse(String valeurHachee) {
        for (int longueur = 1; longueur <= LONGUEUR_MAX; longueur++) {
            StringBuilder motDePasse = new StringBuilder(longueur);
            genererMotsDePasse(motDePasse, longueur, valeurHachee);
            if (motDePasse.length() > 0) {
                return motDePasse.toString();
            }
        }
        return null;
    }

    private boolean genererMotsDePasse(StringBuilder motDePasse, int longueur, String valeurHachee) {
        if (motDePasse.length() == longueur) {
            String motDePasseHache = getMD5Hash(motDePasse.toString());
            if (motDePasseHache.equals(valeurHachee)) {
                return true;
            }
            return false;
        }

        for (int i = 0; i < CARACTERES.length(); i++) {
            motDePasse.append(CARACTERES.charAt(i));
            if (genererMotsDePasse(motDePasse, longueur, valeurHachee)) {
                return true;
            }
            motDePasse.setLength(motDePasse.length() - 1);
        }

        return false;
    }
}

class DictionnaireMotDePasseClair extends MotDePasseClair {
    private static final List<String> DICTIONNAIRE = chargerDictionnaire();

    @Override
    public String casserMotDePasse(String valeurHachee) {
        return attaqueDictionnaire(valeurHachee);
    }

    public String attaqueDictionnaire(String valeurHachee) {
        for (String motDePasse : DICTIONNAIRE) {
            String motDePasseHache = getMD5Hash(motDePasse);
            if (motDePasseHache.equals(valeurHachee)) {
                return motDePasse;
            }
        }
        return null;
    }

    private static List<String> chargerDictionnaire() {
        List<String> dictionnaire = new ArrayList<>();
        // Charger le dictionnaire depuis un fichier ou une autre source
        // Ajouter les mots de passe potentiels au dictionnaire

        dictionnaire.add("pile");
        dictionnaire.add("lait");
        dictionnaire.add("main");
        dictionnaire.add("test");
        dictionnaire.add("voir");
        dictionnaire.add("toit");
        dictionnaire.add("love");

        return dictionnaire;
    }
}
