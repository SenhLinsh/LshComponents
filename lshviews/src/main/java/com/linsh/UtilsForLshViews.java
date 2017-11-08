package com.linsh;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : LshViews 专用工具类
 * </pre>
 */
public class UtilsForLshViews {

    public static int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }

    public static int[] getCopy(int[] srcArray) {
        if (srcArray == null) {
            return null;
        }
        return getCopy(srcArray, srcArray.length);
    }

    public static int[] getCopy(int[] srcArray, int newLength) {
        int[] destArray = new int[newLength];

        if (srcArray != null && srcArray.length > 0 && newLength > 0) {
            System.arraycopy(srcArray, 0, destArray, 0, Math.min(srcArray.length, destArray.length));
        }
        return destArray;
    }

    public static <T> List<T> toList(T[] array) {
        if (array == null) return null;

        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    public static ColorStateList createEnabledColorSelector(int enabledColor, int disabledColor) {
        int[][] states = new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{}};
        int[] colors = new int[]{enabledColor, disabledColor};
        return new ColorStateList(states, colors);
    }

    public static GradientDrawable createRectangleBorder(float roundRadius, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(roundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    public static StateListDrawable createEnabledSelector(Drawable enabledDrawable, Drawable disabledDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, enabledDrawable);
        stateListDrawable.addState(new int[]{}, disabledDrawable);
        return stateListDrawable;
    }

    public static StateListDrawable createPressedSelector(Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }

    public static GradientDrawable createRectangleCorner(float[] radii) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(radii);
        return gradientDrawable;
    }

    public static GradientDrawable createRectangleCorner(float[] radii, int fillColor) {
        GradientDrawable rectangleCorner = createRectangleCorner(radii);
        rectangleCorner.setColor(fillColor);
        return rectangleCorner;
    }
}
