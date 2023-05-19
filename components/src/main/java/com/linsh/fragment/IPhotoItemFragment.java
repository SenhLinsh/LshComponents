package com.linsh.fragment;

import com.linsh.base.mvp.Contract;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/05/04
 *    desc   :
 * </pre>
 */
public interface IPhotoItemFragment extends IFragment<IPhotoItemFragment.Presenter> {

    void setPhotos(List<?> photos);

    interface Presenter extends Contract.Presenter {

        void onPhotoClick(Object item, int position);

        void onPhotoLongClick(Object item, int position);
    }
}