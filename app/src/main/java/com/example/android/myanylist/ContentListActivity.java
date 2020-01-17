package com.example.android.myanylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.myanylist.adapters.ContentRecyclerAdapter;
import com.example.android.myanylist.models.MediaEntry;
import com.example.android.myanylist.util.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContentListActivity extends AppCompatActivity implements ContentRecyclerAdapter.OnContentListener, FloatingActionButton.OnClickListener {

    // ui components
    private RecyclerView mRecyclerView;

    // vars (anything that isnt a view or a widget)
    private ArrayList<MediaEntry> mContent = new ArrayList<>();
    private ContentRecyclerAdapter mContentRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);

        mRecyclerView = findViewById(R.id.content_recycler_view);       // attach the variable to its id

        initRecyclerView();
        insertFakeContent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.content_list_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Games");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        findViewById(R.id.list_fab).setOnClickListener(this);
    }

    private void insertFakeContent() {
        mContent.add(new MediaEntry("Dark Souls", 8, "1 jan 2020", "1 jan 2020", 2, "FromSoftware", "Dark Souls takes place in the fictional kingdom of Lordran, where players assume the role of a cursed undedad who begins a pilgrimage to discover the fate of their kind", R.drawable.dark_souls));
        mContent.add(new MediaEntry("Bloodborne", 10, "2 jan 2020", "2 jan 2020", 1, "FromSoftware", "Bloodborne follows the player's character, a hunter, through the decrepit city of yharnam", R.drawable.bloodborne));
        mContent.add(new MediaEntry("Sekiro", 9, "3 jan 2020", "3 jan 2020", 0, "FromSoftware", "Sekiro takes place in the sengoku period in japan, and follows a shinobi known as wolf as he attempts to take revenge on a samurai clan who attacked him and kidnapped his lord", R.drawable.sekiro));
        mContentRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(16);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mContentRecyclerAdapter = new ContentRecyclerAdapter(mContent, this);
        mRecyclerView.setAdapter(mContentRecyclerAdapter);
    }

    @Override
    public void onContentClick(int position) {
        mContent.get(position);
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("selected_content", mContent.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_fab:
                Intent intent = new Intent(this, ContentActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void deleteEntry(MediaEntry entry){
        mContent.remove(entry);
        mContentRecyclerAdapter.notifyDataSetChanged();
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false; //TODO implement this at some point to allow rearranging the list
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteEntry(mContent.get(viewHolder.getAdapterPosition()));
        }
    };

    //TODO
    // implement soft swipe delete (swiping reveals a delete menu item)
    // ultimate recycler view is apparently good?
    // or implement an undo snackbar (lesser solution)
}
