package com.linsh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linsh.UtilsForLshViews;
import com.linsh.views.R;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 自己开发的提示窗口, 可支持文字/输入框/单选/列表
 * </pre>
 */
public class LshColorDialog extends Dialog {

    private LshColorDialog.BaseDialogBuilder mBuilder;
    private static int bgColor = 0xFF00809C;

    public LshColorDialog(Context context) {
        super(context, R.style.lsh_color_dialog);
        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_lsh_color);
        mBuilder.initView(this);
    }

    public LshColorDialog.TextDialogBuilder buildText() {
        LshColorDialog.TextDialogBuilder textDialogBuilder = new LshColorDialog.TextDialogBuilder();
        mBuilder = textDialogBuilder;
        return textDialogBuilder;
    }

    public LshColorDialog.ListDialogBuilder buildList() {
        LshColorDialog.ListDialogBuilder listDialogBuilder = new LshColorDialog.ListDialogBuilder();
        mBuilder = listDialogBuilder;
        return listDialogBuilder;
    }

    public <T> LshColorDialog.CustomListDialogBuilder<T> buildCustomList(Class<T> clazzOfData) {
        LshColorDialog.CustomListDialogBuilder<T> listDialogBuilder = new LshColorDialog.CustomListDialogBuilder<>();
        mBuilder = listDialogBuilder;
        return listDialogBuilder;
    }

    public LshColorDialog.InputDialogBuilder buildInput() {
        LshColorDialog.InputDialogBuilder inputDialogBuilder = new LshColorDialog.InputDialogBuilder();
        mBuilder = inputDialogBuilder;
        return inputDialogBuilder;
    }

    private abstract class BaseDialogBuilder<T extends LshColorDialog.BaseDialogBuilder> implements LshColorDialog.BaseDialogInterface {
        private String title;
        protected int color = LshColorDialog.bgColor;

        @Override
        public T setTitle(String title) {
            this.title = title;
            return (T) this;
        }

        public T setBgColor(int color) {
            this.color = color;
            return (T) this;
        }

        @Override
        public LshColorDialog show() {
            LshColorDialog.this.show();
            return LshColorDialog.this;
        }

        public void dismiss() {
            LshColorDialog.this.dismiss();
        }

        public LshColorDialog getDialog() {
            return LshColorDialog.this;
        }

        protected void initView(LshColorDialog dialog) {
            // 设置标题
            TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_lsh_color_title);
            if (isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(title);
            }
            // 设置背景
            View bgContentLayout = dialog.findViewById(R.id.ll_dialog_lsh_color_bg_layout);
            setBgContent(bgContentLayout, color);
        }

        // 根据子类需要添加不同布局
        protected void addView(LshColorDialog dialog, View childView) {
            FrameLayout layout = (FrameLayout) dialog.findViewById(R.id.fl_dialog_lsh_color_content);
            layout.addView(childView);
        }

        protected FrameLayout getContentView() {
            return (FrameLayout) LshColorDialog.this.findViewById(R.id.fl_dialog_lsh_color_content);
        }

        // 设置确认取消按钮是否可见
        protected void setBtnLayoutVisible() {
            findViewById(R.id.ll_dialog_lsh_color_btn_layout).setVisibility(View.VISIBLE);
        }

        // 设置窗口宽度占屏幕短边的百分比
        protected void setDialogWidth(LshColorDialog dialog, float shortSidePercent) {
            ViewGroup rootView = (ViewGroup) dialog.findViewById(R.id.ll_dialog_lsh_root);
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            layoutParams.width = (int) (getScreenShortSide() * shortSidePercent);
        }
    }

    private abstract class BtnDialogBuilder<T extends BtnDialogBuilder, P extends OnBasePositiveListener, N extends OnBaseNegativeListener>
            extends BaseDialogBuilder<T> implements BtnDialogInterface<T, P, N> {
        private String positiveText;
        private String negativeText;
        private P mOnPositiveListener;
        private N mOnNegativeListener;
        private boolean positiveBtnVisible;
        private boolean negativeBtnVisible;

        @Override
        public T setPositiveButton(String positiveText, P listener) {
            this.positiveText = positiveText;
            mOnPositiveListener = listener;
            positiveBtnVisible = true;
            return (T) this;
        }

        @Override
        public T setNegativeButton(String negativeText, N listener) {
            this.negativeText = negativeText;
            mOnNegativeListener = listener;
            negativeBtnVisible = true;
            return (T) this;
        }

        @Override
        protected void initView(final LshColorDialog dialog) {
            super.initView(dialog);
            // 判断是否需要确认取消按钮
            if (!positiveBtnVisible && !negativeBtnVisible) {
                dialog.findViewById(R.id.v_dialog_lsh_color_divider).setVisibility(View.GONE);
                return;
            }
            setBtnLayoutVisible();

            // 设置确认取消按钮
            TextView tvPositive = (TextView) dialog.findViewById(R.id.tv_dialog_lsh_color_positive);
            TextView tvNegative = (TextView) dialog.findViewById(R.id.tv_dialog_lsh_color_negative);
            if (positiveBtnVisible && negativeBtnVisible) {
                setBgLeftBtn(tvPositive);
                setBgRightBtn(tvNegative);
            } else {
                dialog.findViewById(R.id.v_dialog_lsh_color_divider).setVisibility(View.GONE);
                if (positiveBtnVisible) {
                    setBgCenterBtn(tvPositive);
                } else {
                    setBgCenterBtn(tvNegative);
                }
            }
            if (positiveBtnVisible) {
                if (!isEmpty(positiveText)) {
                    tvPositive.setText(positiveText);
                } else {
                    tvPositive.setText("确认");
                }
                tvPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnPositiveListener != null) {
                            onPositiveClick(mOnPositiveListener);
                        } else {
                            LshColorDialog.this.dismiss();
                        }
                    }
                });
            } else {
                tvPositive.setVisibility(View.GONE);
            }
            if (negativeBtnVisible) {
                if (!isEmpty(negativeText)) {
                    tvNegative.setText(negativeText);
                } else {
                    tvNegative.setText("取消");
                }
                tvNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnNegativeListener != null) {
                            onNegativeClick(mOnNegativeListener);
                        } else {
                            LshColorDialog.this.dismiss();
                        }
                    }
                });
            } else {
                tvNegative.setVisibility(View.GONE);
            }
        }

        protected abstract void onPositiveClick(P onPositiveListener);

        protected abstract void onNegativeClick(N onNegativeListener);
    }

    public class TextDialogBuilder extends BtnDialogBuilder<TextDialogBuilder, OnPositiveListener, OnNegativeListener> implements TextDialogInterface {
        private String content;

        @Override
        public TextDialogBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        @Override
        protected void initView(LshColorDialog dialog) {
            super.initView(dialog);
            // 生成TextView
            TextView textView = new TextView(dialog.getContext());
            textView.setText(content == null ? "" : content);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTextColor(Color.WHITE);
            addView(dialog, textView);
        }

        @Override
        protected void onPositiveClick(OnPositiveListener onPositiveListener) {
            onPositiveListener.onClick(LshColorDialog.this);
        }

        @Override
        protected void onNegativeClick(OnNegativeListener onNegativeListener) {
            onNegativeListener.onClick(LshColorDialog.this);
        }
    }

    public class ListDialogBuilder extends BtnDialogBuilder<ListDialogBuilder, OnListPositiveListener<String>, OnListNegativeListener<String>>
            implements ListDialogInterface<ListDialogBuilder, String> {

        private List<String> list;
        private LshColorDialog.OnItemClickListener mOnItemClickListener;
        private int curClickedItem = -1;

        @Override
        public LshColorDialog.ListDialogBuilder setList(List<String> list) {
            this.list = list;
            return this;
        }

        public LshColorDialog.ListDialogBuilder setList(String[] list) {
            if (list == null) {
                this.list = null;
            } else {
                this.list = Arrays.asList(list);
            }
            return this;
        }

        @Override
        public LshColorDialog.ListDialogBuilder setOnItemClickListener(LshColorDialog.OnItemClickListener listener) {
            mOnItemClickListener = listener;
            return this;
        }

        @Override
        protected void initView(LshColorDialog dialog) {
            super.initView(dialog);
            // 生成RecyclerView
            RecyclerView recyclerView = new RecyclerView(dialog.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);
            recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
            recyclerView.setAdapter(new LshColorDialog.ListDialogAdapter(list, new OnItemClickListener() {
                @Override
                public void onClick(LshColorDialog dialog, String item, int index) {
                    curClickedItem = index;
                    mOnItemClickListener.onClick(dialog, item, index);
                }
            }));

            FrameLayout contentView = getContentView();
            contentView.addView(recyclerView);
            contentView.setMinimumWidth(UtilsForLshViews.dp2px(getContext(), 150));
        }

        @Override
        protected void onPositiveClick(OnListPositiveListener<String> onPositiveListener) {
            String data = curClickedItem < 0 ? null : list.get(curClickedItem);
            onPositiveListener.onClick(LshColorDialog.this, data, curClickedItem);
        }

        @Override
        protected void onNegativeClick(OnListNegativeListener<String> onNegativeListener) {
            String data = curClickedItem < 0 ? null : list.get(curClickedItem);
            onNegativeListener.onClick(LshColorDialog.this, data, curClickedItem);
        }
    }

    public class CustomListDialogBuilder<T> extends BtnDialogBuilder<CustomListDialogBuilder<T>, OnListPositiveListener<T>, OnListNegativeListener<T>>
            implements CustomListDialogInterface<CustomListDialogBuilder, T> {

        private List<T> list;
        private int layoutId;
        private int curClickedItem = -1;
        private CustomListDialogAdapter.ListDialogAdapterListener<T> mAdapterListener;
        private SimplifiedRcvAdapter.OnItemClickListener<T> mOnItemClickListener;

        @Override
        public CustomListDialogBuilder<T> setList(List<T> list) {
            this.list = list;
            return this;
        }

        @Override
        public CustomListDialogBuilder<T> setCustomItem(int layoutId, CustomListDialogAdapter.ListDialogAdapterListener<T> listener) {
            this.layoutId = layoutId;
            mAdapterListener = listener;
            return this;
        }

        @Override
        public CustomListDialogBuilder<T> setOnItemClickListener(SimplifiedRcvAdapter.OnItemClickListener<T> listener) {
            mOnItemClickListener = listener;
            return this;
        }

        @Override
        protected void initView(LshColorDialog dialog) {
            super.initView(dialog);
            // 生成RecyclerView
            RecyclerView recyclerView = new RecyclerView(dialog.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);
            recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
            CustomListDialogAdapter<T> adapter = new CustomListDialogAdapter<>(layoutId, list, mAdapterListener);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(mOnItemClickListener);

            FrameLayout contentView = getContentView();
            contentView.addView(recyclerView);
            contentView.setMinimumWidth(UtilsForLshViews.dp2px(getContext(), 150));
        }

        @Override
        protected void onPositiveClick(OnListPositiveListener<T> onPositiveListener) {
            T data = curClickedItem < 0 ? null : list.get(curClickedItem);
            onPositiveListener.onClick(LshColorDialog.this, data, curClickedItem);
        }

        @Override
        protected void onNegativeClick(OnListNegativeListener<T> onNegativeListener) {
            T data = curClickedItem < 0 ? null : list.get(curClickedItem);
            onNegativeListener.onClick(LshColorDialog.this, data, curClickedItem);
        }
    }

    public static class CustomListDialogAdapter<T> extends SimplifiedRcvAdapter<T> {
        private ListDialogAdapterListener<T> mListDialogAdapterListener;

        public CustomListDialogAdapter(int layoutId, List<T> list, ListDialogAdapterListener<T> listener) {
            super(layoutId, list);
            mListDialogAdapterListener = listener;
        }

        @Override
        protected void onBindViewHolder(LshSimplifiedViewHolder holder, T data, int position) {
            if (mListDialogAdapterListener != null) {
                mListDialogAdapterListener.onBindViewHolder(holder, data, position);
            }
        }

        public interface ListDialogAdapterListener<T> {
            void onBindViewHolder(LshSimplifiedViewHolder holder, T data, int position);
        }
    }

    private class ListDialogAdapter extends RecyclerView.Adapter implements View.OnClickListener {
        private List<String> data;
        private LshColorDialog.OnItemClickListener mOnItemClickListener;

        public ListDialogAdapter(List<String> list, LshColorDialog.OnItemClickListener listener) {
            data = list;
            mOnItemClickListener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 生成LinearLayout布局
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(dp2px(10), 0, dp2px(10), 0);
            // 添加TextView
            TextView textView = new TextView(parent.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            int dp5 = dp2px(5);
            textView.setPadding(dp5, dp5, dp5, dp5);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTextColor(Color.WHITE);
            // 添加分割线
            View line = new View(parent.getContext());
            line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            line.setBackgroundColor(Color.parseColor("#D9D9D9"));
            // 添加到布局
            linearLayout.addView(textView);
            linearLayout.addView(line);
            return new RecyclerView.ViewHolder(linearLayout) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LinearLayout layout = (LinearLayout) holder.itemView;
            TextView textView = (TextView) layout.getChildAt(0);
            textView.setText(data.get(position));
            layout.setTag(position);
            layout.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                int position = (int) v.getTag();
                mOnItemClickListener.onClick(LshColorDialog.this, data.get(position), position);
            }
        }
    }

    public class InputDialogBuilder extends BtnDialogBuilder<InputDialogBuilder, OnInputPositiveListener, OnInputNegativeListener>
            implements LshColorDialog.InputDialogInterface {

        private CharSequence hint;
        private CharSequence text;
        private EditText mEditText;

        @Override
        public LshColorDialog.InputDialogBuilder setHint(CharSequence hint) {
            this.hint = hint;
            return this;
        }

        @Override
        public InputDialogBuilder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        @Override
        protected void initView(LshColorDialog dialog) {
            super.initView(dialog);
            // 添加输入框
            mEditText = new EditText(dialog.getContext());
            if (!isEmpty(hint)) {
                mEditText.setHint(hint);
            }
            if (!isEmpty(text)) {
                mEditText.setText(text);
                mEditText.setSelection(text.length());
            }
            mEditText.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(dialog, mEditText);
        }

        @Override
        protected void onPositiveClick(OnInputPositiveListener onPositiveListener) {
            onPositiveListener.onClick(LshColorDialog.this, mEditText.getText().toString());
        }

        @Override
        protected void onNegativeClick(OnInputNegativeListener onNegativeListener) {
            onNegativeListener.onClick(LshColorDialog.this, mEditText.getText().toString());
        }
    }

    private interface BaseDialogInterface<T extends LshColorDialog.BaseDialogBuilder> {
        T setTitle(String title);

        LshColorDialog show();
    }

    private interface BtnDialogInterface<T extends BtnDialogBuilder, P extends OnBasePositiveListener, N extends OnBaseNegativeListener> {
        T setPositiveButton(String positiveText, P listener);

        T setNegativeButton(String negativeText, N listener);
    }

    private interface TextDialogInterface<T extends LshColorDialog.TextDialogBuilder> {
        T setContent(String content);
    }

    private interface ListDialogInterface<T extends LshColorDialog.ListDialogBuilder, S> {
        T setList(List<S> list);

        T setOnItemClickListener(LshColorDialog.OnItemClickListener listener);
    }

    private interface CustomListDialogInterface<T extends LshColorDialog.CustomListDialogBuilder, S> {
        T setList(List<S> list);

        T setCustomItem(int layoutId, CustomListDialogAdapter.ListDialogAdapterListener<S> listener);

        T setOnItemClickListener(SimplifiedRcvAdapter.OnItemClickListener<S> listener);
    }

    private interface InputDialogInterface<T extends LshColorDialog.InputDialogBuilder> {
        T setHint(CharSequence hint);

        T setText(CharSequence text);

        T setPositiveButton(String positiveText, LshColorDialog.OnInputPositiveListener listener);

        T setNegativeButton(String negativeText, LshColorDialog.OnInputNegativeListener listener);
    }

    public interface OnBasePositiveListener {
    }

    public interface OnBaseNegativeListener {
    }

    public interface OnPositiveListener extends OnBasePositiveListener {
        void onClick(LshColorDialog dialog);
    }

    public interface OnNegativeListener extends OnBaseNegativeListener {
        void onClick(LshColorDialog dialog);
    }

    public interface OnListPositiveListener<T> extends OnBasePositiveListener {
        void onClick(LshColorDialog dialog, T t, int index);
    }

    public interface OnListNegativeListener<T> extends OnBaseNegativeListener {
        void onClick(LshColorDialog dialog, T t, int index);
    }

    public interface OnInputPositiveListener extends OnBasePositiveListener {
        void onClick(LshColorDialog dialog, String inputText);
    }

    public interface OnInputNegativeListener extends OnBaseNegativeListener {
        void onClick(LshColorDialog dialog, String inputText);
    }

    public interface OnItemClickListener {
        void onClick(LshColorDialog dialog, String item, int index);
    }

    public interface OnCustomItemClickListener {
        void onClick(LshColorDialog dialog, SimplifiedRcvAdapter.LshSimplifiedViewHolder viewHolder, int index);
    }

    //================================================ 工具方法 ================================================//
    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getScreenShortSide() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    private boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    private void setBgContent(View view, int color) {
        int dp10 = dp2px(10);
        GradientDrawable bgContent = UtilsForLshViews.createRectangleCorner(new float[]{dp10, dp10, dp10, dp10, 0, 0, 0, 0}, color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(bgContent);
        } else {
            view.setBackgroundDrawable(bgContent);
        }
    }

    private void setBgBtn(View view, float[] radii) {
        GradientDrawable bgContentWhite = UtilsForLshViews.createRectangleCorner(radii, Color.WHITE);
        GradientDrawable bgContentGray = UtilsForLshViews.createRectangleCorner(radii, 0xFFEDEDF3);
        StateListDrawable pressedSelector = UtilsForLshViews.createPressedSelector(bgContentGray, bgContentWhite);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(pressedSelector);
        } else {
            view.setBackgroundDrawable(pressedSelector);
        }
    }

    private void setBgCenterBtn(View view) {
        int dp10 = dp2px(10);
        setBgBtn(view, new float[]{0, 0, 0, 0, dp10, dp10, dp10, dp10});
    }

    private void setBgLeftBtn(View view) {
        int dp10 = dp2px(10);
        setBgBtn(view, new float[]{0, 0, 0, 0, 0, 0, dp10, dp10});
    }

    private void setBgRightBtn(View view) {
        int dp10 = dp2px(10);
        setBgBtn(view, new float[]{0, 0, 0, 0, dp10, dp10, 0, 0});
    }

}
