package com.linsh.activity;

import com.linsh.base.activity.IntentDelegate;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/04
 *    desc   :
 * </pre>
 */
public interface ActivityFuture {

    IntentDelegate intent();

    void start();
}
