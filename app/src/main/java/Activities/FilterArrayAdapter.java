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

import android.app.Dialog;
import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.Filter;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.searchbox.core.Search;

//created by users Ironman, Marshall Asch from https://stackoverflow.com/questions/38417984/android-spinner-dropdown-checkbox
//and edited by CMPUT301F18T20
public class FilterArrayAdapter extends ArrayAdapter<FilterCheckBoxState> {
    private Context context;
    private ArrayList<FilterCheckBoxState> stateList;
    private FilterArrayAdapter adapter;
    private boolean isFromView = false;
    private CheckBox problemBox;
    private CheckBox recordBox;
    private CheckBox patientRecordBox;
    private CheckBox bodyBox;
    private CheckBox geoBox;
    private String chosenBodyLocation;

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
        if (position == 1){
            this.problemBox = (CheckBox)convertView.findViewWithTag(1);
            this.problemBox.setChecked(true);
        }
        if (position ==2){
            this.recordBox = (CheckBox)convertView.findViewWithTag(2);
        }
        if(position ==3){
            this.patientRecordBox = (CheckBox)convertView.findViewWithTag(3);
        }
        if (position ==4){
            this.bodyBox = (CheckBox)convertView.findViewWithTag(4);
        }
        if(position ==5){
            this.geoBox = (CheckBox)convertView.findViewWithTag(5);
        }
        holder.checkbox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer)buttonView.getTag();
                if(!isFromView){
                    stateList.get(position).setSelected(isChecked);
                    //if bodylocation or geolocation is checked
                    if(position == 1 && isChecked){
                        stateList.get(4).setSelected(false);
                        stateList.get(5).setSelected(false);
                        FilterArrayAdapter.this.bodyBox.setChecked(false);
                        FilterArrayAdapter.this.geoBox.setChecked(false);
                    }
                    if(position == 2 && isChecked){
                        stateList.get(4).setSelected(false);
                        stateList.get(5).setSelected(false);
                        FilterArrayAdapter.this.bodyBox.setChecked(false);
                        FilterArrayAdapter.this.geoBox.setChecked(false);
                    }
                    if(position == 4 && isChecked){
                        stateList.get(1).setSelected(false);
                        stateList.get(2).setSelected(false);
                        FilterArrayAdapter.this.problemBox.setChecked(false);
                        FilterArrayAdapter.this.recordBox.setChecked(false);
                        FilterArrayAdapter.this.patientRecordBox.setChecked(true);
                        showBodyListDialogue();


                    } else if(position == 5 && isChecked){
                        stateList.get(1).setSelected(false);
                        stateList.get(2).setSelected(false);
                        FilterArrayAdapter.this.problemBox.setChecked(false);
                        FilterArrayAdapter.this.recordBox.setChecked(false);
                        FilterArrayAdapter.this.patientRecordBox.setChecked(true);
                    }
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
    public ArrayList<CheckBox> getCheckBoxes(){
        ArrayList<CheckBox> buttonList = new ArrayList<>();
        buttonList.add(this.problemBox);
        buttonList.add(this.recordBox);
        buttonList.add(this.patientRecordBox);
        buttonList.add(this.bodyBox);
        buttonList.add(this.geoBox);

        return buttonList;
    }
    public String getChosenBodyLocation(){ return this.chosenBodyLocation;}

    private void showBodyListDialogue() {
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bodylocation_radiogroup);
        ArrayList<String> bodyList = new ArrayList<>();
        bodyList.addAll(Arrays.asList("head","chest","upperBack"
                ,"leftArm","leftHand","rightArm"
                ,"rightHand","abs","lowerBack"
                ,"leftLeg","leftFoot","rightLeg"
                ,"rightFoot"));

        RadioGroup group = (RadioGroup)dialog.findViewById(R.id.body_location_radioGroup);
        for(int i=0;i <bodyList.size() ;i++){
            RadioButton button = new RadioButton(this.context);
            button.setText(bodyList.get(i));
            group.addView(button);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int index= group.indexOfChild(radioButton);
                RadioButton button = (RadioButton)group.getChildAt(index);
                FilterArrayAdapter.this.chosenBodyLocation = button.getText().toString();
            }
        });
        dialog.show();
    }

}
