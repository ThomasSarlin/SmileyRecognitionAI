import java.util.Random;

public class Perceptron {
    private double[] weights;
    /**
     *
     * @throws
     */
    public Perceptron(){
        setRandomWeights();
    }

    public double getWeight(int index){return this.weights[index];}

    private void setRandomWeights(){
        double rangeMin=0.3;
        double rangeMax=0.5;
        Random random=new Random();
        this.weights=new double[4];
        for(int i=0;i<this.weights.length;i++){
            weights[i] = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        }
    }

    public void adjustWeight(int index, double learningValue,double expectedValue,double actualValue,double nodeValue){
        try {
            weights[index] += nodeValue*learningValue*(expectedValue-actualValue);
        }catch (IndexOutOfBoundsException e){
            System.err.println("adjustWeight index out of bounds");
        }
    }
}
