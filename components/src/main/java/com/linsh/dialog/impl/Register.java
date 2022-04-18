package com.linsh.dialog.impl;

import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.impl.alert.InputAlertDialogImpl;
import com.linsh.dialog.impl.alert.TextAlertDialogImpl;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/25
 *    desc   :
 * </pre>
 */
public class Register {

    public static void init() {
        DialogComponents.register(
                LshInputDialogImpl.class,
                LshTextDialogImpl.class,
                LshTextLoadingDialogImpl.class,
                KeepScreenOnLoadingDialogImpl.class,
                LshListDialogImpl.class,
                LshSingleChoiceDialogImpl.class,
                TextAlertDialogImpl.class,
                InputAlertDialogImpl.class
        );
    }
}
