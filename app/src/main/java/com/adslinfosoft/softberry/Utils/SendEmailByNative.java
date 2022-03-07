package com.adslinfosoft.softberry.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SendEmailByNative {
	String email;
	String subject;
	String body;
	Uri uri;

	public SendEmailByNative(Context context, String email, String subject,
							 String body, Uri uri) {
		this.email = email;
		this.subject = subject;
		this.body = body;
		this.uri = uri;
	}

	public SendEmailByNative(Context context, String email, String subject,
							 String body) {
		this.email = email;
		this.subject = subject;
		this.body = body;
	}

	public Intent sendMailIntent() {
		final Intent emailIntent = new Intent(
				Intent.ACTION_SEND);

		// Explicitly only use Gmail to send
		emailIntent.setClassName("com.google.android.gm",
				"com.google.android.gm.ComposeActivityGmail");

		emailIntent.setType("plain/text");

		// Add the recipients
		emailIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "support@datatracksystem.net" });

		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

		emailIntent.putExtra(Intent.EXTRA_TEXT, body);

		return emailIntent;
	}

}
