package com.linsh.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.linsh.base.LshActivity;
import com.linsh.base.activity.IntentDelegate;
import com.linsh.base.activity.base.BaseActivity;
import com.linsh.base.mvp.Contract;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.utilseverywhere.ContextUtils;
import com.linsh.views.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/08
 *    desc   :
 * </pre>
 */
public class FragmentComponentActivity extends BaseActivity {

    static <T extends Contract.Presenter> IntentDelegate navigate(Class<? extends IFragment<T>> fragmentInterface, Class<? extends T> presenterClass) {
        return LshActivity.intent(new Intent(ContextUtils.get(), FragmentComponentActivity.class)
                .putExtra("fragmentInterface", fragmentInterface)
                .putExtra("presenterClass", presenterClass));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_frame);

        Intent intent = getIntent();
        Class<? extends IFragment<Contract.Presenter>> fragmentInterface = (Class<? extends IFragment<Contract.Presenter>>) intent.getSerializableExtra("fragmentInterface");
        Class<? extends Contract.Presenter> presenterClass = (Class<? extends Contract.Presenter>) intent.getSerializableExtra("presenterClass");

        if (fragmentInterface == null)
            throw new NullPointerException("fragment interface is null");
        if (presenterClass == null)
            throw new NullPointerException("presenter class is null");

        // 实例化 BaseComponentFragment
        IFragment fragment;
        try {
            fragment = FragmentComponents.create(fragmentInterface);
        } catch (Exception e) {
            throw new RuntimeException("new instance IFragment class error", e);
        }
        // 实例化 Contract.Presenter
        Contract.Presenter presenter;
        try {
            presenter = (Contract.Presenter) ClassUtils.newInstance(presenterClass);
        } catch (Exception e) {
            throw new RuntimeException("new presenter Contract.Presenter class error", e);
        }
        // 为 BaseComponentFragment 设置 Presenter
        fragment.setPresenter(presenter);

        // 添加 Fragment 布局
        androidx.fragment.app.FragmentManager fragmentManager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl, fragment.get())
                .commit();
    }
}
