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
        asciiToImage(ImageFile,answerFile);
    }

    public ArrayList<Image> getTrainingImages(){ return trainingImages; }
    public ArrayList<Image> getPerformanceImages(){ return performanceImages; }

    private void asciiToImage(File imageFile,File answerFile) throws IOException {
        BufferedReader imageReader = new BufferedReader(new FileReader(imageFile));
        BufferedReader answerReader = new BufferedReader(new FileReader(answerFile));
        String line = imageReader.readLine();
        String name;
        int imageArray[] = new int[402];
        int index=0;
        String split[];
        while(line!=null) {
            if(line.charAt(0)!=('#')||line.charAt(0)!=' ') {
                split=line.split("\\s+");
                if(split.length==1){
                    name=split[0];
                }
                while(split.length==20){
                    
                }
            }


            line=imageReader.readLine();
        }

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
