package com.mutebi.mchama;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutebi.mchama.models.SharedPrefManager;
import com.mutebi.mchama.models.User;

import java.util.Calendar;


public class homeFragment extends Fragment {
    TextView greetingText, mWallet;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        LayoutInflater lf = getActivity().getLayoutInflater();

        lf.inflate(R.layout.fragment_home, container, false);
        greetingText = (TextView) view.findViewById(R.id.greetingText);
        mWallet = (TextView) view.findViewById(R.id.walletText);
        getGreeting();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getGreeting(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        //instantiating user obj from sharedPrefs
        User currentUser = SharedPrefManager.getInstance(getContext()).getUser();
        String fullName = currentUser.getName();
        String[] split = fullName.split("\\s+");
        String fName = split[0];
        if(timeOfDay < 12){
            greetingText.setText("Good Morning " + fName);
        }
        else if(timeOfDay <16){
            greetingText.setText("Good Afternoon "+ fName);
        }
        else {
            greetingText.setText("Good Evening "+ fName);
        }


        mWallet.setText("mWallet: "+ Integer.toString(currentUser.getWallet()));


    }
}