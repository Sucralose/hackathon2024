package com.example.extrahand2.utils;


//import static com.example.extrahand2.utils.Constants.FirebaseConstants.COLLECTION_GROUP_DETAILS;
import static androidx.core.content.ContextCompat.startActivity;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.COLLECTION_POSTS;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.COLLECTION_USER;
//import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_GROUP_DETAILS_ID;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_DESCRIPTION;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_IMAGE;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_IMAGE_STORAGE_REFERENCE;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_LOCATION;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_OWNER;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_REVIEWS;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_TIME_ADDED;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_POST_TYPE_ADDED;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_AGE;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_CONTACT_INFO;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_DISPLAY_NAME;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_EMAIL_ADDRESS;
//import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_FCM_TOKEN;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_PROFILE_IMAGE;
import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_PROFILE_IMAGE_STORAGE_REFERENCE;
//import static com.example.extrahand2.utils.Constants.FirebaseConstants.KEY_USER_PROFILE_IMAGE_STORAGE_REFERENCE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.extrahand2.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * FirebaseManager
 * This class is used to directly interact with Firebase.
 *
 * @version 1.0
 */
public class FirebaseManager {


	public static void signOut ( ) {
	FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
	firebaseAuth.signOut();
	FirebaseMessaging.getInstance().deleteToken();
}

public static boolean isSignedIn ( ) {
	return FirebaseAuth.getInstance().getCurrentUser() != null;
}

public static String getUserDisplayName ( ) {
	return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
}

public static String getUserEmail ( ) {
	return FirebaseAuth.getInstance().getCurrentUser().getEmail();
}


public static String getCurrentUserUid ( ) {
	return FirebaseAuth.getInstance().getCurrentUser().getEmail();
}

public static String getCurrentUserDisplayName ( ) {
	return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
}

public static Task<DocumentSnapshot> getCurrentUserData ( ) {
	return FirebaseFirestore.getInstance().collection(COLLECTION_USER).document(getCurrentUserUid()).get();
}


public static Task<AuthResult> signUp ( String email, String password, String displayName, Uri profilePictureImageUri, int age, String contactInfo, Context context ) {
	return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
		       .addOnSuccessListener(authResult -> {
			       addToUserFirestore(email, displayName, profilePictureImageUri,age,contactInfo , context);
		       });

}

public static Task<AuthResult> signIn ( String email, String password ) {
	return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
}

public static Task<Void> sendPasswordReset ( String email ) {
	return FirebaseAuth.getInstance().sendPasswordResetEmail(email);
}

public static void createPost(String description, String location, String type, Uri imageUri, Context context) {
		HashMap<String,Object> post = new HashMap<>(  );
		ArrayList<Map<String,Object>> replies = new ArrayList<>();

		String postID = UUID.randomUUID().toString();

		post.put(KEY_POST_DESCRIPTION, description);
		post.put(KEY_POST_LOCATION, location);
		post.put(KEY_POST_REVIEWS, replies);
		post.put(KEY_POST_TYPE_ADDED, type);
		post.put(KEY_POST_TIME_ADDED, Timestamp.now());
		post.put(KEY_POST_OWNER,getCurrentUserUid());
		post.put(KEY_POST_IMAGE, "");

		FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).document(postID).set(post)
				.addOnCompleteListener(new OnCompleteListener<Void>( ) {
					@Override
							       public void onComplete( @NonNull Task<Void> task ) {
								if(task.isSuccessful())
									uploadPostImage(imageUri,postID,context);
							       }
						       }
				);

}

	private static void uploadPostImage( Uri imageUri, String postID ,Context context) {
		StorageReference storageReference = FirebaseStorage.getInstance().getReference(KEY_POST_IMAGE_STORAGE_REFERENCE + "/" + postID + ".jpg");
		storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>( ) {
			@Override
			public void onComplete( @NonNull Task<UploadTask.TaskSnapshot> task ) {
				if( task.isSuccessful( ) ){
					task.getResult().getStorage().getDownloadUrl()
						.addOnCompleteListener(uriTask -> {

							if ( uriTask.isSuccessful() ) {
								saveInPostDetailsProfile(uriTask.getResult().toString(), postID, context);
							}
						});
				}
			}

		});
	}

	private static void saveInPostDetailsProfile( String downloadURL , String postID , Context context ) {
		HashMap<String,Object> userMap = new HashMap<>();
		userMap.put(KEY_POST_IMAGE, downloadURL);
		FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).document(postID)
			.update(userMap).addOnCompleteListener(
				new OnCompleteListener<Void>( ) {
					@Override
					public void onComplete( @NonNull Task<Void> task ) {
						if(task.isSuccessful()){
							Intent intent = new Intent( context, MainActivity.class );
							startActivity(context, intent, null );
						}
					}
				}
			);
	}

	private static void addToUserFirestore ( String email, String displayName, Uri profilePictureImageUri, int age, String contactInfo , Context context) {
	HashMap<String,Object> user = new HashMap<>();

		user.put("Level",1);
		user.put(KEY_USER_DISPLAY_NAME,displayName);
		user.put(KEY_USER_EMAIL_ADDRESS,email);
		user.put(KEY_USER_CONTACT_INFO, contactInfo);
		user.put(KEY_USER_AGE, age);
		user.put(KEY_USER_PROFILE_IMAGE, "");

		FirebaseFirestore.getInstance().collection(COLLECTION_USER).document(email)
			.set(user)
			.addOnSuccessListener(documentReference -> {
				Log.d("FB_TAG", "Added user to firebase");
				uploadUserProfileImage(profilePictureImageUri, context);
			})
			.addOnFailureListener(e -> {
				Log.e("FB_TAG", e.getMessage());
			});
}
/*
public static void uploadPostImage(Uri imageUri, String postID) {
	StorageReference storageReference = FirebaseStorage.getInstance().getReference(KEY_POST_IMAGE_STORAGE_REFERENCE + "/" + postID + ".jpg");
}*/

public static void uploadUserProfileImage ( Uri profilePictureImageUri , Context context) {
	StorageReference storageReference = FirebaseStorage.getInstance().getReference(KEY_USER_PROFILE_IMAGE_STORAGE_REFERENCE + "/" + getCurrentUserUid() + ".jpg");

	storageReference.putFile(profilePictureImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
		@Override
		public void onComplete ( @NonNull Task<UploadTask.TaskSnapshot> task ) {
			if ( task.isSuccessful() ) {
				task.getResult().getStorage().getDownloadUrl()
					.addOnCompleteListener(uriTask -> {
						if ( uriTask.isSuccessful() ) {
							saveImageInUserProfile(uriTask.getResult().toString(), context);
						}
					});
			}
		}
	});
}

private static void saveImageInUserProfile ( String downloadURL , Context context) {

	HashMap<String,Object> userMap = new HashMap<>();
	userMap.put(KEY_USER_PROFILE_IMAGE, downloadURL);
	FirebaseFirestore.getInstance().collection(COLLECTION_USER).document(getCurrentUserUid())
		.update(userMap)
		.addOnCompleteListener(new OnCompleteListener<Void>( ) {
			@Override
			public void onComplete( @NonNull Task<Void> task ) {
				if(task.isSuccessful()){
					Intent intent = new Intent( context, MainActivity.class );
					startActivity(context, intent, null );
				}
			}
		});

}


/*
public static ConversationRecyclerViewAdapter getConversationAdapter ( Context context, String chatID ) {
	Query query = FirebaseFirestore.getInstance().collection(COLLECTION_GROUP_MESSAGES)
		              .document(chatID).collection(SUB_COLLECTION_MESSAGES)
		              .orderBy(KEY_MESSAGE_DATE, Query.Direction.DESCENDING)
		              .limit(50);


	FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
		                                            .setQuery(query, Message.class).build();

	return new ConversationRecyclerViewAdapter(options, context);
}*/


public static Task<DocumentSnapshot> getUserData ( String memberID ) {
	return FirebaseFirestore.getInstance().collection(COLLECTION_USER).document(memberID).get();
}

	public static Task<DocumentSnapshot> getUserData( ) {
		return FirebaseFirestore.getInstance().collection(COLLECTION_USER).document(getCurrentUserUid()).get();

	}

	public static void updateUserLevel(){
		getUserData().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
			@Override
			public void onComplete( @NonNull Task<DocumentSnapshot> task ) {
				if ( task.isSuccessful() ) {
					if(task.getResult().contains("Level")){
						int level = task.getResult().getLong("Level").intValue();
						level++;
						if(level > 3)
							level = 1;
						FirebaseFirestore.getInstance().collection(COLLECTION_USER).document(getCurrentUserUid()).update("Level", level);
					}
				}
			}
		});
	}
}
