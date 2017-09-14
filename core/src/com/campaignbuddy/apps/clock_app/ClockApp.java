package com.campaignbuddy.apps.clock_app;

import com.campaignbuddy.apps.clock_app.components.AnalogClock;
import com.campaignbuddy.apps.clock_app.components.Clock;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.*;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.meta.SwitchEvent;
import com.campaignbuddy.state.meta.App;
import com.campaignbuddy.state.meta.State;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/27/17.
 */
public class ClockApp extends App {

    private int minutes;
    private int hours;
    private int seconds;

    private Clock clock;

    private Switch showingHotbar;
    private Text showingHotbarText;

    private TextButton add;
    private DropDownList incrimentType;
    private NumberTextField incrimentAmount;

    private TextButton set;
    private NumberTextField hour;
    private Text smColon;
    private NumberTextField minute;
    private Text mhColon;
    private NumberTextField second;

    private Text time;

    public ClockApp(String background) {
        super(background,"Clock","images/clock-app/clock-app-logo.png");

        loadPrefs();

        clock = new AnalogClock(0,0,minutes,hours);
        clock.setPosition(CampaignBuddyMain.WIDTH-clock.getWidth()-10,CampaignBuddyMain.HEIGHT/2-clock.getHeight()/2);

        InteractableList group1 = new InteractableList(0,0);

        add = new TextButton(0, 0, "Add", "images/main/button.png", "fonts/header-small.fnt", new Event() {
            @Override
            public void onCall() {
                add();
            }
        });
        incrimentAmount = new NumberTextField(add.getWidth()+20,0,"images/textbox.png","fonts/header-big.fnt",80,camera,viewport);
        incrimentAmount.setClearOnEdit(true);
        incrimentType = new DropDownList(incrimentAmount.getX()+incrimentAmount.getWidth()+20,0,"images/spritesheets/","dropdown","fonts/header-small.fnt",State.camera, State.viewport);
        incrimentType.setOptions(new String[] {
                "minute(s)",
                "hour(s)",
                "second(s)",
        });
        time = new Text(0,0,"fonts/header-big.fnt","The time is " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + " (" + seconds + "s)");

        time.setY(time.getHeight()+10);
        time.setX(10);

        set = new TextButton(0, add.getHeight() + 25, "Set", "images/main/button.png", "fonts/header-small.fnt", new Event() {
            @Override
            public void onCall() {
                set();
            }
        });
        hour = new NumberTextField(set.getWidth()+20,set.getY(),"images/textbox.png","fonts/header-big.fnt",80,camera,viewport);
        mhColon = new Text(hour.getX() + hour.getWidth() + 5,0,"fonts/header-small.fnt",":");
        mhColon.setY(set.getY()+mhColon.getHeight());
        minute = new NumberTextField(mhColon.getX()+mhColon.getWidth()+5,set.getY(),"images/textbox.png","fonts/header-big.fnt",80,camera,viewport);
        smColon = new Text(minute.getX()+minute.getWidth(),0,"fonts/header-small.fnt",":");
        smColon.setY(set.getY()+mhColon.getHeight());
        second = new NumberTextField(smColon.getX()+smColon.getWidth()+5,set.getY(),"images/textbox.png","fonts/header-big.fnt",80,camera,viewport);

        hour.setClearOnEdit(true);
        minute.setClearOnEdit(true);
        second.setClearOnEdit(true);

        group1.add(set,hour,mhColon,smColon,second,minute,add,incrimentAmount,incrimentType);
        group1.setOriginCorner();
        group1.setPosition(10,CampaignBuddyMain.HEIGHT/2-group1.getHeight()/2);

        showingHotbarText = new Text(10,0,"fonts/header-small.fnt","Show in Hotbar: ");
        showingHotbarText.setY(time.getY()+showingHotbarText.getHeight()+10);
        showingHotbar = new Switch(0,time.getY()+10,"images/spritesheets/","checkbox",prefs.getBoolean("showingHotbar"));
        showingHotbar.setOnSwitch(new SwitchEvent() {
            @Override
            public void onSwitch(boolean state) {
                prefs.put("showingHotbar",state);
                save();
            }
        });
        showingHotbar.setState(prefs.getBoolean("showingHotbar"));
        showingHotbar.setX(showingHotbarText.getX()+showingHotbarText.getWidth());

        list.add(showingHotbar,showingHotbarText,clock,time,group1);
    }

    private void setTime() {
        clock.setTime(hours,minutes);
        time.setText("The time is " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + " (" + seconds + "s)");
        prefs.put("hours",hours);
        prefs.put("minutes",minutes);
        prefs.put("seconds",seconds);
        save();
    }

    private void set() {
        if (hour.getText() != "" && minute.getText() != "" && second.getText() != "") {
            if (hour.getNumber() >= 0 && hour.getNumber() < 24 && minute.getNumber() >= 0 && minute.getNumber() < 60 && second.getNumber() >= 0 && second.getNumber() < 60) {
                hours = hour.getNumber();
                minutes = minute.getNumber();
                seconds = second.getNumber();
                setTime();
            }
        }
    }

    private void add() {
        if (incrimentType.getSelectedIndex() != -1 && incrimentAmount.getText() != "") {
            if (incrimentType.getSelectedIndex() == 0) {

                //INCRIMENT BY MINUTES
                int nMinutes = incrimentAmount.getNumber();
                minutes += nMinutes;
                int nHours = (int)Math.floor(minutes/60);
                minutes = minutes%60;
                hours = (hours + nHours) % 24;


            } else if (incrimentType.getSelectedIndex() == 1){

                //INCRIMENT BY HOURS
                int nHours = incrimentAmount.getNumber();
                hours = (hours + nHours) % 24;

            } else {

                int nSeconds = incrimentAmount.getNumber();
                seconds += nSeconds;
                int nMinutes = (int)Math.floor(seconds / 60);
                seconds = seconds%60;
                minutes += nMinutes;
                int nHours = (int)Math.floor(minutes/60);
                minutes = minutes%60;
                hours = (hours + nHours) % 24;

            }
            minutes = Math.abs(minutes);
            hours = Math.abs(hours);
            setTime();
        }
    }

    public void loadPrefs() {
        if (prefs == null) {
            createPrefs();
        } else {
            try {
                setPrefs();
            } catch (Exception e) {
                createPrefs();
            }
        }
        save();
    }

    private void setPrefs() {
        minutes = prefs.getInt("minutes");
        hours = prefs.getInt("hours");
        seconds = prefs.getInt("seconds");
        System.out.println("Setting clock prefs");
        System.out.println("Minutes are " + minutes);
        setTime();
    }

    private void createPrefs() {
        System.out.println("Creating prefs for clock");
        prefs = new JSONObject();
        prefs.put("minutes",0);
        prefs.put("hours",0);
        prefs.put("seconds",0);
        prefs.put("showingHotbar",false);
        minutes = 0;
        hours = 0;
        seconds = 0;
    }

    @Override
    public ArrayList<InteractiveDrawable> getSideBar() {
        return new ArrayList<InteractiveDrawable>();
    }

    @Override
    public ArrayList<InteractiveDrawable> getQuickActionBar() {
        ArrayList<InteractiveDrawable> rList = new ArrayList<InteractiveDrawable>();

        if (prefs.getBoolean("showingHotbar")) {
            InteractableList group = new InteractableList(0,0);

            //NumberTextField inc = new TextButton

            final NumberTextField incrimentAmount = new NumberTextField(0,0,"images/textbox.png","fonts/text-big.fnt",80,State.camera,viewport);
            final Text time = new Text(0,0,"fonts/header-small.fnt",String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));


            Button addh = new Button(0, 0,"images/clock-app/addh.png", new Event() {
                @Override
                public void onCall() {
                    int nHours = incrimentAmount.getNumber();
                    hours = (hours + nHours) % 24;
                    setTime();
                    time.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                }
            });

            Button addm = new Button(addh.getWidth()+5, 0, "images/clock-app/addm.png", new Event() {
                @Override
                public void onCall() {
                    int nMinutes = incrimentAmount.getNumber();
                    minutes += nMinutes;
                    int nHours = (int)Math.floor(minutes/60);
                    minutes = minutes%60;
                    hours = (hours + nHours) % 24;
                    time.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    setTime();
                }
            });

            Button adds = new Button(addm.getX()+addm.getWidth()+5, 0, "images/clock-app/adds.png", new Event() {
                @Override
                public void onCall() {
                    int nSeconds = incrimentAmount.getNumber();
                    seconds += nSeconds;
                    int nMinutes = (int)Math.floor(seconds / 60);
                    seconds = seconds%60;
                    minutes += nMinutes;
                    int nHours = (int)Math.floor(minutes/60);
                    minutes = minutes%60;
                    hours = (hours + nHours) % 24;
                    time.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    setTime();
                }
            });

            incrimentAmount.setClearOnEdit(true);
            incrimentAmount.setPosition((adds.getX()+adds.getWidth()-addh.getX())/2-incrimentAmount.getWidth()/2,addh.getHeight()+5);
            time.setPosition((adds.getX()+adds.getWidth()-addh.getX())/2-time.getWidth()/2,incrimentAmount.getY()+incrimentAmount.getHeight()+time.getHeight()+5);

            group.add(addh,addm,adds,incrimentAmount,time);
            group.setOriginCorner();
            rList.add(group);
        }

        return rList;
    }
}
