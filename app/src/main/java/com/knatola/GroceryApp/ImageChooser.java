package com.knatola.GroceryApp;

import java.util.Random;

/*
 * Created by knatola on 31.10.2017.
 * Simple helper Class to randomly choose an image from the resource files.
 */

public class ImageChooser {

    public int getFoodPic(){
        Random random = new Random();
        int i = random.nextInt(5);
        switch(i){
            case 1:
                return R.drawable.grape;
            case 2:
                return R.drawable.orange;
            case 3:
                return R.drawable.pear;
            case 4:
                return R.drawable.strawberry;
            case 5:
                return R.drawable.watermelon;

            default: return R.drawable.pineapple;
        }
    }
}
