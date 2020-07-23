package com.mutebi.mchama;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class homeFragment extends Fragment {
    TextView userName;





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
        TextView userName = (TextView) view.findViewById(R.id.greetingText);
        String name = getActivity().getIntent().getStringExtra("name");
        userName.append(" "+name);
        return view;


    }
}