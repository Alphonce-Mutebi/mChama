package com.mutebi.mchama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mutebi.mchama.adapters.RotationsAdapter;
import com.mutebi.mchama.models.RotationList;
import com.mutebi.mchama.models.SharedPrefManager;
import com.mutebi.mchama.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RotationListActivity extends AppCompatActivity {

    private static  final String rotation_list_url = "https://mchamatest.jeffreykingori.dev/api/v1/user/all";

    private RecyclerView recyclerView;

    private RotationsAdapter myAdapter;
    private List<RotationList> rotationLists;

    private int currentUserId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_list);

        recyclerView = findViewById(R.id.rotation_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rotationLists = new ArrayList<>();
        loadRotationListData();

    }

    private void loadRotationListData() {
        User currentAuthUser = SharedPrefManager.getInstance(RotationListActivity.this).getUser();
        currentUserId = currentAuthUser.getId();
        token = currentAuthUser.getToken();

        //Display progress Dialog
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Volley Request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, rotation_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("res", "Response:" + response);
                progressDialog.dismiss();
                try {
                    JSONObject rotationResponse = new JSONObject(response);
                    int success = rotationResponse.getInt("success");
                    String message = rotationResponse.getString("message");

                    //Handling Successful response
                    if(success == 1 && rotationResponse.has("users")) {
                        //processing users array and attaching to view
                        JSONArray array = rotationResponse.getJSONArray("users");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo = array.getJSONObject(i);
                                RotationList rotations = new RotationList(jo.getString("name"), jo.getString("rotation"), jo.getString("created_at"));
                                rotationLists.add(rotations);
                                Log.d("res", "all users: " + rotations);

                            }
                            myAdapter = new RotationsAdapter(rotationLists, RotationListActivity.this);
                            recyclerView.setAdapter(myAdapter);
                    }
                    //Handling unsuccessful response
                    else if(success == 0){
                        if(rotationResponse.has("errors")){
                            JSONObject errors = rotationResponse.getJSONObject("errors");
                            String errorMsg = errors.toString();

                            String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                            //Error Dialog
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(RotationListActivity.this);
                            respBuilder.setTitle("Operation Failed!")
                                    .setMessage(errMsg)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Toast.makeText(RotationListActivity.this, "Please try again in a few...",Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent toDash = new Intent(RotationListActivity.this, Dashboard.class);
                                            startActivity(toDash);
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(RotationListActivity.this);
                            respBuilder.setTitle("Operation Failed!")
                                    .setMessage(message)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Toast.makeText(RotationListActivity.this, "Try again in a few...",Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent toDash = new Intent(RotationListActivity.this, Dashboard.class);
                                            startActivity(toDash);
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                    }
                    else{
                        //Some other server response message
                        Toast.makeText(RotationListActivity.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Tag", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RotationListActivity.this,"Error" + error.toString(),Toast.LENGTH_SHORT).show();
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