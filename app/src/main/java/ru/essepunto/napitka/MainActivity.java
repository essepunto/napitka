package ru.essepunto.napitka;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Button scanBtn;
    EditText inputBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBar = findViewById(R.id.inputBarcode);

        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);

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

                inputBar.append(result.getContents()+"%0A"+"\n");



            }
            else{
                Toast.makeText(this,"Отменено",Toast.LENGTH_LONG).show();

            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}