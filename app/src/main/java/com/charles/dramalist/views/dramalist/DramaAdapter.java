package com.charles.dramalist.views.dramalist;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.charles.dramalist.R;
import com.charles.dramalist.api.model.Datum;
import com.charles.dramalist.databinding.DramaItemBinding;
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
        DramaItemBinding myBinding;

        MyViewHolder(DramaItemBinding binding) {
            super(binding.getRoot());
            myBinding = binding;
            binding.dramaItemRow.setOnClickListener(view1 -> {
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
        return new MyViewHolder(DramaItemBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Datum datum = datumList.get(position);

        final String artworkUrl = datum.getThumb();
        Glide.with(context).load(artworkUrl).placeholder(R.drawable.ic_logo).into(holder.myBinding.thumb);

        if (TextUtils.isEmpty(strHighLight)) {
            holder.myBinding.dramaName.setText(datum.getName());
        } else {
            holder.myBinding.dramaName.setText(Html.fromHtml(datum.getName().replaceAll(strHighLight, "<font color='red'>" + strHighLight + "</font>")));
        }
        holder.myBinding.rating.setText(String.format(context.getString(R.string.rating), datum.getRating()));
        holder.myBinding.createdAt.setText(String.format(context.getString(R.string.created_time), String.valueOf(datum.getCreatedAt())));
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    void setHighLight(String keyword) {
        strHighLight = keyword;
    }
}