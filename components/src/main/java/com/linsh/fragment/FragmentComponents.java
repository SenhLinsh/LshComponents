package com.linsh.fragment;

import com.linsh.base.activity.IntentDelegate;
import com.linsh.base.mvp.Contract;
import com.linsh.fragment.impl.Register;
import com.linsh.utilseverywhere.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/06
 *    desc   : Fragment 组件
 *
 *             经过 ActivityComponents 的尝试，我们发现该模式存在一些易用性的问题：
 *             1. 数据传递必须使用 Intent 模式，增加额外的工作量
 *             2. 页面形式固定，无法基于原有的模式进行扩展或增强（ActivitySubscribe 过于麻烦）
 *
 *             经过研究发现，Fragment 组件模式可以恰好解决以上两个问题。
 * </pre>
 */
public class FragmentComponents {

    private static final Map<Class<? extends IFragment<?>>, Class<? extends IFragment>> IMPLEMENTS = new HashMap<>();

    static {
        Register.init();
    }

    /**
     * 创建 IFragment
     */
    public static <T extends Contract.Presenter> IFragment<T> create(Class<? extends IFragment<T>> iFragment) {
        if (!iFragment.isInterface())
            throw new IllegalArgumentException("iFragment must be interface");
        Class<? extends IFragment> fragmentImplClass = IMPLEMENTS.get(iFragment);
        if (fragmentImplClass == null)
            throw new IllegalStateException("no implement of IFragment: " + iFragment.getName());
        try {
            return (IFragment<T>) ClassUtils.newInstance(fragmentImplClass, true);
        } catch (Exception e) {
            throw new RuntimeException("new instance for " + iFragment.getName() + " failed", e);
        }
    }

    /**
     * 注册 IActivity
     *
     * @param iFragment     注册所需声明的 IFragment
     * @param iFragmentImpl IFragment 页面构建所需实现类
     */
    public static void register(Class<? extends IFragment<?>> iFragment, Class<? extends IFragment<?>> iFragmentImpl) {
        if (!iFragment.isInterface())
            throw new IllegalArgumentException("iFragment must be interface");
        if (iFragmentImpl.isInterface())
            throw new IllegalArgumentException("iView can not be interface");
        IMPLEMENTS.put(iFragment, iFragmentImpl);
    }

    public static <T extends Contract.Presenter> IntentDelegate navigateActivity(Class<? extends IFragment<T>> fragmentClass, Class<? extends T> presenterClass) {
        return FragmentComponentActivity.navigate(fragmentClass, presenterClass);
    }
}
