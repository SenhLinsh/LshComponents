package com.linsh.dialog.impl;

import android.content.Context;

import androidx.annotation.Nullable;

import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.text.IListDialog;
import com.linsh.utilseverywhere.ListUtils;
import com.linsh.utilseverywhere.interfaces.Convertible;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/28
 *    desc   :
 * </pre>
 */
public class LshListDialogImpl extends LshDialogImpl implements IListDialog {

    private LshDialog.ListDialogBuilder builder;

    public LshListDialogImpl(Context context) {
        this(new LshDialog(context).buildList());
    }

    private LshListDialogImpl(LshDialog.ListDialogBuilder builder) {
        super(builder);
        this.builder = builder;
    }

    @Override
    public IListDialog setItems(List<? extends CharSequence> tags) {
        builder.setList(ListUtils.convertList(tags, new Convertible<CharSequence, String>() {
            @Override
            public String convert(CharSequence o) {
                return o.toString();
            }
        }));
        return this;
    }

    @Override
    public <T> IListDialog setItems(List<T> tags, final Convertible<T, CharSequence> convertible) {
        builder.setList(ListUtils.convertList(tags, new Convertible<T, String>() {
            @Override
            public String convert(T o) {
                if (convertible != null) {
                    return convertible.convert(o).toString();
                }
                return o.toString();
            }
        }));
        return this;
    }

    @Override
    public IListDialog setItems(CharSequence[] items) {
        return setItems(Arrays.asList(items));
    }

    @Override
    public <T> IListDialog setItems(T[] items, Convertible<T, CharSequence> convertible) {
        return setItems(Arrays.asList(items), convertible);
    }

    @Override
    public IListDialog setOnItemClickListener(@Nullable OnItemClickListener listener) {
        if (listener != null) {
            builder.setOnItemClickListener(new LshDialog.OnItemClickListener() {
                @Override
                public void onClick(LshDialog dialog, int index) {
                    listener.onItemClick(LshListDialogImpl.this, index);
                }
            });
        }
        return this;
    }

    @Override
    public IListDialog setOnItemLongClickListener(@Nullable OnItemClickListener listener) {
        if (listener != null) {
            builder.setOnItemLongClickListener(new LshDialog.OnItemClickListener() {
                @Override
                public void onClick(LshDialog dialog, int index) {
                    listener.onItemClick(LshListDialogImpl.this, index);
                }
            });
        }
        return this;
    }
}
