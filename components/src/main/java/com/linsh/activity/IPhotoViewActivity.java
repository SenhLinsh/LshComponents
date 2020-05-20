package com.linsh.activity;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/19
 *    desc   : 大图展示
 * </pre>
 */
public interface IPhotoViewActivity extends IActivity<IPhotoViewActivity.Presenter> {

    interface View extends IActivity.View {
        void setPhotos(List<Object> photos);

        void setItemSelected(int index);
    }

    interface Presenter extends IActivity.Presenter<View> {

        void onItemSelected(int index, Object photo);
    }
}
