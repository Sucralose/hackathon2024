package com.example.extrahand2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extrahand2.R;
import com.example.extrahand2.utils.FirebaseManager;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

        Button btnEmail, btnJoin;
        ImageView iv;
        public TextView tvDescription, tvDate, tvOwner, tvType, tvPicUrl, tvLocation;
        public String description, date, owner, type, picUrl, location;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_info);

                Intent in = getIntent();
                if (in != null && in.getExtras() != null) {
                        Bundle xtras = in.getExtras();
                        String [] postArray = xtras.getStringArray("post");

                        description = postArray[0];
                        picUrl = postArray[1];
                        location = postArray[2];
                        type = postArray[3];
                        date = postArray[4];
                        owner = postArray[5];

                        tvDescription = findViewById(R.id.tvDescription);
                        tvDate = findViewById(R.id.tvDate);
                        btnEmail = findViewById(R.id.tvEmail);
                        tvType = findViewById(R.id.tvType);
                        tvLocation = findViewById(R.id.tvLocation);
                        //btnJoin = findViewById(R.id.btnJoin);
                        iv = findViewById(R.id.imageView);


                        tvDescription.setText(description);
                        tvDate.setText("Posted At: "+date);
                        btnEmail.setText("Contact At: "+owner);
                        tvType.setText(type);
                        tvLocation.setText("Is held At:"+ location);
                        Picasso.get().load(picUrl).into(iv);
                }
        }

        public void onClickEmail(View v){
                FirebaseManager.updateUserLevel();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + owner));


                int i = 1500;
                Handler handler = new Handler( );
                handler.postDelayed(new Runnable( ) {
                        @Override
                        public void run( ) {
                                //Toast.makeText(this, owner, Toast.LENGTH_LONG).show();
                                startActivity(Intent.createChooser(emailIntent, "Contact user"));
                        }
                },i);

        }

       /* public void onClickJoin(View v){
                //TODO: add points to rank
        }*/
}