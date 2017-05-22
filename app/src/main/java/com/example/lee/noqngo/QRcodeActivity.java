package com.example.lee.noqngo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * Created by Lee on 2017-05-06.
 */
public class QRcodeActivity extends AppCompatActivity{
    private String LOG_TAG = "QRcodeActivity";

    ImageView img;
    String email;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_qrcode);

        img = (ImageView) findViewById(R.id.imageView1);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //get current user
        email =  user.getEmail();
        String uid = user.getUid();

        String strBarcode = "https://fir-82b14.firebaseapp.com/customer.html?sid="+uid;

        String FileName = "QRcode";
        Bitmap barcode = createBarcode(strBarcode);
        img.setImageBitmap(barcode);
        img.invalidate();

    }

    public Bitmap createBarcode(String code){

        Bitmap bitmap =null;
        MultiFormatWriter gen = new MultiFormatWriter();
        try {
            final int WIDTH = 840;
            final int HEIGHT = 840;
            BitMatrix bytemap = gen.encode(code, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0 ; i < WIDTH ; ++i)
                for (int j = 0 ; j < HEIGHT ; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
