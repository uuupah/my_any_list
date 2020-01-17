package com.example.android.myanylist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myanylist.R;
import com.example.android.myanylist.models.MediaEntry;

import java.util.ArrayList;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ViewHolder> {

    private ArrayList<MediaEntry> mItems = new ArrayList<>();
    private OnContentListener mOnContentListener;

    public ContentRecyclerAdapter(ArrayList<MediaEntry> items, OnContentListener onContentListener) {
        this.mItems = items;
        this.mOnContentListener = onContentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_list_item, parent, false);
        return new ViewHolder(view, mOnContentListener);
    }

    // fills contents of view depending on current position in database
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(mItems.get(position).getDateAdded());
        holder.title.setText(mItems.get(position).getTitle());
        holder.status.setText(MediaEntry.getStringStatus(mItems.get(position).getStatus()));
        holder.image.setImageResource(mItems.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, status, date;
        private ImageView image;

        OnContentListener onContentListener;

        public ViewHolder(@NonNull View itemView, OnContentListener onContentListener) {
            super(itemView);
            date = itemView.findViewById(R.id.content_list_item_date_added);
            title = itemView.findViewById(R.id.content_list_item_title);
            status = itemView.findViewById(R.id.content_list_item_status);
            image = itemView.findViewById(R.id.content_list_item_image);

            this.onContentListener = onContentListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onContentListener.onContentClick(getAdapterPosition());
        }
    }

    public interface OnContentListener {
        void onContentClick(int position);
    }
}
