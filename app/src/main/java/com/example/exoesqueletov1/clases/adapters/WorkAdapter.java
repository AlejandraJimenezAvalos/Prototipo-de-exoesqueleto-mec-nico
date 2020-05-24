package com.example.exoesqueletov1.clases.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.items.WorkItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.exoesqueletov1.clases.bleutooth.Constants.UP_LEFT;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.UP_RIGHT;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.WALK_MINUTES;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.WALK_STEPS;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.NewsViewHolder>
        implements Filterable {

    private Context mContext;
    private List<WorkItem> itemList;
    private List<WorkItem> mDataFiltered;
    private OnWorkListener onWorkListener;
    private boolean state = false;


    public WorkAdapter(Context mContext, List<WorkItem> itemList, OnWorkListener onWorkListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = itemList;
        this.onWorkListener = onWorkListener;
    }

    public WorkAdapter(Context mContext, List<WorkItem> itemList, OnWorkListener onWorkListener,
                       boolean state) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mDataFiltered = itemList;
        this.onWorkListener = onWorkListener;
        this.state = state;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(mContext)
                .inflate(R.layout.item_work,viewGroup,false);
        return new NewsViewHolder(layout, onWorkListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        try {
            WorkItem workItem = mDataFiltered.get(position);
            String action = workItem.getAction();
            if (workItem.isState()) {
                holder.imageViewDelete.setEnabled(false);
                holder.imageViewDelete.setVisibility(View.INVISIBLE);
            }
            holder.textViewDate.setText(workItem.getDate());
            if ((action.equals(WALK_MINUTES)) || (action.equals(WALK_STEPS))) {
                holder.imageView.setImageDrawable(mContext.getDrawable(R.mipmap.ic_walk_foreground));
                holder.textViewTitle.setText(mContext.getText(R.string.walk));
            }
            else {
                holder.imageView.setImageDrawable(mContext
                        .getDrawable(R.mipmap.ic_up_and_down_foreground));
                holder.textViewTitle.setText(mContext.getText(R.string.exercise_your_legs));
            }
            switch (action) {
                case WALK_MINUTES:
                    holder.textViewDescription.setText(mContext.getString(R.string.a_session) + " "
                            + mContext.getString(R.string.walk_less) + " " + workItem.getNumber()
                            + " " +  mContext.getString(R.string.minutes));
                    break;
                case WALK_STEPS:
                    holder.textViewDescription.setText(mContext.getString(R.string.a_session) + " "
                            + mContext.getString(R.string.walk_less) + " " + workItem.getNumber()
                            + " " + mContext.getString(R.string.steps));
                    break;
                case UP_RIGHT:
                    holder.textViewDescription.setText(mContext.getString(R.string.a_session)
                            + " subir y bajar " + workItem.getNumber()
                            + " veces la pierna derecha.");
                    break;
                case UP_LEFT:
                    holder.textViewDescription.setText(mContext.getString(R.string.a_session)
                            + "subir y bajar " + workItem.getNumber()
                            + " veces la pierna izquierda.");
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "e: " + e, Toast.LENGTH_SHORT).show();
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
                    List<WorkItem> lstFiltered = new ArrayList<>();
                    for (WorkItem row : itemList) {
                        if (row.getDate().toLowerCase().contains(Key.toLowerCase())){
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
                mDataFiltered = (List<WorkItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ImageView imageViewDelete;
        private TextView textViewDate;
        private TextView textViewTitle;
        private TextView textViewDescription;

        NewsViewHolder(@NonNull View itemView, OnWorkListener onWorkListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.drawable_image);
            imageViewDelete = itemView.findViewById(R.id.image_delete);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewDate = itemView.findViewById(R.id.date);
            textViewDescription = itemView.findViewById(R.id.description);

            imageViewDelete.setOnClickListener(v -> onWorkListener.onWorkClick(getAdapterPosition()));
        }
    }

    public interface OnWorkListener {
        void onWorkClick(int position);
    }
}

