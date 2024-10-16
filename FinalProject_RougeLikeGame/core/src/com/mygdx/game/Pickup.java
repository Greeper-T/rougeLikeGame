package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Pickup {
    private Rectangle pickupData;
    private Texture pickupImage;
    private String pickupType;
    public Pickup (int x, int y, String pickupType){
        pickupData = new Rectangle();
        pickupData.x = x;
        pickupData.y = y;
        pickupData.width = 36;
        pickupData.height = 36;
        this.pickupType = pickupType;
        if (pickupType.equals("health")){
            pickupImage = new Texture(Gdx.files.internal("heart.gif"));
        }else if (pickupType.equals("temp")){
            pickupImage = new Texture(Gdx.files.internal("tempHeart.png"));
        }
    }

    public boolean doThingToPlayer(Player player){
        if (pickupType.equals("health")){
            if (player.getMaxHP() != player.getHp()){
                player.setHp(player.getHp()+1);
                return true;
            }
        }else if (pickupType.equals("temp")){
            player.setTempHp(player.getTempHp()+1);
            return true;
        }
        return false;
    }

    public Rectangle getPickupData() {
        return pickupData;
    }

    public Texture getPickupImage() {
        return pickupImage;
    }
    public void dispose(){
        pickupImage.dispose();
    }
}
