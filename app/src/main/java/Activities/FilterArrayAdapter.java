/*
 * Class name: FilterArrayAdapter
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 03/12/18 1:27 AM
 *
 * Last Modified: 03/12/18 1:27 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;
import java.util.List;

//created by users Ironman, Marshall Asch from https://stackoverflow.com/questions/38417984/android-spinner-dropdown-checkbox
public class FilterArrayAdapter extends ArrayAdapter<FilterCheckBoxState> {
    private Context context;
    private ArrayList<FilterCheckBoxState> stateList;
    private FilterArrayAdapter adapter;
    private boolean isFromView = false;

    public FilterArrayAdapter(Context context, int resource, List<FilterCheckBoxState> items){
        super(context, resource, items);
        this.context = context;
        this.stateList = (ArrayList) items;
        this.adapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.filter_item_list,null);
            holder = new ViewHolder();
            holder.textview = (TextView)convertView.findViewById(R.id.filter_text);
            holder.checkbox = (CheckBox)convertView.findViewById(R.id.filter_checkbox);
            convertView.setTag(holder);

        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textview.setText(stateList.get(position).getTitle());

        //check if event is from getView or user input
        isFromView = true;
        holder.checkbox.setChecked(stateList.get(position).isSelected());
        isFromView = false;
        if (position == 0){
            holder.checkbox.setVisibility(View.INVISIBLE);
        }else{
            holder.checkbox.setVisibility(View.VISIBLE);
        }
        holder.checkbox.setTag(position);
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer)buttonView.getTag();
                if(!isFromView){
                    stateList.get(position).setSelected(isChecked);
                }
            }
        });
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    private class ViewHolder{
        private TextView textview;
        private CheckBox checkbox;


    }
}
