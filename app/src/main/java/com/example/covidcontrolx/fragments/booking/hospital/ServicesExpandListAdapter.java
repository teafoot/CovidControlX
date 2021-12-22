package com.example.covidcontrolx.fragments.booking.hospital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.covidcontrolx.R;

import java.util.ArrayList;
import java.util.List;

public class ServicesExpandListAdapter extends BaseExpandableListAdapter {
    private ArrayList<ArrayList<String>> groupList = new ArrayList<>();

    String[] groupData = {"Hospital Services"};
    String[] childData = {"Vaccination", "PCR Test", "Rapid Test"};

    Context context;
    ArrayList<ArrayList<Boolean>> selectedChildCheckBoxes = new ArrayList<>();
    ArrayList<Boolean> selectedParentCheckBoxes = new ArrayList<>();
    ServicesListener servicesListener;

    public void setListener(ServicesListener servicesListener) {
        this.servicesListener = servicesListener;
    }

    class ExpandableListViewHolder {
        public CheckBox groupCheckBox;
        public TextView groupTextView; // View to expand or shrink the list
        public CheckBox childCheckBox;
    }

    public ServicesExpandListAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < groupData.length; i++) {
            ArrayList<String> services = new ArrayList<>();
            for (int j = 0; j < childData.length; j++) {
                services.add(childData[j]);
            }
            groupList.add(i, services);
        }
        initCheckStates(true);
    }

    private void initCheckStates(boolean defaultState) {
        for (int i = 0; i < groupList.size(); i++) {
            selectedParentCheckBoxes.add(i, defaultState);
            ArrayList<Boolean> childStates = new ArrayList<>();
            for (int j = 0; j < groupList.get(i).size(); j++) {
                childStates.add(defaultState);
            }
            selectedChildCheckBoxes.add(i, childStates);
        }
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View groupView, ViewGroup parent) {
        ExpandableListViewHolder holder;
        if (groupView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            groupView = inflater.inflate(R.layout.search_services_group_layout, null);

            holder = new ExpandableListViewHolder();
            holder.groupCheckBox = (CheckBox) groupView.findViewById(R.id.group_chk_box);
            holder.groupTextView = (TextView) groupView.findViewById(R.id.group_txt_view);
            groupView.setTag(holder);
        } else {
            holder = (ExpandableListViewHolder) groupView.getTag();
        }

        holder.groupCheckBox.setText(groupData[groupPosition]);
        if (selectedParentCheckBoxes.size() <= groupPosition) {
            selectedParentCheckBoxes.add(groupPosition, false);
        } else {
            holder.groupCheckBox.setChecked(selectedParentCheckBoxes.get(groupPosition));
        }

        holder.groupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded)
                    servicesListener.expandGroupEvent(groupPosition, isExpanded);

                boolean state = selectedParentCheckBoxes.get(groupPosition);
                selectedParentCheckBoxes.remove(groupPosition);
                selectedParentCheckBoxes.add(groupPosition, !state);
                for (int i = 0; i < groupList.get(groupPosition).size(); i++) {
                    selectedChildCheckBoxes.get(groupPosition).remove(i);
                    selectedChildCheckBoxes.get(groupPosition).add(i, !state);
                }
                notifyDataSetChanged();
                showServices(groupPosition);
            }
        });

        holder.groupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesListener.expandGroupEvent(groupPosition, isExpanded);
            }
        });

        return groupView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View childView, ViewGroup parent) {
        ExpandableListViewHolder holder;
        if (childView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = inflater.inflate(R.layout.search_services_child_layout, null);

            holder = new ExpandableListViewHolder();
            holder.childCheckBox = (CheckBox) childView.findViewById(R.id.child_check_box);
            childView.setTag(holder);
        } else {
            holder = (ExpandableListViewHolder) childView.getTag();
        }

        holder.childCheckBox.setText(groupList.get(groupPosition).get(childPosition));
        if (selectedChildCheckBoxes.size() <= groupPosition) {
            ArrayList<Boolean> childState = new ArrayList<>();
            for (int i = 0; i < groupList.get(groupPosition).size(); i++) {
                if (childState.size() > childPosition)
                    childState.add(childPosition, false);
                else
                    childState.add(false);
            }
            if (selectedChildCheckBoxes.size() > groupPosition) {
                selectedChildCheckBoxes.add(groupPosition, childState);
            } else
                selectedChildCheckBoxes.add(childState);
        } else {
            holder.childCheckBox.setChecked(selectedChildCheckBoxes.get(groupPosition).get(childPosition));
        }

        holder.childCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = selectedChildCheckBoxes.get(groupPosition).get(childPosition);
                selectedChildCheckBoxes.get(groupPosition).remove(childPosition);
                selectedChildCheckBoxes.get(groupPosition).add(childPosition, !state);
                showServices(groupPosition);

                //uncheck parent group if none of its children are selected
                boolean allUnchecked = true;
                for (int i = 0; i < selectedChildCheckBoxes.get(0).size(); i++) {
                    if (selectedChildCheckBoxes.get(0).get(i)) {
                        allUnchecked = false;
                        break;
                    }
                }
                if (allUnchecked) {
                    selectedParentCheckBoxes.remove(0);
                    selectedParentCheckBoxes.add(0, false);
                    for (int i = 0; i < groupList.get(0).size(); i++) {
                        selectedChildCheckBoxes.get(0).remove(i);
                        selectedChildCheckBoxes.get(0).add(i, false);
                    }
                    notifyDataSetChanged();
                    showServices(0);
                }
            }
        });

        return childView;
    }

    private void showServices(int groupPosition) {
        List<String> services = new ArrayList<>();
        for (int i = 0; i < selectedChildCheckBoxes.size(); i++) {
            for (int j = 0; j < selectedChildCheckBoxes.get(groupPosition).size(); j++) {
                if (selectedChildCheckBoxes.get(i).get(j)) {
                    services.add(groupList.get(i).get(j));
                }
            }
        }
        servicesListener.onServicesChanged(services);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
