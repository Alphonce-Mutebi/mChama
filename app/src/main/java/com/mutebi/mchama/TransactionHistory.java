package com.mutebi.mchama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mutebi.mchama.adapters.TransactionsAdapter;
import com.mutebi.mchama.models.SharedPrefManager;
import com.mutebi.mchama.models.TransactionList;
import com.mutebi.mchama.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionHistory extends AppCompatActivity {

    public static final String URL_DATA = "https://mchamatest.jeffreykingori.dev/api/v1/user/history/1";

    private RecyclerView recyclerView;

    private TransactionsAdapter myAdapter;
    private List<TransactionList> transactionLists;

    private int currentUserId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionLists = new ArrayList<>();
        loadURLData();
    }

    private void loadURLData() {
        User currentAuthUser = SharedPrefManager.getInstance(TransactionHistory.this).getUser();
        currentUserId = currentAuthUser.getId();
        token = currentAuthUser.getToken();
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("res", "Response:" + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        TransactionList transactions = new TransactionList(jo.getString("amount"), jo.getString("type"));
                        transactionLists.add(transactions);
                        Log.d("res", "transactions" + transactions);

                    }
                    myAdapter = new TransactionsAdapter(transactionLists, getApplicationContext());
                    recyclerView.setAdapter(myAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Tag", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransactionHistory.this,"Error" + error.toString(),Toast.LENGTH_SHORT).show();

            }

        }){

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer "+token);
                params.put("Accept", "application/json");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}