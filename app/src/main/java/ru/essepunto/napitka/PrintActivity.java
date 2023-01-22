package ru.essepunto.napitka;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

public class PrintActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    ArrayList<String> names;
    ListView listView;
    ArrayAdapter<String> adapter;
    ImageView qrImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);





// inside onCreate or onViewCreated method
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        names = databaseHelper.getNames();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
        qrImage = (ImageView) findViewById(R.id.qrImage);
        QRCodeButton(qrImage);



    }
    public void QRCodeButton(View view){
        StringBuilder builder = new StringBuilder();
        for (String name : names) {
            builder.append(name + "_ST"+"\n");
        }
        String qr = builder.toString();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();// QR-code generator object
        //Code128Writer codeWriter = new Code128Writer ();  // Bar-code generator object
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qr, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x<200; x++){
                for (int y=0; y<200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}