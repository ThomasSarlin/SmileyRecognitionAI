import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class SmileyRecognizer {
    private ASCIIreader asciiReader;
    private ArrayList<Perceptron> perceptrons;
    public static Function<ArrayList<Image>,ArrayList<Image>> imageListShuffle = a-> {
        Collections.shuffle(a);
        return a;
    };
    public SmileyRecognizer(File imageFile,File answerFile,File testFile){
        initializeImages(imageFile,answerFile);
        initializeWeights();
    }
    public static Function<Double,Double> activationFunction = e->Math.tanh(e);

    public double sumWWeights(int index, Image image){
        double sum = 0;
        for(int i= 0; i<402 ; i++){
              sum+=perceptrons.get(i).getWeight(index)*image.getImage()[i];
        }
        return sum;
    }

    public void run(){

    }

    public void initializeImages(File imageFile,File answerFile){
        asciiReader=new ASCIIreader(imageFile,answerFile);
    }

    public void initializeWeights(){
        for(int i=0 ; i<402 ; i++){
            perceptrons.add(new Perceptron());
        }

    }

    public static void main(String args[]){

        SmileyRecognizer smileyRecognizer= new SmileyRecognizer(
                new File(args[0])
                ,new File(args[1])
                ,new File(args[2]));
        smileyRecognizer.run();
    }

}
