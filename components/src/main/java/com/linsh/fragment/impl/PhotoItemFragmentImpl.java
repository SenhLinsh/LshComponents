package com.linsh.fragment.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linsh.fragment.BaseComponentFragment;
import com.linsh.fragment.IPhotoItemFragment;
import com.linsh.lshutils.adapter.BaseRcvAdapterEx;
import com.linsh.lshutils.viewholder.ViewHolderEx;
import com.linsh.utilseverywhere.ScreenUtils;
import com.linsh.views.R;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/05/04
 *    desc   :
 * </pre>
 */
public class PhotoItemFragmentImpl extends BaseComponentFragment<IPhotoItemFragment.Presenter> implements IPhotoItemFragment {

    private PhotoFlowAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(inflater.getContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new GridLayoutManager(inflater.getContext(), 4));
        recyclerView.setAdapter(adapter = new PhotoFlowAdapter());
        adapter.setOnItemClickListener((holder, position) -> {
            getPresenter().onPhotoClick(adapter.getData().get(position), position);
        });
        adapter.setOnItemLongClickListener((holder, position) -> {
            getPresenter().onPhotoLongClick(adapter.getData().get(position), position);
            return true;
        });
        return recyclerView;
    }

    @Override
    public void setPhotos(List<Object> photos) {
        if (adapter != null) {
            adapter.setData(photos);
        }
    }

    static class PhotoFlowAdapter extends BaseRcvAdapterEx<Object, ViewHolder> {
        @Override
        protected ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow, parent, false));
        }

        @Override
        protected void onBindItemViewHolder(ViewHolder holder, Object item, int position) {
            int typeRes = getResByType(item);
            if (typeRes > 0) {
                holder.ivType.setVisibility(View.VISIBLE);
                holder.ivType.setImageResource(typeRes);
            } else {
                holder.ivType.setVisibility(View.GONE);
            }
            Glide.with(holder.ivImage)
                    .load(item)
                    .into(holder.ivImage);
        }

        private int getResByType(Object item) {
            String filename = null;
            if (item instanceof String) {
                filename = (String) item;
            } else if (item instanceof File) {
                filename = ((File) item).getName();
            }
            if (filename != null) {
                if (filename.endsWith(".gif"))
                    return R.drawable.ic_gif;
                if (filename.endsWith(".mp4") || filename.endsWith(".flv"))
                    return R.drawable.ic_video;
            }
            return -1;
        }
    }

    static class ViewHolder extends ViewHolderEx {

        private final ImageView ivImage;
        private final ImageView ivType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_flow_image);
            ivType = itemView.findViewById(R.id.iv_item_flow_type);
            int screenWidth = ScreenUtils.getScreenWidth() / 4;
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(screenWidth, screenWidth);
            } else {
                layoutParams.height = screenWidth;
                layoutParams.width = screenWidth;
            }
            itemView.setLayoutParams(layoutParams);
        }
    }
}
