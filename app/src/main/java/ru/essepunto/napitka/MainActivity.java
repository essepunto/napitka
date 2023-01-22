package ru.essepunto.napitka;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Button scanBtn;
    EditText inputBar;
    ImageView imageView;
    CheckBox checkBox;
    DatabaseHelper myDb;
    TextView textView;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBar = findViewById(R.id.inputBarcode);
        imageView = findViewById(R.id.imageView);
         checkBox = findViewById(R.id.checkBox);
        myDb = new DatabaseHelper(this);
        textView = findViewById(R.id.textView);
        MainActivity context = this;
        String code = null;
        String apiUrl = "https://lenta.com/api/v2/stores/0033/skus/" + code;
        APIRequest apiRequest = new APIRequest(MainActivity.this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }






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

                String apiUrl = "https://lenta.com/api/v1/stores/0033/skus?barcode=" + result.getContents();
                APIRequest apiRequest = new APIRequest(MainActivity.this);
                apiRequest.makeAPIRequest(apiUrl, new APIRequest.VolleyCallback() {
                    @SuppressLint("Range")
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");
                            myDb.addData(code);
                            Cursor res = myDb.getData();
                            String db_codes = "";
                            while (res.moveToNext()) {
                                db_codes += res.getString(res.getColumnIndex("name"));
                            }
                            textView.setText(db_codes);
                            inputBar.append(code+"_ST");

                            if (checkBox.isChecked()){
                                scanCode();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onError(String error) {
                        if (checkBox.isChecked()){
                            scanCode();
                        }
                        Toast.makeText(MainActivity.this, "Ошибка добавления!\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Если ошибка повторяется,\nвведите ШК вручную", Toast.LENGTH_LONG).show();




                    }
                });


                //inputBar.append(result.getContents()+"\n");




            }
            else{
                Toast.makeText(this,"Сканирование остановлено",Toast.LENGTH_LONG).show();

            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}