package com.linsh.fragment.impl;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linsh.adapter.holder.SimpleTextViewHolder;
import com.linsh.base.adapter.DataAdaptedRcvAdapter;
import com.linsh.components.R;
import com.linsh.fragment.BaseComponentFragment;
import com.linsh.fragment.ISimpleToolFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/04/13
 *    desc   :
 * </pre>
 */
public class SimpleToolFragmentImpl extends BaseComponentFragment<ISimpleToolFragment.Presenter> implements ISimpleToolFragment {

    private final List<String> logs = new ArrayList<>();
    private DataAdaptedRcvAdapter adapter;
    private LinearLayout llButtons;
    private TextView tvContent;

    @Override
    protected int onCreateViewByLayoutId() {
        return R.layout.components_layout_fragment_simple_tool;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llButtons = view.findViewById(R.id.components_ll_buttons);
        tvContent = view.findViewById(R.id.components_tv_content);
        RecyclerView rcvLogs = view.findViewById(R.id.components_rcv_logs);
        rcvLogs.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvLogs.setAdapter(adapter = new DataAdaptedRcvAdapter(SimpleTextViewHolder.class));
        adapter.setData(logs);
    }

    @Override
    public void buildOrUpdateButton(@NonNull String id, @NonNull String name) {
        Button button = llButtons.findViewWithTag(id);
        if (button != null) {
            button.setText(name);
            return;
        }
        button = new Button(getContext());
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(name);
        button.setTag(id);
        button.setOnClickListener(v -> getPresenter().onButtonClick(id));
        llButtons.addView(button);
    }

    @Override
    public void setContent(@NonNull String content) {
        tvContent.setText(content);
    }

    @Override
    public void addLog(@NonNull String log) {
        logs.add(0, log);
        adapter.notifyItemInserted(0);
    }

    @Override
    public void clearLogs() {
        logs.clear();
        adapter.notifyDataSetChanged();
    }
}
