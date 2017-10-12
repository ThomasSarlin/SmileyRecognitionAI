import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class SmileyRecognizer {
    private ASCIIreader asciiReader;
    private ASCIIreader testReader;
    private ArrayList<Perceptron> perceptrons;
    private double learningValue;
    private double errorThreshold;

    private SmileyRecognizer(File imageFile,File answerFile,File testFile)
            throws IOException {

        this.errorThreshold =0.15;
        this.learningValue = 0.05;
        perceptrons=new ArrayList<>();
        initializeImages(imageFile,answerFile);
        initializeWeights();
        initializeTestFile(testFile);

    }
    private static Function<Double,Double> activationFunction =
            x->(1/( 1 + Math.pow(Math.E,(-1*x))));


    private double sumWeights(int index, Image image){
        double sum = 0;
        for(int i= 0; i<402 ; i++){
            sum+=perceptrons.get(i).getWeight(index)
                    *normalizeImageValue(image.getImage()[i]);
        }
        return sum;
    }
    private double normalizeImageValue(double value){
        return value/(32);
    }

    private void run() throws FileNotFoundException
            , UnsupportedEncodingException {

        double averageError=1;
        int index=0;
        while(averageError> errorThreshold) {
            calculateActivationValues();
            averageError=characterizePerformanceImages(asciiReader
                    .getPerformanceImages());

            asciiReader.shuffleImages();
            index++;

        }
        
        System.out.println("# Average Error: "+averageError);
        System.out.println("# Number of calibration rounds: "+index);

        characterizeTestImages();
    }
    private void calculateActivationValues(){
        double activationValues[] = new double[4];
        for(Image image:asciiReader.getPerformanceImages()){
            for (int j = 0 ; j < activationValues.length; j++){
                activationValues[j] = activationFunction
                        .apply(sumWeights(j,image));
            }
            adjustWeights(activationValues,image);
        }
    }

    private void adjustWeights(double activationValues[],Image image){

        for(int i=0;i<perceptrons.size();i++){
            for(int j=0;j<4;j++) {
                if ((j + 1) == image.getEmotion()) {
                    perceptrons.get(i).adjustWeight(j, learningValue
                            , 1, activationValues[j]
                            ,normalizeImageValue(image.getImage()[i]));
                } else {
                    perceptrons.get(i).adjustWeight(j, learningValue
                            , 0, activationValues[j]
                            ,normalizeImageValue(image.getImage()[i]));
                }
            }
        }

    }

    private double characterizePerformanceImages(ArrayList<Image> images) {
        double activationResult[]=new double[4];
        double errorSum=0;
        for (Image image : images) {
            for (int j = 0; j < 4; j++)
                activationResult[j] = activationFunction
                        .apply(sumWeights(j, image));

            errorSum += (1-activationResult[getBestGuess(activationResult)-1]);
        }
        return (errorSum/(double)asciiReader.getPerformanceImages().size());
    }

    private void characterizeTestImages()
            throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        ArrayList<Image> images = testReader.getImages();
        double activationResult[]=new double[4];
        int bestGuess;
        for (Image image : images) {
            for (int j = 0; j < 4; j++)
                activationResult[j] = activationFunction
                        .apply(sumWeights(j, image));

            bestGuess = getBestGuess(activationResult);
            writer.println(image.getName() + " " + bestGuess);
            System.out.println(image.getName() + " " + bestGuess);
        }
        writer.close();
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
    private void initializeImages(File imageFile,File answerFile)
            throws IOException {

        asciiReader=new ASCIIreader(imageFile,answerFile, 2.0/3.0);
    }

    private void initializeWeights(){
        for(int i=0 ; i<402 ; i++){
            perceptrons.add(new Perceptron());
        }

    }

    private void initializeTestFile(File testFile) throws IOException {
        testReader = new ASCIIreader(testFile);
    }
    public static void main(String args[]) throws IOException {

        SmileyRecognizer smileyRecognizer
                = new SmileyRecognizer(new File(args[0])
                ,new File(args[1])
                ,new File(args[2]));
        smileyRecognizer.run();
    }

}
