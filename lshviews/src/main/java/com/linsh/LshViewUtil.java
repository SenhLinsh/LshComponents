package com.linsh;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public class LshViewUtil {

    public static int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * 给 View 设置背景, 用于简化 SDK 版本判断
     *
     * @param view     View
     * @param drawable 背景
     */
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
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

    public static Drawable getDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    public static GradientDrawable createRectangleBorder(float roundRadius, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(roundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
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


    public static ColorStateList createEnabledColorSelector(int enabledColor, int disabledColor) {
        int[][] states = new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{}};
        int[] colors = new int[]{enabledColor, disabledColor};
        return new ColorStateList(states, colors);
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

    public static StateListDrawable createSelectedSelector(Drawable selectedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, selectedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }

    /**
     * 给 View 添加触按效果, 触按效果为混合一个透明度为 30% 的黑色背景
     *
     * @param view 指定添加效果的 View
     */
    public static void addPressedEffect(View view) {
        addPressedEffect(view, 0x33333333);
    }

    /**
     * 给 View 添加一个颜色或者颜色蒙层作为触按效果<br/>
     * 该方法将自动在原本背景的基础上给 View 添加一个触按状态下的颜色混合 (新状态颜色 = 原本颜色 + color)
     *
     * @param view  指定添加效果的 View
     * @param color 覆盖触按状态的颜色
     */
    public static void addPressedEffect(View view, int color) {
        Drawable pressedDr = null;
        Drawable background = view.getBackground();
        if (Color.alpha(color) == 0xFF) {
            if (background instanceof StateListDrawable) {
                ((StateListDrawable) background).addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(color));
                return;
            } else {
                pressedDr = new ColorDrawable(color);
            }
        } else {
            if (background == null) {
                pressedDr = new ColorDrawable(color);
            } else if (background instanceof ColorDrawable) {
                int fgColor = ((ColorDrawable) background).getColor();
                int pressedColor = coverColor(fgColor, color);
                pressedDr = new ColorDrawable(pressedColor);
            } else if (background instanceof BitmapDrawable) {
                Bitmap fgBitmap = ((BitmapDrawable) background).getBitmap();
                Bitmap pressedBitmap = addColorMask(fgBitmap, color, false);
                pressedDr = new BitmapDrawable(view.getContext().getResources(), pressedBitmap);
            } else if (background instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) background;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    ColorStateList stateList = gradientDrawable.getColor();
                    if (stateList != null) {
                        int normalColor = stateList.getColorForState(new int[]{}, Color.TRANSPARENT);
                        int enabledColor = stateList.getColorForState(new int[]{android.R.attr.state_enabled}, Color.TRANSPARENT);
                        int selectedColor = stateList.getColorForState(new int[]{android.R.attr.state_selected}, Color.TRANSPARENT);
                        int pressedColor = coverColor(normalColor, color);
                        gradientDrawable.setColor(new ColorStateList(new int[][]{new int[]{android.R.attr.state_selected},
                                new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_enabled}, new int[]{}},
                                new int[]{selectedColor, pressedColor, enabledColor, normalColor}));
                    } else {
                        gradientDrawable.setColor(new ColorStateList(new int[][]{new int[]{android.R.attr.state_selected}}, new int[]{color}));
                    }
                    return;
                } else {
                    // TODO: 17/9/14  可以通过反射来获取 GradientState ?
                }
            } else if (background instanceof StateListDrawable) {
                StateListDrawable stateListDrawable = (StateListDrawable) background;

                stateListDrawable.setState(new int[]{});
                Drawable normalStateDr = stateListDrawable.getCurrent();
                pressedDr = addColorMask(view.getContext(), normalStateDr, color);

                StateListDrawable newDrawable = new StateListDrawable();
                stateListDrawable.setState(new int[]{android.R.attr.state_selected});
                newDrawable.addState(new int[]{android.R.attr.state_selected}, stateListDrawable.getCurrent());
                newDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDr);
                stateListDrawable.setState(new int[]{android.R.attr.state_enabled});
                newDrawable.addState(new int[]{android.R.attr.state_enabled}, stateListDrawable.getCurrent());
                newDrawable.addState(new int[]{}, normalStateDr);
                setBackground(view, newDrawable);
                return;
            }
        }
        if (pressedDr != null) {
            setBackground(view, createPressedSelector(pressedDr, background));
        }
    }

    /**
     * 覆盖颜色, 在背景颜色上覆盖一层颜色(有透明度)后得出的混合颜色
     * <p>注意: 覆盖的颜色需要有一定的透明度, 如果覆盖的颜色不透明, 得到的颜色为覆盖的颜色
     *
     * @param baseColor  基础颜色(背景颜色)
     * @param coverColor 覆盖颜色(前景颜色)
     * @return
     */
    public static int coverColor(@ColorInt int baseColor, @ColorInt int coverColor) {
        int fgAlpha = Color.alpha(coverColor);
        int bgAlpha = Color.alpha(baseColor);
        int a = blendColorNormalFormula(255, bgAlpha, fgAlpha, bgAlpha);
        int r = blendColorNormalFormula(Color.red(coverColor), Color.red(baseColor), fgAlpha, bgAlpha);
        int g = blendColorNormalFormula(Color.green(coverColor), Color.green(baseColor), fgAlpha, bgAlpha);
        int b = blendColorNormalFormula(Color.blue(coverColor), Color.blue(baseColor), fgAlpha, bgAlpha);
        return Color.argb(a, r, g, b);
    }

    /**
     * 颜色混合模式中正常模式的计算公式
     * 公式: 最终色 = 基色 * a% + 混合色 * (1 - a%)
     */
    private static int blendColorNormalFormula(int fgArgb, int bgArgb, int fgAlpha, int bgAlpha) {
        int mix = (int) (fgArgb * fgAlpha / 255f + (1 - fgAlpha / 255f) * bgArgb * bgAlpha / 255f);
        return mix > 255 ? 255 : mix;
    }

    /**
     * 盖印颜色蒙层
     *
     * @param src     源 Bitmap 对象
     * @param color   盖印颜色
     * @param recycle 是否回收所处理的原 Bitmap 对象
     * @return 盖印后的图片
     */
    public static Bitmap addColorMask(Bitmap src, int color, final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap ret = src.copy(src.getConfig(), true);
        Canvas canvas = new Canvas(ret);
        canvas.drawColor(color);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 为 Drawable 添加颜色蒙层, 可用于改变状态颜色等
     *
     * @param background 背景 Drawable
     * @param color      颜色蒙层
     * @return 添加蒙层后的新的 Drawable 对象, 处理失败返回 null
     */
    public static Drawable addColorMask(Context context, Drawable background, int color) {
        if (background == null || Color.alpha(color) == 0xFF) {
            return new ColorDrawable(color);
        } else if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int fgColor = colorDrawable.getColor();
            int pressedColor = coverColor(fgColor, color);
            return new ColorDrawable(pressedColor);
        } else if (background instanceof BitmapDrawable) {
            Bitmap fgBitmap = ((BitmapDrawable) background).getBitmap();
            Bitmap pressedBitmap = addColorMask(fgBitmap, color, false);
            return new BitmapDrawable(context.getResources(), pressedBitmap);
        } else if (background instanceof GradientDrawable) {
            GradientDrawable pressedDr = copyGradientDrawable((GradientDrawable) background);
            if (pressedDr != null) {
                try {
                    Object gradientState = getField(pressedDr, "mGradientState");
                    if (gradientState == null) {
                        pressedDr.setColor(color);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        ColorStateList solidColors = pressedDr.getColor();
                        if (solidColors != null) {
                            int normalColor = solidColors.getColorForState(new int[]{}, Color.TRANSPARENT);
                            pressedDr.setColor(coverColor(normalColor, color));
                        } else {
                            int[] gradientColors = pressedDr.getColors();
                            if (gradientColors != null) {
                                for (int i = 0; i < gradientColors.length; i++) {
                                    gradientColors[i] = coverColor(gradientColors[i], color);
                                }
                                pressedDr.setColors(gradientColors);
                            } else {
                                pressedDr.setColor(color);
                            }
                        }
                    } else {
                        ColorStateList solidColors = (ColorStateList) getField(gradientState, "mSolidColors");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (solidColors != null) {
                                int normalColor = solidColors.getColorForState(new int[]{}, Color.TRANSPARENT);
                                pressedDr.setColor(coverColor(normalColor, color));
                            } else {
                                int[] gradientColors = (int[]) getField(gradientState, "mGradientColors");
                                if (gradientColors != null) {
                                    for (int i = 0; i < gradientColors.length; i++) {
                                        gradientColors[i] = coverColor(gradientColors[i], color);
                                    }
                                    pressedDr.setColors(gradientColors);
                                } else {
                                    pressedDr.setColor(color);
                                }
                            }
                        } else {
                            if (solidColors != null) {
                                pressedDr.setColor(coverColor((solidColors).getColorForState(new int[0], 0), color));
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    int[] gradientColors = (int[]) getField(gradientState, "mGradientColors");
                                    if (gradientColors != null) {
                                        for (int i = 0; i < gradientColors.length; i++) {
                                            gradientColors[i] = coverColor(gradientColors[i], color);
                                        }
                                        pressedDr.setColors(gradientColors);
                                    }
                                }
                            }
                        }
                    }
                    return pressedDr;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 拷贝 GradientDrawable  TODO: 17/9/15  need test case
     *
     * @param drawable 源 GradientDrawable 对象
     * @return 拷贝后的新的 GradientDrawable 对象
     */
    static GradientDrawable copyGradientDrawable(GradientDrawable drawable) {
        try {
            GradientDrawable copy = new GradientDrawable();
            Object gradientState = getField(drawable, "mGradientState");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                copy.setOrientation(drawable.getOrientation());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                copy.setAlpha(drawable.getAlpha());
                copy.setStroke((Integer) getField(gradientState, "mStrokeWidth"),
                        (ColorStateList) getField(gradientState, "mStrokeColors"),
                        (Float) getField(gradientState, "mStrokeDashWidth"),
                        (Float) getField(gradientState, "mStrokeDashGap"));
            } else {
                copy.setAlpha((Integer) getField(drawable, "mAlpha"));
                copy.setStroke((Integer) getField(gradientState, "mStrokeWidth"),
                        ((ColorStateList) getField(gradientState, "mStrokeColors")).getColorForState(new int[0], 0),
                        (Float) getField(gradientState, "mStrokeDashWidth"),
                        (Float) getField(gradientState, "mStrokeDashGap"));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                float[] cornerRadii = null;
                try {
                    cornerRadii = drawable.getCornerRadii(); // 没有设置 CornerRadii 时会导致空指针异常
                } catch (Exception ignored) {
                }
                if (cornerRadii != null) {
                    copy.setCornerRadii(cornerRadii);
                } else {
                    copy.setCornerRadius(drawable.getCornerRadius());
                }
                ColorStateList solidColors = drawable.getColor();
                if (solidColors != null) {
                    copy.setColor(solidColors);
                } else {
                    copy.setColors(drawable.getColors());
                }
                copy.setGradientRadius(drawable.getGradientRadius());
                copy.setGradientType(drawable.getGradientType());
                copy.setShape(drawable.getShape());
            } else {
                float[] cornerRadii = (float[]) getField(gradientState, "mRadiusArray");
                if (cornerRadii != null) {
                    copy.setCornerRadii(cornerRadii);
                } else {
                    copy.setCornerRadius((Float) getField(gradientState, "mRadius"));
                }
                invokeMethod(copy, "setGradientType",
                        new Class[]{int.class}, new Object[]{getField(gradientState, "mGradient")});
                invokeMethod(copy, "setShape",
                        new Class[]{int.class}, new Object[]{getField(gradientState, "mShape")});

                ColorStateList solidColors = (ColorStateList) getField(gradientState, "mSolidColors");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (solidColors != null) {
                        copy.setColor(solidColors);
                    } else {
                        copy.setColors((int[]) getField(gradientState, "mGradientColors"));
                    }
                } else {
                    if (solidColors != null) {
                        copy.setColor((solidColors).getColorForState(new int[0], 0));
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            Object gradientColors = getField(gradientState, "mGradientColors");
                            if (gradientColors != null) {
                                copy.setColors((int[]) gradientColors);
                            }
                        }
                    }
                }
            }
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LshLog", "copyGradientDrawable: ", e);
        }
        return null;
    }

    /**
     * Bitmap 对象是否为空 (为 null 或者宽和高其中一项为0)
     *
     * @param src Bitmap 对象
     * @return true 为空; false 不为空
     */
    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }


    private static Object getField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private static Object invokeMethod(Object obj, String methodName, Class[] parameterTypes, Object[] args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(obj, args);
    }
}
