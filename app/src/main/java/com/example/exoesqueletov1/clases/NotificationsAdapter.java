package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NewsViewHolder>
        implements Filterable  {

    private Context mContext;
    private List<NotificationsItem> itemList;
    private List<NotificationsItem> mDataFiltered;
    private NotificationsAdapter.OnMenuListener onMenuListener;

    public NotificationsAdapter(Context mContext, List<NotificationsItem> itemList,
                                NotificationsAdapter.OnMenuListener onMenuListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = itemList;
        this.onMenuListener = onMenuListener;
    }

    @NonNull
    @Override
    public NotificationsAdapter.NewsViewHolder
    onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(mContext)
                .inflate(R.layout.item_notification,viewGroup,false);
        return new NewsViewHolder(layout, onMenuListener);
    }

    @Override
    public void
    onBindViewHolder(@NonNull NotificationsAdapter.NewsViewHolder holder, int position) {
        holder.title.setText(mDataFiltered.get(position).getTitle());
        holder.date.setText(mDataFiltered.get(position).getDate());
        holder.description.setText(mDataFiltered.get(position).getDescription());
        if (mDataFiltered.get(position).isState()) {
            holder.viewItemPending.setBackgroundResource(R.color.blue);
        }
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
                    List<NotificationsItem> lstFiltered = new ArrayList<>();
                    for (NotificationsItem row : itemList) {
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
                mDataFiltered = (List<NotificationsItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView title, date, description;
        private View viewItemPending;
        private NotificationsAdapter.OnMenuListener onMenuListener;

        NewsViewHolder(@NonNull View itemView, NotificationsAdapter.OnMenuListener onMenuListener) {
            super(itemView);

            LinearLayout itemPending = itemView.findViewById(R.id.item_pending);
            title = itemView.findViewById(R.id.text_title_item_pending);
            date = itemView.findViewById(R.id.text_date_item_pending);
            description = itemView.findViewById(R.id.text_description_item_pending);
            viewItemPending = itemPending.findViewById(R.id.view_item_pending);

            this.onMenuListener = onMenuListener;
            itemPending.setOnClickListener(this);
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
