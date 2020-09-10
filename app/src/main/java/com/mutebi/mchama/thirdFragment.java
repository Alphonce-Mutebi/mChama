package com.mutebi.mchama;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mutebi.mchama.adapters.NoticesAdapter;
import com.mutebi.mchama.adapters.TransactionsAdapter;
import com.mutebi.mchama.models.NoticesList;
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


public class thirdFragment extends Fragment {

    private RecyclerView recyclerView;
    private String notice_url = "https://mchamatest.jeffreykingori.dev/api/v1/user/notices";

    private NoticesAdapter myAdapter;
    private List<NoticesList> noticesLists;

    private int currentUserId;
    private String token;



    public thirdFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_third, container, false);

        View view = inflater.inflate(R.layout.fragment_third, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        noticesLists = new ArrayList<>();
        loadURLData();
        return view;



    }

    private void loadURLData() {
        User currentAuthUser = SharedPrefManager.getInstance(getContext()).getUser();
        currentUserId = currentAuthUser.getId();
        token = currentAuthUser.getToken();

        final String URL_DATA = notice_url;


        final ProgressDialog progressDialog= new ProgressDialog(getContext());
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
                        NoticesList notices = new NoticesList(jo.getString("message"), jo.getString("created_at"));
                        noticesLists.add(notices);
                        Log.d("res", "notices" + notices);

                    }
                    myAdapter = new NoticesAdapter(noticesLists, getActivity().getApplicationContext());
                    recyclerView.setAdapter(myAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Tag", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error" + error.toString(),Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

}