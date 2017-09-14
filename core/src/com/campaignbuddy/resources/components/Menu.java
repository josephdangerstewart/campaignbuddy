package com.campaignbuddy.resources.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.meta.MenuEvent;

/**
 * Created by josephstewart on 7/16/17.
 */
public class Menu extends InteractiveDrawable {

    private boolean showing = false;

    private final int MAX_WIDTH = 600;
    private final int X_BUFFER = 10;
    private final int Y_BUFFER = 20;

    private BitmapFont font;

    private int x;
    private int y;

    private int selected = 0;

    private int titleBuffer = 20;
    private int optionBuffer = 10;

    private Sprite background;

    private GlyphLayout gl;

    private Color selectedColor;
    private Color unselectedColor;
    private Color titleColor;

    private String title;
    private String[] options;
    private MenuEvent[] callables;

    public void setData(String background, String fontFile, String title,
                        String[] options, MenuEvent[] callables,
                        Color selectedColor, Color unselectedColor, Color titleColor) {

        this.selectedColor = selectedColor;
        this.unselectedColor = unselectedColor;
        this.titleColor = titleColor;

        this.background = new Sprite(new Texture(background));
        this.background.setPosition(x,y);

        font = new BitmapFont(Gdx.files.internal(fontFile));

        gl = new GlyphLayout();

        gl.setText(font,title);
        int maxWidth = (int)(gl.width);
        int height = titleBuffer + (int)(gl.height);

        for (int i = 0; i < options.length; i++) {
            gl.setText(font,options[i]);
            int w = (int)(gl.width);
            if (w > maxWidth) {
                maxWidth = w;
            }
            height += (int)(gl.height)+optionBuffer;
        }

        this.options = options;
        this.callables = callables;
        this.title = title;
        this.background.setSize(maxWidth + 2*X_BUFFER,height + 2*Y_BUFFER);

        x = CampaignBuddyMain.WIDTH/2 - (int)(this.background.getWidth()/2);
        y = CampaignBuddyMain.HEIGHT/2 - (int)(this.background.getHeight()/2);
        this.background.setPosition(x,y);
    }

    public Menu() {}

    public Menu(String background, String fontFile, String title, String[] options, MenuEvent[] callables) {
        this.background = new Sprite(new Texture(background));
        this.background.setPosition(x,y);

        font = new BitmapFont(Gdx.files.internal(fontFile));

        gl = new GlyphLayout();

        gl.setText(font,title);
        int maxWidth = (int)(gl.width);
        int height = titleBuffer + (int)(gl.height);

        for (int i = 0; i < options.length; i++) {
            gl.setText(font,options[i]);
            int w = (int)(gl.width);
            if (w > maxWidth) {
                maxWidth = w;
            }
            height += (int)(gl.height)+optionBuffer;
        }

        this.options = options;
        this.callables = callables;
        this.title = title;
        this.background.setSize(maxWidth + 2*X_BUFFER,height + 2*Y_BUFFER);

        x = CampaignBuddyMain.WIDTH/2 - (int)(this.background.getWidth()/2);
        y = CampaignBuddyMain.HEIGHT/2 - (int)(this.background.getHeight()/2);
        this.background.setPosition(x,y);
    }

    public Menu(String background, String fontFile, String title,
                String[] options, MenuEvent[] callables,
                Color selectedColor, Color unselectedColor, Color titleColor) {

        this.selectedColor = selectedColor;
        this.unselectedColor = unselectedColor;
        this.titleColor = titleColor;

        this.background = new Sprite(new Texture(background));
        this.background.setPosition(x,y);

        font = new BitmapFont(Gdx.files.internal(fontFile));

        gl = new GlyphLayout();

        gl.setText(font,title);
        int maxWidth = (int)(gl.width);
        int height = titleBuffer + (int)(gl.height);

        for (int i = 0; i < options.length; i++) {
            gl.setText(font,options[i]);
            int w = (int)(gl.width);
            if (w > maxWidth) {
                maxWidth = w;
            }
            height += (int)(gl.height)+optionBuffer;
        }

        this.options = options;
        this.callables = callables;
        this.title = title;
        this.background.setSize(maxWidth + 2*X_BUFFER,height + 2*Y_BUFFER);

        x = CampaignBuddyMain.WIDTH/2 - (int)(this.background.getWidth()/2);
        y = CampaignBuddyMain.HEIGHT/2 - (int)(this.background.getHeight()/2);
        this.background.setPosition(x,y);
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (showing) {
            background.draw(batch);

            font.setColor(titleColor);
            gl.setText(font, title);
            font.draw(batch, gl, x + background.getWidth()/2 - (gl.width/2), y + background.getHeight() - (int) (gl.height));

            int height = (int) (gl.height) + titleBuffer;
            for (int i = 0; i < options.length; i++) {
                if (selected == i)
                    font.setColor(selectedColor);
                else
                    font.setColor(unselectedColor);
                gl.setText(font, options[i]);
                height += optionBuffer + (int) (gl.height);
                font.draw(batch, gl, x + background.getWidth()/2 - (gl.width/2), y + background.getHeight() - height);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (showing) {
            if (keycode == Input.Keys.DOWN) {
                selected++;
                if (selected >= options.length)
                    selected = 0;
            } else if (keycode == Input.Keys.UP) {
                selected--;
                if (selected <= -1)
                    selected = options.length-1;
            } else if (keycode == Input.Keys.ENTER) {
                if (selected < callables.length) {
                    try {
                        callables[selected].onSelect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        return showing;
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        return showing;
    }

    @Override
    public boolean update(int x, int y) {
        return showing;
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
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void setX(float x) {

    }

    @Override
    public void setY(float y) {

    }

    @Override
    public void dispose() {

    }
}
