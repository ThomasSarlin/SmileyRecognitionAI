/**
 * Small class representing an Image file, with name, array of pixels
 * representing a 20x20 px image (grayscale 0-32 value range) and finally
 * a corresponding emotion to set image.
 */

public class Image {
    private String name;
    private int image[];
    private int correctEmotion;

    /**
     * Constructor for Image
     * @param name set name of image
     * @param imageArray array representing 20x20px image
     * @param correctEmotion int in range 1-4 depending on mood of image.
     */
    public Image(String name,int imageArray[], int correctEmotion){
        setName(name);
        setImage(imageArray);
        setEmotion(correctEmotion);
    }
    public void setName(String name){this.name=name;}
    public void setImage(int image[]){
        this.image=image;
    }

    public void setEmotion(int correctEmotion){
        this.correctEmotion=correctEmotion;
    }

    public String getName(){return  name;}
    public int[] getImage(){
        return this.image;
    }

    public int getEmotion() {
        return this.correctEmotion;
    }
}
