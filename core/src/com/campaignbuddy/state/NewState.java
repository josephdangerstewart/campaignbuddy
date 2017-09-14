package com.campaignbuddy.state;

import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.Text;
import com.campaignbuddy.resources.components.TextButton;
import com.campaignbuddy.resources.components.TextField;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.state.meta.State;

/**
 * Created by josephstewart on 8/24/17.
 */
public class NewState extends State {

    private TextButton back;
    private TextField name;
    private TextButton create;
    private Text title;

    public NewState(String background) {
        super(background);

        title = new Text(0,0,"fonts/header-big.fnt","Name Your New Campaign");
        title.setPosition(CampaignBuddyMain.WIDTH/2-title.getWidth()/2,CampaignBuddyMain.HEIGHT-10);
        list.add(title);

        back = new TextButton(0, 0, "Back", "images/main/button.png", "fonts/header-big.fnt", new Event() {
            @Override
            public void onCall() {
                State.gsm.setState(CampaignBuddyMain.MENU_STATE);
            }
        });
        back.setPosition(CampaignBuddyMain.WIDTH-back.getWidth()-10,CampaignBuddyMain.HEIGHT-back.getHeight());
        list.add(back);

        create = new TextButton(0, 0, "Create", "images/main/button.png", "fonts/header-big.fnt", new Event() {
            @Override
            public void onCall() {
                if (name.getText() != "") {
                    State.gsm.getPrefs().setCampaign(name.getText());
                    State.gsm.setState(CampaignBuddyMain.MAIN_STATE);
                }
            }
        });

        name = new TextField(0,0,"images/textbox.png","fonts/header-small.fnt",400,camera,viewport);
        name.setPosition(0-name.getWidth()/2,0);
        create.setPosition(0-create.getWidth()/2,0-name.getHeight()-create.getHeight()-10);

        InteractableList tempList = new InteractableList(0,0);

        tempList.add(name);
        tempList.add(create);

        tempList.setPosition(CampaignBuddyMain.WIDTH/2,CampaignBuddyMain.HEIGHT/2);
        list.add(tempList);
    }

}
