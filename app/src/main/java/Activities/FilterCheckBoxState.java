/*
 * Class name: FilterCheckBoxState
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 03/12/18 1:24 AM
 *
 * Last Modified: 03/12/18 1:24 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

public class FilterCheckBoxState {
    private String title;
    private boolean selected;

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public boolean isSelected(){
        return this.selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
