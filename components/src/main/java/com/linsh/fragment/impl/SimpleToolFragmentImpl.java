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

import com.linsh.fragment.BaseComponentFragment;
import com.linsh.fragment.ISimpleToolFragment;
import com.linsh.lshutils.adapter.SimpleRcvAdapterEx;
import com.linsh.lshutils.viewholder.ViewHolderEx;
import com.linsh.views.R;

import java.util.ArrayList;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/04/13
 *    desc   :
 * </pre>
 */
public class SimpleToolFragmentImpl extends BaseComponentFragment<ISimpleToolFragment.Presenter> implements ISimpleToolFragment {

    private LinearLayout llButtons;
    private TextView tvContent;
    private SimpleRcvAdapterEx<String, ViewHolderEx> adapter;

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
        rcvLogs.setAdapter(adapter = new SimpleRcvAdapterEx<String, ViewHolderEx>() {

            @Override
            protected String getItem(int position) {
                return getData().get(getData().size() - 1 - position);
            }

            @Override
            protected ViewHolderEx initViewHolder(ViewGroup parent, int viewType) {
                TextView itemView = new TextView(parent.getContext());
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolderEx(itemView);
            }

            @Override
            protected void onBindViewHolder(ViewHolderEx holder, String s, int position) {
                ((TextView) holder.itemView).setText(s);
            }
        });
        adapter.setData(new ArrayList<>());
    }

    @Override
    public void buildButton(String id, String name) {
        Button button = new Button(getContext());
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(name);
        button.setOnClickListener(v -> getPresenter().onButtonClick(id));
        llButtons.addView(button);
    }

    @Override
    public void setContent(String content) {
        tvContent.setText(content);
    }

    @Override
    public void addLog(String log) {
        adapter.getData().add(log);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearLogs() {
        adapter.getData().clear();
        adapter.notifyDataSetChanged();
    }
}
