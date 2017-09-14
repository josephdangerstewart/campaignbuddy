package com.campaignbuddy.state;

import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.components.Image;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.components.TextButton;
import com.campaignbuddy.state.meta.State;

/**
 * Created by josephstewart on 8/23/17.
 */
public class MenuState extends State {

    public MenuState(String background) {
        super(background);

        Image logo = new Image("images/menu/cblogo.png");
        logo.setPosition(CampaignBuddyMain.WIDTH/2+CampaignBuddyMain.WIDTH/4-logo.getWidth()/2,CampaignBuddyMain.HEIGHT/2-logo.getHeight()/2);
        list.add(logo);

        TextButton continueCampaign = new TextButton(0,0,"Select Campaign","images/main/button.png","fonts/header-big.fnt",new Event() {
            @Override
            public void onCall() {
                State.gsm.setState(CampaignBuddyMain.CONTINUE_STATE);
            }
        });
        continueCampaign.setPosition(0-continueCampaign.getWidth()/2,0);

        TextButton newCampaign = new TextButton(0, 0, "New Campaign", "images/main/button.png", "fonts/header-big.fnt", new Event() {
            @Override
            public void onCall() {
                State.gsm.setState(CampaignBuddyMain.NEW_STATE);
            }
        });
        newCampaign.setPosition(0-newCampaign.getWidth()/2,continueCampaign.getHeight()+40);

        InteractableList buttons = new InteractableList(continueCampaign.getX(),continueCampaign.getY());
        buttons.add(continueCampaign);
        buttons.add(newCampaign);

        buttons.setPosition(CampaignBuddyMain.WIDTH/2-CampaignBuddyMain.WIDTH/4-buttons.getWidth()/2,CampaignBuddyMain.HEIGHT/2-buttons.getHeight()/2);
        list.add(buttons);
    }

}
