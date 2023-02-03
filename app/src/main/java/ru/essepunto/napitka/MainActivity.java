package ru.essepunto.napitka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Button scanBtn;
    CheckBox checkBox;
    DatabaseHelper myDb;
    TextView textView;
    TextView counterMain;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         checkBox = findViewById(R.id.checkBox);
        myDb = new DatabaseHelper(this);
        textView = findViewById(R.id.textView);
        counterMain = findViewById(R.id.counter);


        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        setCountToTextView();


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
        integrator.setPrompt("Просканируйте ШК товара");
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
                            String name = result.getString("title");
                            myDb.addData(code,name);
                            setCountToTextView();
                            Toast.makeText(MainActivity.this, "Успешно добавлено", Toast.LENGTH_SHORT).show();

                            textView.setText(name);
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
                        Toast.makeText(MainActivity.this, "Не добавлено!", Toast.LENGTH_SHORT).show();




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
    @Override
    protected void onResume() {
        super.onResume();
        setCountToTextView();

    }

    private void setCountToTextView()
    {
        String co = myDb.getRecordCount();
        counterMain.setText("Количество записей в БД:"+co);
    }
}