package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.ArrayList;

public class Boss {
    private int hp = 500;
    private Texture bossTexture;
    private Rectangle bossData;
    private long interValBetweenAttacks;
    private long fireIntervals;
    private long fireRateOfMiniGun = 50000000;
    private int bulletsToGetTo = 0;
    private ArrayList<Projectile> bullets = new ArrayList<>();
    private Sound teleport = Gdx.audio.newSound(Gdx.files.internal("teleport.mp3"));
    private Sound sneeze = Gdx.audio.newSound(Gdx.files.internal("sneeze.mp3"));

    public Boss(){
        bossData = new Rectangle();
        bossData.width = 75;
        bossData.height = 100;
        bossData.x = Gdx.graphics.getWidth()/2 - bossData.width/2;
        bossData.y = Gdx.graphics.getHeight()/2 - bossData.height/2;
        bossTexture = new Texture(Gdx.files.internal("boss.png"));
    }

    public void moves(Player player){
        if (TimeUtils.nanoTime() - interValBetweenAttacks > 3000000000L){
            int random = Random.getRandomNumber(1,3);
            if (random == 1){
                bossData.x = Random.getRandomNumber(0,Gdx.graphics.getWidth()-bossData.width);
                bossData.y = Random.getRandomNumber(0,Gdx.graphics.getHeight() - bossData.height);
                teleport.play(10);
            }else if (random == 2){
                sneeze.play(10);
                bullets.add(new Projectile(bossData.x + bossData.width/2, bossData.y + bossData.height/2, player.getPlayerData().x + player.getPlayerData().width/2, player.getPlayerData().y + player.getPlayerData().height/2, 64, true));
            }else if (random == 3){
                bulletsToGetTo = bullets.size() + 10;
            }
            interValBetweenAttacks = TimeUtils.nanoTime();
        }
        if (TimeUtils.nanoTime() - fireIntervals > fireRateOfMiniGun && bullets.size() < bulletsToGetTo){
            bullets.add(new Projectile(bossData.x + bossData.width/2, bossData.y + bossData.height/2, player.getPlayerData().x + player.getPlayerData().width/2, player.getPlayerData().y + player.getPlayerData().height/2, bossData.width/2));
            fireIntervals = TimeUtils.nanoTime();
        }
        bulletLoop(player);
    }
    public void bulletLoop(Player player){
        for (Projectile projectile: bullets){
            projectile.moveBullet();
        }
        for (int i = bullets.size()-1; i >= 0; i--){
            if (bullets.get(i).getBulletData().intersects(player.getPlayerData()) && player.isCanTakeDamage()){
                player.takeDamage();
                bullets.get(i).dispose();
                bullets.remove(i);
            }else if (!bullets.get(i).isCanMove() && !bullets.get(i).isMine()){
                bullets.get(i).dispose();
                bullets.remove(i);
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
        return bossData;
    }

    public Texture getTexture() {
        return bossTexture;
    }

    public ArrayList<Projectile> getEnemyBullets() {
        return bullets;
    }

    public void dispose(){
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).dispose();
        }
        bossTexture.dispose();
        teleport.dispose();
        sneeze.dispose();
    }

}
