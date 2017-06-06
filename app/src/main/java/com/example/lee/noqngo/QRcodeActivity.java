package com.example.lee.noqngo;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

        Button qrbutton = (Button)findViewById(R.id.qrbutton);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //get current user
        email =  user.getEmail();
        final String uid = user.getUid();

        final String strBarcode = "https://fir-82b14.firebaseapp.com/customer.html?sid="+uid;


        qrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String FileName = "QRcode";
                Bitmap barcode = createBarcode(strBarcode);
                img.setImageBitmap(barcode);
                img.invalidate();
                checkPermission();
                String path = saveBitmapToJpeg(barcode,uid);

            }
        });



    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
        }
    }
    //권한 체크관련

    public void onRequestPermissionsResult(int requestCode, String permissions, int grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "기기에 qr코드가 저장됩니다", Toast.LENGTH_SHORT).show();
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다

                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                    Toast.makeText(getApplicationContext(), "권한 없이 해당 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                }
        }
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

    public static String saveBitmapToJpeg( Bitmap bitmap, String name){


       // File storage = context.getCacheDir(); // 이 부분이 임시파일 저장 경로

        String fileName = name + ".jpg";  // 파일이름은 마음대로!

        File tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/noqNgo/",fileName);

        try{
            tempFile.createNewFile();  // 파일을 생성해주고

            FileOutputStream out = new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);  // 넘거 받은 bitmap을 jpeg(손실압축)으로 저장해줌

            out.close(); // 마무리로 닫아줍니다.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴해주면 끝!
    }

}
