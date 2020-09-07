package com.mutebi.mchama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DepositMoney extends AppCompatActivity {

    ListView listView;
    String mTitle[] = {"Wallet", "Loan Payment"};
    String mDescription[] = {"Deposit money into your saving wallet","Pay your loan"};

    
    int images[] = {R.drawable.ic_withdraw_dashboard, R.drawable.ic_loans};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_money);

        listView = findViewById(R.id.listView);

        MyAdapter adapter = new MyAdapter(this, mTitle, mDescription, images);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 ){
                    Toast.makeText(DepositMoney.this,"Wallet", Toast.LENGTH_SHORT).show();
                        Intent wallet= new Intent(DepositMoney.this, Wallet.class);
                        startActivity(wallet);


                }
                if (position == 1  ){
                    Toast.makeText(DepositMoney.this,"Loan Payment", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter (Context c, String title[], String description[], int imgs[]){
            super(c, R.layout.row, R.id.email_title, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;


        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(
               Context.LAYOUT_INFLATER_SERVICE
            );
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle =row.findViewById(R.id.title);
            TextView myDescription =row.findViewById(R.id.subtitle);


            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);




            return row;
        }
    }
}