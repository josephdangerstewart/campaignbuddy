package com.campaignbuddy.state;

import com.campaignbuddy.components.Dashboard;
import com.campaignbuddy.components.QuickActionBar;
import com.campaignbuddy.components.Sidebar;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.components.TextButton;
import com.campaignbuddy.state.meta.State;

import java.util.ArrayList;

/**
 * Created by josephstewart on 7/30/17.
 */
public class MainState extends State {

    Sidebar sidebar;
    QuickActionBar bar;
    Dashboard dashboard;

    public MainState(String backgroundName) throws IllegalArgumentException {
        super(backgroundName);

        sidebar = new Sidebar(camera,viewport);
        bar = new QuickActionBar(camera,viewport,sidebar,State.gsm.getPool());
        dashboard = new Dashboard(sidebar,bar,State.gsm.getPool(),State.gsm.getPrefs(),camera,viewport);

        TextButton back = new TextButton(0, 0, "Back", "images/main/button.png", "fonts/header-big.fnt", new Event() {
            @Override
            public void onCall() {
                State.gsm.setState(CampaignBuddyMain.MENU_STATE);
            }
        });
        back.setPosition(CampaignBuddyMain.WIDTH-10-back.getWidth(),CampaignBuddyMain.HEIGHT-10-back.getHeight());

        list.add(bar);
        list.add(sidebar);
        list.add(dashboard);
        list.add(back);
    }

    public void init() {
        String spApp = State.gsm.getPrefs().getSideBarApp();

        System.out.println(spApp);

        if (!spApp.equals("")) {
            System.out.println("INIT STATE");
            sidebar.add(State.gsm.getPool().getSideBar(spApp));
            sidebar.setHeaderText(spApp);
            bar.init();
        } else {
            sidebar.add(new ArrayList<InteractiveDrawable>());
            sidebar.setHeaderText("Campaign Buddy");
            bar.init();
        }
    }
}
