package com.linsh.activity;

import java.io.File;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
public interface PhotoViewActivityStarter extends ActivityStarter {

    PhotoViewActivityStarter setPhotos(String... urls);

    PhotoViewActivityStarter setPhotos(Integer... resources);

    PhotoViewActivityStarter setPhotos(File... files);

    PhotoViewActivityStarter setDisplayItemIndex(int index);
}
