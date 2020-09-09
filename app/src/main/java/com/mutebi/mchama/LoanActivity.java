package com.mutebi.mchama;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class LoanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText fundDate;
    private EditText repayment_start_date;
    private int mYear,mMonth,mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        fundDate = findViewById(R.id.fund_date);
        fundDate.setOnClickListener(this);

        repayment_start_date = findViewById(R.id.repayment_start_date);
        repayment_start_date.setOnClickListener(this);

        Spinner spinner = findViewById(R.id.repayment_period);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repayment_period, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        if (v == fundDate) {
            final Calendar C = Calendar.getInstance();
            mYear = C.get(Calendar.YEAR);
            mMonth = C.get(Calendar.MONTH);
            mDay = C.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    fundDate.setText(day + "/" + (month + 1 + "/" + year));
                }
            }, mYear, mMonth, mDay);
            mDatePickerDialog.show();
        } else {


            if (v == repayment_start_date) {
                final Calendar C = Calendar.getInstance();
                mYear = C.get(Calendar.YEAR);
                mMonth = C.get(Calendar.MONTH);
                mDay = C.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        repayment_start_date.setText(day + "/" + (month + 1 + "/" + year));
                    }
                }, mYear, mMonth, mDay);
                mDatePickerDialog.show();


            }
        }
    }




}