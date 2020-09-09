package com.foodi.Clases;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class clsUtilitarios {

    public static String IP_SERVIDOR = "192.168.100.111";

    public byte[] imagenAbyte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        return baos.toByteArray();
    }



}
