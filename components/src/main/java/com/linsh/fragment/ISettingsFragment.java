package com.linsh.fragment;

import com.linsh.base.mvp.Contract;

import java.util.Map;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/06
 *    desc   : 设置页面
 *
 *             可用于：
 *                 1. 编辑配置
 *                 2. 输入模板
 * </pre>
 */
public interface ISettingsFragment extends IFragment<Contract.Presenter> {

    void setProperties(Map<String, String> properties);

    void setPropertiesGroups(Map<String, Map<String, String>> propertiesGroups);

    Map<String, String> getProperties();

    Map<String, Map<String, String>> getPropertiesGroups();
}