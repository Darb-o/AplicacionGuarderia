package com.example.aplicacionguarderia;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListAdapter(List<ListElement> list, Context context){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //por si se quiere crear otra lista
    public void setItems(List<ListElement> list){this.list = list;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout card, cardinfo;
        ImageView card_image;
        TextView card_name, card_date, card_status;
        ViewHolder(View itemView){
            super(itemView);
            card_image = itemView.findViewById(R.id.card_image);
            card_name = itemView.findViewById(R.id.card_name);
            card_date = itemView.findViewById(R.id.card_date);
            card_status = itemView.findViewById(R.id.card_status);
            card = itemView.findViewById(R.id.card);
        }

        void bindData(final ListElement item){
            card_name.setText(item.getName());
            card_date.setText(item.getDate());
            card_status.setText(item.getStatus());
            card_status.setBackgroundColor(Color.parseColor(item.getColor()));
            Glide.with(context).load(item.getImgUrl()).into(card_image);
        }
    }
}
