import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;


public class ASCIIReaderTest {

    @Test
    public void shouldBeAbleToCreate() throws Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),(0.6666));
    }
    @Test
    public void shuffleAllImages() throws  Exception{
        ASCIIreader ar = new ASCIIreader(new File("./pictures/training.txt")
                ,new File("./pictures/training-facit.txt"),(0.6666));
        Image i1= ar.getImages().get(0);
        ar.shuffleImages();
        Image i2= ar.getImages().get(0);
        assertFalse(i1.equals(i2));
    }
}
