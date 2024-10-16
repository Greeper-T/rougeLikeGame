package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;
import java.util.ArrayList;

public class Enemy {
    private int speed = 100;
    private int hp;
    private Texture texture;
    private Rectangle enemyData;
    private boolean ranged;
    private String enemyType = "";
    private long timeSinceLastShot = 0;
    private ArrayList<Projectile> enemyBullets = new ArrayList<>();
    private Sound sneeze = Gdx.audio.newSound(Gdx.files.internal("sneeze.mp3"));
    public Enemy(int x, int y, String texture, boolean ranged, String type){
        enemyData = new Rectangle();
        enemyData.x = x;
        enemyData.y = y;
        enemyType = type;
        int randomSize = Random.getRandomNumber(32,128);
        enemyData.width = randomSize;
        enemyData.height = randomSize;
        hp = Random.getRandomNumber(20,35);
        timeSinceLastShot = TimeUtils.nanoTime();
        this.ranged = ranged;
        this.texture = new Texture(Gdx.files.internal(texture));
    }
    public void moveEnemy(Player player){
        if (Math.abs(player.getPlayerData().x - getEnemyData().x) <= 300 && Math.abs(player.getPlayerData().y - getEnemyData().y) <= 300 && ranged){
            if (TimeUtils.nanoTime() - timeSinceLastShot > 1000000000){
                timeSinceLastShot = TimeUtils.nanoTime();
                if (!enemyType.equals("mine")){
                    enemyBullets.add(new Projectile(enemyData.x + enemyData.width/2, enemyData.y + enemyData.height/2, player.getPlayerData().x + player.getPlayerData().width/2, player.getPlayerData().y + player.getPlayerData().height/2, enemyData.width/2));
                }else {
                    enemyBullets.add(new Projectile(enemyData.x + enemyData.width/2, enemyData.y + enemyData.height/2, player.getPlayerData().x + player.getPlayerData().width/2, player.getPlayerData().y + player.getPlayerData().height/2, enemyData.width/2, true));
                    sneeze.play(10);
                }
            }
        }else {
            if (player.getPlayerData().x > enemyData.x){
                enemyData.x += speed * Gdx.graphics.getDeltaTime();
            }
            if (player.getPlayerData().x < enemyData.x){
                enemyData.x -= speed * Gdx.graphics.getDeltaTime();
            }
            if (player.getPlayerData().y > enemyData.y){
                enemyData.y += speed * Gdx.graphics.getDeltaTime();
            }
            if (player.getPlayerData().y < enemyData.y){
                enemyData.y -= speed * Gdx.graphics.getDeltaTime();
            }
        }
        bulletLoop(player);
    }

    public void bulletLoop(Player player){
        for (Projectile projectile: enemyBullets){
            projectile.moveBullet();
        }
        for (int i = enemyBullets.size()-1; i >= 0; i--){
            if (enemyBullets.get(i).getBulletData().intersects(player.getPlayerData()) && player.isCanTakeDamage()){
                player.takeDamage();
                enemyBullets.get(i).dispose();
                enemyBullets.remove(i);
            }else if (!enemyBullets.get(i).isCanMove() && !enemyType.equals("mine")){
                enemyBullets.get(i).dispose();
                enemyBullets.remove(i);
            }
        }
    }
    public void takeDamage(int dmg){
        hp -= dmg;
    }

    public int getHp() {
        return hp;
    }

    public Rectangle getEnemyData() {
        return enemyData;
    }

    public Texture getTexture() {
        return texture;
    }

    public ArrayList<Projectile> getEnemyBullets() {
        return enemyBullets;
    }

    public void dispose(){
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).dispose();
        }
        texture.dispose();
    }
}
