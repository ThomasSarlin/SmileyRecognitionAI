import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;


public class ASCIIReaderTest {

    @Test
    public void shouldBeAbleToCreate() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),2.0/3.0);
    }
    @Test
    public void shouldReadCorrectName() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),2.0/3.0);
        assertTrue(ar.getImages().get(0).getName().equals("Image1"));
    }
    @Test
    public void shouldReadCorrectEmotion() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),2.0/3.0);
        assertTrue(ar.getImages().get(0).getEmotion()==2);
    }
    @Test
    public void displayFirstImage() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),2.0/3.0);
        for(int i=0;i<402;i++) {
            System.out.print(ar.getImages().get(0).getImage()[i] + " ");
            if((i+1)%20==0)
                System.out.println();
        }
    }
    @Test
    public void shouldHaveCorrectNumberOfImages() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),2.0/3.0);
        assertTrue(ar.getImages().size()==300);
    }

    @Test
    public void shouldSplitCorrect() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),2.0/3.0);
        System.out.println(ar.getTrainingImages().size());
        assertTrue(ar.getTrainingImages().size()==200);
        assertTrue(ar.getPerformanceImages().size()==100);
    }
    @Test
    public void shuffleAllImages() throws  Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),2.0/3.0);
        Image i1= ar.getImages().get(0);
        ar.shuffleImages();
        Image i2= ar.getImages().get(0);
        assertFalse(i1.equals(i2));
    }
}
