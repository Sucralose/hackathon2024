package com.example.extrahand2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extrahand2.R;
import com.example.extrahand2.adapter.PostAdapter;
import com.example.extrahand2.datamodels.Post;
import com.example.extrahand2.utils.FirebaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView;
        FloatingActionButton floatingActionButton;
        TextView settingsButton, levelText;

        public void onStart ( ) {
                super.onStart();
                setupBadgeButton();
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                recyclerView = findViewById(R.id.recyclerview);
                floatingActionButton = findViewById(R.id.floatingActionButton);
                settingsButton = (TextView) findViewById(R.id.settingsButton);

                floatingActionButton.setOnClickListener(v -> {
                        Intent intent = new Intent(this,addVolunteer.class);
                        startActivity(intent);
                });
                
                settingsButton.setOnClickListener(new View.OnClickListener( ) {
                        @Override
                        public void onClick ( View view ) {
                                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                                popupMenu.setOnMenuItemClickListener(MainActivity.this::onMenuItemClick);
                                popupMenu.inflate(R.menu.menu_main);
                                popupMenu.show();
                        }
                });

                setupBadgeButton();

                List<Post> postsLists = new ArrayList<Post>();
                PostAdapter postAdapter = new PostAdapter(this,postsLists);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(postAdapter);

                db.collection("Posts")
                        .orderBy("TimeAdded", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @SuppressLint( "NotifyDataSetChanged" )
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String description = document.getString("Description");
                                                        String owner = document.getString("Owner");
                                                        String type = document.getString("Type");
                                                        Timestamp timeAdded = (Timestamp) document.get("TimeAdded");
                                                        String location = document.getString("Location");
                                                        String picUrl = document.getString("ImageLink");
                                                        Post post = new Post(description, owner, type, timeAdded, location, picUrl);
                                                        postsLists.add(post);
                                                        postAdapter.notifyDataSetChanged();
                                                }
                                        } else {
                                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                }
                        });
        }

        private void setupBadgeButton( ) {
                TextView badge = findViewById(R.id.badgeImage);
                TextView levelText = findViewById(R.id.LevelText);
                FirebaseManager.getUserData()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                                @Override
                                public void onComplete( @NonNull Task<DocumentSnapshot> task ) {
                                        if ( task.isSuccessful() ) {
                                                DocumentSnapshot document = task.getResult();
                                                if ( document.exists() && document.contains("Level")) {
                                                        int level = Integer.parseInt(document.get("Level").toString( ));
                                                        switch ( level ){
                                                                case 2:
                                                                        badge.setBackground(AppCompatResources.getDrawable(MainActivity.this,R.drawable.level2));
                                                                        break;
                                                                case 3:
                                                                        badge.setBackground(AppCompatResources.getDrawable(MainActivity.this,R.drawable.level3));
                                                                        break;
                                                                default:
                                                                        badge.setBackground(AppCompatResources.getDrawable(MainActivity.this,R.drawable.level1));
                                                                        break;
                                                        }
                                                        levelText.setText("Level "+ level);
                                                }else{
                                                        badge.setBackground(AppCompatResources.getDrawable(MainActivity.this,R.drawable.level1));
                                                        levelText.setText("Level 1");
                                                }
                                        }
                                }
                        });
        }

        private boolean onMenuItemClick( MenuItem menuItem ) {
                int clickedItemId = menuItem.getItemId();

                boolean handledClick = false;
                if ( clickedItemId == R.id.aboutUsMenuItem && !handledClick) {
                        Toast.makeText(this,"To be Added >.<",Toast.LENGTH_LONG).show();
                        handledClick = true;
                }
                if ( clickedItemId == R.id.logoutMenuItem && !handledClick) {
                        FirebaseManager.signOut();
                        Intent intent = new Intent(this,SignUpActivity.class);
                        startActivity(intent);
                        handledClick = true;
                }

                return handledClick;
        }

}