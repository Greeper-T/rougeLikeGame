package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class Room {
    private ArrayList<Door> doors = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Player player = null;
    private boolean beenTo = false;
    private boolean hasBeenSeen = false;
    private boolean canLeave = false;
    private Pickup pickup;
    private Sound bossDeath = Gdx.audio.newSound(Gdx.files.internal("bossDeath.mp3"));
    Boss boss = null;
    public Room(int numEnemies){
        for (int i = 0; i < numEnemies; i++){
            int random = Random.getRandomNumber(1,3);
            if (random == 1){
                enemies.add(new Enemy(getRandomNumber(200, Gdx.graphics.getWidth()-64-200), getRandomNumber(200,Gdx.graphics.getHeight()-64-200), "enemy.png",false, ""));
            }else if (random == 2){
                enemies.add(new Enemy(getRandomNumber(200,Gdx.graphics.getWidth()-64-200), getRandomNumber(200,Gdx.graphics.getHeight()-64-200), "ranged.png",true, ""));
            }else{
                enemies.add(new Enemy(getRandomNumber(200,Gdx.graphics.getWidth()-64-200), getRandomNumber(200,Gdx.graphics.getHeight()-64-200), "nose.png",true, "mine"));

            }
        }
    }

    public Room(){
        boss = new Boss();
    }

    public void render(Batch batch){
        if (boss == null){
            player.playerLoop(enemies);
            // renders all the enemies in the room
            for (Enemy enemy: enemies){
                batch.draw(enemy.getTexture(),enemy.getEnemyData().x,enemy.getEnemyData().y, enemy.getEnemyData().width,enemy.getEnemyData().height);
                for (Projectile projectile: enemy.getEnemyBullets()){
                    batch.draw(projectile.getTexture(),projectile.getBulletData().x,projectile.getBulletData().y,projectile.getBulletData().width, projectile.getBulletData().height);
                }
                enemy.moveEnemy(player);

            }
            // draws the pickups and check if intersection
            if (pickup != null){
                batch.draw(pickup.getPickupImage(), pickup.getPickupData().x, pickup.getPickupData().y, pickup.getPickupData().width, pickup.getPickupData().height);
                if (player.getPlayerData().intersects(pickup.getPickupData())){
                    if (pickup.doThingToPlayer(player)){
                        pickup.dispose();
                        pickup = null;
                    }
                }
            }

            //does not leave unless no enemies left
            if (enemies.isEmpty()){
                canLeave = true;
            }else {
                canLeave = false;
            }

            // draws the doors
            for (Door door: doors){
                batch.draw(door.getDoorTexture(), door.getDoorData().x, door.getDoorData().y, door.getDoorData().width, door.getDoorData().height);
            }
            enemyLoop();
        }else{
            player.playerLoop(boss);
            batch.draw(boss.getTexture(),boss.getEnemyData().x,boss.getEnemyData().y, boss.getEnemyData().width,boss.getEnemyData().height);
            for (Projectile projectile: boss.getEnemyBullets()){
                batch.draw(projectile.getTexture(),projectile.getBulletData().x,projectile.getBulletData().y,projectile.getBulletData().width, projectile.getBulletData().height);
            }
            boss.moves(player);
            if (boss.getHp()<= 0){
                boss.dispose();
                boss = null;
                bossDeath.play(10);
            }
        }
    }

    public void enemyLoop(){
        for (int i = enemies.size()-1; i >= 0; i--){
            if (enemies.get(i).getHp() <= 0){
                enemies.get(i).dispose();
                enemies.remove(i);
                if (enemies.isEmpty()){
                    int random = Random.getRandomNumber(1,3);
                    if (player.getPlayerType().equals("Jiho")){
                        pickup = new Pickup(Gdx.graphics.getWidth()/2 - 18, Gdx.graphics.getHeight()/2 - 18, "health");
                    }else {
                        if (random == 1){
                            pickup = new Pickup(Gdx.graphics.getWidth()/2 - 18, Gdx.graphics.getHeight()/2 - 18, "health");
                        }else if (random == 2){
                            pickup = new Pickup(Gdx.graphics.getWidth()/2 - 18, Gdx.graphics.getHeight()/2 - 18, "temp");
                        }
                    }

                }
            }
        }
    }

    public Door interceptDoor(){
        for (Door door: doors){
            if (door.getDoorData().intersects(player.getPlayerData())){
                return door;
            }
        }
        return null;
    }

    public void addDoor(Door door){
        doors.add(door);
    }
    public void beenSeen (){
        hasBeenSeen = true;
    }

    public boolean isHasBeenSeen() {
        return hasBeenSeen;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
    public int getRandomNumber(int lowest, int highest){
        highest -= lowest -1;
        return (int)(Math.random()*highest+lowest);
    }

    public void beenTo(){
        beenTo = true;
    }

    public boolean isBeenTo() {
        return beenTo;
    }

    public boolean isCanLeave() {
        return canLeave;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Boss getBoss() {
        return boss;
    }

    public void dispose(){
        bossDeath.dispose();
        for (Door door: doors){
            door.dispose();
        }
    }
}
