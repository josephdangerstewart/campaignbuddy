package com.campaignbuddy.apps.party_manager.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.apps.party_manager.PartyManager;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.List;
import com.campaignbuddy.resources.components.Text;
import com.campaignbuddy.resources.components.TextButton;
import com.campaignbuddy.resources.components.TextField;
import com.campaignbuddy.resources.containers.Window;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveContainer;
import com.campaignbuddy.state.meta.State;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/21/17.
 */
public class PlayerAdder extends InteractiveContainer {

    private final int Y_BUFFER = 40;

    private Window window;
    private Text playerName;
    private Text characterName;
    private Text type;
    private Text race;

    private TextField playerNameField;
    private TextField characterNameField;
    private TextField typeField;
    private TextField raceField;

    private TextButton add;
    private TextButton back;
    private List list;
    private ArrayList<PartyInfo> party;

    public PlayerAdder(OrthographicCamera camera, Viewport viewport, List list, ArrayList<PartyInfo> party) {
        window = new Window("images/party-manager/playeradder.png",0,0,camera, viewport);
        window.setX(CampaignBuddyMain.WIDTH/2-window.getWidth()/2);
        window.setY(CampaignBuddyMain.HEIGHT/2-window.getHeight()/2);

        playerName = new Text(0,0,"fonts/header-big.fnt","Player Name: ");
        characterName = new Text(0,0,"fonts/header-big.fnt","Character Name: ");
        type = new Text(0,0,"fonts/header-big.fnt","Class: ");
        race = new Text(0,0,"fonts/header-big.fnt","Race: ");

        playerNameField = new TextField(0,0,"images/textbox.png","fonts/header-big.fnt",200,camera, State.viewport);
        characterNameField = new TextField(0,0,"images/textbox.png","fonts/header-big.fnt",200,camera,State.viewport);
        typeField = new TextField(0,0,"images/textbox.png","fonts/header-big.fnt",200,camera,State.viewport);
        raceField = new TextField(0,0,"images/textbox.png","fonts/header-big.fnt",200,camera,State.viewport);

        int y = window.getHeight()-80;
        window.add(playerName,window.getWidth()/2-(playerName.getWidth()+playerNameField.getWidth())/2,y+(playerNameField.getHeight()/2-playerName.getHeight()/2)+playerName.getHeight());
        window.add(playerNameField,window.getWidth()/2-(playerName.getWidth()+playerNameField.getWidth())/2+playerName.getWidth(),y);

        y -= (playerName.getHeight()+Y_BUFFER);
        window.add(characterName,window.getWidth()/2-(characterName.getWidth()+characterNameField.getWidth())/2,y+(characterNameField.getHeight()/2-characterName.getHeight()/2)+characterName.getHeight());
        window.add(characterNameField,window.getWidth()/2-(characterName.getWidth()+characterNameField.getWidth())/2+characterName.getWidth(),y);

        y -= (characterName.getHeight()+Y_BUFFER);
        window.add(type,window.getWidth()/2-(type.getWidth()+typeField.getWidth())/2,y+(typeField.getHeight()/2-type.getHeight()/2)+type.getHeight());
        window.add(typeField,window.getWidth()/2-(type.getWidth()+typeField.getWidth())/2+type.getWidth(),y);

        y -= (type.getHeight() + Y_BUFFER);
        window.add(race,window.getWidth()/2-(race.getWidth()+raceField.getWidth())/2,y+(raceField.getHeight()/2-race.getHeight()/2)+race.getHeight());
        window.add(raceField,window.getWidth()/2-(race.getWidth()+raceField.getWidth())/2+race.getWidth(),y);

        add = new TextButton(0, 0, "Add", "images/main/button.png", "fonts/text-big.fnt", new Event() {
            @Override
            public void onCall() {
                setVisible(false);
                add();
            }
        });
        window.add(add,window.getWidth()/2-add.getWidth()/2,20);

        back = new TextButton(0, 0, "Back", "images/main/button.png", "fonts/text-big.fnt", new Event() {
            @Override
            public void onCall() {
                setVisible(false);
            }
        });
        window.add(back,window.getWidth()/2-back.getWidth()/2,40+add.getHeight());

        setVisible(false);

        this.list = list;
        this.party = party;
    }

    private void add() {
        JSONArray players = PartyManager.pm.getPrefs().getJSONArray("players");

        PartyInfo playerInfo = new PartyInfo(0,0,playerNameField.getText(),characterNameField.getText(),typeField.getText(),raceField.getText());
        System.out.println("Character: " + characterNameField.getText());
        list.add(characterNameField.getText());
        party.add(playerInfo);
        players.put(playerInfo.getObject());
        PartyManager.pm.save();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            playerNameField.setText("");
            characterNameField.setText("");
            typeField.setText("");
            raceField.setText("");
        }
        super.setVisible(visible);
    }

    @Override
    public void draw(SpriteBatch batch) {
        window.draw(batch);
    }

    @Override
    public boolean update(int x, int y) {
        window.update(x,y);
        return true;
    }

    @Override
    public void cancelUpdate() {
        window.cancelUpdate();
    }

    @Override
    public int getWidth() {
        return window.getWidth();
    }

    @Override
    public int getHeight() {
        return window.getHeight();
    }

    @Override
    public int getX() {
        return window.getX();
    }

    @Override
    public int getY() {
        return window.getY();
    }

    @Override
    public void setX(float x) {
        window.setX(x);
    }

    @Override
    public void setY(float y) {
        window.setY(y);
    }

    @Override
    public void dispose() {
        window.dispose();
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        window.clickDown(x,y,button);
        return true;
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        window.clickUp(x,y,button);
        return true;
    }

    @Override
    public boolean dragged(int x, int y) {
        window.dragged(x,y);
        return true;
    }

    @Override
    public boolean keyDown(int keyCode) {
        window.keyDown(keyCode);
        return true;
    }

    @Override
    public boolean keyUp(int keyCode) {
        window.keyUp(keyCode);
        return true;
    }

    @Override
    public boolean keyTyped(char key) {
        window.keyTyped(key);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        window.scrolled(amount);
        return true;
    }
}
