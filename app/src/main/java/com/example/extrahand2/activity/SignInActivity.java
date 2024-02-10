package com.example.extrahand2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.extrahand2.databinding.ActivitySignInBinding;
;
import com.example.extrahand2.utils.FirebaseManager;

public class SignInActivity extends AppCompatActivity {

	private ActivitySignInBinding binding;

@Override
protected void onCreate ( Bundle savedInstanceState ) {
	super.onCreate(savedInstanceState);

	binding = ActivitySignInBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());
	setListeners();
}


private void setListeners ( ) {
	binding.createNewAccountText.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
	binding.signInButton.setOnClickListener(view -> {
		if ( isValidSignInData() )
			signIn();
	});
	binding.forgotPasswordText.setOnClickListener(view -> {
		if ( isValidEmail() )
			sendPasswordReset();
	});
}

private void sendPasswordReset ( ) {
	ChangeUserInterfaceLoadingAppearance(true);
	FirebaseManager.sendPasswordReset(binding.inputEmailAddress.getText().toString())
		.addOnCompleteListener(task -> {
			ChangeUserInterfaceLoadingAppearance(false);
			String msg = task.isSuccessful() ? "Please check your email address for the password reset" : task.getResult().toString();
			showShortToast(msg);
		});
}


private void signIn ( ) {
	ChangeUserInterfaceLoadingAppearance(true);
	FirebaseManager.signIn(binding.inputEmailAddress.getText().toString(), binding.inputPassword.getText().toString())
		.addOnCompleteListener(task -> {
			ChangeUserInterfaceLoadingAppearance(false);
			if ( task.isSuccessful() ) {
				//FirebaseManager.updateFCMToken();
				Intent mainActivityIntent = new Intent(SignInActivity.this, MainActivity.class);
				startActivity(mainActivityIntent);
				finish();
			} else
				showShortToast(task.getException().getMessage());
		});
}

private void ChangeUserInterfaceLoadingAppearance ( Boolean isLoading ) {
	if ( isLoading ) {
		binding.signInButton.setVisibility(View.INVISIBLE);
		binding.progressBar.setVisibility(View.VISIBLE);
	} else {
		binding.signInButton.setVisibility(View.VISIBLE);
		binding.progressBar.setVisibility(View.INVISIBLE);
	}

}

private boolean isValidSignInData ( ) {
	String password = binding.inputPassword.getText().toString();
	if ( !isValidEmail() )
		return false;
	if ( password.isEmpty() ) {
		showShortToast("Please enter your password");
		return false;
	}
	return true;
}

private boolean isValidEmail ( ) {
	String emailAddress = binding.inputEmailAddress.getText().toString();
	if ( emailAddress.isEmpty() ) {
		showShortToast("Please enter your email address");
		return false;
	}
	//Use builtin regex to determine if is valid address
	if ( !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() ) {
		showShortToast("Please enter a valid email address");
		return false;
	}
	return true;
}


private void showShortToast ( String message ) {
	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
}

}