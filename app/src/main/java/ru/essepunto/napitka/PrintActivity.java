package ru.essepunto.napitka;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

public class PrintActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    ArrayList<String> names;
    ArrayList<String> namesAndData;

    ListView listView;
    ArrayAdapter<String> adapter;
    ImageView qrImage;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        myDb = new DatabaseHelper(this);
        // Get the current window
        Window window = getWindow();

        // Create a LayoutParams object
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // Set the screen brightness to maximum
        layoutParams.screenBrightness = 1.0f;

        // Apply the changes
        window.setAttributes(layoutParams);






// inside onCreate or onViewCreated method
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        names = databaseHelper.getNames();
        namesAndData = databaseHelper.getNamesAndData();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namesAndData);
        listView.setAdapter(adapter);
        qrImage = (ImageView) findViewById(R.id.qrImage);
        QRCodeButton(qrImage);




    }

    public void ClearButton(View view) {
        // Создаем диалоговое окно подтверждения
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение");
        builder.setMessage("Вы уверены, что хотите удалить все данные?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDb.removeAll();
                // Обновляем источник данных в ArrayAdapter
                namesAndData = myDb.getNamesAndData();
                adapter.clear();
                adapter.addAll(namesAndData);
                adapter.notifyDataSetChanged();
                qrImage.setImageResource(R.drawable.galery_icon);
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ничего не делаем, просто закрываем диалог
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            BitMatrix bitMatrix = qrCodeWriter.encode(qr, BarcodeFormat.QR_CODE, 1000, 1000);
            Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.RGB_565);
            for (int x = 0; x<1000; x++){
                for (int y=0; y<1000; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}