package com.linsh.fragment;

import com.linsh.base.mvp.Contract;
import com.linsh.utilseverywhere.interfaces.Convertible;

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

    <T> void setPhotos(List<T> photos, Convertible<T, ?> photoConverter);

    <T> void setPhotos(List<T> photos, Convertible<T, ?> photoConverter, Convertible<T, CharSequence> nameConverter);

    interface Presenter extends Contract.Presenter {

        void onPhotoClick(Object item, int position);

        void onPhotoLongClick(Object item, int position);
    }
}