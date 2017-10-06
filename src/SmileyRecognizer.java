import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class SmileyRecognizer {
    private ArrayList<Image> imageList;
    private ASCIIreader asciiReader;
    private File testFile;

    public static Function<ArrayList<Image>,ArrayList<Image>> imageListShuffle = a-> {
        Collections.shuffle(a);
        return a;
    };

    public static void main(String args[]){

    }
}
