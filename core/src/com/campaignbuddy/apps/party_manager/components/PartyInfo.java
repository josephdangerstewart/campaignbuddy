package com.campaignbuddy.apps.party_manager.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.apps.party_manager.PartyManager;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import org.json.JSONObject;

/**
 * Created by josephstewart on 8/5/17.
 */
public class PartyInfo extends InteractiveDrawable {

    private final static int INDENT = 5;
    private final static int SPACE = 10;
    private final static int X_BUFFER = 5;

    private String playerName;
    private String characterName;
    private String type = "Bard";
    private String race = "Elf";
    private int inspiration;
    private int level;
    private int passivePerception;
    private int armorClass;
    private int maxHP;
    private int currentHP;
    private boolean showingInspiration, showingLevel, showingPassivePerception, showingArmorClass, showingMaxHP, showingCurrentHP, showingRace;

    private JSONObject player;

    private BitmapFont bigFont;
    private BitmapFont smallFont;
    private GlyphLayout layout;

    private int x,y;

    public JSONObject getObject() {
        return player;
    }

    public PartyInfo(JSONObject object) {

        this.setAlwaysUpdate(true);

        x = object.getInt("x");
        y = object.getInt("y");

        playerName = object.getString("playerName");
        race = object.getString("race");
        characterName = object.getString("characterName");
        type = object.getString("type");
        inspiration = object.getInt("inspiration");
        level = object.getInt("level");
        passivePerception = object.getInt("passivePerception");
        armorClass = object.getInt("armorClass");
        maxHP = object.getInt("maxHP");
        currentHP = object.getInt("currentHP");

        this.player = object;

        bigFont = new BitmapFont(Gdx.files.internal("fonts/header-small.fnt"));
        smallFont = new BitmapFont(Gdx.files.internal("fonts/text-big.fnt"));
        bigFont.setColor(Color.BLACK);
        smallFont.setColor(Color.BLACK);

        layout = new GlyphLayout();
    }

    public PartyInfo(int x, int y, String playerName, String characterName, String type, String race) {

        this.setAlwaysUpdate(true);

        this.x = x;
        this.y = y;

        this.playerName = playerName;
        this.characterName = characterName;
        this.type = type;
        this.race = race;

        inspiration = 0;
        level = 0;
        passivePerception = 0;
        armorClass = 0;
        maxHP = 0;
        currentHP = 0;

        bigFont = new BitmapFont(Gdx.files.internal("fonts/header-small.fnt"));
        smallFont = new BitmapFont(Gdx.files.internal("fonts/text-big.fnt"));
        bigFont.setColor(Color.BLACK);
        smallFont.setColor(Color.BLACK);

        player = new JSONObject();

        player.put("x",x);
        player.put("y",y);
        player.put("playerName",playerName);
        player.put("characterName",characterName);
        player.put("type",type);
        player.put("inspiration",inspiration);
        player.put("level",level);
        player.put("passivePerception",passivePerception);
        player.put("armorClass",armorClass);
        player.put("maxHP",maxHP);
        player.put("currentHP",currentHP);
        player.put("race",race);

        layout = new GlyphLayout();
    }

    @Override
    public void draw(SpriteBatch batch) {

        JSONObject prefs = PartyManager.pm.getPrefs();
        showingInspiration = prefs.getBoolean("inspirationShowingSidebar");
        showingLevel = prefs.getBoolean("levelShowingSidebar");
        showingPassivePerception = prefs.getBoolean("passivePerceptionShowingSidebar");
        showingArmorClass = prefs.getBoolean("armorClassShowingSidebar");
        showingMaxHP = prefs.getBoolean("maxHPShowingSidebar");
        showingCurrentHP = prefs.getBoolean("currentHPShowingSidebar");
        showingRace = prefs.getBoolean("raceShowingSidebar");

        int curX = INDENT + x;
        int curY = y;

        if (showingInspiration) {
            layout.setText(smallFont,"Inspiration: " + inspiration);
            smallFont.draw(batch,layout,curX,curY);
            curY += layout.height + SPACE;
        }

        if (showingMaxHP) {
            layout.setText(smallFont,"Max HP: " + maxHP);
            smallFont.draw(batch,layout,curX,curY);
            curY += layout.height + SPACE;
        }

        if (showingArmorClass) {
            layout.setText(smallFont, "AC: " + armorClass);
            smallFont.draw(batch,layout,curX,curY);
            curX += layout.width + SPACE;
            if (!showingCurrentHP) {
                curY+=layout.height+SPACE;
            }
        }

        if (showingCurrentHP) {
            layout.setText(smallFont,"HP: " + currentHP);
            smallFont.draw(batch,layout,curX,curY);
            curY += layout.height + SPACE;
        }
        curX = INDENT + x;

        if (showingPassivePerception) {
            layout.setText(smallFont,"Passive Perception: " + passivePerception);
            smallFont.draw(batch,layout,curX,curY);
            curY += layout.height + SPACE;
        }

        String raceClass = "";
        if (showingRace) {
            raceClass = race + " -- ";
        }

        layout.setText(smallFont,raceClass + type);
        smallFont.draw(batch,layout,curX,curY);
        curY += layout.height + SPACE;

        String nameLvl = playerName;
        if (showingLevel) {
            nameLvl = nameLvl + "   Lvl: " + level;
        }
        layout.setText(smallFont,nameLvl);
        smallFont.draw(batch,layout,curX,curY);
        curY += layout.height + SPACE*2;

        bigFont.draw(batch,characterName,x,curY);
    }

    @Override
    public boolean update(int x, int y) {

        playerName = player.getString("playerName");
        race = player.getString("race");
        characterName = player.getString("characterName");
        type = player.getString("type");
        inspiration = player.getInt("inspiration");
        level = player.getInt("level");
        passivePerception = player.getInt("passivePerception");
        armorClass = player.getInt("armorClass");
        maxHP = player.getInt("maxHP");
        currentHP = player.getInt("currentHP");

        return false;
    }

    @Override
    public void cancelUpdate() {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {

        JSONObject prefs = PartyManager.pm.getPrefs();
        showingInspiration = prefs.getBoolean("inspirationShowingSidebar");
        showingLevel = prefs.getBoolean("levelShowingSidebar");
        showingPassivePerception = prefs.getBoolean("passivePerceptionShowingSidebar");
        showingArmorClass = prefs.getBoolean("armorClassShowingSidebar");
        showingMaxHP = prefs.getBoolean("maxHPShowingSidebar");
        showingCurrentHP = prefs.getBoolean("currentHPShowingSidebar");
        showingRace = prefs.getBoolean("raceShowingSidebar");

        int curY = 0;

        if (showingInspiration) {
            layout.setText(smallFont,"Inspiration: " + inspiration);
            curY += layout.height + SPACE;
        }

        if (showingMaxHP) {
            layout.setText(smallFont,"Max HP: " + maxHP);
            curY += layout.height + SPACE;
        }

        if (showingArmorClass) {
            layout.setText(smallFont, "AC: " + armorClass);
            if (!showingCurrentHP) {
                curY+=layout.height+SPACE;
            }
        }

        if (showingCurrentHP) {
            layout.setText(smallFont,"HP: " + currentHP);
            curY += layout.height + SPACE;
        }

        if (showingPassivePerception) {
            layout.setText(smallFont,"Passive Perception: " + passivePerception);
            curY += layout.height + SPACE;
        }

        String raceClass = "";
        if (showingRace) {
            raceClass = race;
        }

        layout.setText(smallFont,raceClass + type);
        curY += layout.height + SPACE;

        String nameLvl = playerName;
        if (showingLevel) {
            nameLvl = nameLvl + "   Lvl: " + level;
        }
        layout.setText(smallFont,nameLvl);
        curY += layout.height + SPACE*2;

        layout.setText(bigFont,characterName);
        curY+=layout.height;
        return curY;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(float x) {
        this.x = (int)x;
    }

    @Override
    public void setY(float y) {
        this.y = (int)y;
    }

    @Override
    public void dispose() {
        smallFont.dispose();
        bigFont.dispose();
    }

    public String getPlayerName() {return playerName;}

    public String getCharacterName() {return characterName;}

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        level++;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPassivePerception() {
        return passivePerception;
    }

    public void setPassivePerception(int passivePerception) {
        this.passivePerception = passivePerception;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void changeCurrentHP(int change) {
        currentHP += change;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getInspiration() {
        return inspiration;
    }

    public void increaseInspiration() {
        inspiration++;
    }

    public void setInspiration(int inspiration) {
        this.inspiration = inspiration;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }
}
