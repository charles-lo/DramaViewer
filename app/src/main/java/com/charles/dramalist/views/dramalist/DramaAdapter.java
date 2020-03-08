package com.charles.dramalist.views.dramalist;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.dramalist.R;
import com.charles.dramalist.api.model.Datum;
import com.charles.dramalist.views.dramadetail.DramaDetailView;

import java.util.List;

/**
 *
 */

public class DramaAdapter extends RecyclerView.Adapter<DramaAdapter.MyViewHolder> {

    private Context context;
    private List<Datum> datumList;
    private String strHighLight;

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout row;
        ImageView imgThumb;
        TextView txtName, txtRating, txtCreated_at;


        MyViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.drama_item_row);
            imgThumb = view.findViewById(R.id.thumb);
            txtName = view.findViewById(R.id.drama_name);
            txtRating = view.findViewById(R.id.rating);
            txtCreated_at = view.findViewById(R.id.created_at);
            row.setOnClickListener(view1 -> {
                Intent detail = new Intent(context, DramaDetailView.class);
                detail.putExtra("drama", datumList.get(getAdapterPosition()));
                context.startActivity(detail);
            });
        }
    }

    DramaAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drama_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Datum datum = datumList.get(position);

        final String artworkUrl = datum.getThumb();
        Glide.with(context).load(artworkUrl).placeholder(R.drawable.ic_logo).into(holder.imgThumb);

        if (TextUtils.isEmpty(strHighLight)) {
            holder.txtName.setText(datum.getName());
        } else {
            holder.txtName.setText(Html.fromHtml(datum.getName().replaceAll(strHighLight, "<font color='red'>" + strHighLight + "</font>")));
        }
        holder.txtRating.setText(String.format(context.getString(R.string.rating), datum.getRating()));
        holder.txtCreated_at.setText(String.format(context.getString(R.string.created_time), String.valueOf(datum.getCreatedAt())));
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    void setHighLight(String keyword) {
        strHighLight = keyword;
    }
}