package com.mutebi.mchama.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutebi.mchama.R;
import com.mutebi.mchama.models.NoticesList;
import com.mutebi.mchama.models.TransactionList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoticesAdapter extends RecyclerView.Adapter<NoticesAdapter.ViewHolder> {
    private List<NoticesList> noticeList;
    private Context mContext;

    public String result;

    public NoticesAdapter(List<NoticesList> noticeList, Context context){
        this.noticeList = noticeList;
        this.mContext =context;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView messageBox;
        public TextView created_at;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            messageBox = itemView.findViewById(R.id.msg_box);
            created_at = itemView.findViewById(R.id.msd_date);
            linearLayout = itemView.findViewById(R.id.notice_linear);


        }
    }

    @NonNull
    @Override
    public NoticesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticesAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_list,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull NoticesAdapter.ViewHolder holder, int position) {
        final NoticesList currentNotice = noticeList.get(position);

        holder.messageBox.setText(currentNotice.getMessage());
        holder.created_at.setText(currentNotice.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
