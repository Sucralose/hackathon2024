package com.example.extrahand2.activity;


import static com.example.extrahand2.utils.Constants.UserConstants.MAX_DISPLAY_NAME_LENGTH;
import static com.example.extrahand2.utils.Constants.UserConstants.MAX_PASSWORD_LENGTH;
import static com.example.extrahand2.utils.Constants.UserConstants.MIN_DISPLAY_NAME_LENGTH;
import static com.example.extrahand2.utils.Constants.UserConstants.MIN_PASSWORD_LENGTH;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.extrahand2.databinding.ActivitySignUpBinding;
import com.example.extrahand2.utils.FirebaseManager;

public class SignUpActivity extends AppCompatActivity {

private ActivitySignUpBinding binding;
private Uri profilePictureImageUri;
//Image Picker handler
private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
	new ActivityResultContracts.StartActivityForResult(),
	result -> {
		if ( result.getData() != null ) {
			profilePictureImageUri = result.getData().getData();
			binding.profileImage.setImageURI(profilePictureImageUri);
			binding.addImageText.setVisibility(View.INVISIBLE);
		}
	}
);

@Override
protected void onCreate ( Bundle savedInstanceState ) {
	super.onCreate(savedInstanceState);
	binding = ActivitySignUpBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());
	setListeners();
}


private void setListeners ( ) {
	binding.signInText.setOnClickListener(view -> startActivity(new Intent(this, SignInActivity.class)));
	binding.signUpButton.setOnClickListener(view -> {
		if ( isValidSignUpDetails() ) signUp();
	});
	binding.layoutImage.setOnClickListener(view -> {
		Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickImageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		//pickImageIntent.setAction(Intent.ACTION_);
		pickImage.launch(pickImageIntent);
	});
}

private void showShortToast ( String message ) {
	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
}



private void signUp ( ) {
	loading(true);

	FirebaseManager.signUp(binding.inputEmailAddress.getText().toString(),
			binding.inputPassword.getText().toString(),
			binding.inputDisplayName.getText().toString(),
			profilePictureImageUri,
			Integer.parseInt(binding.inputAge.getText().toString()),
			binding.inputExtraContactInfo.getText().toString(),
			this);

}

private boolean isValidSignUpDetails ( ) {
	String displayName, emailAddress, password, extraInformation;
	int age;
	displayName = binding.inputDisplayName.getText().toString();
	emailAddress = binding.inputEmailAddress.getText().toString();
	password = binding.inputPassword.getText().toString();
	age = binding.inputAge.getText().toString() == null ? 0 : Integer.parseInt(binding.inputAge.getText().toString());
	extraInformation = binding.inputExtraContactInfo.getText().toString();

	if ( profilePictureImageUri == null ) {
		showShortToast("Please select a profile picture");
		return false;
	}
	if ( displayName.length() < MIN_DISPLAY_NAME_LENGTH || displayName.length() > MAX_DISPLAY_NAME_LENGTH ) {
		showShortToast("Your display name must be between " + MIN_DISPLAY_NAME_LENGTH + " and "
			               + MAX_DISPLAY_NAME_LENGTH + " Characters.");
		return false;
	}
	if ( emailAddress.isEmpty() ) {
		showShortToast("Please enter your email address");
		return false;
	}
	//Use builtin regex to determine if is valid address
	if ( !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() ) {
		showShortToast("Please enter a valid email address");
		return false;
	}
	if ( password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH ) {
		showShortToast("Your password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " Characters.");
		return false;
	}
	if( age == 0){
		showShortToast("Please enter your age");
		return false;
	}
	/*if( extraInformation.isEmpty()){
		showShortToast("Please enter your extra information");
		return false;
	}*/
	return true;

}

private void loading ( Boolean isLoading ) {
	if ( isLoading ) {
		binding.signUpButton.setVisibility(View.INVISIBLE);
		binding.progressBar.setVisibility(View.VISIBLE);
	} else {
		binding.signUpButton.setVisibility(View.VISIBLE);
		binding.progressBar.setVisibility(View.INVISIBLE);
	}

}
}