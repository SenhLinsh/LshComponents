package com.linsh.dialog.impl;

import com.linsh.dialog.DialogComponents;

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
                LshInputDialogHelperImpl.class,
                LshTextDialogHelperImpl.class,
                LshTextLoadingDialogHelperImpl.class
        );
    }
}
