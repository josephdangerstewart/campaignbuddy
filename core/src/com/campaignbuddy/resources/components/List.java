package com.campaignbuddy.resources.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.containers.ScrollableWindow;
import com.campaignbuddy.resources.meta.ChangeEvent;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/20/17.
 */
public class List extends InteractiveDrawable {

    private final int Y_BUFFER = 15;
    private final int X_BUFFER = 30;

    private boolean indexed = false;

    private InteractableList list;

    private ChangeEvent onChange;
    private ScrollableWindow window;
    private ArrayList<String> options;
    private Image indicator;
    private ScrollBar scrollBar;
    private BitmapFont font;
    private String fontString;
    private GlyphLayout layout;
    private Event onEnter;

    private int selectedIndex = -1;
    private int height;

    private boolean hovering = false;

    public List(int x, int y, Sprite background, Sprite indicator, String font, OrthographicCamera camera, Viewport viewport) {
        list = new InteractableList(x,y);
        options = new ArrayList<String>();

        this.fontString = font;
        this.indicator = new Image(indicator);
        this.indicator.setVisible(false);
        this.indicator.setX(x);

        this.font = new BitmapFont(Gdx.files.internal(font));
        layout = new GlyphLayout();

        layout.setText(this.font,"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*(){}");
        height = (int)layout.height;

        this.indicator.getSprite().setSize(this.indicator.getWidth(),(height+2*Y_BUFFER));

        window = new ScrollableWindow(background,x,y,camera,0,0,viewport);
        window.setYBuffer(4);
        scrollBar = new ScrollBar("images/main/spritesheets/","scroll",window,window.getX()+window.getWidth()-25,y+10);
        scrollBar.setHeight(window.getHeight()-20);
        window.setScrollBar(scrollBar);

        list.add(window);
        list.add(scrollBar);
        window.getList().add(this.indicator);
    }

    public List(int x, int y, String background, String indicator, String font, OrthographicCamera camera, Viewport viewport) {
        list = new InteractableList(x,y);
        options = new ArrayList<String>();

        this.fontString = font;
        this.indicator = new Image(new Sprite(new Texture(indicator)));
        this.indicator.setVisible(false);
        this.indicator.setX(x);

        this.font = new BitmapFont(Gdx.files.internal(font));
        layout = new GlyphLayout();

        layout.setText(this.font,"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*(){}");
        height = (int)layout.height;

        this.indicator.getSprite().setSize(this.indicator.getWidth(),(height+2*Y_BUFFER));

        window = new ScrollableWindow(background,x,y,camera,0,0,viewport);
        window.setYBuffer(4);
        scrollBar = new ScrollBar("images/main/spritesheets/","scroll",window,window.getX()+window.getWidth()-25,y+10);
        scrollBar.setHeight(window.getHeight()-20);
        window.setScrollBar(scrollBar);

        list.add(window);
        list.add(scrollBar);
        window.getList().add(this.indicator);
    }

    public boolean contains(int x, int y) {
        return window.contains(x,y);
    }

    public void setSelectedIndex(int index) {
        if (index > options.size()-1 || index < 0) {
            selectedIndex = -1;
            indicator.setVisible(false);
            if (onChange != null) {
                onChange.onChange(selectedIndex);
            }
        } else {
            indicator.setY(window.getList().getList().get(selectedIndex+1).getY()+Y_BUFFER-(height+2*Y_BUFFER));
            indicator.setVisible(true);
            if (onChange != null) {
                onChange.onChange(selectedIndex);
            }
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setOnChange(ChangeEvent onChange) {
        this.onChange = onChange;
    }

    public void add(String option) {
        options.add(option);

        int totalHeight = (height+2*Y_BUFFER) * (options.size()-1);
        if (totalHeight > window.getHeight()) {
            window.setMax(totalHeight - window.getHeight() + (height+2*Y_BUFFER));
        } else {
            window.setMax(0);
        }

        layout.setText(font,option);

        int curY = (window.getHeight()-totalHeight)-(height+2*Y_BUFFER)/2+(int)layout.height/2;
        window.add(new Text(0,0,fontString,option),X_BUFFER,curY);
    }

    public void remove(int index) {
        index = index+1;
        ArrayList<InteractiveDrawable> list = window.getList().getList();

        if (index < list.size()) {
            list.remove(index);
            options.remove(index-1);
            for (int i = index; i < list.size(); i++) {
                list.get(i).setY(list.get(i).getY()+(height+2*Y_BUFFER));
            }
            if (index-1 == selectedIndex) {
                selectedIndex = -1;
                indicator.setVisible(false);
                if (onChange != null) {
                    onChange.onChange(selectedIndex);
                }
            }
        }
    }

    public void clear() {
        options.clear();
        window.setMax(0);
        window.clear();
        this.indicator.setVisible(false);
        window.getList().add(indicator);
    }

    public String getOption(int index) {
        if (index >= 0 && index < options.size()) {
            return options.get(index);
        }
        return "";
    }

    @Override
    public void draw(SpriteBatch batch) {
        list.draw(batch);
    }

    @Override
    public boolean clickDown(int x, int y, int button) {

        scrollBar.clickDown(x,y,button);

        if (window.contains(x,y)) {
            indexed = true;
            y-=window.getScrollAmount();
            if (button == Input.Buttons.RIGHT) {
                selectedIndex = -1;
                indicator.setVisible(false);
                if (onChange != null) {
                    onChange.onChange(selectedIndex);
                }
                return true;
            }
            selectedIndex = (int)Math.ceil((window.getHeight()-(y-window.getY()))/(height+2*Y_BUFFER));

            if (selectedIndex > options.size()-1) {
                selectedIndex = -1;
                indicator.setVisible(false);
                if (onChange != null) {
                    onChange.onChange(selectedIndex);
                }
            } else {
                indicator.setY(window.getList().getList().get(selectedIndex+1).getY()+Y_BUFFER-(height+2*Y_BUFFER));
                indicator.setVisible(true);

                if (onChange != null) {
                    onChange.onChange(selectedIndex);
                }
            }
            return true;
        }
        indexed = false;
        return false;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        scrollBar.clickUp(x,y,button); return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (indexed) {
            if (keycode == Input.Keys.DOWN && selectedIndex + 1 < options.size()) {
                selectedIndex++;
                if (selectedIndex > options.size() - 1) {
                    selectedIndex = -1;
                    indicator.setVisible(false);
                } else {
                    indicator.setY(window.getList().getList().get(selectedIndex + 1).getY() + Y_BUFFER-(height+2*Y_BUFFER));
                    indicator.setVisible(true);
                    if (onChange != null) {
                        onChange.onChange(selectedIndex);
                    }
                }
            } else if (keycode == Input.Keys.UP && selectedIndex - 1 >= 0) {
                selectedIndex--;
                if (selectedIndex > options.size()-1) {
                    selectedIndex = -1;
                    indicator.setVisible(false);
                } else {
                    indicator.setY(window.getList().getList().get(selectedIndex+1).getY()+Y_BUFFER-(height+2*Y_BUFFER));
                    indicator.setVisible(true);
                    if (onChange != null) {
                        onChange.onChange(selectedIndex);
                    }
                }
            } else if (keycode == Input.Keys.ENTER && selectedIndex >= 0) {
                onEnter.onCall();
            }
            return true;
        }
        return false;
    }

    public void setOnEnter(Event onEnter) {
        this.onEnter = onEnter;
    }

    @Override
    public boolean dragged(int x, int y) {
        scrollBar.dragged(x,y); return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return list.scrolled(amount);
    }

    @Override
    public boolean update(int x, int y) {

        InteractableList list = window.getList();

        if (window.contains(x,y)) {
            y-=window.getScrollAmount();
            int selectedIndex = (int) Math.ceil((window.getHeight() - (y - window.getY())) / (height + 2 * Y_BUFFER));
            if (selectedIndex < options.size()) {
                Text text;

                for (int i = 0; i < options.size(); i++) {
                    text = (Text)list.getList().get(i+1);
                    text.getFont().setColor(Color.BLACK);
                }

                text = (Text)(list.getList().get(selectedIndex+1));
                text.getFont().setColor(Color.LIGHT_GRAY);
                hovering = true;
            } else if (hovering) {
                for (int i = 0; i < options.size(); i++) {
                    Text text = (Text) list.getList().get(i+1);
                    text.getFont().setColor(Color.BLACK);
                }
                hovering = false;
            }
        } else if (hovering) {
            for (int i = 0; i < options.size(); i++) {
                Text text = (Text) list.getList().get(i+1);
                text.getFont().setColor(Color.BLACK);
            }
            hovering = false;
        }

        return list.update(x,y);
    }

    @Override
    public void cancelUpdate() {
        list.cancelUpdate();
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
