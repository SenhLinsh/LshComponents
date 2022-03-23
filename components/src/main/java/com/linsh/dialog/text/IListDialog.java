package com.linsh.dialog.text;

import com.linsh.dialog.IDefaultDialog;
import com.linsh.utilseverywhere.interfaces.Convertible;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/28
 *    desc   :
 * </pre>
 */
public interface IListDialog extends IDefaultDialog {

    IListDialog setItems(List<? extends CharSequence> items);

    <T> IListDialog setItems(List<T> items, Convertible<T, CharSequence> convertible);

    IListDialog setItems(CharSequence[] items);

    <T> IListDialog setItems(T[] items, Convertible<T, CharSequence> convertible);

    IListDialog setOnItemClickListener(OnItemClickListener listener);

    IListDialog setOnItemLongClickListener(OnItemClickListener listener);
}
