/**
 * @auth Thomas Sarlin - id15tsn, Petter Poucette - id15ppe
 *
 * Responsible for running and training the neural network
 * to respond and identify 20x20px images representing the emotions
 * "happy","sad","mischievous" and "angry".
 */

import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class SmileyRecognizer {
    private ASCIIreader asciiReader;
    private ASCIIreader testReader;
    private ArrayList<Perceptron> perceptrons;
    private double learningValue;
    private double errorTreshold;

    /**
     * Constructor SmileyRecognizer
     * @param imageFile File that contains training images (Format: ASCII,
     *                  Image size: 20x20 px)
     * @param answerFile File that contains answers to imageFile
     *                   (Format: ASCII, "(string)Name (int)CorrectEmotion")
     * @param testFile File to that contains images (Format: ASCII,
     *                 Image size: 20x20 px)
     * @throws IOException
     */
    private SmileyRecognizer(File imageFile,File answerFile,File testFile) throws IOException {
        this.errorTreshold=0.05;
        this.learningValue = 0.05;
        perceptrons=new ArrayList<>();
        initializeImages(imageFile,answerFile);
        initializeWeights();
        initializeTestFile(testFile);

    }

    /**
     *Acivation function representing sigmoid(x)
     */
    private static Function<Double,Double> activationFunction = x->(1/( 1 + Math.pow(Math.E,(-1*x))));


    /**
     * Summarizes Weights multiplied by gray scale value representing an image
     * @param index Index representing emotion
     * @param image
     * @return Summation of Weights
     */
    private double sumWeights(int index, Image image){
        double sum = 0;
        for(int i= 0; i<400 ; i++){
            sum+=perceptrons.get(i).getWeight(index)
                    *normalizeImageValue(image.getImage()[i]);
        }
        return sum;
    }


    /**
     * Take gray scale value between 0 and 31  and normalize
     * @param value Gray scale value
     * @return Normalized value
     */
    private double normalizeImageValue(double value){
        return value/(31);
    }

    /**
     * Runs the neural network
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void run() throws FileNotFoundException, UnsupportedEncodingException {
        double averageError=1;
        int index=0;
        while(averageError> errorTreshold ||index<7) {
            calculateActivationValues();
            averageError= classifyPerformanceImages(asciiReader
                    .getPerformanceImages());

            asciiReader.shuffleImages();
            index++;
        }
        
        System.out.println("# Average Error: "+averageError);
        System.out.println("# Number of calibration rounds: "+index);

        classifyTestImages();
    }

    /**
     * Perceptron training of the neural network
     */
    private void calculateActivationValues(){
        double activationValues[] = new double[4];
        for(Image image:asciiReader.getTrainingImages()){
            for (int j = 0 ; j < activationValues.length; j++){
                activationValues[j] = activationFunction
                        .apply(sumWeights(j,image));
            }
            adjustWeights(activationValues,image);
        }
    }

    /**
     * Adjusts the weight thought perceptron traning
     * @param activationValues Values representing activation function
     * @param image
     */
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

    /**
     * Performance test
     * @param images
     * @return Average error
     */
    private double classifyPerformanceImages(ArrayList<Image> images) {
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

    /**
     * Characterizes images from test file and presents them to System.out
     * if format "(string)Name (int)Correct Emotion".
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private void classifyTestImages() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("./pictures/result.txt", "UTF-8");
        writer.println("#Authors: Thomas Sarlin & Petter Poucette");

        ArrayList<Image> images = testReader.getImages();
        double activationResult[]=new double[4];
        int bestGuess;
        for (Image image : images) {
            for (int j = 0; j < 4; j++)
                activationResult[j] = activationFunction
                        .apply(sumWeights(j, image));

            bestGuess = getBestGuess(activationResult);
            writer.println(image.getName() + " " + bestGuess);
        }
        writer.close();
    }

    /**
     * Selects the most likely emotion represented in the image
     * @param values activation Values
     * @return Index representing emotion
     */
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


    /**
     * Creates an ASCII class containing an images file and the answers for those file
     * @param imageFile
     * @param answerFile
     * @throws IOException
     */
    private void initializeImages(File imageFile,File answerFile) throws IOException {
        asciiReader=new ASCIIreader(imageFile,answerFile, 2.0/3.0);
    }

    /**
     * Initialize all perceprons
     */
    private void initializeWeights(){
        for(int i=0 ; i<400 ; i++){
            perceptrons.add(new Perceptron());
        }

    }

    /**
     * Creates an ASCII class containing test images
     * @param testFile
     * @throws IOException
     */
    private void initializeTestFile(File testFile) throws IOException {
        testReader = new ASCIIreader(testFile);
    }

    /**
     * @param args Training file, performance file, test file
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        SmileyRecognizer smileyRecognizer
                = new SmileyRecognizer(new File(args[0])
                ,new File(args[1])
                ,new File(args[2]));
        smileyRecognizer.run();
    }

}
