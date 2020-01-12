package com.example.android.myanylist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myanylist.R;
import com.example.android.myanylist.models.ContentItem;

import java.util.ArrayList;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ViewHolder> {

    ArrayList<ContentItem> mItems = new ArrayList<>();

    public ContentRecyclerAdapter(ArrayList<ContentItem> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_list_item, parent, false);
        return new ViewHolder(view);
    }

    // fills contents of view depending on current position in database
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(mItems.get(position).getDateAdded());
        holder.title.setText(mItems.get(position).getTitle());
        holder.status.setText(ContentItem.getStringStatus(mItems.get(position).getStatus()));
        holder.image.setImageResource(mItems.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title, status, date;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.content_list_item_date_added);
            title = itemView.findViewById(R.id.content_list_item_title);
            status = itemView.findViewById(R.id.content_list_item_status);
            image = itemView.findViewById(R.id.content_list_item_image);
        }
    }
}
