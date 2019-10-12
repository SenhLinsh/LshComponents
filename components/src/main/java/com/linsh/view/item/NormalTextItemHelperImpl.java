package com.linsh.view.item;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linsh.utilseverywhere.ResourceUtils;
import com.linsh.views.R;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/10
 *    desc   :
 * </pre>
 */
public class NormalTextItemHelperImpl implements TextItemHelper {

    private final TextView textView;

    public NormalTextItemHelperImpl(Context context) {
        textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = ResourceUtils.getDimens(R.dimen.lsh_item_padding);
        textView.setPadding(padding, padding, padding, padding);
        int textSize = ResourceUtils.getDimens(R.dimen.lsh_item_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Override
    public void setText(CharSequence text) {
        textView.setText(text);
    }

    @NonNull
    @Override
    public View getView() {
        return textView;
    }
}
