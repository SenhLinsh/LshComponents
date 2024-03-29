package com.linsh.fragment.impl;

import com.linsh.fragment.FragmentComponents;
import com.linsh.fragment.IManagerItemFragment;
import com.linsh.fragment.IPhotoItemFragment;
import com.linsh.fragment.ISettingsFragment;
import com.linsh.fragment.ISimpleToolFragment;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/23
 *    desc   :
 * </pre>
 */
public class _Register {

    public static void init() {
        FragmentComponents.register(ISettingsFragment.class, SettingsFragmentImpl.class);
        FragmentComponents.register(IManagerItemFragment.class, ManagerItemActivityImpl.class);
        FragmentComponents.register(ISimpleToolFragment.class, SimpleToolFragmentImpl.class);
        FragmentComponents.register(IPhotoItemFragment.class, PhotoItemFragmentImpl.class);
    }
}
