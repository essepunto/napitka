package ru.essepunto.napitka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Button scanBtn;
    Button clearButton;
    EditText inputBar;
    ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBar = findViewById(R.id.inputBarcode);
        imageView = findViewById(R.id.imageView);

        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);


    }

    public void QRCodeButton(View view){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();// QR-code generator object
        //Code128Writer codeWriter = new Code128Writer ();  // Bar-code generator object
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(inputBar.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x<200; x++){
                for (int y=0; y<200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        scanCode();
    }
    private void  scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        integrator.setPrompt("Сканирование");
        integrator.setBeepEnabled(false);
        integrator.initiateScan();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() != null){

                inputBar.append(result.getContents()+"\n");




            }
            else{
                Toast.makeText(this,"Отменено",Toast.LENGTH_LONG).show();

            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}