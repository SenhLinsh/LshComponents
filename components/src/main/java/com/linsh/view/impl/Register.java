package com.linsh.view.impl;

import com.linsh.view.ViewComponents;

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
        ViewComponents.register(
                INormalTextItemViewImpl.class,
                IDefaultButtonItemViewImpl.class,
                DefaultTagFlowLayout.class,
                TagView.class
        );
    }
}
