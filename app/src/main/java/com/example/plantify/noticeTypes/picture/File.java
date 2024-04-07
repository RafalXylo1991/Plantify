package com.example.plantify.noticeTypes.picture;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import com.example.plantify.objects.users;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class File {

    static   public java.io.File[] getImages(Context context, users user){
        ContextWrapper cw = new ContextWrapper(context);
        java.io.File directory = cw.getDir(String.valueOf(user.getId()), Context.MODE_PRIVATE);

       return directory.listFiles();

    }
  static   public String saveToInternalStorage(Bitmap bitmapImage, Context context, users user){
        ContextWrapper cw = new ContextWrapper(context);

        java.io.File directory = cw.getDir(String.valueOf(user.getId()), context.MODE_PRIVATE);

        java.io.File mypath=new java.io.File(directory,getRandomString(10)+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    static  public void deleteFromInternalStorage(String path, Context context){
     new java.io.File(path).delete();
    }


    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
