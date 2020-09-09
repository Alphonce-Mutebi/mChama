package com.mutebi.mchama;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mutebi.mchama.models.SharedPrefManager;
import com.mutebi.mchama.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button logoutBtn, updateBtn;
    private EditText uName, uEmail, uPhone;
    private ProgressDialog progressDialog;

    private int currentUserId;
    private String token;




    public String update_url = "https://mchamatest.jeffreykingori.dev/api/v1/user/update";

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        LayoutInflater lf = getActivity().getLayoutInflater();

        lf.inflate(R.layout.fragment_profile, container, false);
        //instantiate widgets
        logoutBtn = view.findViewById(R.id.logoutButton);
        updateBtn = view.findViewById(R.id.save_button);

        uName = view.findViewById(R.id.name);
        uEmail = view.findViewById(R.id.email);
        uPhone = view.findViewById(R.id.phone);

        //Instantiate Progress Dialog
        progressDialog = new ProgressDialog(getContext());



        setCurrentProfile();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                SharedPrefManager.getInstance(getContext()).logout();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating Profile...");
                progressDialog.show();
                updateDetails(uName.getText().toString().trim(), uEmail.getText().toString().trim(), uPhone.getText().toString().trim());
            }
        });



        return view;


    }

    private void updateDetails(final String name, final String email, final String phone) {
        //volley request
        User currentAuthUser = SharedPrefManager.getInstance(getContext()).getUser();
        currentUserId = currentAuthUser.getId();
        token = currentAuthUser.getToken();
        StringRequest userRequest = new StringRequest(Request.Method.POST, update_url, new com.android.volley.Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    //Handling Response
                    System.out.println(response);
                    JSONObject regResponse = new JSONObject(response);

                    int success = regResponse.getInt("success");
                    String message = regResponse.getString("message");

                    //Handling Successful login
                    if(success == 1){
                        if(regResponse.has("updates")){
                            JSONObject mUser = regResponse.getJSONObject("updates");

                            String updatedName = mUser.getString("name");
                            String updatedEmail = mUser.getString("email");
                            String updatedPhone = mUser.getString("phone");

                            //updating the user details in shared preferences
                            SharedPrefManager.getInstance(requireActivity().getApplicationContext()).userUpdate(updatedName, updatedEmail, updatedPhone);

                            setCurrentProfile();
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(getContext());
                            respBuilder.setTitle("Profile Updated")
                                    .setMessage("Your profile details have been updated successfully")
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();

                        }
                    }
                    //Handling unsuccessful login
                    else if(success == 0){
                        if(regResponse.has("errors")){
                            JSONObject errors = regResponse.getJSONObject("errors");
                            String errorMsg = errors.toString();

                            String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                            //Error Dialog
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(getContext());
                            respBuilder.setTitle("Profile Update Failed!")
                                    .setMessage(errMsg)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(getContext(), "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(getContext());
                            respBuilder.setTitle("Profile Update Failed!")
                                    .setMessage(message)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }

                    }
                    else{
                        //Some other server response message
                        Toast.makeText(getContext(), message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(getContext(), je.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            //HTTP headers

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer "+token);
                params.put("Accept", "application/json");

                return params;
            }
            //Req params
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("user_id", Integer.toString(currentUserId));
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone );

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(userRequest);
    }

    private void setCurrentProfile() {
        User currentUser = SharedPrefManager.getInstance(getContext()).getUser();
        uName.setText(currentUser.getName());
        uEmail.setText(currentUser.getEmail());
        uPhone.setText(currentUser.getPhone());
    }

}