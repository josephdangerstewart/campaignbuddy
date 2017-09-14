package com.campaignbuddy.resources.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.ChangeEvent;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveContainer;

/**
 * Created by josephstewart on 8/22/17.
 */
public class DropDownList extends InteractiveContainer {

    private final int Y_BUFFER = 10;
    private final int X_BUFFER = 2;

    private InteractableList list;

    private Image box;
    private List itemList;
    private Button button;

    private ChangeEvent event;

    private Text selectedOption;

    private OrthographicCamera camera;

    double id;

    public DropDownList(int x, int y, String src, String name, String font, OrthographicCamera camera, Viewport viewport) {
        TextureAtlas atlas = new TextureAtlas(src + name + ".txt");

        selectedOption = new Text(x+X_BUFFER,y,font,"");
        selectedOption.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0987654321|");

        id = Math.random();

        this.camera = camera;
        list = new InteractableList(x,y);

        box = new Image(new Sprite(atlas.findRegion(name+"-box")));
        box.setPosition(x,y);
        box.getSprite().setSize(box.getWidth(),selectedOption.getHeight() + 2*Y_BUFFER);

        selectedOption.setY(y+selectedOption.getHeight() + (box.getHeight()/2-selectedOption.getHeight()/2));
        selectedOption.setText("");

        button = new Button(x, y, new Sprite(atlas.findRegion(name+"-button")), new Event() {
            @Override
            public void onCall() {
                if (box.getY()-itemList.getHeight()>0) {
                    itemList.setY(box.getY()-itemList.getHeight());
                } else {
                    itemList.setY(box.getY()+box.getHeight());
                }
                itemList.setSelectedIndex(-1);
                itemList.setVisible(true);
            }
        });
        button.setSize(button.getWidth(),box.getHeight());
        button.setX(x+box.getWidth());

        itemList = new List(x,y,new Sprite(atlas.findRegion(name+"-list")),new Sprite(atlas.findRegion(name+"-indicator")),font,camera,viewport);
        itemList.setVisible(false);

        itemList.setOnChange(new ChangeEvent() {
            @Override
            public void onChange(int change) {
                if (change != -1) {
                    itemList.setVisible(false);
                    selectedOption.setText(itemList.getOption(change));
                    if (event != null) {
                        event.onChange(change);
                    }
                } else {
                    itemList.setVisible(false);
                }
            }
        });

        list.add(box);
        list.add(button);
        list.add(itemList);
        list.add(selectedOption);
    }

    public void setEvent(ChangeEvent event) {
        this.event = event;
    }

    public int getSelectedIndex() {
        return itemList.getSelectedIndex();
    }

    public String getOption(int index) {
        return itemList.getOption(index);
    }

    public void setOptions(String[] options) {
        itemList.clear();
        for (int i = 0; i < options.length; i++) {
            itemList.add(options[i]);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        box.draw(batch);
        button.draw(batch);
        if(itemList.getVisible()) {
            itemList.draw(batch);
        }

        Rectangle scissors = new Rectangle();
        Rectangle bounds = new Rectangle(box.getX(),box.getY(),box.getWidth(),box.getHeight());
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), bounds, scissors);
        batch.flush();

        boolean bound = ScissorStack.pushScissors(scissors);
        if (bound) {
            //DRAWING STARTS HERE

            selectedOption.draw(batch);

            //DRAWING ENDS HERE

            batch.flush();
            ScissorStack.popScissors();
        } else {
            batch.flush();
        }
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
        return box.getWidth() + button.getWidth();
    }

    @Override
    public int getHeight() {
        return button.getHeight();
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
        if (itemList.getVisible() && !itemList.contains(x,y)) {
            itemList.setVisible(false);
        }
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
