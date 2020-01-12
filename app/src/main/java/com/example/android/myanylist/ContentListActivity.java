package com.example.android.myanylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.android.myanylist.adapters.ContentRecyclerAdapter;
import com.example.android.myanylist.models.ContentItem;
import com.example.android.myanylist.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;

public class ContentListActivity extends AppCompatActivity implements ContentRecyclerAdapter.OnContentListener{

    // ui components
    private RecyclerView            mRecyclerView;

    // vars (anything that isnt a view or a widget)
    private ArrayList<ContentItem>  mContent = new ArrayList<>();
    private ContentRecyclerAdapter  mContentRecyclerAdapater;

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

    }

    private void insertFakeContent() {
        mContent.add(new ContentItem("Dark Souls", 8, "1 jan 2020", "1 jan 2020", 2, "FromSoftware", "Dark Souls takes place in the fictional kingdom of Lordran, where players assume the role of a cursed undedad who begins a pilgrimage to discover the fate of their kind", R.drawable.dark_souls));
        mContent.add(new ContentItem("Bloodborne", 10, "2 jan 2020", "2 jan 2020", 1, "FromSoftware", "Bloodborne follows the player's character, a hunter, through the decrepit city of yharnam", R.drawable.bloodborne));
        mContent.add(new ContentItem("Sekiro", 9, "3 jan 2020","3 jan 2020", 0, "FromSoftware", "Sekiro takes place in the sengoku period in japan, and follows a shinobi known as wolf as he attempts to take revenge on a samurai clan who attacked him and kidnapped his lord", R.drawable.sekiro));
        mContentRecyclerAdapater.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(16);
        mRecyclerView.addItemDecoration(itemDecorator);
        mContentRecyclerAdapater = new ContentRecyclerAdapter(mContent, this);
        mRecyclerView.setAdapter(mContentRecyclerAdapater);
    }


    @Override
    public void onContentClick(int position) {
        mContent.get(position);
        Intent intent = new Intent(this, /** activity **/);
        startActivity(/**activity**/);

    }
}
