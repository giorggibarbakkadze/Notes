package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
    ArrayList<products> datalist;
    public myAdapter(ArrayList<products> datalist) {
        this.datalist = datalist;
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent,false);
        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.note_title.setText(datalist.get(position).getTitle());
        holder.note_description.setText(datalist.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
    TextView note_title, note_description;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        note_title = itemView.findViewById(R.id.textView);
        note_description = itemView.findViewById(R.id.textView2);
    }
}
}
