import java.io.File;
import java.io.IOException;

public class Faces {

    /**
     * @param args Training file, performance file, test file
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        if(args.length!=3) {
            System.err.println("Invalid number of arguments");
            System.exit(1);
        }
        SmileyRecognizer smileyRecognizer
                = new SmileyRecognizer(new File(args[0])
                ,new File(args[1])
                ,new File(args[2]));
        smileyRecognizer.run();
    }
}
