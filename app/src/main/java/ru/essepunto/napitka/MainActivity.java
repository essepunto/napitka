package ru.essepunto.napitka;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBar = findViewById(R.id.inputBarcode);
        imageView = findViewById(R.id.imageView);
        MainActivity context = this;
        String code = null;
        String apiUrl = "https://lenta.com/api/v2/stores/0033/skus/" + code;
        APIRequest apiRequest = new APIRequest(MainActivity.this);





        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);


    }
    String url = "http://my-json-feed";

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    textView.setText("Response: " + response.toString());
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error

                }
            });

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Закрыть сканер?")
                .setMessage("Если вы выйдите,всё что вы просканировали будет удалено")
                .setNegativeButton(R.string.no,null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
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
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");
                            inputBar.append(code+"_ST"+"\n");
                            scanCode();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onError(String error) {
                        scanCode();
                        Toast.makeText(MainActivity.this, "Ошибка добавления!\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Если ошибка повторяется,\nвведите ШК или артикул вручную", Toast.LENGTH_LONG).show();




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