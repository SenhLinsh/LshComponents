package com.linsh.dialog.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.linsh.lshutils.utils.BackgroundUtilsEx;
import com.linsh.views.R;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 自己开发的简单提示窗口, 可支持文字/输入框/单选/列表
 * </pre>
 */
public class LshDialog extends Dialog {

    private final View rootLayout;
    private BaseDialogBuilder mBuilder;
    private View mContentView;
    private boolean isCreated;

    private CharSequence title;
    private CharSequence positiveText;
    private CharSequence negativeText;
    private OnPositiveListener mOnPositiveListener;
    private OnNegativeListener mOnNegativeListener;
    private boolean positiveBtnVisible;
    private boolean negativeBtnVisible;

    public LshDialog(Context context) {
        super(context);
        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootLayout = LayoutInflater.from(context).inflate(R.layout.dialog_lsh, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp2px(250), ViewGroup.LayoutParams.WRAP_CONTENT);
        rootLayout.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(rootLayout);
        isCreated = true;
        setTitle(title);
        layoutButtons();
    }

    public TextDialogBuilder buildText() {
        TextDialogBuilder textDialogBuilder = new TextDialogBuilder();
        mBuilder = textDialogBuilder;
        mContentView = mBuilder.initView(this);
        return textDialogBuilder;
    }

    public ListDialogBuilder buildList() {
        ListDialogBuilder listDialogBuilder = new ListDialogBuilder();
        mBuilder = listDialogBuilder;
        mContentView = mBuilder.initView(this);
        return listDialogBuilder;
    }

    public SelectDialogBuilder buildSelect() {
        SelectDialogBuilder selectDialogBuilder = new SelectDialogBuilder();
        mBuilder = selectDialogBuilder;
        mContentView = mBuilder.initView(this);
        return selectDialogBuilder;
    }

    public InputDialogBuilder buildInput() {
        InputDialogBuilder inputDialogBuilder = new InputDialogBuilder();
        mBuilder = inputDialogBuilder;
        mContentView = mBuilder.initView(this);
        return inputDialogBuilder;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        if (isCreated) {
            // 设置标题
            TextView tvTitle = rootLayout.findViewById(R.id.tv_dialog_lsh_title);
            if (isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    public LshDialog setPositiveButton(String positiveText, OnPositiveListener listener) {
        this.positiveText = positiveText;
        mOnPositiveListener = listener;
        positiveBtnVisible = positiveText != null || listener != null;
        layoutButtons();
        return this;
    }

    public LshDialog setNegativeButton(String negativeText, OnNegativeListener listener) {
        this.negativeText = negativeText;
        mOnNegativeListener = listener;
        negativeBtnVisible = negativeText != null || listener != null;
        layoutButtons();
        return this;
    }

    private void layoutButtons() {
        if (!isCreated) return;
        // 判断是否需要确认取消按钮
        if (!positiveBtnVisible && !negativeBtnVisible) {
            rootLayout.findViewById(R.id.ll_dialog_lsh_btn_layout).setVisibility(View.GONE);
            return;
        }
        // 设置确认取消按钮可见
        rootLayout.findViewById(R.id.ll_dialog_lsh_btn_layout).setVisibility(View.VISIBLE);
        // 设置确认取消按钮
        TextView tvPositive = (TextView) rootLayout.findViewById(R.id.tv_dialog_lsh_positive);
        TextView tvNegative = (TextView) rootLayout.findViewById(R.id.tv_dialog_lsh_negative);
        if (positiveBtnVisible) {
            tvPositive.setVisibility(View.VISIBLE);
            if (!isEmpty(positiveText)) {
                tvPositive.setText(positiveText);
            } else {
                tvPositive.setText("确认");
            }
            tvPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPositiveListener != null) {
                        mOnPositiveListener.onClick(LshDialog.this);
                    } else {
                        LshDialog.this.dismiss();
                    }
                }
            });
        } else {
            tvPositive.setVisibility(View.GONE);
        }
        if (negativeBtnVisible) {
            tvNegative.setVisibility(View.VISIBLE);
            if (!isEmpty(negativeText)) {
                tvNegative.setText(negativeText);
            } else {
                tvNegative.setText("取消");
            }
            tvNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNegativeListener != null) {
                        mOnNegativeListener.onClick(LshDialog.this);
                    } else {
                        LshDialog.this.dismiss();
                    }
                }
            });
        } else {
            tvNegative.setVisibility(View.GONE);
        }
    }

    public abstract class BaseDialogBuilder<T extends BaseDialogBuilder> implements BaseDialogInterface {

        @Override
        public LshDialog show() {
            LshDialog.this.show();
            return LshDialog.this;
        }

        public LshDialog getDialog() {
            return LshDialog.this;
        }

        protected View initView(LshDialog dialog) {
            return null;
        }

        // 根据子类需要添加不同布局
        protected void addView(LshDialog dialog, View childView) {
            FrameLayout layout = (FrameLayout) rootLayout.findViewById(R.id.fl_dialog_lsh_content);
            layout.addView(childView);
        }

        // 设置窗口宽度占屏幕短边的百分比
        protected void setDialogWidth(LshDialog dialog, float shortSidePercent) {
            ViewGroup rootView = (ViewGroup) rootLayout.findViewById(R.id.ll_dialog_lsh_root);
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            layoutParams.width = (int) (getScreenShortSide() * shortSidePercent);
        }
    }

    private class BtnDialogBuilder<T extends BtnDialogBuilder> extends BaseDialogBuilder<T> implements BtnDialogInterface {


        @Override
        protected View initView(final LshDialog dialog) {
            super.initView(dialog);
            // 设置布局宽度为屏幕短边的3/4
            setDialogWidth(dialog, 0.75F);
            return null;
        }
    }

    private class NoBtnDialogBuilder<T extends NoBtnDialogBuilder> extends BaseDialogBuilder<T> {
        @Override
        protected View initView(LshDialog dialog) {
            super.initView(dialog);
            // 设置布局宽度为屏幕短边的3/5
            setDialogWidth(dialog, 0.6F);
            return null;
        }
    }

    public class TextDialogBuilder extends BtnDialogBuilder<TextDialogBuilder> implements TextDialogInterface {

        private String content;

        @Override
        public TextDialogBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        @Override
        protected View initView(LshDialog dialog) {
            super.initView(dialog);
            // 生成TextView
            TextView textView = new TextView(dialog.getContext());
            textView.setText(content == null ? "" : content);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            addView(dialog, textView);
            return textView;
        }
    }

    public class ListDialogBuilder extends NoBtnDialogBuilder<ListDialogBuilder> implements ListDialogInterface<ListDialogBuilder, String> {
        private List<String> list;
        private OnItemClickListener mOnItemClickListener;
        private ListDialogAdapter adapter;


        @Override
        public ListDialogBuilder setList(List<String> list) {
            this.list = list;
            if (adapter != null) {
                adapter.data = list;
            }
            return this;
        }

        @Override
        public ListDialogBuilder setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
            if (adapter != null) {
                adapter.mOnItemClickListener = listener;
            }
            return this;
        }

        @Override
        protected View initView(LshDialog dialog) {
            super.initView(dialog);
            // 生成RecyclerView
            RecyclerView recyclerView = new RecyclerView(dialog.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);
            recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
            adapter = new ListDialogAdapter(list, mOnItemClickListener);
            recyclerView.setAdapter(adapter);
            addView(dialog, recyclerView);
            return recyclerView;
        }
    }

    private class ListDialogAdapter extends RecyclerView.Adapter implements View.OnClickListener {
        private List<String> data;
        private OnItemClickListener mOnItemClickListener;

        public ListDialogAdapter(List<String> list, OnItemClickListener listener) {
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
            // 添加分割线
            View line = new View(parent.getContext());
            line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            line.setBackgroundColor(Color.parseColor("#D9D9D9"));
            // 添加到布局
            linearLayout.addView(textView);
            linearLayout.addView(line);
            BackgroundUtilsEx.addPressedEffect(linearLayout);
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
                mOnItemClickListener.onClick(LshDialog.this, position);
            }
        }
    }

    public class SelectDialogBuilder extends NoBtnDialogBuilder<SelectDialogBuilder> implements SelectDialogInterface {
        private String[] items;
        private OnItemClickListener mOnItemClickListener;

        @Override
        public SelectDialogBuilder setItem(String[] items) {
            this.items = items;
            return this;
        }

        @Override
        public SelectDialogBuilder setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
            return this;
        }

        @Override
        protected View initView(LshDialog dialog) {
            super.initView(dialog);
            // 生成RadioGroup
            RadioGroup radioGroup = new RadioGroup(dialog.getContext());
            radioGroup.setPadding(dp2px(10), 0, dp2px(10), 0);
            // 添加RadioButton
            if (items != null && items.length > 0) {
                for (int i = 0; i < items.length; i++) {
                    // 设置RadioButton的参数
                    RadioButton radioButton = new RadioButton(dialog.getContext());
                    radioButton.setText(items[i]);
                    radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    radioButton.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    radioGroup.addView(radioButton);
                    if (mOnItemClickListener != null) {
                        final int finalI = i;
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnItemClickListener.onClick(LshDialog.this, finalI);
                            }
                        });
                    }
                    // 添加分割线
                    if (i != items.length - 1) {
                        View line = new View(dialog.getContext());
                        line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                        line.setBackgroundColor(Color.parseColor("#D9D9D9"));
                        radioGroup.addView(line);
                    }
                }
            }
            addView(dialog, radioGroup);
            return radioGroup;
        }
    }

    public class InputDialogBuilder extends BaseDialogBuilder<InputDialogBuilder> implements InputDialogInterface {
        private String hint;

        @Override
        public InputDialogBuilder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        @Override
        protected View initView(LshDialog dialog) {
            super.initView(dialog);
            // 添加输入框
            final EditText editText = new EditText(dialog.getContext());
            if (!isEmpty(hint)) {
                editText.setHint(hint);
            }
            editText.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(dialog, editText);
            return editText;
        }
    }

    public interface BaseDialogInterface<T extends BaseDialogBuilder> {
        LshDialog getDialog();

        LshDialog show();
    }

    private interface BtnDialogInterface<T extends BtnDialogBuilder> {
    }

    private interface TextDialogInterface<T extends TextDialogBuilder> extends BtnDialogInterface {
        T setContent(String content);
    }

    private interface ListDialogInterface<T extends ListDialogBuilder, S> extends BaseDialogInterface {
        T setList(List<S> list);

        T setOnItemClickListener(OnItemClickListener listener);
    }

    private interface SelectDialogInterface<T extends SelectDialogBuilder> extends BaseDialogInterface {
        T setItem(String[] items);

        T setOnItemClickListener(OnItemClickListener listener);
    }

    private interface InputDialogInterface<T extends InputDialogBuilder> extends BaseDialogInterface {
        T setHint(String hint);
    }

    public interface OnPositiveListener {
        void onClick(LshDialog dialog);
    }

    public interface OnNegativeListener {
        void onClick(LshDialog dialog);
    }

    public interface OnInputPositiveListener {
        void onClick(LshDialog dialog, String inputText);
    }

    public interface OnInputNegativeListener {
        void onClick(LshDialog dialog, String inputText);
    }

    public interface OnItemClickListener {
        void onClick(LshDialog dialog, int index);
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
}
