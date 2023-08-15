package com.linsh.fragment.impl;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.text.IListDialog;
import com.linsh.fragment.BaseComponentFragment;
import com.linsh.fragment.ISettingsFragment;
import com.linsh.lshutils.adapter.BaseRcvAdapterEx;
import com.linsh.lshutils.utils.BackgroundUtilsEx;
import com.linsh.lshutils.viewholder.ViewHolderEx;
import com.linsh.utilseverywhere.DateUtils;
import com.linsh.utilseverywhere.EditTextUtils;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.StringUtils;
import com.linsh.components.R;

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

    private final List<Object> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private BaseRcvAdapterEx<Object, ViewHolderEx> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(inflater.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        adapter = new BaseRcvAdapterEx<Object, ViewHolderEx>() {

            @Override
            public int getItemViewType(int position) {
                return getData().get(position) instanceof String ? 0 : 1;
            }

            @Override
            protected ViewHolderEx onCreateItemViewHolder(ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_title, parent, false));
                }
                return new TypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false));
            }

            @Override
            protected void onBindItemViewHolder(ViewHolderEx holder, Object item, int position) {
                if (holder instanceof TitleViewHolder) {
                    ((TitleViewHolder) holder).tvTitle.setText(item.toString());
                    return;
                }
                if (holder instanceof TypeViewHolder && item instanceof Map.Entry) {
                    TypeViewHolder typeViewHolder = (TypeViewHolder) holder;
                    typeViewHolder.tvName.setText(((Map.Entry<?, ?>) item).getKey().toString());
                    String value = StringUtils.nullToEmpty(((Map.Entry<?, ?>) item).getValue()).trim();
                    Matcher matcher = Pattern.compile("<(.+?):(.+)>").matcher(value);
                    if (matcher.matches()) {
                        setTimeMode(typeViewHolder, matcher);
                    } else if ((matcher = Pattern.compile("\\[(.+)(\\|.+)+]").matcher(value)).matches()) {
                        setSelectionMode(typeViewHolder, matcher);
                    } else {
                        typeViewHolder.ivTool.setVisibility(View.GONE);
                        typeViewHolder.etInfo.setText(value);
                        EditTextUtils.moveCursorToLast(typeViewHolder.etInfo);
                    }
                    typeViewHolder.etInfo.setOnFocusChangeListener((v, hasFocus) -> {
                        ((Map.Entry) item).setValue(typeViewHolder.etInfo.getText().toString().trim());
                    });
                }
            }
        };
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    private void setTimeMode(TypeViewHolder typeViewHolder, Matcher matcher) {
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
            if (items.size() > 0) {
                typeViewHolder.etInfo.setText(DateUtils.format(new Date(), formats.get(0)));
                EditTextUtils.moveCursorToLast(typeViewHolder.etInfo);
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
                                    typeViewHolder.etInfo.setText(DateUtils.format(new Date(), formats.get(position1)));
                                    EditTextUtils.moveCursorToLast(typeViewHolder.etInfo);
                                    break;
                                case "手动选择日期":
                                    DatePickerDialog pickerDialog = new DatePickerDialog(dialog.getDialog().getContext());
                                    pickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, month, dayOfMonth);
                                        typeViewHolder.etInfo.setText(DateUtils.format(calendar.getTime(), formats.get(position1)));
                                        EditTextUtils.moveCursorToLast(typeViewHolder.etInfo);
                                    });
                                    pickerDialog.show();
                                    break;
                            }
                        })
                        .show();
            });
        }
    }

    private void setSelectionMode(TypeViewHolder typeViewHolder, Matcher matcher) {
        typeViewHolder.ivTool.setVisibility(View.VISIBLE);
        typeViewHolder.ivTool.setImageResource(R.drawable.ic_selection);
        String[] items = new String[matcher.groupCount()];
        items[0] = matcher.group(1);
        for (int i = 1; i < matcher.groupCount(); i++) {
            items[i] = matcher.group(i + 1).substring(1);
        }
        typeViewHolder.ivTool.setOnClickListener(v -> {
            DialogComponents.create(getContext(), IListDialog.class)
                    .setItems(items)
                    .setOnItemClickListener((dialog, position) -> {
                        dialog.dismiss();
                        typeViewHolder.etInfo.setText(items[position]);
                        EditTextUtils.moveCursorToLast(typeViewHolder.etInfo);
                    })
                    .show();
        });
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        items.clear();
        items.addAll(properties.entrySet());
        if (adapter != null) {
            adapter.setData(items);
            moveFocusToHeader();
        }
    }

    @Override
    public void setPropertiesGroups(Map<String, Map<String, String>> propertiesGroups) {
        for (Map.Entry<String, Map<String, String>> entry : propertiesGroups.entrySet()) {
            items.add(entry.getKey());
            items.addAll(entry.getValue().entrySet());
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
        for (Object item : items) {
            if (item instanceof Map.Entry) {
                map.put((String) ((Map.Entry<?, ?>) item).getKey(), (String) ((Map.Entry<?, ?>) item).getValue());
            }
        }
        return map;
    }

    @Override
    public Map<String, Map<String, String>> getPropertiesGroups() {
        clearTextFocus();
        HashMap<String, Map<String, String>> groups = new LinkedHashMap<>();
        String title = "";
        Map<String, String> properties = null;
        for (Object item : items) {
            if (item instanceof String) {
                title = (String) item;
                properties = null;
            } else if (item instanceof Map.Entry) {
                if (properties == null) {
                    properties = new LinkedHashMap<>();
                    groups.put(title, properties);
                }
                properties.put((String) ((Map.Entry<?, ?>) item).getKey(), (String) ((Map.Entry<?, ?>) item).getValue());
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
