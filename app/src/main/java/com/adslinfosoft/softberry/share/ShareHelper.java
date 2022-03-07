package com.adslinfosoft.softberry.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShareHelper implements AdapterView.OnItemClickListener {
	private static final String TAG = ShareHelper.class.getSimpleName();

	private final Activity mContext;
	private Dialog mDialog;
	private LayoutInflater mInflater;
	private GridView mGrid;

	private ShareIntentAdapter mAdapter;
	private int mMaxColumns;

	private List<ResolveInfo> plainTextActivities;
	private Set<String> htmlActivitiesPackages;

	private final String subject;
	private final String textbody;
	private final CharSequence htmlbody;
	private final String twitterBody;
	private final String facebookBody;

	public ShareHelper(Activity context, String subject, String textbody,
					   CharSequence htmlbody, String twitterBody, String facebookBody) {
		this.mContext = context;

		this.subject = subject;
		this.textbody = textbody;
		this.htmlbody = htmlbody;
		this.twitterBody = twitterBody;
		this.facebookBody = facebookBody;
	}

	public void share() {

		this.mInflater = LayoutInflater.from(mContext);

		final Intent sendIntent = new Intent(Intent.ACTION_SEND);

		sendIntent.setType("text/plain");
		plainTextActivities = mContext.getPackageManager()
				.queryIntentActivities(sendIntent, 0);

		if (plainTextActivities.size() > 0) {
			htmlActivitiesPackages = new HashSet<String>();
			sendIntent.setType("text/html");
			final List<ResolveInfo> htmlActivities = mContext
					.getPackageManager().queryIntentActivities(sendIntent, 0);
			for (ResolveInfo resolveInfo : htmlActivities) {
				htmlActivitiesPackages
						.add(resolveInfo.activityInfo.packageName);
			}

			mAdapter = new ShareIntentAdapter();

			final View chooserView = mInflater.inflate(
					R.layout.dialog_share_us_chooser, null);
			mGrid = chooserView.findViewById(R.id.resolver_grid);
			mGrid.setAdapter(mAdapter);
			mGrid.setOnItemClickListener(this);

			mMaxColumns = 2;
			mGrid.setNumColumns(Math.min(plainTextActivities.size(),
					mMaxColumns));

			AlertDialog.Builder builder;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				builder = new AlertDialog.Builder(mContext,
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			} else {
				builder = new AlertDialog.Builder(mContext);
			}
			builder.setTitle("Share as app");
			builder.setView(chooserView);

			mDialog = builder.create();
			mDialog.show();
		} else {
			Toast.makeText(mContext,
					"No social apps installed to share ChurchLink!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ResolveInfo info = mAdapter.getItem(position);

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setClassName(info.activityInfo.packageName,
				info.activityInfo.name);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TITLE, subject);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);

		if (info.activityInfo.packageName.contains("facebook")) {
			intent.putExtra(Intent.EXTRA_TEXT, facebookBody);
			intent.putExtra(Intent.EXTRA_STREAM,
					R.mipmap.softberry_icon);

		} else if (info.activityInfo.packageName.contains("twitter")) {
			intent.putExtra(Intent.EXTRA_TEXT, twitterBody);
		} else if (htmlActivitiesPackages
				.contains(info.activityInfo.packageName)) {
			intent.putExtra(Intent.EXTRA_TEXT, htmlbody);
		} else {
			intent.putExtra(Intent.EXTRA_TEXT, textbody);
		}
		Log.d(TAG, info.activityInfo.packageName);
		mContext.startActivity(intent);

		mDialog.dismiss();
	}

	public class ShareIntentAdapter extends BaseAdapter {

		public ShareIntentAdapter() {
			super();
		}

		@Override
		public int getCount() {
			return plainTextActivities != null ? plainTextActivities.size() : 0;
		}

		@Override
		public ResolveInfo getItem(int position) {
			return plainTextActivities.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = mInflater.inflate(R.layout.griditem_share_us, parent,
						false);
			} else {
				view = convertView;
			}
			bindView(view, plainTextActivities.get(position));
			return view;
		}

		private final void bindView(View view, ResolveInfo info) {
			TextView text = view.findViewById(android.R.id.text1);
			ImageView icon = view.findViewById(android.R.id.icon);

			text.setText(info.activityInfo.applicationInfo.loadLabel(
					mContext.getPackageManager()).toString());
			icon.setImageDrawable(info.activityInfo.applicationInfo
					.loadIcon(mContext.getPackageManager()));
		}
	}
}