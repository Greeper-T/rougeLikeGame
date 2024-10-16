package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private int speed;
    private int maxHP = 5;
    private int hp = 5;
    private long fireRate;
    private int damage;
    private int bulletSize = 21;
    private int range;
    private long lastDamageTick, lastBulletFired;
    private int tempHp = 0;
    private int contactDamage = 5;
    private boolean canTakeDamage = true;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private Texture playerTexture, heartTexture, damagedTexture, emptyHeart, tempHeart;
    private Sound damaged;
    private Rectangle playerData;
    private String playerType;

    public Player(String playerType){
        playerData = new Rectangle();
        playerData.x = Gdx.graphics.getWidth()/2 - 32;
        playerData.y = Gdx.graphics.getHeight()/2 - 32;
        playerData.width = 64;
        playerData.height = 64;

        heartTexture = new Texture(Gdx.files.internal("heart.gif"));
        emptyHeart = new Texture(Gdx.files.internal("emptyHeart.png"));
        tempHeart = new Texture(Gdx.files.internal("tempHeart.png"));

        this.playerType = playerType;
        if (playerType.equals("Dylan")){
            playerTexture = new Texture(Gdx.files.internal("dylan.png"));
            damagedTexture = new Texture(Gdx.files.internal("dylanTakeDamage.png"));
            damaged = Gdx.audio.newSound(Gdx.files.internal("fart.mp3"));
            maxHP = 1;
            hp = maxHP;
            speed = 500;
            damage = 2;
            fireRate = 25000000;
            bulletSize = 10;
            range = 500;
            playerData.width = 40;
            playerData.height = 40;
        }else if (playerType.equals("Daniel")){
            playerTexture = new Texture(Gdx.files.internal("daniel.png"));
            damagedTexture = new Texture(Gdx.files.internal("danielTakeDamage.png"));
            damaged = Gdx.audio.newSound(Gdx.files.internal("fart.mp3"));
            maxHP = 10;
            hp = maxHP;
            speed = 200;
            damage = 50;
            fireRate = 1000000000L;
            bulletSize = 100;
            range  = 1000;
        }else if (playerType.equals("Jiho")){
            playerTexture = new Texture(Gdx.files.internal("jiho.png"));
            damagedTexture = new Texture(Gdx.files.internal("jihoTakeDamage.png"));
            damaged = Gdx.audio.newSound(Gdx.files.internal("fart.mp3"));
            maxHP = 10;
            hp = maxHP;
            speed = 700;
            contactDamage = 50;
        }else if (playerType.equals("Big Head Joe")){
            playerTexture = new Texture(Gdx.files.internal("joe.png"));
            damagedTexture = new Texture(Gdx.files.internal("joe.png"));
            damaged = Gdx.audio.newSound(Gdx.files.internal("fart.mp3"));
            maxHP = 20;
            hp = maxHP;
            speed = 800;
            damage = 150;
            fireRate = 25000000;
            bulletSize = 32;
            range  = 1000;
        }
    }
    public void playerLoop(ArrayList<Enemy> enemies){
        movePlayer();
        fireBullet();
        for (Enemy enemy: enemies){
            if (enemy.getEnemyData().intersects(playerData)){
                if (canTakeDamage || playerType.equals("Jiho")){
                    enemy.takeDamage(contactDamage);
                    takeDamage();
                }
            }
        }
        if (TimeUtils.nanoTime() - lastDamageTick > 1000000000){
            canTakeDamage = true;
        }
        if (projectiles.size()>0){
            for (int i = projectiles.size()-1; i >= 0; i--){
                projectiles.get(i).moveBullet();
                if (!projectiles.get(i).isCanMove()){
                    projectiles.get(i).dispose();
                    projectiles.remove(i);
                }
                for (int j = enemies.size()-1; j >= 0; j--){
                    if (!projectiles.isEmpty()){
                        if (projectiles.get(i).getBulletData().intersects(enemies.get(j).getEnemyData())){
                            enemies.get(j).takeDamage(damage);
                            projectiles.get(i).dispose();
                            projectiles.remove(i);
                            break;
                        }
                    }
                }
            }
        }
    }
    public void playerLoop(Boss boss){
        movePlayer();
        fireBullet();
        if (boss.getEnemyData().intersects(playerData)){
            if (canTakeDamage || playerType.equals("Jiho")){
                boss.takeDamage(contactDamage);
                takeDamage();
            }
        }
        if (TimeUtils.nanoTime() - lastDamageTick > 1000000000){
            canTakeDamage = true;
        }
        if (projectiles.size()>0){
            for (int i = projectiles.size()-1; i >= 0; i--){
                projectiles.get(i).moveBullet();
                if (!projectiles.get(i).isCanMove()){
                    projectiles.get(i).dispose();
                    projectiles.remove(i);
                }
                if (!projectiles.isEmpty()){
                    if (projectiles.get(i).getBulletData().intersects(boss.getEnemyData())){
                        boss.takeDamage(damage);
                        projectiles.get(i).dispose();
                        projectiles.remove(i);
                    }
                }
            }
        }
    }
    public void fireBullet(){
        if (!playerType.equals("Sword Guy")){
            if (TimeUtils.nanoTime() - lastBulletFired > fireRate){
                if (Gdx.input.isKeyPressed(Input.Keys.UP)){
                    projectiles.add(new Projectile(playerData.x + playerData.width/2 - bulletSize/2, playerData.y + 64,"up",bulletSize,range));
                }else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                    projectiles.add(new Projectile(playerData.x + playerData.width/2 - bulletSize/2, playerData.y - bulletSize,"down",bulletSize,range));
                }else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                    projectiles.add(new Projectile(playerData.x - bulletSize, playerData.y + playerData.height/2 - bulletSize/2,"left",bulletSize,range));
                }else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                    projectiles.add(new Projectile(playerData.x + 64, playerData.y + playerData.height/2 - bulletSize/2,"right",bulletSize,range));
                }
                lastBulletFired = TimeUtils.nanoTime();
            }
        }


    }

    public void clearEverthing(){
        for (int i = projectiles.size() -1; i >= 0; i--) {
            projectiles.get(i).dispose();
            projectiles.remove(i);
        }
    }
    public void movePlayer(){
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            playerData.y += speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            playerData.y -= speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            playerData.x -= speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            playerData.x += speed * Gdx.graphics.getDeltaTime();
        }
        if (playerData.x <= 64){
            playerData.x = 64;
        }
        if (playerData.x >= Gdx.graphics.getWidth() - playerData.width - 64){
            playerData.x = Gdx.graphics.getWidth() - playerData.width - 64;
        }
        if (playerData.y <= 64) {
            playerData.y = 64;
        }
        if (playerData.y >= Gdx.graphics.getHeight() - playerData.height - 64){
            playerData.y = Gdx.graphics.getHeight() - playerData.height - 64;
        }
    }

    public Rectangle getPlayerData() {
        return playerData;
    }

    public Texture getPlayerTexture() {
        if (canTakeDamage){
            return playerTexture;
        }else {
            return damagedTexture;
        }
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public int getHp() {
        return hp;
    }
    public void takeDamage(){
        if (canTakeDamage){
            if (tempHp == 0){
                hp--;
            }else{
                tempHp--;
            }
            damaged.play(5);
            canTakeDamage = false;
            lastDamageTick = TimeUtils.nanoTime();
        }
    }

    public boolean isCanTakeDamage() {
        return canTakeDamage;
    }

    public Texture getHeartTexture() {
        return heartTexture;
    }

    public Texture getEmptyHeart() {
        return emptyHeart;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Texture getTempHeart() {
        return tempHeart;
    }

    public int getTempHp() {
        return tempHp;
    }

    public void setTempHp(int hp){
        tempHp = hp;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void dispose(){
        playerTexture.dispose();
        heartTexture.dispose();
        damagedTexture.dispose();
        emptyHeart.dispose();
        damaged.dispose();
        tempHeart.dispose();
    }
}
