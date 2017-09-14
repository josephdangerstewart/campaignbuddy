package com.campaignbuddy.state;

import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.List;
import com.campaignbuddy.resources.components.Text;
import com.campaignbuddy.resources.components.TextButton;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.ChangeEvent;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.state.meta.State;

/**
 * Created by josephstewart on 8/23/17.
 */
public class ContinueState extends State {

    private final int BUTTONS_Y = 600;

    private List itemList;
    private Text title;
    private String[] names;

    private TextButton remove;
    private TextButton select;
    private TextButton back;

    public ContinueState(String background) throws IllegalArgumentException {
        super(background);

        names = State.gsm.getPrefs().getCampaigns();

        title = new Text(0,0,"fonts/header-big.fnt","Select Campaign");
        itemList = new List(0,0,"images/list.png","images/list-indicator.png","fonts/text-big.fnt",camera,viewport);
        select = new TextButton(0, 0, "Select", "images/main/button.png", "fonts/header-small.fnt", new Event() {
            @Override
            public void onCall() {
                select();
            }
        });
        remove = new TextButton(0, 0, "Remove", "images/main/button.png", "fonts/header-small.fnt", new Event() {
            @Override
            public void onCall() {
                remove();
                itemList.clear();
                names = State.gsm.getPrefs().getCampaigns();
                for (int i = 0; i < names.length; i++) {
                    itemList.add(names[i]);
                }
            }
        });

        back = new TextButton(0, 0, "Back", "images/main/button.png", "fonts/header-big.fnt", new Event() {
            @Override
            public void onCall() {
                State.gsm.setState(CampaignBuddyMain.MENU_STATE);
            }
        });

        itemList.setOnChange(new ChangeEvent() {
            @Override
            public void onChange(int change) {
                if (change != -1) {
                    remove.setVisible(true);
                    select.setVisible(true);
                }
            }
        });

        for (int i = 0; i < names.length; i++) {
            itemList.add(names[i]);
        }

        remove.setVisible(false);
        select.setVisible(false);

        title.setPosition(CampaignBuddyMain.WIDTH/2-title.getWidth()/2,CampaignBuddyMain.HEIGHT-10);
        itemList.setPosition(0,CampaignBuddyMain.HEIGHT/2-itemList.getHeight()/2);
        select.setPosition(itemList.getX() + itemList.getWidth() + 100-select.getWidth()/2,BUTTONS_Y);
        remove.setPosition(itemList.getX() + itemList.getWidth() + 100-remove.getWidth()/2,BUTTONS_Y-remove.getHeight()-40);
        back.setPosition(CampaignBuddyMain.WIDTH-back.getWidth()-10,CampaignBuddyMain.HEIGHT-back.getHeight()-10);

        list.add(title);

        InteractableList list = new InteractableList(0,0);

        list.add(itemList);
        list.add(select);
        list.add(remove);

        itemList.setOnEnter(new Event() {
            @Override
            public void onCall() {
                select.click();
            }
        });

        list.setPosition(CampaignBuddyMain.WIDTH/2-list.getWidth()/2,0);

        this.list.add(back);

        this.list.add(list);

    }

    @Override
    public void init() {
        super.init();

        names = State.gsm.getPrefs().getCampaigns();
        itemList.clear();
        for (int i = 0; i < names.length; i++) {
            itemList.add(names[i]);
        }
    }

    private void remove() {
        if (itemList.getSelectedIndex() != -1) {
            System.out.println(names[itemList.getSelectedIndex()]);
            State.gsm.getPrefs().removeCampaign(names[itemList.getSelectedIndex()]);
        }
    }

    private void select() {
        if (itemList.getSelectedIndex() != -1) {
            if (State.gsm.getPrefs().setCampaign(names[itemList.getSelectedIndex()])) {
                State.gsm.getPool().init();
                State.gsm.setState(CampaignBuddyMain.MAIN_STATE);
            }
        }
    }

}
