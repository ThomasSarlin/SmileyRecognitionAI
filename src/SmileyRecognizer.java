import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SmileyRecognizer {
    private ASCIIreader asciiReader;
    private ArrayList<Perceptron> perceptrons;
    private ArrayList<Double[]> activationValues;
    private double learningValue;

    public SmileyRecognizer(File imageFile,File answerFile,File testFile) throws IOException {
        this.learningValue = 0.05;
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
        calculateActivationValues();
    }
    private void calculateActivationValues(){
        Image image;

        for (int i = 0; i < asciiReader.getTrainingImages().size(); i++){
            activationValues.add(new Double[4]);
            image = asciiReader.getTrainingImages().get(i);
            for (int j = 0 ; j < activationValues.get(i).length; j++){
                activationValues.get(i)[j] = activationFunction.apply(
                        sumWWeights(j, image));
            }
            adjustWeights(activationValues.get(i),image);
            System.out.println("Image " + i + " CorrectValue: " + image.getEmotion()
                    + " Weighted values: "
                    + "1: " + activationValues.get(i)[0]
                    + "2: " + activationValues.get(i)[1]
                    + "3: " + activationValues.get(i)[2]
                    + "4: " + activationValues.get(i)[3]);
        }
    }

    private void adjustWeights(Double[] activationValues,Image image){
        for(int i=0;i<perceptrons.size();i++){
            for(int j=0;j<4;j++){
                if((j+1)==image.getEmotion())
                    perceptrons.get(i).adjustWeight(j,learningValue,1,activationValues[j]);
                else
                    perceptrons.get(i).adjustWeight(j,learningValue,0,activationValues[j]);
            }

        }

    }

    private void initializeImages(File imageFile,File answerFile) throws IOException {
        asciiReader=new ASCIIreader(imageFile,answerFile, 2.0/3.0);
    }

    private void initializeWeights(){
        for(int i=0 ; i<402 ; i++){
            perceptrons.add(new Perceptron());
        }

    }

    public static void main(String args[]) throws IOException {

        SmileyRecognizer smileyRecognizer= new SmileyRecognizer(
                new File(args[0])
                ,new File(args[1])
                ,new File(args[2]));
        smileyRecognizer.run();
    }

}
