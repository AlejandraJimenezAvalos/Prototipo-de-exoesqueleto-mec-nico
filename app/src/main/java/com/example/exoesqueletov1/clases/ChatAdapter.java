package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.NewsViewHolder> implements Filterable  {

    private Context mContext;
    private List<ChatItem> itemList;
    private List<ChatItem> mDataFiltered;
    private ChatAdapter.OnMenuListener onMenuListener;

    private static final long ONE_MEGABYTE = 1024 * 1024;

    public ChatAdapter(Context mContext, List<ChatItem> itemList, ChatAdapter.OnMenuListener onMenuListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = itemList;
        this.onMenuListener = onMenuListener;
    }

    @NonNull
    @Override
    public ChatAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_chat,viewGroup,false);
        return new NewsViewHolder(layout, onMenuListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.NewsViewHolder holder, int position) {
        holder.textViewName.setText(mDataFiltered.get(position).getName());
        holder.textViewMessage.setText(mDataFiltered.get(position).getMessage());
        holder.textViewDate.setText(mDataFiltered.get(position).getDate());
        final CircleImageView circleImageView = holder.circleImageViewProfilePhoto;
        FirebaseStorage.getInstance().getReference().child("pictureProfile").child(mDataFiltered.get(position).getId())
                .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                circleImageView.setImageBitmap(photo);
            }
        });
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
                    List<ChatItem> lstFiltered = new ArrayList<>();
                    for (ChatItem row : itemList) {
                        if (row.getName().toLowerCase().contains(Key.toLowerCase())){
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
                mDataFiltered = (List<ChatItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ChatAdapter.OnMenuListener onMenuListener;

        private TextView textViewName;
        private TextView textViewMessage;
        private TextView textViewDate;
        private CircleImageView circleImageViewProfilePhoto;

        NewsViewHolder(@NonNull View itemView, ChatAdapter.OnMenuListener onMenuListener) {
            super(itemView);
            this.onMenuListener = onMenuListener;

            textViewName = itemView.findViewById(R.id.text_item_chat_name);
            textViewMessage = itemView.findViewById(R.id.text_item_chat_message);
            textViewDate = itemView.findViewById(R.id.text_item_chat_date);
            circleImageViewProfilePhoto = itemView.findViewById(R.id.image_item_chat);
            ConstraintLayout constraintLayout = itemView.findViewById(R.id.item_chat);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMenuListener.onMenuClick(getAdapterPosition());
        }

    }
    public interface OnMenuListener {
        void onMenuClick(int position);
    }
}
