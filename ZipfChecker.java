import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class ZipfChecker {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("church.txt"));
        sc.useDelimiter("[^a-zA-Z']+");
        // Scanner sc = new Scanner("this     string");
        WordInv church = new WordInv(sc);
        
        church.showError(500);
    }

}
