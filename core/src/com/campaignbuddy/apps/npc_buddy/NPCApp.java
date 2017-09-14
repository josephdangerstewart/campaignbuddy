package com.campaignbuddy.apps.npc_buddy;

import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.state.meta.App;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by josephstewart on 9/12/17.
 */
public class NPCApp extends App {

    public NPCApp(String background) {
        super(background,"NPC Generator","images/npc-app/npc-app-icon.png");
    }

    @Override
    public void loadPrefs() {
        if (prefs == null) {
            prefs = new JSONObject();
        }
    }

    @Override
    public ArrayList<InteractiveDrawable> getSideBar() {
        return new ArrayList<InteractiveDrawable>();
    }

    @Override
    public ArrayList<InteractiveDrawable> getQuickActionBar() {
        ArrayList<InteractiveDrawable> list = new ArrayList<InteractiveDrawable>();
        return list;
    }
}
