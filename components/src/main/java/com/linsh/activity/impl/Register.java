package com.linsh.activity.impl;

import com.linsh.activity.ActivityComponents;
import com.linsh.activity.IManagerItemActivity;
import com.linsh.activity.IPhotoViewActivity;
import com.linsh.activity.ISearchActivity;
import com.linsh.activity.ITextEditActivity;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/23
 *    desc   :
 * </pre>
 */
public class Register {

    public static void init() {
        ActivityComponents.register(IManagerItemActivity.class, ManagerItemActivityImpl.class);
        ActivityComponents.register(IPhotoViewActivity.class, PhotoViewActivityImpl.class);
        ActivityComponents.register(ITextEditActivity.class, TextEditActivityImpl.class);
        ActivityComponents.register(ISearchActivity.class, SearchActivityImpl.class);
    }
}
