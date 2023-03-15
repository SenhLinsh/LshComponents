package com.linsh.activity.impl;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.linsh.activity.ITextEditActivity;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.views.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/20
 *    desc   :
 * </pre>
 */
class TextEditActivityImpl extends IActivityViewImpl<ITextEditActivity.Presenter>
        implements ITextEditActivity.View, ActivitySubscribe.OnCreate, ActivitySubscribe.OnCreateOptionsMenu, ActivitySubscribe.OnOptionsItemSelected {

    private EditText editText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setContentView(R.layout.lsh_components_activity_text_edit);
        editText = getActivity().findViewById(R.id.et_edit);
    }

    @Override
    public void setText(String content) {
        editText.setText(content);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("保存");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals("保存")) {
            getPresenter().saveText(editText.getText().toString());
            return true;
        }
        return false;
    }
}
