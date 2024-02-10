package com.example.extrahand2.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Constants {
public static class UserConstants {
	public static final int MIN_PASSWORD_LENGTH = 6;
	public static final int MAX_PASSWORD_LENGTH = 25;
	public static final int MAX_DISPLAY_NAME_LENGTH = 25;
	public static final int MIN_DISPLAY_NAME_LENGTH = 2;

}

public static class FirebaseConstants {

	public static final String COLLECTION_USER = "Users";
	public static final String KEY_USER_DISPLAY_NAME = "Name";
	public static final String KEY_USER_EMAIL_ADDRESS = "Email";
	public static final String KEY_USER_AGE = "Age";

	public static final String KEY_USER_CONTACT_INFO = "Contact";
	public static final String KEY_USER_PROFILE_IMAGE = "ProfileImage";

	public static final String COLLECTION_POSTS = "Posts";
	public static final String KEY_POST_LOCATION = "Location";
	public static final String KEY_POST_TITLE = "Name";
	public static final String KEY_POST_IMAGE = "ImageLink";
	public static final String KEY_POST_OWNER = "Owner";

	public static final String KEY_POST_TIME_ADDED = "TimeAdded";
	public static final String KEY_POST_TYPE_ADDED = "Type";
	public static final String KEY_POST_UPVOTES = "Upvotes";

	public static final String KEY_POST_REVIEWS = "Reviews";
	public static final String KEY_POST_REVIEW_POSITIVE = "Positive";

	public static final String KEY_POST_REVIEW_TEXT = "Reply";

	public static final String KEY_POST_DESCRIPTION = "Description";

	public static final String KEY_USER_PROFILE_IMAGE_STORAGE_REFERENCE = "UserProfiles";
	public static final String KEY_POST_IMAGE_STORAGE_REFERENCE = "PostImages";

	}

public static class ApplicationConstants {
	public static final String APPLICATION_NAME = "ExtraHand";
}

}
