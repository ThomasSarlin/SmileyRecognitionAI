import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class SmileyRecognizer {
    private ASCIIreader asciiReader;
    private ArrayList<Perceptron> perceptrons;
    private ArrayList<Double[]> activationValues;
    private ArrayList<Double[]> sumValues;
    private double learningValue;

    public SmileyRecognizer(File imageFile,File answerFile,File testFile) throws IOException {
        this.learningValue = 0.05;
        perceptrons=new ArrayList<>();
        activationValues= new ArrayList<>();
        sumValues = new ArrayList<>();
        initializeImages(imageFile,answerFile);
        initializeWeights();
    }
    public static Function<Double,Double> activationFunction = x->(1/( 1 + Math.pow(Math.E,(-1*x))));


    public double sumWeights(int index, Image image){
        double sum = 0;
        for(int i= 0; i<402 ; i++){
            sum+=perceptrons.get(i).getWeight(index)*normalizeImageValue(image.getImage()[i]);
        }
        return sum;
    }
    public double normalizeImageValue(double value){
        return value/(32);
    }

    public void run(){
        calculateActivationValues();
        asciiReader.shuffleImages();
        characterizePerformanceImages(asciiReader.getPerformanceImages());

        calculateActivationValues();
        asciiReader.shuffleImages();
        characterizePerformanceImages(asciiReader.getPerformanceImages());

        calculateActivationValues();
        asciiReader.shuffleImages();
        characterizePerformanceImages(asciiReader.getPerformanceImages());

        calculateActivationValues();
        asciiReader.shuffleImages();
        characterizePerformanceImages(asciiReader.getPerformanceImages());


    }
    private void calculateActivationValues(){
        Image image;
        for (int i = 0; i < asciiReader.getTrainingImages().size(); i++){
            activationValues.add(new Double[4]);
            sumValues.add(new Double[4]);
            image = asciiReader.getTrainingImages().get(i);
            for (int j = 0 ; j < activationValues.get(i).length; j++){
                sumValues.get(i)[j] = sumWeights(j,image);
                activationValues.get(i)[j] = activationFunction.apply(sumValues.get(i)[j]);
            }
            adjustWeights(activationValues.get(i),image);
        }
    }

    private void adjustWeights(Double[] activationValues,Image image){

        for(int i=0;i<perceptrons.size();i++){
            for(int j=0;j<4;j++) {
                if ((j + 1) == image.getEmotion()) {
                    perceptrons.get(i).adjustWeight(j, learningValue, 1, activationValues[j],image.getImage()[i]);
                } else {
                    perceptrons.get(i).adjustWeight(j, learningValue, 0, activationValues[j],image.getImage()[i]);
                }
            }
        }

    }

    private void characterizePerformanceImages(ArrayList<Image> images){
        double activationResult[]=new double[4];
        int correctAnswers=0;

        for(int i=0;i<images.size();i++){
            for(int j=0;j<4;j++)
                activationResult[j]=activationFunction.apply(sumWeights(j,images.get(i)));
            if(getBestGuess(activationResult)==images.get(i).getEmotion())
                correctAnswers++;

        }

        System.out.println("# Total percentage: "+(correctAnswers/(double)100));
    }

    private int getBestGuess(double values[]){
        int maxIndex=0;
        double max=0;
        for(int i=0;i<4;i++){
            if(values[i]>max) {
                max = values[i];
                maxIndex=i;
            }
        }
        return maxIndex+1;
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

        SmileyRecognizer smileyRecognizer= new SmileyRecognizer(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),new File("./pictures/training.txt"));
        smileyRecognizer.run();
    }

}
