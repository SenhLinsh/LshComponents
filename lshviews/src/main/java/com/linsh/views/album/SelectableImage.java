package com.linsh.views.album;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 接口: 可选择的图片
 * </pre>
 */
public interface SelectableImage extends Image {

    void setSelected(boolean isSelected);

    boolean isSelected();
}
