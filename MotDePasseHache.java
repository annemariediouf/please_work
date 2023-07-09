import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MotDePasseHache {

    public interface CasserMotDePasse {
        String casserMotDePasse(String valeurHachee);
    }

    public static class CrackerBruteForce implements CasserMotDePasse {
        private String caracteres;
        private int longueurMotDePasse;

        public CrackerBruteForce(String caracteres, int longueurMotDePasse) {
            this.caracteres = caracteres;
            this.longueurMotDePasse = longueurMotDePasse;
        }

        @Override
        public String casserMotDePasse(String valeurHachee) {
            StringBuilder motDePasse = new StringBuilder();
            char[] tableauCaracteres = caracteres.toCharArray();
            int[] indices = new int[longueurMotDePasse];
            boolean trouve = false;

            while (!trouve) {
                motDePasse.setLength(0);
                for (int index : indices) {
                    motDePasse.append(tableauCaracteres[index]);
                }

                String motDePasseHache = getMD5Hash(motDePasse.toString());

                if (motDePasseHache.equals(valeurHachee)) {
                    trouve = true;
                } else {
                    int i = longueurMotDePasse - 1;
                    while (i >= 0 && indices[i] == tableauCaracteres.length - 1) {
                        indices[i] = 0;
                        i--;
                    }
                    if (i < 0) {
                        break;
                    }
                    indices[i]++;
                }
            }

            if (trouve) {
                return motDePasse.toString();
            } else {
                return null;
            }
        }
    }

    public static class CrackerDictionnaire implements CasserMotDePasse {
        private List<String> dictionnaire;

        public CrackerDictionnaire(List<String> dictionnaire) {
            this.dictionnaire = dictionnaire;
        }

        @Override
        public String casserMotDePasse(String valeurHachee) {
            for (String motDePasse : dictionnaire) {
                String motDePasseHache = getMD5Hash(motDePasse);
                if (motDePasseHache.equals(valeurHachee)) {
                    return motDePasse;
                }
            }
            return null;
        }
    }

    private static List<String> chargerDictionnaire(String nomFichier) {
        List<String> dictionnaire = new ArrayList<>();

        // Chargement du dictionnaire depuis un fichier ou une autre source
        // Ajouter les mots de passe potentiels au dictionnaire

        dictionnaire.add("test");
        dictionnaire.add("lait");
        dictionnaire.add("love");
        dictionnaire.add("peur");
        dictionnaire.add("yeux");

        return dictionnaire;
    }

    private static String getMD5Hash(String input) {
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

    public static void main(String[] args) {
        String valeurHachee = "098f6bcd4621d373cade4e832627b4f6";

        Scanner scanner = new Scanner(System.in);

        System.out.print("Veuillez saisir votre mot de passe : ");
        String motDePasse = scanner.nextLine();

        scanner.close();

        // Création du cracker en utilisant la Factory Method
        CasserMotDePasse cracker = creerCracker();

        String motDePasseForceBrute = cracker.casserMotDePasse(valeurHachee);

        if (motDePasseForceBrute != null) {
            System.out.println("Le mot de passe trouvé par force brute est : " + motDePasseForceBrute);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par force brute.");
        }

        String motDePasseDictionnaire = cracker.casserMotDePasse(valeurHachee);

        if (motDePasseDictionnaire != null) {
            System.out.println("Le mot de passe trouvé par attaque par dictionnaire est : " + motDePasseDictionnaire);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par attaque par dictionnaire.");
        }
    }

    private static CasserMotDePasse creerCracker() {
        // Choix du type de cracker (BruteForce ou Dictionary)
        String typeCracker = "BruteForce";

        if (typeCracker.equalsIgnoreCase("BruteForce")) {
            String caracteres = "abcdefghijklmnopqrstuvwxyz";
            int longueurMotDePasse = 4;
            return new CrackerBruteForce(caracteres, longueurMotDePasse);
        } else if (typeCracker.equalsIgnoreCase("Dictionary")) {
            List<String> dictionnaire = chargerDictionnaire("dictionnaire.txt");
            return new CrackerDictionnaire(dictionnaire);
        }

        throw new IllegalArgumentException("Type de cracker invalide.");
    }
}
