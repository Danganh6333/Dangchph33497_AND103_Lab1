package com.dangchph33497.fpoly.lab1.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangchph33497.fpoly.lab1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<DTO> list;
    private FirebaseFirestore database;

    public RecycleViewAdapter(Context context, ArrayList<DTO> list, FirebaseFirestore database) {
        this.context = context;
        this.list = list;
        this.database = database;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.recycle_view_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText("Tên: "+list.get(position).getName());
        holder.tvCountry.setText("Thành Phố: "+list.get(position).getCountry());
        holder.tvPopulation.setText("Dân Số: "+list.get(position).getPopulation());
        if (list.get(position).getState() == null){
            holder.tvState.setText("Không Phải Là Bang");
        }else {
            holder.tvState.setText("Bang: "+list.get(position).getState());
        }
        if (list.get(position).isCapital()){
            holder.tvCapital.setText("Thủ Đô");
        }else {
            holder.tvCapital.setText("Không Phải Thủ Đô");
        }
        StringBuilder regionsBuilder = new StringBuilder("Khu vực: ");
        ArrayList<String> regions = list.get(position).getRegions();
        if (regions != null && regions.size() > 0) {
            for (String region : regions) {
                regionsBuilder.append(region).append(" và ");
            }
            regionsBuilder.setLength(regionsBuilder.length() - 2);
        } else {
            regionsBuilder.append("Không tồn tại ");
        }

        holder.tvRegions.setText(regionsBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvState,tvCountry,tvCapital,tvPopulation,tvRegions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvState = itemView.findViewById(R.id.tvState);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvCapital = itemView.findViewById(R.id.tvCapital);
            tvPopulation = itemView.findViewById(R.id.tvPopulation);
            tvRegions = itemView.findViewById(R.id.tvRegions);
        }
    }
}
