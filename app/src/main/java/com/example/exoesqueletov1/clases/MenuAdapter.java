package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.NewsViewHolder> implements Filterable {

    private Context mContext;
    private List<MenuItem> itemList;
    private List<MenuItem> mDataFiltered;
    private OnMenuListener onMenuListener;

    public MenuAdapter(Context mContext, List<MenuItem> itemList, OnMenuListener onMenuListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = itemList;
        this.onMenuListener = onMenuListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_menu,viewGroup,false);
        return new NewsViewHolder(layout, onMenuListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.img_user.setImageResource(mDataFiltered.get(position).getUserPhoto());
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = itemList;
                }
                else {
                    List<MenuItem> lstFiltered = new ArrayList<>();
                    for (MenuItem row : itemList) {
                        if (row.getTitle().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (List<MenuItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img_user;
        private OnMenuListener onMenuListener;

        NewsViewHolder(@NonNull View itemView, OnMenuListener onMenuListener) {
            super(itemView);
            img_user = itemView.findViewById(R.id.img_user);
            this.onMenuListener = onMenuListener;
            img_user.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onMenuListener.onMenuClick(getAdapterPosition());
        }
    }
    public interface OnMenuListener {
        void onMenuClick (int position);
    }
}

