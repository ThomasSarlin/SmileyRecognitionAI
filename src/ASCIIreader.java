/**
 * This class is used as a tool for converting ASCII-coded 20x20px images
 * to an list of Images(local class) and should be able to keep track of
 * corresponding emotion of these images. It is also possible to shuffle the
 * elements on the list and the list is also split into two to be able to have
 * one set of images for training and another set for performance test.
 * 
 * @Version 1.0
 * @author Thomas Sarlin, Petter Poucette
 */


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ASCIIreader {
    private ArrayList<Image> images;
    private ArrayList<Image> trainingImages;
    private ArrayList<Image> performanceImages;
    private double fileRatio;

    /**
     * Constructor used for creating an ASCII-reader which reads and returns
     * image files for training.
     * @param ImageFile - File containing information on images
     * @param answerFile - File containing correct emotion displayed on image
     * @param fileRatio - Rate of which the images are split into training/performance.
     * @throws IOException in case of files missing or non-exsistent.
     */
    public ASCIIreader(File ImageFile,File answerFile,double fileRatio) throws IOException {
        this.fileRatio=fileRatio;
        this.trainingImages=new ArrayList<>();
        this.performanceImages = new ArrayList<>();
        asciiToImage(ImageFile,answerFile);
    }

    /**
     * Constructor used for creating an ASCII-reader which reads and returns
     * image files for classification only.
     * @param testFile - File with image information to be classified.
     * @throws IOException
     */
    public ASCIIreader(File testFile) throws IOException {
        this.images=new ArrayList<>();
        this.fileRatio=1;
        BufferedReader imageReader = new BufferedReader(new FileReader(testFile));
        addImages(imageReader);
    }

    /**
     * @return List of all images.
     */
    public ArrayList<Image> getImages() { return images; }

    /**
     * @return List of training specific images
     */
    public ArrayList<Image> getTrainingImages(){ return trainingImages; }
    /**
     * @return List of performance specific images
     */
    public ArrayList<Image> getPerformanceImages(){ return performanceImages; }
    /**
     * Method that scrambles images, used before each training round.
     */
    public void shuffleImages(){ Collections.shuffle(images);splitImages();}

    /**
     * Converts ASCII-files to Image-arrays and connects to the right emotion.
     * @param imageFile - File with image information
     * @param answerFile - File with inforation on correct emotions.
     * @throws IOException - File corrupt or non-existent.
     */
    private void asciiToImage(File imageFile,File answerFile) throws IOException {
        BufferedReader imageReader = new BufferedReader(new FileReader(imageFile));
        BufferedReader answerReader = new BufferedReader(new FileReader(answerFile));

        addImages(imageReader);
        addEmotions(answerReader);

        splitImages();
    }

    /**
     * Adds images to Array-list from ascii format
     * @param imageReader
     * @throws IOException
     */
    private void addImages(BufferedReader imageReader) throws IOException {
        String line = imageReader.readLine();
        String name;
        int imageArray[];
        int imageIndex;
        String split[];
        while(line!=null) {
            if(line.length()>0&&(line.charAt(0)!=('#'))){
                imageArray = new int[402];
                split=line.split("\\s+");
                imageIndex=0;
                name=split[0];

                line=imageReader.readLine();
                split=line.split("\\s+");
                while(split.length==20){
                    for(int i=0;i<split.length;i++) {
                        imageArray[imageIndex] = Integer.parseInt(split[i]);
                        imageIndex++;
                    }
                    line=imageReader.readLine();
                    split=line.split("\\s+");
                    if(split.length<20)
                        images.add(new Image(name,imageArray,0));

                }
            }
            line=imageReader.readLine();
        }

    }

    /**
     * Adds emotions to Array-list from ascii format
     * @param answerReader
     * @throws IOException
     */
    private void addEmotions(BufferedReader answerReader) throws IOException {
        String line= answerReader.readLine();
        int imageIndex;
        String split[];

        imageIndex=0;

        while(line!=null){
            if(line.length()>0&&line.charAt(0)!=('#')) {
                split=line.split("\\s+");
                if(images.get(imageIndex).equals(split[0]))
                    images.get(imageIndex).correctEmotion=Integer.parseInt(split[1]);
                else {
                    forceChangeEmotion(split[0],Integer.parseInt(split[1]));
                }
                imageIndex++;
            }
            line=answerReader.readLine();
        }

    }

    /**
     * If the emotions are in scrambled order, this function finds the correct
     * image element and adds the corresponding emotion.
     * @param name - name of image
     * @param value - emotional value to be set.
     */
    private void forceChangeEmotion(String name, int value){
        images.forEach(e->{
            if(e.name.equals(name))
                e.correctEmotion=value;
        });
    }


    /**
     * Splits Images between training & performance.
     */
    private void splitImages(){
        images.forEach(e->{
            if(images.indexOf(e)<(int)(fileRatio*images.size()))
                trainingImages.add(e);
            else
                performanceImages.add(e);
        });
    }
}
