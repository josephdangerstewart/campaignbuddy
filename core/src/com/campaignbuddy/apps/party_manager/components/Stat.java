package com.campaignbuddy.apps.party_manager.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.apps.party_manager.PartyManager;
import com.campaignbuddy.resources.components.NumberTextField;
import com.campaignbuddy.resources.components.Switch;
import com.campaignbuddy.resources.components.Text;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.InteractiveContainer;
import com.campaignbuddy.resources.meta.SwitchEvent;
import com.campaignbuddy.resources.meta.TextChangeEvent;
import com.campaignbuddy.state.meta.State;
import org.json.JSONObject;

/**
 * Created by josephstewart on 8/21/17.
 */
public class Stat extends InteractiveContainer {

    private final int HOTBAR_SIDEBAR_SPACE = 80;
    private final int SIDEBAR_STAT_SPACE = 120;
    private final int STAT_AMOUNT_SPACE = 10;
    private final int WIDTH = 40;

    private Switch showInHotbar;
    private Switch showInSidebar;

    private Text stat;
    private NumberTextField amount;
    private String statName;

    private InteractableList list;

    private String prefName;
    private JSONObject player;

    @Override
    public void setVisible(boolean visible) {
        amount.setVisible(visible);
        if (visible) {
            stat.setText(statName + ":");
        } else {
            stat.setText(statName);
        }
    }

    public Stat(int x, int y, String stat, final String prefName, OrthographicCamera camera) {
        boolean showInHotbar = PartyManager.pm.getPrefs().getBoolean(prefName + "ShowingHotbar");
        boolean showInSidebar = PartyManager.pm.getPrefs().getBoolean(prefName+"ShowingSidebar");

        this.statName = stat;
        this.prefName = prefName;
        this.showInHotbar = new Switch(x,y,"images/spritesheets/","checkbox",showInHotbar);
        this.showInSidebar = new Switch(x + HOTBAR_SIDEBAR_SPACE + this.showInHotbar.getWidth(),y,"images/spritesheets/","checkbox",showInSidebar);
        this.stat = new Text(x+HOTBAR_SIDEBAR_SPACE+this.showInHotbar.getWidth()+SIDEBAR_STAT_SPACE+this.showInSidebar.getWidth(),0,"fonts/text-big.fnt",stat + ":");
        this.amount = new NumberTextField(x+HOTBAR_SIDEBAR_SPACE+this.showInHotbar.getWidth()+SIDEBAR_STAT_SPACE+this.showInSidebar.getWidth()+this.stat.getWidth()+STAT_AMOUNT_SPACE,y,"images/textbox.png","fonts/text-big.fnt",WIDTH,camera, State.viewport);
        this.amount.setClearOnEdit(true);
        this.amount.setText("0");
        this.stat.setY(this.stat.getHeight()+y + (this.amount.getHeight()/2-this.stat.getHeight()/2));

        this.showInHotbar.setOnSwitch(new SwitchEvent() {
            @Override
            public void onSwitch(boolean state) {
                PartyManager.pm.getPrefs().put(prefName + "ShowingHotbar",state);
                PartyManager.pm.save();
            }
        });

        this.showInSidebar.setOnSwitch(new SwitchEvent() {
            @Override
            public void onSwitch(boolean state) {
                PartyManager.pm.getPrefs().put(prefName + "ShowingSidebar",state);
                PartyManager.pm.save();
            }
        });

        this.amount.setOnChange(new TextChangeEvent() {
            @Override
            public void onChange(String text) {
                if (player != null) {
                    player.put(prefName,Integer.parseInt(text));
                    PartyManager.pm.save();
                }
            }
        });

        list = new InteractableList(x,y);

        list.add(this.showInHotbar);
        list.add(this.showInSidebar);
        list.add(this.stat);
        list.add(this.amount);
    }

    public void setPlayer(JSONObject player) {
        System.out.println(player.getInt(prefName));
        this.player = player;
        this.amount.setText(player.getInt(prefName) + "");
    }

    @Override
    public void draw(SpriteBatch batch) {
        list.draw(batch);
    }

    @Override
    public boolean update(int x, int y) {
        this.showInHotbar.setState(PartyManager.pm.getPrefs().getBoolean(prefName+"ShowingHotbar"));
        this.showInSidebar.setState(PartyManager.pm.getPrefs().getBoolean(prefName+"ShowingSidebar"));
        if(list.update(x,y)) return true;
        if (player != null && !amount.getEdit())
            amount.setText(player.getInt(prefName) + "");
        return false;
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

    @Override
    public boolean clickDown(int x, int y, int button) {
        return list.clickDown(x,y,button);
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        return list.clickUp(x,y,button);
    }

    @Override
    public boolean dragged(int x, int y) {
        return list.dragged(x,y);
    }

    @Override
    public boolean keyDown(int keyCode) {
        return list.keyDown(keyCode);
    }

    @Override
    public boolean keyUp(int keyCode) {
        return list.keyUp(keyCode);
    }

    @Override
    public boolean keyTyped(char key) {
        return list.keyTyped(key);
    }

    @Override
    public boolean scrolled(int amount) {
        return list.scrolled(amount);
    }
}
