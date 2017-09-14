package com.campaignbuddy.apps.party_manager;

import com.campaignbuddy.apps.party_manager.components.PartyInfo;
import com.campaignbuddy.apps.party_manager.components.PlayerAdder;
import com.campaignbuddy.apps.party_manager.components.Stat;
import com.campaignbuddy.apps.party_manager.components.StatChanger;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.*;
import com.campaignbuddy.resources.meta.ChangeEvent;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.state.meta.App;
import com.campaignbuddy.state.meta.State;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/5/17.
 */
public class PartyManager extends App {

    public static PartyManager pm;

    private final int STAT_X = 473;
    private final int STAT_Y = 645;
    private final int STAT_Y_BUFFER = 50;
    private final int TITLE_X = 940;
    private final int TITLE_Y = 866;
    private final int Y_BUFFER = 10;

    private Stat levelStat;
    private Stat inspirationStat;
    private Stat passivePerceptionStat;
    private Stat armorClassStat;
    private Stat maxHPStat;
    private Stat currentHPStat;

    private StatChanger levelStatChanger;
    private StatChanger inspirationStatChanger;
    private StatChanger passivePerceptionStatChanger;
    private StatChanger armorClassStatChanger;
    private StatChanger maxHPStatChanger;
    private StatChanger currentHPStatChanger;

    private Button add;
    private Button remove;

    private ArrayList<PartyInfo> party;
    private JSONArray players;

    private Text characterName;
    private Text playerName;
    private Text classRace;
    private Text hotbarText;
    private Text sidebarText;

    private List itemList;

    private DropDownList dropDownList;

    private PlayerAdder adder;

    public PartyManager(String backgroundName) throws IllegalArgumentException {
        super(backgroundName,"Party Manager","images/party-manager/party-manager-icon.png");

        pm = this;

        party = new ArrayList<PartyInfo>();

        itemList = new List(35,145,"images/list.png","images/list-indicator.png","fonts/header-small.fnt",camera,viewport);

        loadPrefs();

        itemList.setOnChange(new ChangeEvent() {
            @Override
            public void onChange(int selectedIndex) {
                System.out.println("SELECTED: " + selectedIndex);
                if (selectedIndex >= 0 && selectedIndex < players.length()) {
                    JSONObject player = players.getJSONObject(selectedIndex);

                    levelStat.setVisible(true);
                    levelStat.setPlayer(player);

                    inspirationStat.setVisible(true);
                    inspirationStat.setPlayer(player);

                    passivePerceptionStat.setVisible(true);
                    passivePerceptionStat.setPlayer(player);

                    armorClassStat.setVisible(true);
                    armorClassStat.setPlayer(player);

                    maxHPStat.setVisible(true);
                    maxHPStat.setPlayer(player);

                    currentHPStat.setVisible(true);
                    currentHPStat.setPlayer(player);

                    characterName.setText(player.getString("characterName"));
                    characterName.setVisible(true);
                    characterName.setX(TITLE_X-characterName.getWidth()/2);

                    playerName.setText("[" + player.getString("playerName") + "]");
                    playerName.setVisible(true);
                    playerName.setX((characterName.getX()+characterName.getWidth()/2)-playerName.getWidth()/2);

                    classRace.setText(player.getString("race") + " - " + player.getString("type"));
                    classRace.setVisible(true);
                    classRace.setX((characterName.getX()+characterName.getWidth()/2)-classRace.getWidth()/2);
                } else {
                    levelStat.setVisible(false);
                    inspirationStat.setVisible(false);
                    passivePerceptionStat.setVisible(false);
                    armorClassStat.setVisible(false);
                    maxHPStat.setVisible(false);
                    currentHPStat.setVisible(false);

                    characterName.setVisible(false);
                    playerName.setVisible(false);
                    classRace.setVisible(false);
                }
            }
        });
        this.list.add(itemList);

        //this.list.add(new Switch(0,0,"images/spritesheets/","checkbox",false));
        //TextField field = new TextField(799,846,"images/textbox.png","fonts/header-big.fnt",200,camera);

        characterName = new Text(TITLE_X,TITLE_Y,"fonts/header-big.fnt","Character Name");
        characterName.setX(TITLE_X-characterName.getWidth()/2);
        this.list.add(characterName);
        characterName.setVisible(false);

        playerName = new Text(0,TITLE_Y-(characterName.getHeight()+Y_BUFFER),"fonts/header-small.fnt","[Player Name]");
        playerName.setX((characterName.getX()+characterName.getWidth()/2)-playerName.getWidth()/2);
        this.list.add(playerName);
        playerName.setVisible(false);

        classRace = new Text(0,TITLE_Y-(characterName.getHeight()+Y_BUFFER)-(playerName.getHeight()+Y_BUFFER),"fonts/header-small.fnt","Race - Class");
        classRace.setX((characterName.getX()+characterName.getWidth()/2)-classRace.getWidth()/2);
        this.list.add(classRace);
        classRace.setVisible(false);

        levelStat = new Stat(STAT_X,STAT_Y,"Level","level",camera);
        inspirationStat = new Stat(STAT_X,levelStat.getY()-(levelStat.getHeight()+STAT_Y_BUFFER),"Inspiration","inspiration",camera);
        passivePerceptionStat = new Stat(STAT_X, inspirationStat.getY()-(inspirationStat.getHeight()+STAT_Y_BUFFER),"Passive Perception","passivePerception",camera);
        armorClassStat = new Stat(STAT_X,passivePerceptionStat.getY()-(passivePerceptionStat.getHeight()+STAT_Y_BUFFER),"Armor Class","armorClass",camera);
        maxHPStat = new Stat(STAT_X,armorClassStat.getY()-(armorClassStat.getHeight()+STAT_Y_BUFFER),"Max HP","maxHP",camera);
        currentHPStat = new Stat(STAT_X,maxHPStat.getY()-(maxHPStat.getHeight()+STAT_Y_BUFFER),"Current HP","currentHP",camera);

        levelStatChanger = new StatChanger(0,0,"Level","level");
        inspirationStatChanger = new StatChanger(0,0,"Inspiration","inspiration");
        passivePerceptionStatChanger = new StatChanger(0,0,"Passive Perception","passivePerception");
        armorClassStatChanger = new StatChanger(0,0,"Armor Class","armorClass");
        maxHPStatChanger = new StatChanger(0,0,"Max HP","maxHP");
        currentHPStatChanger = new StatChanger(0,0,"Current HP", "currentHP");

        levelStat.setVisible(false);
        inspirationStat.setVisible(false);
        passivePerceptionStat.setVisible(false);
        armorClassStat.setVisible(false);
        maxHPStat.setVisible(false);
        currentHPStat.setVisible(false);

        this.list.add(levelStat);
        this.list.add(inspirationStat);
        this.list.add(passivePerceptionStat);
        this.list.add(armorClassStat);
        this.list.add(maxHPStat);
        this.list.add(currentHPStat);

        adder = new PlayerAdder(camera,viewport,itemList,party);

        add = new Button(itemList.getX(), 0, "images/party-manager/addbutton.png", new Event() {
            @Override
            public void onCall() {
                adder.setVisible(true);
            }
        });

        add.setY(itemList.getY()-add.getHeight()-10);

        this.list.add(add);

        remove = new Button(0,add.getY(), "images/party-manager/subtractbutton.png",new Event() {
            @Override
            public void onCall() {
                if (itemList.getSelectedIndex() >= 0) {
                    int selectedIndex = itemList.getSelectedIndex();
                    System.out.println(selectedIndex);
                    itemList.remove(selectedIndex);
                    players.remove(selectedIndex);
                    party.remove(selectedIndex);
                    System.out.println(party.size());
                    save();
                }
            }
        });
        remove.setX(itemList.getX()+itemList.getWidth()-remove.getWidth());

        this.list.add(remove);

        hotbarText = new Text(411,780,"fonts/header-small.fnt","Hotbar");
        sidebarText = new Text(411+hotbarText.getWidth()+50,780,"fonts/header-small.fnt","Sidebar");

        this.list.add(hotbarText);
        this.list.add(sidebarText);

        TextButton setSidebar = new TextButton(0, 0, "Set As Sidebar", "images/main/button.png", "fonts/header-small.fnt", new Event() {
            @Override
            public void onCall() {
                State.gsm.getPrefs().setSideBarApp(name);
            }
        });
        setSidebar.setPosition(CampaignBuddyMain.WIDTH-setSidebar.getWidth()-10,CampaignBuddyMain.HEIGHT-setSidebar.getHeight()-10);
        this.list.add(setSidebar);

        this.list.add(adder);

        dropDownList = new DropDownList(20,20,"images/spritesheets/","dropdown","fonts/text-big.fnt",camera,viewport);
        dropDownList.setAlwaysShowing(true);
        dropDownList.setEvent(new ChangeEvent() {
            @Override
            public void onChange(int change) {
                levelStatChanger.setPlayerIndex(change);
                inspirationStatChanger.setPlayerIndex(change);
                passivePerceptionStatChanger.setPlayerIndex(change);
                armorClassStatChanger.setPlayerIndex(change);
                maxHPStatChanger.setPlayerIndex(change);
                currentHPStatChanger.setPlayerIndex(change);
            }
        });
    }

    @Override
    public void loadPrefs() {
        party.clear();
        if (prefs == null) {
            prefs = new JSONObject();
            prefs.put("inspirationShowingSidebar", false);
            prefs.put("levelShowingSidebar", true);
            prefs.put("passivePerceptionShowingSidebar", true);
            prefs.put("armorClassShowingSidebar", true);
            prefs.put("maxHPShowingSidebar", false);
            prefs.put("currentHPShowingSidebar", false);
            prefs.put("raceShowingSidebar", true);

            prefs.put("inspirationShowingHotbar", false);
            prefs.put("levelShowingHotbar", false);
            prefs.put("passivePerceptionShowingHotbar", false);
            prefs.put("armorClassShowingHotbar", false);
            prefs.put("maxHPShowingHotbar", false);
            prefs.put("currentHPShowingHotbar", false);
            prefs.put("raceShowingHotbar", false);

            players = new JSONArray();
            prefs.put("players",players);
            players = prefs.getJSONArray("players");
            save();
        } else {
            players = null;
            players = prefs.getJSONArray("players");
            for (int i = 0; i < players.length(); i++) {
                party.add(new PartyInfo(players.getJSONObject(i)));
            }
        }

        itemList.clear();

        for (int i = 0; i < party.size(); i++) {
            itemList.add(party.get(i).getCharacterName());
        }
    }


    @Override
    public ArrayList<InteractiveDrawable> getSideBar() {
        System.out.println(party.size());
        ArrayList<InteractiveDrawable> list = new ArrayList<InteractiveDrawable>();
        for (int i = 0; i < party.size(); i++) {
            list.add(party.get(i));
        }
        return list;
    }

    @Override
    public ArrayList<InteractiveDrawable> getQuickActionBar() {
        ArrayList<InteractiveDrawable> list = new ArrayList<InteractiveDrawable>();

        boolean lev = levelStatChanger.isShowing();
        boolean ins = inspirationStatChanger.isShowing();
        boolean pp = passivePerceptionStatChanger.isShowing();
        boolean ac = armorClassStatChanger.isShowing();
        boolean mhp = maxHPStatChanger.isShowing();
        boolean chp = currentHPStatChanger.isShowing();

        if (lev || ins || pp || ac || mhp || chp) {
            String[] playerNames = new String[party.size()];
            for (int i = 0; i < party.size(); i++) {
                playerNames[i] = party.get(i).getCharacterName();
            }
            dropDownList.setOptions(playerNames);
            list.add(dropDownList);
        }

        if (lev) {
            list.add(levelStatChanger);
        }

        if (ins) {
            list.add(inspirationStatChanger);
        }

        if (pp) {
            list.add(passivePerceptionStatChanger);
        }

        if (ac) {
            list.add(armorClassStatChanger);
        }

        if (mhp) {
            list.add(maxHPStatChanger);
        }

        if (chp) {
            list.add(currentHPStatChanger);
        }

        return list;
    }
}
