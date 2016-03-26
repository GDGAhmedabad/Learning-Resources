package com.rrr.databindingonclick.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by RRR on 26-03-2016.
 */
public class User extends BaseObservable {
    private String firstName, lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(com.rrr.databindingonclick.BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(com.rrr.databindingonclick.BR.lastName);
    }
}
