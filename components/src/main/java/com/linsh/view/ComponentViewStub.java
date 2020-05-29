package com.linsh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.views.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/28
 *    desc   : 用于指定 ViewComponents 的 ViewStub
 * </pre>
 */
public class ComponentViewStub extends View {

    private IView viewInterface;

    public ComponentViewStub(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ComponentViewStub);
            String classOfView = array.getString(R.styleable.ComponentViewStub_viewInterface);
            if (classOfView != null) {
                try {
                    viewInterface = ViewComponents.create(getContext(), (Class) ClassUtils.getClass(classOfView));
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("class of viewInterface not found", e);
                }
            }
            array.recycle();
        }
    }

    public void setViewInterface(Class<? extends IView> classOfIView) {
        viewInterface = ViewComponents.create(getContext(), classOfIView);
    }

    public <T extends IView> T inflate() {
        if (viewInterface == null)
            throw new IllegalArgumentException("ComponentViewStub must have a viewInterface");
        ViewParent viewParent = getParent();
        if (!(viewParent instanceof ViewGroup))
            throw new IllegalStateException("ComponentViewStub must have a non-null ViewGroup viewParent");
        View view = viewInterface.getView();
        ViewGroup parent = (ViewGroup) viewParent;
        replaceSelfWithView(view, parent);
        return (T) viewInterface;
    }

    private void replaceSelfWithView(View view, ViewGroup parent) {
        int index = parent.indexOfChild(this);
        parent.removeViewInLayout(this);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            parent.addView(view, index, layoutParams);
        } else {
            parent.addView(view, index);
        }
    }
}
