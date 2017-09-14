package com.campaignbuddy.apps.party_manager.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.apps.party_manager.PartyManager;
import com.campaignbuddy.resources.components.Button;
import com.campaignbuddy.resources.components.Text;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by josephstewart on 8/22/17.
 */
public class StatChanger extends InteractiveDrawable {

    private final int Y_BUFFER = 5;

    private Button add;
    private Button subtract;
    private Text stat;
    private String statName;
    private String prefStat;

    private int playerIndex;
    private JSONArray players;
    private JSONObject player;

    private InteractableList list;

    public StatChanger(int x, int y, String stat, String prefStat) {
        list = new InteractableList(x,y);

        this.statName = stat;
        this.prefStat = prefStat;

        players = PartyManager.pm.getPrefs().getJSONArray("players");
        player = null;

        add = new Button(x, y, "images/party-manager/addbutton.png", new Event() {
            @Override
            public void onCall() {
                callAddButton();
            }
        });

        this.stat = new Text(x,0,"fonts/text-big.fnt",stat + ": 0");
        this.stat.setY(y+add.getHeight()+Y_BUFFER+this.stat.getHeight());

        subtract = new Button(0, y, "images/party-manager/subtractbutton.png", new Event() {
            @Override
            public void onCall() {
                callSubtractButton();
            }
        });
        subtract.setX(x+this.stat.getWidth()-subtract.getWidth());

        list.add(add);
        list.add(this.stat);
        list.add(subtract);
    }

    public boolean isShowing() {
        System.out.println(statName + " is showing: " + PartyManager.pm.getPrefs().getBoolean(prefStat+"ShowingHotbar"));
        return PartyManager.pm.getPrefs().getBoolean(prefStat+"ShowingHotbar");
    }

    private void callAddButton() {
        System.out.println("HI");
        if (player != null) {
            System.out.println("HELLO");
            int amount = player.getInt(prefStat);
            amount+=1;
            stat.setText(statName + ": " + amount);
            player.put(prefStat,amount);
            PartyManager.pm.save();
        }
    }

    private void callSubtractButton() {
        if (player != null) {
            int amount = player.getInt(prefStat);
            amount-=1;
            stat.setText(statName + ": " + amount);
            player.put(prefStat,amount);
            PartyManager.pm.save();
        }
    }

    public void setPlayerIndex(int playerIndex) {
        players = PartyManager.pm.getPrefs().getJSONArray("players");
        if (playerIndex >= 0 && playerIndex < players.length()) {
            this.playerIndex = playerIndex;
            player = players.getJSONObject(playerIndex);
            stat.setText(statName + ": " + player.getInt(prefStat));
        } else {
            player = null;
            this.playerIndex = -1;
            stat.setText(statName + ": 0");
        }
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        return list.clickDown(x,y,button);
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        return list.clickUp(x,y,button);
    }

    @Override
    public void draw(SpriteBatch batch) {
        list.draw(batch);
    }

    @Override
    public boolean update(int x, int y) {
        return list.update(x,y);
    }

    @Override
    public void cancelUpdate() {
        list.cancelUpdate();
    }

    @Override
    public int getWidth() {
        return list.getWidth();
    }

    @Override
    public int getHeight() {
        return list.getHeight();
    }

    @Override
    public int getX() {
        return list.getX();
    }

    @Override
    public int getY() {
        return list.getY();
    }

    @Override
    public void setX(float x) {
        list.setX(x);
    }

    @Override
    public void setY(float y) {
        list.setY(y);
    }

    @Override
    public void dispose() {
        list.dispose();
    }
}
