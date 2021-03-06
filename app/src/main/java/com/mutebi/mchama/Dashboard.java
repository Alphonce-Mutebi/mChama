package com.mutebi.mchama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        //Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }

    public void goToDeposit(View view) {
        Intent depositMoney = new Intent(Dashboard.this, DepositMoney.class);
        startActivity(depositMoney);

    }

    public void goToLoans(View view) {
        Intent loanActivity = new Intent(Dashboard.this, LoanActivity.class);
        startActivity(loanActivity);
    }

    public void goToTransactionHistory(View view) {
        Intent transaction = new Intent(Dashboard.this, TransactionHistory.class);
        startActivity(transaction);
    }

    public void goToRotationList(View view){
        Intent toRotation = new Intent(Dashboard.this, RotationListActivity.class);
        startActivity(toRotation);
    }


}