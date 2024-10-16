package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;


public class GameScreen implements Screen {
    private Rouge game;
    private Player player;
    private ArrayList<Floor> floors = new ArrayList<>();
    private Texture[][] map = new Texture[20][20];
    private Texture background = new Texture(Gdx.files.internal("background.png"));
    private String playerType;
    private Texture win = new Texture(Gdx.files.internal("win.png"));
    private Sound teleport = Gdx.audio.newSound(Gdx.files.internal("teleport.mp3"));
    private Music backgroundTrack;
    private Room bossRoom = new Room();
    public GameScreen (Rouge game, String player){
        this.game = game;
        this.player = new Player(player);
        this.playerType = player;
    }

    @Override
    public void show() {
        backgroundTrack = Gdx.audio.newMusic(Gdx.files.internal("backgroundTrack.mp3"));
        backgroundTrack.setLooping(true);
        backgroundTrack.setVolume(1);
        backgroundTrack.play();

        floors.add(new Floor(15, player));
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (floors.get(0).getRooms()[i][j] == null){
                    System.out.print("x");
                }else {
                    System.out.print("O");
                }
            }
            System.out.println();
        }
        updateMap();
    }

    private boolean once = false;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f,0.2f,0.2f,1);
        game.batch.begin();
        game.batch.draw(background, 0,0,1200,800);
        //player stuff
        game.batch.draw(player.getPlayerTexture(),player.getPlayerData().x,player.getPlayerData().y,player.getPlayerData().width, player.getPlayerData().height);

        for (int i = 1; i <= player.getMaxHP(); i++) {
            if (i <= player.getHp()){
                game.batch.draw(player.getHeartTexture(), i * 32,Gdx.graphics.getHeight() - 32,32,32);
            }else {
                game.batch.draw(player.getEmptyHeart(), i * 32,Gdx.graphics.getHeight() - 32,32,32);
            }
        }
        for (int i = 1; i <= player.getTempHp(); i++){
            game.batch.draw(player.getTempHeart(), (player.getMaxHP()+i)*32, Gdx.graphics.getHeight() -32, 32, 32);
        }
        for (Projectile projectile: player.getProjectiles()){
            game.batch.draw(projectile.getTexture(), projectile.getBulletData().x, projectile.getBulletData().y, projectile.getBulletData().width, projectile.getBulletData().height);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            this.dispose();
            game.setScreen(new GameScreen(game,playerType));
        }
        // end game when player dies
        if (player.getHp() <= 0){
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
        }
        //renders the floor which also renders only the room player is in
        if (floors.size()>= 1){
            if(floors.get(0).render(game.batch, player)){
                floors.get(0).checkForSeenDoors();
                updateMap();
                player.clearEverthing();
            }
            if (floors.get(0).isAllEnemiesDead()){
                teleport.play(10);
                floors.remove(0);
            }
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    game.batch.draw(map[i][j],1000 + (j*10),Gdx.graphics.getHeight() - (i*10),10,10);
                }
            }
        }else {
            if (!once){
                bossRoom.setPlayer(player);
                once = true;
                backgroundTrack.dispose();
                backgroundTrack = null;
                backgroundTrack = Gdx.audio.newMusic(Gdx.files.internal("bossTrack.mp3"));
                backgroundTrack.setLooping(true);
                backgroundTrack.setVolume(1);
                backgroundTrack.play();
            }
            if (bossRoom.getBoss() != null){
                bossRoom.render(game.batch);
            }else {
                game.batch.draw(win,0,0,1200,800);
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                    this.dispose();
                    game.setScreen(new MainMenuScreen(game));
                }
                backgroundTrack.pause();
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
        }



//        for (Enemy enemy: enemies){
//            game.batch.draw(enemy.getTexture(),enemy.getEnemyData().x,enemy.getEnemyData().y, enemy.getEnemyData().width,enemy.getEnemyData().height);
//            for (Projectile projectile: enemy.getEnemyBullets()){
//                game.batch.draw(projectile.getTexture(),projectile.getBulletData().x,projectile.getBulletData().y,projectile.getBulletData().width, projectile.getBulletData().height);
//            }
//            enemy.moveEnemy(player);
//        }
        game.batch.end();
//        player.playerLoop(enemies);
//        for (int i = enemies.size()-1; i >= 0; i--){
//            if (enemies.get(i).getHp() <= 0){
//                enemies.get(i).dispose();
//                enemies.remove(i);
//            }
//        }
    }
    public void updateMap(){
        for (int i = 0; i < map.length; i++){
             for (int j = 0; j < map[0].length; j++){
                 if (floors.get(0).getRooms()[i][j] == null){
                     map[i][j] = new Texture(Gdx.files.internal("transparentImage.png"));
                 }else if (floors.get(0).getRooms()[i][j] != null && floors.get(0).getRooms()[i][j].getPlayer() != null){
                     map[i][j] = new Texture(Gdx.files.internal("roomWithPlayer.png"));
                 }else if (floors.get(0).getRooms()[i][j] != null && !floors.get(0).getRooms()[i][j].isHasBeenSeen()){
                     map[i][j] = new Texture(Gdx.files.internal("transparentImage.png"));
                 } else if (floors.get(0).getRooms()[i][j] != null && floors.get(0).getRooms()[i][j].getPlayer() == null && floors.get(0).getRooms()[i][j].isBeenTo() && floors.get(0).getRooms()[i][j].isHasBeenSeen()){
                     map[i][j] = new Texture(Gdx.files.internal("roomButNoPlayerButBeenTo.png"));
                 }else if (floors.get(0).getRooms()[i][j] != null && floors.get(0).getRooms()[i][j].getPlayer() == null){
                     map[i][j] = new Texture(Gdx.files.internal("roomButNoPlayer.png"));
                 }
             }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        System.out.println("paused");
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bossRoom.dispose();
        if (!floors.isEmpty()){
            floors.get(0).dispose();
        }
        backgroundTrack.dispose();
        background.dispose();
        teleport.dispose();
        for (int i = 0; i< map.length; i++){
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != null){
                    map[i][j].dispose();
                }
            }
        }
        player.dispose();
    }
}
