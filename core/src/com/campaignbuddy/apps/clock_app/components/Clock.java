package com.campaignbuddy.apps.clock_app.components;

import com.campaignbuddy.resources.meta.InteractiveDrawable;

/**
 * Created by josephstewart on 8/27/17.
 */
public abstract class Clock extends InteractiveDrawable {
    public abstract void setTime(int hours, int minutes);

    public double getHourRotation(int hours, int minutes) {
        return .5 * (60*hours+minutes) * -1;
    }

    public double getMinuteRotation(int minutes) {
        return minutes*-6;
    }
}
