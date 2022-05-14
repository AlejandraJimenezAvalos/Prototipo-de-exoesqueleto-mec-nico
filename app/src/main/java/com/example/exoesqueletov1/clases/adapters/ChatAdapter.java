package com.example.exoesqueletov1.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.ui.ConstantsDatabase;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Storge;
import com.example.exoesqueletov1.clases.items.ChatItem;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.NewsViewHolder>
        implements Filterable  {

    private Context mContext;
    private List<ChatItem> itemList;
    private List<ChatItem> mDataFiltered;
    private ChatAdapter.OnMenuListener onMenuListener;

    public ChatAdapter(Context mContext, List<ChatItem> itemList, OnMenuListener onMenuListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = itemList;
        this.onMenuListener = onMenuListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(mContext)
                .inflate(R.layout.item_chat,viewGroup,false);
        return new NewsViewHolder(layout, onMenuListener);
    }

    @Override
    public void
    onBindViewHolder(@NonNull final ChatAdapter.NewsViewHolder holder, final int position) {
        if (!mDataFiltered.get(position).getName().equals("")) {
            holder.textViewName.setText(mDataFiltered.get(position).getName());
            holder.textViewMessage.setText(mDataFiltered.get(position).getMessage());
            holder.textViewDate.setText(mDataFiltered.get(position).getDate());
        } else {
            FirebaseFirestore.getInstance().collection(mDataFiltered.get(position).getId()).
                    document(ConstantsDatabase.DOCUMENT_PROFILE).get().addOnSuccessListener(documentSnapshot -> {
                        holder.textViewName.setText(documentSnapshot.getData()
                                .get(ConstantsDatabase.NAME).toString());
                        holder.textViewMessage.setText(mDataFiltered
                                .get(position).getMessage());
                        mDataFiltered.get(position).setName(documentSnapshot
                                .getData().get(ConstantsDatabase.NAME).toString());

                        String typeUser = "";
                        if (documentSnapshot.getData().get(ConstantsDatabase.USER).toString().equals("a")) {
                            typeUser = "Administrador";
                        }
                        if (documentSnapshot.getData().get(ConstantsDatabase.USER).toString().equals("b")) {
                            typeUser = "Fisioterapeuta";
                        }
                        if (documentSnapshot.getData().get(ConstantsDatabase.USER).toString().equals("c")) {
                            typeUser = "Pasiente";
                        }

                        mDataFiltered.get(position).setMessage(typeUser);
                    });
        }
        new Storge().getProfileImage(holder.circleImageViewProfilePhoto,
                mDataFiltered.get(position).getId());
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

    public static class NewsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ChatAdapter.OnMenuListener onMenuListener;

        private TextView textViewName;
        private TextView textViewMessage;
        private TextView textViewDate;
        private ImageView circleImageViewProfilePhoto;

        NewsViewHolder(@NonNull View itemView, ChatAdapter.OnMenuListener onMenuListener) {
            super(itemView);
            this.onMenuListener = onMenuListener;

            textViewName = itemView.findViewById(R.id.text_item_chat_name);
            textViewMessage = itemView.findViewById(R.id.text_item_chat_message);
            textViewDate = itemView.findViewById(R.id.text_item_chat_date);
            circleImageViewProfilePhoto = itemView.findViewById(R.id.image_item_chat);
            MaterialCardView constraintLayout = itemView.findViewById(R.id.item_chat);
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
