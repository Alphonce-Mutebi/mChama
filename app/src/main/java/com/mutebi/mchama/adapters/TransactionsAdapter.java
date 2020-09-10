package com.mutebi.mchama.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.LayoutInflater;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mutebi.mchama.R;
import com.mutebi.mchama.models.TransactionList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private List<TransactionList> transactionList;
    private Context mContext;

    public String result;


    public static final String KEY_AMOUNT ="amount";
    public static final String KEY_TYPE ="type";

    public TransactionsAdapter(List<TransactionList> transactionList, Context context){
        this.transactionList = transactionList;
        this.mContext =context;
    }
    public class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView amount;
        public TextView type;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            type = itemView.findViewById(R.id.transaction_type);
            linearLayout = itemView.findViewById(R.id.linearLayout);


        }
    }


    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {
        final TransactionList currentTransaction = transactionList.get(position);

        holder.amount.setText(currentTransaction.getAmount());
        holder.type.setText(currentTransaction.getTransactionType());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TransactionList transactionList1 = transactionList.get(position);
//                Intent skipIntent = new Intent(v.getContext(), ProfileActivity.class);
//
//                skipIntent.putExtra(KEY_NAME, developersList1.getLogin());
//                skipIntent.putExtra(KEY_URL, developersList1.getHtml_url());
//                skipIntent.putExtra(KEY_IMAGE, developersList1.getAvatar_url());
//                v.getContext().startActivity(skipIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
