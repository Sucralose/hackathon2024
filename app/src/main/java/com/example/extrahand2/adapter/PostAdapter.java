package com.example.extrahand2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrahand2.R;
import com.example.extrahand2.activity.InfoActivity;
import com.example.extrahand2.datamodels.Post;
import com.google.android.material.timepicker.TimeFormat;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{


        Activity activity;
        List<Post> items;

        public PostAdapter( Activity activity, List<Post> items){
                this.activity = activity;
                this.items = items;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PostViewHolder(LayoutInflater.from(activity).inflate(R.layout.view_post,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
                Post post = items.get(position);
                holder.description.setText(post.getDescription());
                Picasso.get().load(post.getPicUrl()).into(holder.imageView);
                holder.location.setText(post.getLocation());

                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                String date = sf.format(post.getTimeAdded().getSeconds()*1000);
                holder.timeAdded.setText("Posted at: " + date);
//                holder.type.setText(post.getType());

                holder.imageView.setOnClickListener(new View.OnClickListener( ) {
                        @Override
                        public void onClick( View view ) {
                                Intent intent = new Intent(activity, InfoActivity.class);
                                String [] postArray = {post.getDescription(), post.getPicUrl(),post.getLocation(), post.getType(), date, post.getOwner()};
                                intent.putExtra("post", postArray);
                                activity.startActivity(intent);
                        }
                });

        }

        @Override
        public int getItemCount() {
                return items.size();
        }

        public class PostViewHolder extends RecyclerView.ViewHolder {
                TextView description,timeAdded,location,type;
                ImageView imageView;

                public PostViewHolder(@NonNull View itemView){
                        super(itemView);
                        description = itemView.findViewById(R.id.tvDescription);
                        imageView = itemView.findViewById(R.id.ivContact);
                        timeAdded = itemView.findViewById(R.id.tvDate);
                        location = itemView.findViewById(R.id.tvLocation);
                }
        }
}
