package com.campaignbuddy.state.meta;

import com.campaignbuddy.resources.components.Button;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/4/17.
 */
public class AppPool {

    public static final int WIDTH = 150;
    public static final int HEIGHT = 150;

    private ArrayList<App> apps;

    public AppPool() {
        apps = new ArrayList<App>();
    }

    public void add(App app) {
        apps.add(app);
    }

    public ArrayList<InteractiveDrawable> getSideBar(String app) {
        System.out.println("apps.size() = " + apps.size());
        for (int i = 0; i < apps.size(); i++) {
            if (apps.get(i).getName().equals(app)) {
                return apps.get(i).getSideBar();
            }
        }
        return new ArrayList<InteractiveDrawable>();
    }

    public void init() {
        for (int i = 0; i <apps.size(); i++) {
            apps.get(i).init();
        }
    }

    public ArrayList<InteractiveDrawable> getApps() {

        ArrayList<InteractiveDrawable> logos = new ArrayList<InteractiveDrawable>();


        System.out.println("apps.size() = " + apps.size());
        for (int i = 0; i < apps.size(); i ++) {
            final int index = i;
            Button button = new Button(0, 0, apps.get(i).getLogo(), new Event() {
                @Override
                public void onCall() {
                    launchApp(apps.get(index).getName());
                }
            });

            button.setSize(WIDTH,HEIGHT);
            logos.add(button);
        }

        return logos;
    }

    public void launchApp(String app) {
        for (int i = 0; i < apps.size(); i++) {
            if (apps.get(i).getName().equals(app)) {
                State.gsm.setState(apps.get(i).getID());
            }
        }
    }

    public ArrayList<InteractiveDrawable> getQuickActionBar() {
        ArrayList<InteractiveDrawable> rList = new ArrayList<InteractiveDrawable>();

        for (int i = 0; i < apps.size(); i++) {
            ArrayList<InteractiveDrawable> qa = apps.get(i).getQuickActionBar();

            for (int j = 0; j < qa.size(); j++) {
                rList.add(qa.get(j));
            }
        }

        return rList;
    }

}
