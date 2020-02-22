package com.linsh.activity.impl;

import com.linsh.activity.ActivityComponents;

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
        ActivityComponents.register(
                ManagerItemActivityFutureImpl.class,
                PhotoViewActivityFutureImpl.class
        );
    }
}
