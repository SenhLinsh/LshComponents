package com.linsh.fragment.impl;

import com.linsh.fragment.FragmentComponents;
import com.linsh.fragment.ISettingsFragment;

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
        FragmentComponents.register(ISettingsFragment.class, SettingsFragmentImpl.class);
    }
}
