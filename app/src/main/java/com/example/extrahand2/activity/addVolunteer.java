package com.example.extrahand2.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extrahand2.R;
import com.example.extrahand2.utils.FirebaseManager;

public class addVolunteer extends AppCompatActivity {

        private Uri profilePictureImageUri;
        private ImageButton layoutImage;
        private TextView addImageText;
        private EditText type,location,description;
        private Button postButton;
        private ProgressBar progressBar;

        @SuppressLint( "MissingInflatedId" )
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_add_volunteer);
                layoutImage = (ImageButton) findViewById(R.id.addVolunteerImage);
                type = (EditText) findViewById(R.id.addVolunteerType);
                location = (EditText) findViewById(R.id.addVolunteerLocation);
                description = (EditText) findViewById(R.id.addVolunteerDescription);
                postButton = (Button) findViewById(R.id.uploadPost);
                addImageText = (TextView) findViewById(R.id.addImageText);
                progressBar = (ProgressBar) findViewById(R.id.progressBar);


                layoutImage.setOnClickListener(view -> {
                        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickImageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        pickImage.launch(pickImageIntent);
                });

                postButton.setOnClickListener(view -> {
                        if(profilePictureImageUri == null) {
                                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                                return;
                        }
                        if(type.getText().toString().isEmpty()) {
                                Toast.makeText(this, "Please enter type", Toast.LENGTH_SHORT).show();
                                return;
                        }
                        if(location.getText().toString().isEmpty()) {
                                Toast.makeText(this, "Please enter location", Toast.LENGTH_SHORT).show();
                                return;
                        }
                        if(description.getText().toString().isEmpty()) {
                                Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
                                return;
                        }

                        loading(true);
                        FirebaseManager.createPost(
                                description.getText().toString(),
                                location.getText().toString(),
                                type.getText().toString(),
                                profilePictureImageUri,
                                this
                        );


                });
        }






        //Image Picker handler
        private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                        if ( result.getData() != null ) {
                                profilePictureImageUri =   result.getData().getData();
                                addImageText.setVisibility(View.INVISIBLE);
                                layoutImage.setImageURI(profilePictureImageUri);
                        }
                }
        );


        private void loading ( Boolean isLoading ) {
                if ( isLoading ) {
                        postButton.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                } else {
                        postButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                }

        }
}