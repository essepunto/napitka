package ru.essepunto.napitka;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckPrice extends AppCompatActivity implements View.OnClickListener {
    TextView byCardSite;
    TextView byCardPrice;
    TextView noCardSite;
    TextView noCardPrice;
    TextView isCorrect;
    TextView nameTittle;
    TextView textCounter;
    DatabaseHelper myDb;
    String co = "0";




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_price);
        byCardPrice = findViewById(R.id.by_card_price);
        byCardSite = findViewById(R.id.by_card_site);
        noCardPrice = findViewById(R.id.no_card_price);
        noCardSite = findViewById(R.id.no_card_site);
        isCorrect = findViewById(R.id.is_correct);
        nameTittle = findViewById(R.id.nameTittle);
        textCounter = findViewById(R.id.textCounter);
        myDb = new DatabaseHelper(this);


        textCounter.setText("Количество записей в БД:"+co);





    }

    @Override
    public void onClick(View view) {
        scanCode();
    }
    private void  scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Просканируйте QR ценника");
        integrator.setBeepEnabled(false);
        integrator.initiateScan();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() != null){
                QrData qrData = new QrData(result.getContents());



                String apiUrl = "https://lenta.com/api/v1/stores/0033/skus?barcode=" + qrData.getBarcode();
                APIRequest apiRequest = new APIRequest(CheckPrice.this);
                apiRequest.makeAPIRequest(apiUrl, new APIRequest.VolleyCallback() {
                    @SuppressLint({"Range", "ResourceAsColor"})
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");
                            String name = result.getString("title");
                            String regularPriceBySite = result.getString("regularPrice");
                            String discountPriceBySite = result.getString("discountPrice");
                            String regularPrice = String.valueOf(qrData.getPrice1());
                            String discountPrice = String.valueOf(qrData.getPrice2());

                            noCardSite.setText(regularPriceBySite);
                            byCardSite.setText(discountPriceBySite);
                            noCardPrice.setText(regularPrice);
                            byCardPrice.setText(discountPrice);
                            nameTittle.setText(name);
                            if (regularPrice.equals(regularPriceBySite) && discountPrice.equals(discountPriceBySite))
                            {
                                isCorrect.setTextColor(Color.GREEN);
                                isCorrect.setText("Цена корректна");
                                Toast.makeText(CheckPrice.this, "Цена корректна", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                isCorrect.setTextColor(Color.RED);
                                isCorrect.setText("Цена не корректна!");
                                Toast.makeText(CheckPrice.this, "Цена не корректна!", Toast.LENGTH_SHORT).show();
                                myDb.addData(name,code);
                                String co = myDb.getRecordCount();
                                textCounter.setText("Количество записей в БД:"+co);


                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onError(String error) {
                        Toast.makeText(CheckPrice.this, "Пожалуйста,отсканируйте QR ценника!", Toast.LENGTH_SHORT).show();


                    }
                });
            }
            else{
                Toast.makeText(this,"Сканирование остановлено",Toast.LENGTH_LONG).show();

            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        textCounter.setText("Количество записей в БД:"+co);



    }
}