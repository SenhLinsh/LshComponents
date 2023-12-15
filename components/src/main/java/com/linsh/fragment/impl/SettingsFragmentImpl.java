package com.linsh.fragment.impl;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linsh.base.mvp.Contract;
import com.linsh.components.R;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.text.IListDialog;
import com.linsh.fragment.BaseComponentFragment;
import com.linsh.fragment.ISettingsFragment;
import com.linsh.lshutils.adapter.BaseRcvAdapterEx;
import com.linsh.lshutils.adapter.TextWatcherAdapter;
import com.linsh.lshutils.utils.BackgroundUtilsEx;
import com.linsh.lshutils.utils.StringUtilsEx;
import com.linsh.lshutils.viewholder.ViewHolderEx;
import com.linsh.utilseverywhere.DateUtils;
import com.linsh.utilseverywhere.EditTextUtils;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/06
 *    desc   :
 * </pre>
 */
public class SettingsFragmentImpl extends BaseComponentFragment<Contract.Presenter> implements ISettingsFragment {

    private final List<ItemInfo> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private BaseRcvAdapterEx<ItemInfo, ViewHolderEx> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(inflater.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        adapter = new BaseRcvAdapterEx<ItemInfo, ViewHolderEx>() {

            @Override
            public int getItemViewType(int position) {
                return getData().get(position).getDefaultValue() == null ? 0 : 1;
            }

            @Override
            protected ViewHolderEx onCreateItemViewHolder(ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_title, parent, false));
                }
                TypeViewHolder typeViewHolder = new TypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false));
                typeViewHolder.etInfo.addTextChangedListener(new TextWatcherAdapter() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        ItemInfo item = getData().get(typeViewHolder.getAbsoluteAdapterPosition());
                        item.setValue(s.toString().trim());
                    }
                });
                return typeViewHolder;
            }

            @Override
            protected void onBindItemViewHolder(ViewHolderEx holder, ItemInfo item, int position) {
                if (holder instanceof TitleViewHolder) {
                    ((TitleViewHolder) holder).tvTitle.setText(item.getKey());
                    return;
                }
                if (holder instanceof TypeViewHolder) {
                    TypeViewHolder typeViewHolder = (TypeViewHolder) holder;
                    typeViewHolder.tvName.setText(item.getKey());
                    typeViewHolder.ivTool.setVisibility(View.GONE);
                    boolean isInitial = item.getValue() == null;
                    typeViewHolder.etInfo.setText(isInitial ? "" : item.getValue());
                    String defaultValue = item.getDefaultValue();
                    if (StringUtils.isEmpty(defaultValue)) {
                        return;
                    }
                    // 匹配选择时间模式
                    Matcher matcher = Pattern.compile("<(.+?):(.+)>").matcher(defaultValue);
                    if (matcher.matches()) {
                        setTimeMode(typeViewHolder, matcher, isInitial);
                        return;
                    }
                    // 匹配多选模式
                    if (Pattern.compile("\\[(.+?)(\\|.+?)+]").matcher(defaultValue).matches()) {
                        setSelectionMode(typeViewHolder, defaultValue, isInitial);
                        return;
                    }
                    // 正常模式
                    if (isInitial) {
                        updateValue(typeViewHolder, defaultValue, false);
                    }
                }
            }
        };
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    /**
     * 选择时间
     */
    private void setTimeMode(TypeViewHolder typeViewHolder, Matcher matcher, boolean isInitial) {
        typeViewHolder.ivTool.setVisibility(View.VISIBLE);
        if (matcher.group(1).equals("time")) {
            String format = matcher.group(2);
            String[] args = format.split("\\|");
            List<String> items = new ArrayList<>();
            List<String> formats = new ArrayList<>();
            for (String arg : args) {
                if (arg.contains("yyyy")) {
                    if (arg.contains("HH")) {
                        items.add("使用当前日期+时间");
                    } else {
                        items.add("使用当前日期");
                        items.add("手动选择日期");
                        formats.add(arg);
                    }
                    formats.add(arg);
                }
            }
            if (isInitial && items.size() > 0) {
                updateValue(typeViewHolder, DateUtils.format(new Date(), formats.get(0)), false);
            }
            typeViewHolder.ivTool.setImageResource(R.drawable.ic_time);
            typeViewHolder.ivTool.setOnClickListener(v -> {
                if (items.size() == 0) {
                    return;
                }
                DialogComponents.create(getContext(), IListDialog.class)
                        .setItems(items)
                        .setOnItemClickListener((dialog, position1) -> {
                            dialog.dismiss();
                            switch (items.get(position1)) {
                                case "使用当前日期":
                                case "使用当前日期+时间":
                                    updateValue(typeViewHolder, DateUtils.format(new Date(), formats.get(position1)), true);
                                    break;
                                case "手动选择日期":
                                    DatePickerDialog pickerDialog = new DatePickerDialog(dialog.getDialog().getContext());
                                    pickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, month, dayOfMonth);
                                        updateValue(typeViewHolder, DateUtils.format(calendar.getTime(), formats.get(position1)), true);
                                    });
                                    pickerDialog.show();
                                    break;
                            }
                        })
                        .show();
            });
        }
    }

    /**
     * 多选
     */
    private void setSelectionMode(TypeViewHolder typeViewHolder, String text, boolean isInitial) {
        typeViewHolder.ivTool.setVisibility(View.VISIBLE);
        typeViewHolder.ivTool.setImageResource(R.drawable.ic_selection);
        String[] items = StringUtilsEx.trimArr(text.substring(1, text.length() - 1).split("\\|"));
        if (isInitial && items.length > 0) {
            updateValue(typeViewHolder, items[0], false);
        }
        typeViewHolder.ivTool.setOnClickListener(v -> {
            DialogComponents.create(getContext(), IListDialog.class)
                    .setItems(items)
                    .setOnItemClickListener((dialog, position) -> {
                        dialog.dismiss();
                        updateValue(typeViewHolder, items[position], true);
                    })
                    .show();
        });
    }

    private void updateValue(TypeViewHolder typeViewHolder, String value, boolean moveCursor) {
        typeViewHolder.etInfo.setText(value);
        if (moveCursor) {
            EditTextUtils.moveCursorToLast(typeViewHolder.etInfo);
        }
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        items.clear();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            items.add(new ItemInfo(entry.getKey(), null, entry.getValue()));
        }
        if (adapter != null) {
            adapter.setData(items);
            moveFocusToHeader();
        }
    }

    @Override
    public void setPropertiesGroups(Map<String, Map<String, String>> propertiesGroups) {
        for (Map.Entry<String, Map<String, String>> groupEntry : propertiesGroups.entrySet()) {
            items.add(new ItemInfo(groupEntry.getKey(), null, null));
            for (Map.Entry<String, String> valueEntry : groupEntry.getValue().entrySet()) {
                items.add(new ItemInfo(valueEntry.getKey(), null, valueEntry.getValue()));
            }
        }
        if (adapter != null) {
            adapter.setData(items);
            moveFocusToHeader();
        }
    }

    private void moveFocusToHeader() {
        // notify 之后没有及时添加 View，添加间隔来保证设置成功
        HandlerUtils.postRunnable(() -> {
            if (items.size() > 0) {
                recyclerView.scrollToPosition(0);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder instanceof TypeViewHolder) {
                    EditTextUtils.moveCursorToLast(((TypeViewHolder) viewHolder).etInfo);
                }
            }
        }, 100);
    }

    @Override
    public Map<String, String> getProperties() {
        clearTextFocus();
        HashMap<String, String> map = new LinkedHashMap<>();
        for (ItemInfo item : items) {
            map.put(item.getKey(), StringUtils.nullToEmpty(item.getValue()));
        }
        return map;
    }

    @Override
    public Map<String, Map<String, String>> getPropertiesGroups() {
        clearTextFocus();
        HashMap<String, Map<String, String>> groups = new LinkedHashMap<>();
        String title = "";
        Map<String, String> properties = null;
        for (ItemInfo item : items) {
            if (item.getDefaultValue() == null) {
                title = item.getKey();
                properties = null;
            } else {
                if (properties == null) {
                    properties = new LinkedHashMap<>();
                    groups.put(title, properties);
                }
                properties.put(item.getKey(), StringUtils.nullToEmpty(item.getValue()));
            }
        }
        return groups;
    }

    private void clearTextFocus() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            View focus = activity.getCurrentFocus();
            if (focus != null) {
                focus.clearFocus();
            }
        }
    }

    static class ItemInfo {
        private final String key;
        private String value;
        private final String defaultValue;

        public ItemInfo(String key, String value, String defaultValue) {
            this.key = key;
            this.value = value;
            this.defaultValue = StringUtils.nullToEmpty(defaultValue);
        }


        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static class TypeViewHolder extends ViewHolderEx {
        private final TextView tvName;
        private final EditText etInfo;
        private final ImageView ivTool;
        private final View vDivider;

        public TypeViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_item_type_name);
            etInfo = (EditText) itemView.findViewById(R.id.tv_item_type_info);
            vDivider = itemView.findViewById(R.id.v_item_divider);
            ivTool = itemView.findViewById(R.id.iv_tool);
            BackgroundUtilsEx.addPressedEffect(ivTool);
        }
    }

    static class TitleViewHolder extends ViewHolderEx {

        private final TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView;
        }
    }
}

