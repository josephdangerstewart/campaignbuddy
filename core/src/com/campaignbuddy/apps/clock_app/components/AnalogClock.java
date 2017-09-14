package com.campaignbuddy.apps.clock_app.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.resources.containers.InteractableList;

/**
 * Created by josephstewart on 8/27/17.
 */
public class AnalogClock extends Clock{

    InteractableList list;

    Sprite body;
    Sprite hourHand;
    Sprite minuteHand;

    public AnalogClock(int x, int y, int minute, int hour) {
        list = new InteractableList(x,y);

        body = new Sprite(new Texture("images/clock-app/clock-body.png"));
        hourHand = new Sprite(new Texture("images/clock-app/minute-hand.png"));
        minuteHand = new Sprite(new Texture("images/clock-app/hour-hand.png"));

        hourHand.setOrigin(hourHand.getX(),hourHand.getY()+hourHand.getHeight()/2);
        minuteHand.setOrigin(minuteHand.getX(),minuteHand.getY()+minuteHand.getHeight()/2);

        body.setPosition(x,y);
        setTime(hour,minute);

        hourHand.setPosition(body.getX()+body.getWidth()/2+6,body.getY()+body.getHeight()/2-20);
        minuteHand.setPosition(body.getX()+body.getWidth()/2+6,body.getY()+body.getHeight()/2-20);

        list.add(body);
        list.add(hourHand);
        list.add(minuteHand);
    }

    @Override
    public void setTime(int hour, int minute) {
        hourHand.setRotation((float)getHourRotation(hour,minute)+90);
        minuteHand.setRotation((float)getMinuteRotation(minute)+90);
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
        return (int) body.getWidth();
    }

    @Override
    public int getHeight() {
        return (int)body.getHeight();
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
