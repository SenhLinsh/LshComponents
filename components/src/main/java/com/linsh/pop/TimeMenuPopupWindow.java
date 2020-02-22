package com.linsh.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.linsh.utilseverywhere.BackgroundUtils;
import com.linsh.views.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/07/13
 *    desc   : 小时光通用 PopupWindow
 * </pre>
 */
public class TimeMenuPopupWindow extends PopupWindow {

    private ViewGroup rootLayout;
    private ViewGroup itemLayout;

    public TimeMenuPopupWindow(Context context) {
        this(getView(context));
    }

    private TimeMenuPopupWindow(ViewGroup layout) {
        super(layout, -2, -2);
        this.rootLayout = (ViewGroup) layout.getChildAt(0);
        this.itemLayout = (ViewGroup) ((ViewGroup) layout.getChildAt(0)).getChildAt(0);
        init();
    }

    private void init() {
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setFocusable(true);
    }

    /**
     * 设置条目
     *
     * @param resItems            条目数据
     * @param onItemClickListener 点击事件
     */
    public void setItems(int[] resItems, final OnItemClickListener onItemClickListener) {
        String[] items = new String[resItems.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = itemLayout.getContext().getResources().getText(resItems[i]).toString();
        }
        setItems(items, onItemClickListener);
    }

    /**
     * 设置条目
     *
     * @param items               条目数据
     * @param onItemClickListener 点击事件
     */
    public void setItems(String[] items, final OnItemClickListener onItemClickListener) {
        if (itemLayout.getChildCount() > 0)
            itemLayout.removeAllViews();
        Context context = itemLayout.getContext();
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            TextView textView = new TextView(context);
            textView.setText(item);
            textView.setTextSize(16);
            textView.setTextColor(context.getResources().getColor(R.color.dark_grey));
            int dp30 = dp(30);
            int dp11 = dp(11);
            int dp5 = dp(5);
            textView.setPadding(dp30, dp11, dp30, dp11);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp5, dp5, dp5, dp5);
            textView.setLayoutParams(params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.setTranslationZ(100);
            }
            BackgroundUtils.addPressedEffect(textView);
            itemLayout.addView(textView, 0x1E000000);

            if (onItemClickListener != null) {
                final int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(finalI);
                    }
                });
            }
        }
    }

    /**
     * 弹出 PopupWindow, 默认自动调整为在控件下方弹出, 并右对齐
     *
     * @param view 在指定控件下方弹出
     */
    public void show(View view) {
        show(view, 0, 0, null, false);
    }

    /**
     * 弹出 PopupWindow, 默认自动调整为在控件下方弹出, 并右对齐
     *
     * @param view 在指定控件下方弹出
     * @param rect y PopupWindow 可以显示的边界, 超出边界部分会自动进行偏移至边界内
     */
    public void show(View view, Rect rect) {
        show(view, 0, 0, rect, false);
    }

    /**
     * 弹出 PopupWindow, 默认自动调整为在控件下方弹出, 并右对齐
     *
     * @param view       在指定控件下方弹出
     * @param rect       y PopupWindow 可以显示的边界, 超出边界部分会自动进行偏移至边界内
     * @param scaleFirst 如果边界超出, 是否优先进行压缩
     */
    public void show(View view, Rect rect, boolean scaleFirst) {
        show(view, 0, 0, rect, scaleFirst);
    }

    /**
     * 弹出 PopupWindow, 默认自动调整为在控件下方弹出, 并右对齐
     *
     * @param view 在指定控件下方弹出
     * @param x    x 偏移
     * @param y    y 偏移
     */
    public void show(View view, int x, int y) {
        show(view, x, y, null, false);
    }

    /**
     * 弹出 PopupWindow, 默认自动调整为在控件下方弹出, 并右对齐
     *
     * @param view       在指定控件下方弹出
     * @param x          x 偏移
     * @param y          y 偏移
     * @param rect       y PopupWindow 可以显示的边界, 超出边界部分会自动进行偏移至边界内
     * @param scaleFirst 如果边界超出, 是否优先进行压缩
     */
    public void show(View view, int x, int y, Rect rect, boolean scaleFirst) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        // x轴偏移 = 屏幕宽度 - 控件X - 控件宽 - 右边阴影宽度
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        int shadowRight = dp(7);
        int shadowBottom = dp(9);
        int xOff = screenWidth - (location[0] + view.getWidth()) - shadowRight + x;
        int yOff = location[1] + view.getHeight() + y;
        // 处理边界
        if (rect != null) {
            rootLayout.measure(0, 0);
            int width = rootLayout.getMeasuredWidth() - shadowRight * 2;
            int height = rootLayout.getMeasuredHeight() - shadowBottom; // 6dp 为箭头的高度
            if (rect.left > 0 || rect.right > 0) {
                if (rect.right == 0) {
                    rect.right = screenWidth;
                }
                if (rect.left < rect.right) {
                    // 如果优先缩小, 则提前控制边界
                    if (scaleFirst) {
                        int right = screenWidth - xOff - shadowRight;
                        int left = right - width;
                        if (rect.left > left && rect.right > right) {
                            rect.right = right;
                        } else if (rect.right < right && rect.left < left) {
                            rect.left = left;
                        }
                    }
                    // 是否需要缩小
                    boolean widthScale = rect.right - rect.left < width;
                    if (widthScale) {
                        xOff = screenWidth - rect.right - shadowRight;
                        int fixedWidth = rect.right - rect.left + shadowRight * 2;
                        ViewGroup.LayoutParams params = rootLayout.getLayoutParams();
                        params.width = fixedWidth;
                        rootLayout.setLayoutParams(params);
                    } else if (rect.left > 0) {
                        int left = screenWidth - xOff - width - shadowRight;
                        if (rect.left > left) { // 左边界超出
                            xOff -= (rect.left - left);
                        }
                    } else if (rect.right > 0) {
                        int right = screenWidth - xOff - shadowRight;
                        if (rect.right < right) { // 右边界超出
                            xOff += (right - rect.right);
                        }
                    }
                }
            }
            if (rect.top > 0 || rect.bottom > 0) {
                if (rect.bottom == 0) {
                    rect.bottom = screenHeight;
                }
                if (rect.top < rect.bottom) {
                    // 如果优先缩小, 则提前控制边界
                    if (scaleFirst) {
                        int top = yOff;
                        int bottom = yOff + height;
                        if (rect.top > top && rect.bottom > bottom) {
                            rect.bottom = bottom;
                        } else if (rect.bottom < bottom && rect.top < top) {
                            rect.top = top;
                        }
                    }
                    // 是否需要缩小
                    boolean heightScale = rect.bottom - rect.top < height;
                    if (heightScale) {
                        yOff = rect.top;
                        int fixedHeight = rect.bottom - rect.top + shadowBottom;
                        ViewGroup.LayoutParams params = rootLayout.getLayoutParams();
                        params.height = fixedHeight;
                        rootLayout.setLayoutParams(params);
                    } else if (rect.top > 0) {
                        if (rect.top > yOff) { // 上边界超出
                            yOff = rect.top;
                        }
                    } else if (rect.bottom > 0) {
                        int bottom = yOff + height;
                        if (rect.bottom < bottom) { // 下边界超出
                            yOff -= (bottom - rect.bottom);
                        }
                    }
                }
            }
        }
        showAtLocation(view.getRootView(), Gravity.TOP | Gravity.RIGHT,
                xOff, yOff);
    }

    private static ViewGroup _getView(Context context) {
        FrameLayout window = new FrameLayout(context);

        LinearLayout layout = new LinearLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        layout.setBackgroundResource(R.drawable.ic_components_menu_shadow_bg); // 气泡阴影距边框: 7 1 7 9 dp
        layout.setOrientation(LinearLayout.VERTICAL);

        GradientDrawable divider = new GradientDrawable();
        divider.setShape(GradientDrawable.RECTANGLE);
        divider.setSize(0, dp2px(context, 0.5F));
        divider.setColor(0xFFDFDFE2);
        layout.setDividerDrawable(divider);
        layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        window.addView(layout);
        return window;
    }

    private static ViewGroup getView(Context context) {
        FrameLayout window = new FrameLayout(context);

        FrameLayout scrollView = new ScrollView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollView.setLayoutParams(params);
        scrollView.setBackgroundResource(R.drawable.ic_components_menu_shadow_bg); // 气泡阴影距边框: 7 1 7 9 dp


        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable divider = new GradientDrawable();
        divider.setShape(GradientDrawable.RECTANGLE);
        divider.setSize(0, dp2px(context, 0.5F));
        divider.setColor(0xFFDFDFE2);
        layout.setDividerDrawable(divider);
        layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        window.addView(scrollView);
        scrollView.addView(layout);
        return window;
    }

    private int dp(float dp) {
        float density = itemLayout.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) itemLayout.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) itemLayout.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public interface OnItemClickListener {
        void onItemClick(int index);
    }
}
