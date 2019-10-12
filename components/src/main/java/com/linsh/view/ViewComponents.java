package com.linsh.view;

import android.content.Context;

import com.linsh.view.item.NormalTextItemHelperImpl;
import com.linsh.view.item.TextItemHelper;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/11
 *    desc   :
 * </pre>
 */
public class ViewComponents {

    @NonNull
    public static <T extends ViewHelper> T create(final Context context, Class<T> viewHelper) {
        if (viewHelper == TextItemHelper.class) {
            return (T) new NormalTextItemHelperImpl(context);
        }
        return null;
    }
}
