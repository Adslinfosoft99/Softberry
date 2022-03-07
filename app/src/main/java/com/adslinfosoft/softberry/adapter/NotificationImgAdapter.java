package com.adslinfosoft.softberry.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.view.GravityCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adslinfosoft.softberry.model.GridItem;
import com.adslinfosoft.softberry.R;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shreshtha on 19/09/16.
 */
public class NotificationImgAdapter extends ArrayAdapter<GridItem> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private final Context mContext;
    private final int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();
    private final ImageLoader imageLoader;
    private final boolean isGallery;

    public NotificationImgAdapter(Context mContext, boolean isGallery, int layoutResourceId, ArrayList<GridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(mContext).threadPoolSize(3).threadPriority(1).defaultDisplayImageOptions(new Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).build()).memoryCache(new LRULimitedMemoryCache(GravityCompat.RELATIVE_LAYOUT_DIRECTION)).diskCache(new LimitedAgeDiskCache(new File(StorageUtils.getCacheDirectory(mContext), "icons"), null, 2592000)).build());
        }
        imageLoader = ImageLoader.getInstance();
        this.isGallery = isGallery;
    }

//    public static boolean shouldEnableCacheOnMemory() {
//        if (Build.MODEL.equalsIgnoreCase("GT-N7100") || (Build.MANUFACTURER.equalsIgnoreCase("SAMSUNG") && Build.VERSION.RELEASE.equalsIgnoreCase("4.4.2"))) {
//            return false;
//        }
//        return true;
//    }


    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = row.findViewById(R.id.grid_item_title);
            holder.imageView = row.findViewById(R.id.grid_item_image);
            holder.tvTvGroup = row.findViewById(R.id.tv_img_group);
            holder.tvCount = row.findViewById(R.id.tv_img_count);
            if (isGallery) {
                holder.titleTextView.setVisibility(View.GONE);
            } else {
                row.findViewById(R.id.lout_bottom).setVisibility(View.GONE);
            }
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.setData(position);

        return row;
    }

    private class ViewHolder {
        TextView tvTvGroup;
        TextView titleTextView;
        ImageView imageView;
        TextView tvCount;

        public void setData(int pos) {
            GridItem item = mGridData.get(pos);
            if (isGallery) {
                tvTvGroup.setText(Html.fromHtml(item.getTitle()));
                tvCount.setText(""+item.getImgCount());
            } else {
                titleTextView.setText(Html.fromHtml(item.getTitle()));
            }

            if (item.isPdf())
            {
                imageView.setImageResource(R.drawable.pdffile);
            }
            else {
                imageLoader.loadImage(item.getImage(), new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        imageView.setImageBitmap(loadedImage);
                    }
                });
            }
        }
    }
}
