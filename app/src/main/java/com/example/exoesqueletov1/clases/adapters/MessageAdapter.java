package com.example.exoesqueletov1.clases.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.items.MessageItem;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.NewsViewHolder>
        implements Filterable {

    private Context mContext;
    private List<MessageItem> itemList;
    private List<MessageItem> mDataFiltered;

    private static final int CODE_MY_MESSAGE = 0;
    private static final int CODE_YOUR_MESSAGE = 1;

    public MessageAdapter(Context mContext, List<MessageItem> itemList,
                          List<MessageItem> mDataFiltered) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = mDataFiltered;
    }

    @NonNull
    @Override
    public MessageAdapter.NewsViewHolder
    onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        if (viewType == CODE_MY_MESSAGE) {
            layout = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_my_message, viewGroup, false);
        } else {
            layout = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_your_message, viewGroup, false);
        }
        return new NewsViewHolder(layout);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataFiltered.get(position).getMyMessage()){ return CODE_MY_MESSAGE; }
        else return CODE_YOUR_MESSAGE;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void
    onBindViewHolder(@NonNull final MessageAdapter.NewsViewHolder holder, final int position) {
        holder.textViewMessage.setText(mDataFiltered.get(position).getMessage());
        holder.textViewDate.setText(mDataFiltered.get(position).getHour());
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
                    List<MessageItem> lstFiltered = new ArrayList<>();
                    for (MessageItem row : itemList) {
                        if (row.getMessage().toLowerCase().contains(Key.toLowerCase())){
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
                mDataFiltered = (List<MessageItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {


        private TextView textViewMessage;
        private TextView textViewDate;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textView_message);
            textViewDate = itemView.findViewById(R.id.textView_hour);
        }
    }
}
