package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;

import java.util.ArrayList;
import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NewsViewHolder> implements Filterable  {

    private Context mContext;
    private List<ItemNotify> itemList;
    private List<ItemNotify> mDataFiltered;
    private MenuAdapter.OnMenuListener onMenuListener;

    public NotifyAdapter(Context mContext, List<ItemNotify> itemList, MenuAdapter.OnMenuListener onMenuListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = itemList;
        this.onMenuListener = onMenuListener;
    }

    @NonNull
    @Override
    public NotifyAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_notify,viewGroup,false);
        return new NewsViewHolder(layout, onMenuListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.NewsViewHolder holder, int position) {
        //holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        holder.title.setText(mDataFiltered.get(position).getTitle());
        holder.date.setText(mDataFiltered.get(position).getDate());
        holder.description.setText(mDataFiltered.get(position).getDescription());
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
                    List<ItemNotify> lstFiltered = new ArrayList<>();
                    for (ItemNotify row : itemList) {
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
                mDataFiltered = (List<ItemNotify>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, date, description;
        private ImageView buttonGo;
        private MenuAdapter.OnMenuListener onMenuListener;
        private LinearLayout linearLayout;

        NewsViewHolder(@NonNull View itemView, MenuAdapter.OnMenuListener onMenuListener) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.item_pending);
            title = itemView.findViewById(R.id.text_title_item_pending);
            date = itemView.findViewById(R.id.text_date_item_pending);
            description = itemView.findViewById(R.id.text_description_item_pending);
            buttonGo = itemView.findViewById(R.id.button_go_item_pending);
            this.onMenuListener = onMenuListener;
            buttonGo.setOnClickListener(this);
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
