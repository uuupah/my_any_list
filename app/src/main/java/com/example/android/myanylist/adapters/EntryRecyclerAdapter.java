package com.example.android.myanylist.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.myanylist.R;
import com.example.android.myanylist.models.MediaEntry;
import com.example.android.myanylist.util.Utility;

import java.io.File;
import java.util.ArrayList;

public class EntryRecyclerAdapter extends RecyclerView.Adapter<EntryRecyclerAdapter.ViewHolder> {

    private OnContentListener mOnContentListener;
    private ArrayList<MediaEntry> mItems;

    public EntryRecyclerAdapter(ArrayList<MediaEntry> items, OnContentListener onContentListener) {
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
        holder.date.setText(mItems.get(position).getTimeStamp());
        holder.title.setText(mItems.get(position).getTitle());
        holder.status.setText(MediaEntry.getStringStatus(mItems.get(position).getStatus()));
        holder.status.setTextColor(ContextCompat.getColor(holder.status.getContext(), MediaEntry.getStatusColor(mItems.get(position).getStatus())));
        holder.blocker.setBackgroundColor(ContextCompat.getColor(holder.blocker.getContext(), MediaEntry.getStatusColor(mItems.get(position).getStatus())));

        // check if image file and imagelocation is valid, if not, display default image
        if(Utility.isValidImageLocation(mItems.get(position).getImageLocation())){
            Glide.with(holder.image.getContext()).load(Uri.fromFile(new File(mItems.get(position).getImageLocation()))).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, status, date;
        private ImageView image;
        private View blocker;

        OnContentListener onContentListener;

        public ViewHolder(@NonNull View itemView, OnContentListener onContentListener) {
            super(itemView);
            date = itemView.findViewById(R.id.content_list_item_date_added);
            title = itemView.findViewById(R.id.content_list_item_title);
            status = itemView.findViewById(R.id.content_list_item_status);
            image = itemView.findViewById(R.id.content_list_item_image);
            blocker = itemView.findViewById(R.id.content_list_item_blocker);

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
