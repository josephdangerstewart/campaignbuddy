package com.campaignbuddy.state.meta;

import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.Text;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.components.TextButton;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/4/17.
 */
public abstract class App extends State {

    protected String logo;
    protected String name;
    protected JSONObject prefs;
    private int id;

    public App(String backgroundName,String name, String logo) throws IllegalArgumentException {
        super(backgroundName);
        this.name = name;
        this.logo = logo;
        TextButton back = new TextButton(10, CampaignBuddyMain.HEIGHT - 70, "Back", "images/main/button.png", "fonts/header-small.fnt", new Event() {
            @Override
            public void onCall() {
                State.gsm.setState(CampaignBuddyMain.MAIN_STATE);
            }
        });
        list.add(back);


        prefs = State.gsm.getPrefs().getAppPrefs(name);
    }

    public abstract void loadPrefs();

    public JSONObject getPrefs() {
        return prefs;
    }

    @Override
    public void init() {
        System.out.println("PREFS");
        prefs = State.gsm.getPrefs().getAppPrefs(this.name);
        loadPrefs();
    }

    public void save() {
        System.out.println("SAVING '" + name + "'");
        State.gsm.getPrefs().saveAppPrefs(name,prefs);
    }

    public String resolveName() {return name;}

    public String getName() {
        return name;
    }

    public String getLogo() { return logo; }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public abstract ArrayList<InteractiveDrawable> getSideBar();
    public abstract ArrayList<InteractiveDrawable> getQuickActionBar();
}
