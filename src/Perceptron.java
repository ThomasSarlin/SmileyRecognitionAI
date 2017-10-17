import java.util.Random;

public class Perceptron {
    private double[] weights;
    /**
     * Constructor for perceptron, creates a perceptron with random weights.
     * @throws
     */
    public Perceptron(){
        setRandomWeights();
    }

    /**
     * @param index weight of a specific index, representing a feeling.
     *              "happy,sad,mischievous, angry"
     * @return weight to corresponing node.
     */
    public double getWeight(int index){return this.weights[index];}

    /**
     * each weight of the perceptron is set to a random start value.
     */
    private void setRandomWeights(){
        double rangeMin=0.3;
        double rangeMax=0.5;
        Random random=new Random();
        this.weights=new double[4];
        for(int i=0;i<this.weights.length;i++){
            weights[i] = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        }
    }

    /**
     * Weight adjustment according to perceptron training.
     * @param index weight to be adjusted
     * @param learningValue rate of learning
     * @param expectedValue value to expect (1=true,0=false)
     * @param actualValue actual sigmoid value
     * @param nodeValue normalized grayscale value of node.
     */

    public void adjustWeight(int index, double learningValue,double expectedValue,double actualValue,double nodeValue){
        try {
            weights[index] += nodeValue*learningValue*(expectedValue-actualValue);
        }catch (IndexOutOfBoundsException e){
            System.err.println("adjustWeight index out of bounds");
        }
    }
}
