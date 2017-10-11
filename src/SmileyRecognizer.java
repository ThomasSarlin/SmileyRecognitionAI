import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class SmileyRecognizer {
    private ASCIIreader asciiReader;
    private ArrayList<Perceptron> perceptrons;
    private ArrayList<Double[]> activationValues;
    private double learningValue;

    public SmileyRecognizer(File imageFile,File answerFile,File testFile) throws IOException {
        this.learningValue = 0.001;
        perceptrons=new ArrayList<>();
        activationValues= new ArrayList<>();
        initializeImages(imageFile,answerFile);
        initializeWeights();
    }
    public static Function<Double,Double> activationFunction = e->Math.tanh(e);


    public double sumWWeights(int index, Image image){
        double sum = 0;
        for(int i= 0; i<402 ; i++){
            //System.out.println(perceptrons.get(i).getWeight(index) + " " + image.getImage()[i]);
            sum+=perceptrons.get(i).getWeight(index)*image.getImage()[i];
        }
        return sum;
    }

    public void run(){
        calculateActivationValues();
        asciiReader.shuffleImages();
        calculateActivationValues();

        asciiReader.shuffleImages();

        calculateActivationValues();

        asciiReader.shuffleImages();
        calculateActivationValues();

        asciiReader.shuffleImages();
        calculateActivationValues();
        characterizePerformanceImages();

        asciiReader.shuffleImages();
        calculateActivationValues();
        characterizePerformanceImages();

    }
    private void calculateActivationValues(){
        Image image;
        int correctAnswers=0;
        for (int i = 0; i < asciiReader.getTrainingImages().size(); i++){
            activationValues.add(new Double[4]);
            image = asciiReader.getTrainingImages().get(i);
            for (int j = 0 ; j < activationValues.get(i).length; j++){
                activationValues.get(i)[j] = activationFunction.apply(
                        sumWWeights(j, image));
            }
            adjustWeights(activationValues.get(i),image);
            for(int g=0;g<4;g++)
                if(activationValues.get(i)[g]==1.0 && asciiReader.getTrainingImages().get(i).getEmotion()==(g+1))
                    correctAnswers++;
        }
        System.out.println(correctAnswers/200.0);
    }

    private void adjustWeights(Double[] activationValues,Image image){
        for(int i=0;i<perceptrons.size();i++){
            for(int j=0;j<4;j++){
                if((j+1)==image.getEmotion())
                    perceptrons.get(i).adjustWeight(j,learningValue,1,activationValues[j]);
                else
                    perceptrons.get(i).adjustWeight(j,learningValue,-1,activationValues[j]);
            }

        }

    }

    private void characterizePerformanceImages(){

        asciiReader.getPerformanceImages().forEach(e->{
            double a1;
            double a2;
            double a3;
            double a4;
            double temp;

            String correct;
            a1 = sumWWeights(0,e);
            temp=a1;
            correct="a1";
            a2 = sumWWeights(1,e);
            if(a2>temp) {
                temp = a2;
                correct="a2";
            }
            a3 = sumWWeights(2,e);
            if(a3>temp) {
                temp = a3;
                correct="a3";
            }
            a4 = sumWWeights(3,e);
            if(a4>temp) {
                temp = a4;
                correct="a4";
            }
            System.out.println("Correct: " + e.getEmotion() + " Guess: " + correct + " " + temp);
        });

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
