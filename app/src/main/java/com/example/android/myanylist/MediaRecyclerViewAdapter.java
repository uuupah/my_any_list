package com.example.android.myanylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MediaRecyclerViewAdapter extends RecyclerView.Adapter<MediaRecyclerViewAdapter
        .ViewHolder> {

    private static final String TAG = "MediaRecyclerViewAdapter";                                   // error tag

//    private ArrayList<String> mStringArray = new ArrayList<>();                                   // why is it redundant below but not here?
    private ArrayList<Media> mMediaArrayList = new ArrayList<>();                                   // list of Media elements
    private Context mContext;


    public MediaRecyclerViewAdapter(Context context, ArrayList<Media> media) {                      // constructor
        mMediaArrayList = media;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout                       // inflate a new view into layout
                        .media_layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);                                                   // create new holder and attach to new view
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");                                               // log every time a view holder is bound

//        Glide.with(mContext) // grabs the image from position and places it in the view holder
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.image);

        holder.image.setImageResource(mMediaArrayList.get(position).getImageResourceId());          // update the media resources in the current view holder
        holder.name.setText(mMediaArrayList.get(position).getName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {                         // create on click listener for view, currently displays a toast
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mMediaArrayList.get(position).getName());

                Toast.makeText(mContext, mMediaArrayList.get(position).getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMediaArrayList.size();                                                              // tells the adapter how large the list is
    }


    public class ViewHolder extends RecyclerView.ViewHolder {                                       // acts as a package for easy reference to the views of the list

        ImageView image;
        TextView name;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {                                                 // constructor
            super(itemView);
            image = itemView.findViewById(R.id.media_image);
            name = itemView.findViewById(R.id.media_name);
            parentLayout = itemView.findViewById(R.id.media_recycler_parent);
        }
    }

}
