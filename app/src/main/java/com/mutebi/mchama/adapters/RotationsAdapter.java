package com.mutebi.mchama.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutebi.mchama.R;
import com.mutebi.mchama.models.RotationList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RotationsAdapter extends RecyclerView.Adapter<RotationsAdapter.ViewHolder>  {
    private List<RotationList> rotationList;
    private Context mContext;


    public RotationsAdapter(List<RotationList> rotationList, Context context){
        this.rotationList = rotationList;
        this.mContext =context;
    }
    public class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView rotationTurn;
        public TextView userType;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.rotation_name);
            rotationTurn = itemView.findViewById(R.id.rotation_turn);
            userType = itemView.findViewById(R.id.rotation_role);
            linearLayout = itemView.findViewById(R.id.rotation_linearLayout);


        }
    }

    @NonNull
    @Override
    public RotationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RotationsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rotation_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RotationsAdapter.ViewHolder holder, int position) {
        final RotationList currentUser = rotationList.get(position);

        holder.name.setText(currentUser.getName());
        holder.rotationTurn.setText(currentUser.getRotationTurn());
        holder.userType.setText(currentUser.getUserType());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

    }

    @Override
    public int getItemCount() {
        return rotationList.size();
    }
}
