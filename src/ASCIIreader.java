import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ASCIIreader {
    private ArrayList<Image> images;
    private ArrayList<Image> trainingImages;
    private ArrayList<Image> performanceImages;
    double fileRatio;

    public ASCIIreader(File ImageFile,File answerFile,double fileRatio) throws IOException {
        this.fileRatio=fileRatio;
        this.images=new ArrayList<>();
        this.trainingImages=new ArrayList<>();
        this.performanceImages = new ArrayList<>();
        asciiToImage(ImageFile,answerFile);
    }

    public ArrayList<Image> getTrainingImages(){ return trainingImages; }
    public ArrayList<Image> getPerformanceImages(){ return performanceImages; }

    private void asciiToImage(File imageFile,File answerFile) throws IOException {
        BufferedReader imageReader = new BufferedReader(new FileReader(imageFile));
        BufferedReader answerReader = new BufferedReader(new FileReader(answerFile));
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

        line= answerReader.readLine();

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

        splitImages();
    }
    private void forceChangeEmotion(String name, int value){
        images.forEach(e->{
            if(e.name.equals(name))
                e.correctEmotion=value;
        });
    }
    private void splitImages(){
        images.forEach(e->{
            if(images.indexOf(e)<(int)(fileRatio*images.size()))
                trainingImages.add(e);
            else
                performanceImages.add(e);
        });
    }

    public void shuffleImages(){
        Collections.shuffle(images);
        splitImages();
    }
}
