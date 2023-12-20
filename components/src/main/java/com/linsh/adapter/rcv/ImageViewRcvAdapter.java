package com.linsh.adapter.rcv;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linsh.lshutils.adapter.BaseRcvAdapter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/12/19
 *    desc   :
 * </pre>
 */
public class ImageViewRcvAdapter extends BaseRcvAdapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ImageViewAdapter";
    private List<?> images;

    public ImageViewRcvAdapter(Object... images) {
        this.images = Arrays.asList(images);
    }

    public ImageViewRcvAdapter(List<?> images) {
        this.images = images;
    }

    public void setImages(List<?> images) {
        this.images = images;
    }

    public void setUrls(List<String> images) {
        this.images = images;
    }

    public void setFiles(List<File> images) {
        this.images = images;
    }

    public void setResIds(List<Integer> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new RecyclerView.ViewHolder(imageView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object image = images.get(position);
        if (image instanceof String) {
            Glide.with(holder.itemView.getContext()).load((String) image).into((ImageView) holder.itemView);
        } else if (image instanceof File) {
            Glide.with(holder.itemView.getContext()).load((File) image).into((ImageView) holder.itemView);
        } else if (image instanceof Integer) {
            Glide.with(holder.itemView.getContext()).load((Integer) image).into((ImageView) holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }
}
