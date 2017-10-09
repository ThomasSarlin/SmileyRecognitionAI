import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;


public class ASCIIReaderTest {

    @Test
    public void createAsciiReader() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("C:\\Users\\Kanin\\Documents\\gitStuff\\interactiveAudio\\SmileyRecognitionAI\\src\\pictures\\training.txt")
                ,new File("C:\\Users\\Kanin\\Documents\\gitStuff\\interactiveAudio\\SmileyRecognitionAI\\src\\pictures\\training-facit.txt"),(0.6666));

        ar.getTrainingImages().forEach(e->{
        System.out.println("\n" + e.name + "\n" + "\n" + e.correctEmotion);
        for(int i=0;i<e.image.length;i++) {
            System.out.print(e.image[i] + " ");
            if((i+1)%20==0)
                System.out.println(" ");
        }

        });
    }
}
