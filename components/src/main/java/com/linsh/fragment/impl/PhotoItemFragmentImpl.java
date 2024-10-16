package com.linsh.fragment.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linsh.components.R;
import com.linsh.fragment.BaseComponentFragment;
import com.linsh.fragment.IPhotoItemFragment;
import com.linsh.lshutils.adapter.BaseRcvAdapterEx;
import com.linsh.utilseverywhere.ScreenUtils;
import com.linsh.utilseverywhere.interfaces.Convertible;

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
    public void setPhotos(List<?> photos) {
        if (adapter != null) {
            adapter.setData(photos);
        }
    }

    @Override
    public <T> void setPhotos(List<T> photos, Convertible<T, ?> photoConverter) {
        if (adapter != null) {
            adapter.setPhotoConverter(photoConverter);
            adapter.setData(photos);
        }
    }

    @Override
    public <T> void setPhotos(List<T> photos, Convertible<T, ?> photoConverter, Convertible<T, CharSequence> nameConverter) {
        if (adapter != null) {
            adapter.setPhotoConverter(photoConverter);
            adapter.setNameConverter(nameConverter);
            adapter.setData(photos);
        }
    }

    static class PhotoFlowAdapter extends BaseRcvAdapterEx<Object, ViewHolder> {
        private Convertible photoConverter;
        private Convertible nameConverter;

        @Override
        protected ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.components_item_flow, parent, false));
        }

        public void setPhotoConverter(Convertible<?, ?> photoConverter) {
            this.photoConverter = photoConverter;
        }

        public void setNameConverter(Convertible<?, CharSequence> nameConverter) {
            this.nameConverter = nameConverter;
        }

        @Override
        protected void onBindItemViewHolder(ViewHolder holder, Object item, int position) {
            // 设置名称
            CharSequence name = nameConverter == null ? null : (CharSequence) nameConverter.convert(item);
            // 适配类型
            if (photoConverter != null) {
                item = photoConverter.convert(item);
            }
            if (name != null) {
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvName.setText(name);
            } else {
                holder.tvName.setVisibility(View.GONE);
            }
            // 显示特殊文件类型图标
            int typeRes = getResByType(item);
            if (typeRes > 0) {
                holder.ivType.setVisibility(View.VISIBLE);
                holder.ivType.setImageResource(typeRes);
            } else {
                holder.ivType.setVisibility(View.GONE);
            }
            // 加载图片
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
                if (filename.endsWith(".mp4") || filename.endsWith(".flv")
                        || filename.endsWith(".avi") || filename.endsWith(".mov"))
                    return R.drawable.ic_video;
            }
            return -1;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivImage;
        private final TextView tvName;
        private final ImageView ivType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_flow_image);
            tvName = itemView.findViewById(R.id.tv_item_flow_name);
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
