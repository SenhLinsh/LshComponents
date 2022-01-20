package com.linsh.dialog.impl;

import android.content.Context;

import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.text.ISingleChoiceDialog;
import com.linsh.utilseverywhere.ListUtils;
import com.linsh.utilseverywhere.interfaces.Convertible;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/28
 *    desc   :
 * </pre>
 */
public class LshSingleChoiceDialogImpl extends LshDialogImpl
        implements ISingleChoiceDialog, LshDialog.OnItemClickListener {

    private final LshDialog.ListDialogBuilder builder;
    private final List<CharSequence> items = new ArrayList<>();
    private final List<OnItemClickListener> listeners = new ArrayList<>();

    public LshSingleChoiceDialogImpl(Context context) {
        this(new LshDialog(context).buildList());
        builder.setOnItemClickListener(this);
    }

    private LshSingleChoiceDialogImpl(LshDialog.ListDialogBuilder builder) {
        super(builder);
        this.builder = builder;
    }

    @Override
    public ISingleChoiceDialog addItem(CharSequence item, OnItemClickListener listener) {
        items.add(item);
        listeners.add(listener);
        builder.setList(ListUtils.convertList(items, new Convertible<CharSequence, String>() {
            @Override
            public String convert(CharSequence from) {
                return from.toString();
            }
        }));
        return this;
    }

    @Override
    public void onClick(LshDialog dialog, int index) {
        dialog.dismiss();
        OnItemClickListener listener = listeners.get(index);
        if (listener != null) {
            listener.onItemClick(this, index);
        }
    }
}
