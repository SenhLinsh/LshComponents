package com.linsh.dialog.loading;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/04
 *    desc   :
 * </pre>
 */
public interface IKeepScreenOnLoadingDialog extends IProgressLoadingDialog {
    IKeepScreenOnLoadingDialog keepScreenOn(boolean enable);
}
