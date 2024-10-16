package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.awt.*;

public class Projectile {
    private int speed = 500;
    private Texture texture;
    private Rectangle bulletData;
    private int targetX, targetY;
    private boolean canMove = true;
    private boolean isMine = false;

    public Projectile(int x, int y, int targetX, int targetY, int size){
        bulletData = new Rectangle();
        bulletData.x = x;
        bulletData.y = y;
        bulletData.width = size;
        bulletData.height = size;
        this.targetY = targetY;
        this.targetX = targetX;
        this.speed = 250;
        texture = new Texture(Gdx.files.internal("enemyBullet.png"));
    }
    public Projectile(int x, int y, int targetX, int targetY, int size, boolean isMine){
        bulletData = new Rectangle();
        bulletData.x = x;
        bulletData.y = y;
        bulletData.width = size;
        bulletData.height = size;
        this.targetY = targetY;
        this.targetX = targetX;
        this.speed = 250;
        this.isMine = true;
        texture = new Texture(Gdx.files.internal("snot.png"));
    }
    public Projectile(int x, int y, String direction, int size, int range){
        bulletData = new Rectangle();
        bulletData.x = x;
        bulletData.y = y;
        bulletData.width = size;
        bulletData.height = size;
        if (direction.equals("up")){
            targetY = y + range;
            targetX = x;
        }else if (direction.equals("down")){
            targetY = y - range;
            targetX = x;
        }else if (direction.equals("left")){
            targetX = x - range;
            targetY = y;
        }else if (direction.equals("right")){
            targetX = x + range;
            targetY = y;
        }
        texture = new Texture(Gdx.files.internal("bullet.png"));
    }

    public void moveBullet(){
//        if (!canDamagePlayer){

//            if (direction.equals("up")){
//                bulletData.y += speed * Gdx.graphics.getDeltaTime();
//                range -= speed;
//            }else if(direction.equals("down")){
//                bulletData.y -= speed * Gdx.graphics.getDeltaTime();
//                range -= speed;
//            }else if(direction.equals("left")){
//                bulletData.x -= speed * Gdx.graphics.getDeltaTime();
//                range -= speed;
//            }else if(direction.equals("right")){
//                bulletData.x += speed * Gdx.graphics.getDeltaTime();
//                range -= speed;
//            }
//        }targetX == bulletData.x && targetY == bulletData.y

        if (Math.abs(targetX - bulletData.x) <= 10 && Math.abs(targetY - bulletData.y) <= 10){
            canMove = false;
            bulletData.x = targetX;
            bulletData.y = targetY;
        }else if (canMove){
            // from ChatGPT

            // Calculate the angle between the current position of the bullet and the target position
            float angle = MathUtils.atan2(targetY - bulletData.y, targetX - bulletData.x);

            // Calculate the velocity components in x and y directions
            float velocityX = speed * MathUtils.cos(angle);
            float velocityY = speed * MathUtils.sin(angle);

            // Move the bullet along the calculated velocity components
            bulletData.x += velocityX * Gdx.graphics.getDeltaTime();
            bulletData.y += velocityY * Gdx.graphics.getDeltaTime();
//            if (bulletData.x > targetX){
//                bulletData.x -= speed * Gdx.graphics.getDeltaTime();
//            }
//            if (bulletData.x < targetX){
//                bulletData.x += speed * Gdx.graphics.getDeltaTime();
//            }
//            if (bulletData.y > targetY){
//                bulletData.y -= speed * Gdx.graphics.getDeltaTime();
//            }
//            if (bulletData.y < targetY){
//                bulletData.y += speed * Gdx.graphics.getDeltaTime();
//            }
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBulletData() {
        return bulletData;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void dispose(){
        texture.dispose();
    }

    public boolean isMine() {
        return isMine;
    }
}
