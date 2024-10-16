package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Door {
    private Rectangle doorData;
    private Texture doorTexture;
    private String direction;
    public Door(String direction){
        doorData = new Rectangle();
        doorData.width = 80;
        doorData.height = 80;
        this.direction = direction;
        if (direction.equals("left")){
            doorData.x = 0;
            doorData.y = Gdx.graphics.getHeight()/2 - doorData.height/2;
            doorTexture = new Texture(Gdx.files.internal("doorOpenLeft.png"));
        }
        if (direction.equals("right")){
            doorData.x = Gdx.graphics.getWidth() - doorData.width;
            doorData.y = Gdx.graphics.getHeight()/2 - doorData.height/2;
            doorTexture = new Texture(Gdx.files.internal("doorOpenRight.png"));
        }
        if (direction.equals("top")){
            doorData.x = Gdx.graphics.getWidth()/2 - doorData.width/2;
            doorData.y = Gdx.graphics.getHeight() - doorData.height;
            doorTexture = new Texture(Gdx.files.internal("doorOpenTop.png"));
        }
        if (direction.equals("bottom")){
            doorData.x = Gdx.graphics.getWidth()/2 - doorData.width/2;
            doorData.y = 0;
            doorTexture = new Texture(Gdx.files.internal("doorOpenBottom.png"));
        }
        System.out.println(direction);
    }

    public Rectangle getDoorData() {
        return doorData;
    }

    public Texture getDoorTexture() {
        return doorTexture;
    }

    public String getDirection() {
        return direction;
    }
    public void dispose(){
        doorTexture.dispose();
    }
}
