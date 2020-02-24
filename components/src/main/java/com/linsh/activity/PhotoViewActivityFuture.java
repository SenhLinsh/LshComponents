package com.linsh.activity;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
public interface PhotoViewActivityFuture extends ActivityFuture {

    PhotoViewActivityFuture setPhotos(String... urls);

    PhotoViewActivityFuture setPhotos(Integer... resources);

    PhotoViewActivityFuture setPhotos(File... files);

    PhotoViewActivityFuture setDisplayItemIndex(int index);

    interface IPhotoViewActivity extends IActivity {

        void onPhotosReceived(List<Object> photos);

        void onItemSelected(int index, Object photo);
    }
}
