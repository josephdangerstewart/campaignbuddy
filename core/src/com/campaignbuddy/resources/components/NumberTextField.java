package com.campaignbuddy.resources.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by josephstewart on 8/21/17.
 */
public class NumberTextField extends TextField {

    public NumberTextField(int x, int y, String background, String font, int width, OrthographicCamera camera, Viewport viewport) {
        super(x,y,background,font,width,camera, viewport);
        setText("0");
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    @Override
    public void change(String text) {
        System.out.println(":D");
        if (!isInteger(text)) {
            setTextNoChange("0");
            super.change("0");
        } else {
            super.change(text);
        }
    }

    @Override
    public void setText(String text) {
        if (isInteger(text)) {
            super.setText(text);
        } else {
            super.setText("0");
        }
    }

    public int getNumber() {
        if (super.getText().equals("")) {
            return 0;
        } else {
            return Integer.parseInt(super.getText());
        }
    }

    @Override
    public boolean keyTyped(char character) {
        if ((character >= '0' && character <= '9') ||
                character == 8 || character == '-') {
            return super.keyTyped(character);
        }
        return true;
    }
}
