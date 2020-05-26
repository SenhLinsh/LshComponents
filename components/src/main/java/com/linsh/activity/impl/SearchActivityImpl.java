package com.linsh.activity.impl;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linsh.activity.ISearchActivity;
import com.linsh.adapter.recycler.SingleItemTypeRcvAdapter;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.lshutils.decoration.DividerItemDecoration;
import com.linsh.lshutils.utils.Dps;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.interfaces.Function;
import com.linsh.view.item.ITextItemView;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/26
 *    desc   :
 * </pre>
 */
public class SearchActivityImpl extends IActivityViewImpl<ISearchActivity.Presenter> implements ISearchActivity.View,
        ActivitySubscribe.OnCreate, ActivitySubscribe.OnCreateOptionsMenu {

    private SearchAdapter adapter;
    private Function converter;

    @Override
    public void setResults(List<? extends CharSequence> results) {
        adapter.setData(results);
        this.converter = null;
    }

    @Override
    public <T> void setResults(List<T> results, Function<CharSequence, T> converter) {
        adapter.setData(results);
        this.converter = converter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setTitle("搜索");
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(Dps.toPx(1), 0x11333333));
        adapter = new SearchAdapter();
        adapter.setOnItemClickListener(new SingleItemTypeRcvAdapter.OnItemClickListener<Object, ITextItemView>() {
            @Override
            public void onItemClick(ITextItemView viewHelper, Object item, int position) {
                getPresenter().onItemClick(position);
            }
        });
        adapter.setOnItemLongClickListener(new SingleItemTypeRcvAdapter.OnItemLongClickListener<Object, ITextItemView>() {
            @Override
            public void onItemLongClick(ITextItemView viewHelper, Object item, int position) {
                getPresenter().onItemLongClick(position);
            }
        });
        recyclerView.setAdapter(adapter);
        getActivity().setContentView(recyclerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView = new SearchView(getActivity());
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            private String curNewText = "";
            private Runnable mRunnable = new Runnable() {
                @Override
                public void run() {
                    getPresenter().search(curNewText);
                }
            };

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals(curNewText)) {
                    curNewText = query.trim();
                    HandlerUtils.removeRunnable(mRunnable);
                    HandlerUtils.postRunnable(mRunnable);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.trim();
                if (!newText.equals(curNewText)) {
                    curNewText = newText.trim();
                    HandlerUtils.removeRunnable(mRunnable);
                    HandlerUtils.postRunnable(mRunnable, 500);
                }
                return true;
            }
        });

        menu.add("搜索")
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    private class SearchAdapter extends SingleItemTypeRcvAdapter<Object, ITextItemView> {

        @Override
        public void setData(List data) {
            super.setData(data);
        }

        @Override
        public void onBindViewHelper(ITextItemView viewHelper, Object item, int position) {
            if (converter == null) {
                viewHelper.setText((CharSequence) item);
            } else {
                viewHelper.setText((CharSequence) converter.call(item));
            }
        }
    }
}
