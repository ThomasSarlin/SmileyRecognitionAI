import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class Image {
    String name;
    int image[];
    int correctEmotion;

    public Image(String name,int imageArray[], int correctEmotion){
        this.name=name;
        this.image=imageArray;
        this.correctEmotion=correctEmotion;
    }
    void setImage(int image[]){
        this.image=image;
    }

    void setCorrectEmotion(int correctEmotion){
        this.correctEmotion=correctEmotion;
    }

    int[] getImage(){
        return this.image;
    }

    int getCorrectEmotion() {
        return this.correctEmotion;
    }
}
