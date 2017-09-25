package com.campaignbuddy.resources.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.meta.TextChangeEvent;
import com.campaignbuddy.state.meta.State;

/**
 * Created by josephstewart on 8/21/17.
 */
public class TextField extends InteractiveDrawable {

    //Comment this is a comment

    private final int Y_BUFFER = 10;
    private final int X_BUFFER = 0;
    private final String cursor = "|";
    private int cursorOffset = 0;
    private int width;
    private int height;
    private String text;
    private Sprite background;
    private BitmapFont font;
    private GlyphLayout layout;
    private boolean edit;
    private boolean clearOnEdit;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TextChangeEvent event;
    private Event onEnter;
    private TextField beforeTab;
    private TextField afterTab;

    private int x;
    private int y;

    public TextField(int x, int y, String background, String font, int width, OrthographicCamera camera, Viewport viewport) {
        this.x = x;
        this.y = y;
        this.background = new Sprite(new Texture(background));
        this.background.setX(x);
        this.background.setY(y);
        this.font = new BitmapFont(Gdx.files.internal(font));
        this.width = width;
        this.camera = camera;
        this.viewport = viewport;

        edit = false;
        text = "";

        layout = new GlyphLayout();

        layout.setText(this.font,"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-+[]");
        height = (int)layout.height+(Y_BUFFER*2);

        this.background.setSize(width,layout.height+(Y_BUFFER*2));

        clearOnEdit = false;
    }

    public void setOnChange(TextChangeEvent event) {
        this.event = event;
    }

    public boolean getEdit() {
        return edit;
    }

    @Override
    public void draw(SpriteBatch batch) {
        background.draw(batch);

        Rectangle scissors = new Rectangle();
        Rectangle bounds = new Rectangle(x+X_BUFFER,y,width-(X_BUFFER*2),height);
        ScissorStack.calculateScissors(camera, viewport.getScreenX(),viewport.getScreenY(),viewport.getScreenWidth(),viewport.getScreenHeight(), batch.getTransformMatrix(), bounds, scissors);
        batch.flush();
        ScissorStack.pushScissors(scissors);


        //DRAWING STARTS HERE

        layout.setText(font,text + cursor);
        String tempText;

        if (edit) {
            if (cursorOffset > 0) {
                String text1;
                String text2;

                text1 = text.substring(0, text.length() - cursorOffset);
                text2 = text.substring(text.length() - cursorOffset, text.length());
                tempText = text1 + cursor + text2;
            } else {
                tempText = text + cursor;
            }
        } else {
            tempText = text;
        }

        int width = (int)layout.width + X_BUFFER;
        if (width < this.width)
            font.draw(batch,tempText,x+X_BUFFER,y+layout.height/2+height/2);
        else
            font.draw(batch,tempText,x+X_BUFFER-(width-this.width),y+layout.height/2+height/2);

        //DRAWING ENDS HERE

        batch.flush();
        ScissorStack.popScissors();

    }

    public void setClearOnEdit(boolean clearOnEdit) {
        this.clearOnEdit = clearOnEdit;
    }

    public void change(String text) {
        edit = false;
        if (event != null)
            event.onChange(text);
    }

    public void setTextNoChange(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
        change(text);
    }

    public String getText() {
        System.out.println("Text: " + text);
        return text;
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        if (background.getBoundingRectangle().contains(x,y)) {
            edit = true;
            if (clearOnEdit) {
                text = "";
            }
        } else {
            change(text);
        }
        return false;
    }

    public void click() {
        edit = true;
        if (clearOnEdit) {
            text = "";
        }
    }

    @Override
    public boolean keyTyped(char character) {
        if (edit) {
            if (character == 8 && text.length() - cursorOffset > 0) {
                if (cursorOffset > 0) {
                    String text1 = text.substring(0, text.length() - cursorOffset);
                    String text2 = text.substring(text.length() - cursorOffset, text.length());

                    text1 = text1.substring(0, text1.length() - 1);

                    text = text1 + text2;
                } else {
                    String newText = text.substring(0, text.length() - 1);
                    System.out.println(newText);
                    text = newText;
                }
                return true;
            }
            if ((character >= '!' && character <= '~') || character == ' ') {
                String key = Character.toString(character);
                if (cursorOffset > 0) {
                    String text1 = text.substring(0, text.length() - cursorOffset);
                    String text2 = text.substring(text.length() - cursorOffset, text.length());

                    System.out.println("Text: " + text1 + text2);

                    text = text1 + key + text2;
                } else {
                    System.out.println("Text: " + text);
                    text = text + key;
                }
                return true;
            }
        }
        return false;
    }

    public void setTabs(TextField beforeTab, TextField afterTab) {
        this.beforeTab = beforeTab;
        this.afterTab = afterTab;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (edit) {
            if (keycode == Input.Keys.LEFT && cursorOffset < text.length()) {
                cursorOffset++;
            } else if (keycode == Input.Keys.RIGHT && cursorOffset > 0) {
                cursorOffset--;
            } else if (keycode == Input.Keys.ENTER) {
                change(text);
                if (onEnter != null) {
                    onEnter.onCall();
                }
            } else if (keycode == Input.Keys.TAB && (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))) {
                change(text);
                if (beforeTab != null) {
                    beforeTab.click();
                }
            } else if (keycode == Input.Keys.TAB) {
                change(text);
                if (afterTab != null) {
                    afterTab.click();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean update(int x, int y) {
        return false;
    }

    @Override
    public void cancelUpdate() {
        if (edit) {
            edit = false;
            if (event != null) {
                event.onChange(text);
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setOnEnter(Event onEnter) {
        this.onEnter = onEnter;
    }

    @Override
    public void setX(float x) {
        this.x = (int)x;
        background.setX(x);
    }

    @Override
    public void setY(float y) {
        this.y = (int)y;
        background.setY(y);
    }

    @Override
    public void dispose() {
        background.getTexture().dispose();
    }
}
