package ru.essepunto.napitka;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class APIRequest {
    private Context context;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    public APIRequest(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Ищу товар...");
        progressDialog.setCancelable(false);
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void makeAPIRequest(String url, final VolleyCallback callback) {
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if(error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    // code to handle 404 error
                    callback.onError(String.valueOf(error.networkResponse.statusCode));
                }else {
                    callback.onError(error.getMessage());
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onError(String error);
    }
}