package com.linsh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.components.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/28
 *    desc   : 用于指定 ViewComponents 的自定义 View
 * </pre>
 */
public class ComponentView extends FrameLayout {

    private IView viewInterface;

    public ComponentView(Context context) {
        this(context, null);
    }

    public ComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ComponentView);
            String classOfView = array.getString(R.styleable.ComponentView_viewInterface);
            if (classOfView != null) {
                try {
                    setViewInterface((Class<? extends IView>) ClassUtils.getClass(classOfView), attrs);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    TextView child = new TextView(context);
                    child.setText("加载失败...");
                    addView(child);
                }
            }
        }
    }

    public void setViewInterface(Class<? extends IView> classOfView) {
        removeAllViews();
        setViewInterface(classOfView, null);
    }

    private void setViewInterface(Class<? extends IView> classOfView, AttributeSet attrs) {
        viewInterface = ViewComponents.create(getContext(), classOfView);

        if (attrs != null) {
            addView(viewInterface.getView(), new LayoutParams(getContext(), attrs));
        } else if (getLayoutParams() != null) {
            addView(viewInterface.getView(), new LayoutParams(getLayoutParams()));
        } else {
            addView(viewInterface.getView());
        }
    }

    public <T extends IView> T getViewInterface() {
        return (T) viewInterface;
    }
}
