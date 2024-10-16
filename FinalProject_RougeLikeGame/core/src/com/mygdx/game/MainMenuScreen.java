package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Rouge;

import java.awt.*;

public class MainMenuScreen implements Screen {
    // main screen of the game from https://www.youtube.com/watch?v=67ZCQt8QpNA&list=PLrnO5Pu2zAHKAIjRtTLAXtZKMSA6JWnmf&index=4
    private Rectangle exitButton = new Rectangle((Gdx.graphics.getWidth()/2)-100,50,200,100);
    private Rectangle playButton = new Rectangle((Gdx.graphics.getWidth()/2)-100,150,200,100);
    private Rectangle displayCharacter = new Rectangle(Gdx.graphics.getWidth()/2-100, 300, 200,200);
    private Rectangle leftArrow = new Rectangle(400, 375, 50,50);
    private Rectangle rightArrow = new Rectangle(750, 375, 50,50);
    private Rouge game;
    private Texture exitButtonActive, exitButtonInactive, playButtonActive, playButtonInactive, character, left, right;
    private OrthographicCamera camera = new OrthographicCamera();
    private String playerType = "Dylan";
    private int temp = 500;
    public MainMenuScreen(Rouge game){
        this.game = game;
    }
    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        playButtonActive = new Texture(Gdx.files.internal("play_button_active.png"));
        playButtonInactive = new Texture(Gdx.files.internal("play_button_inactive.png"));
        exitButtonActive = new Texture(Gdx.files.internal("exit_button_active.png"));
        exitButtonInactive = new Texture(Gdx.files.internal("exit_button_inactive.png"));
        character = new Texture(Gdx.files.internal("dylan.png"));
        left = new Texture(Gdx.files.internal("leftArrowInactive.png"));
        right = new Texture(Gdx.files.internal("rightArrowInactive.png"));
    }

    private float t = 0;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f,1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // font is from chatGPT

        // Set the desired font size
        int fontSize = 4; // Change this value to adjust the font size

// Set the scale of the font
        game.font.getData().setScale(fontSize); // defaultFontSize is the original size of the font

// Draw the text with the adjusted font size
        game.font.draw(game.batch, "Totally Not Inspired By TBOI", 190, 750);
        game.font.draw(game.batch, playerType, (float) Gdx.graphics.getWidth() /2-100, 600);

        game.font.getData().setScale(1);
        game.font.draw(game.batch, "inspired by TBOI", 5,15);

// Reset the scale back to its original value (optional)
        game.font.getData().setScale(1); // Reset the scale to its default value



        game.batch.draw(character, displayCharacter.x, displayCharacter.y, displayCharacter.width, displayCharacter.height);

        game.batch.draw(left, leftArrow.x, leftArrow.y, leftArrow.width, leftArrow.height);
        game.batch.draw(right,rightArrow.x, rightArrow.y, rightArrow.width, rightArrow.height);

        if (isMouseIn(leftArrow)){
            left = new Texture(Gdx.files.internal("leftArrowActive.png"));
            if (Gdx.input.isTouched() && TimeUtils.nanoTime() - t > 250000000){
                setPlayerImage("left");
                t= TimeUtils.nanoTime();
            }
        }else {
            left = new Texture(Gdx.files.internal("leftArrowInactive.png"));
        }

        if (isMouseIn(rightArrow)){
            right = new Texture(Gdx.files.internal("rightArrowActive.png"));
            if (Gdx.input.isTouched() && TimeUtils.nanoTime() - t > 250000000){
                setPlayerImage("right");
                t = TimeUtils.nanoTime();
            }
        }else {
            right = new Texture(Gdx.files.internal("rightArrowInactive.png"));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            setPlayerImage("left");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            setPlayerImage("right");
        }

        if (isMouseIn(exitButton)){
            game.batch.draw(exitButtonActive, exitButton.x,exitButton.y,exitButton.width,exitButton.height);
            if (Gdx.input.isTouched()){
                this.dispose();
                Gdx.app.exit();
            }
        }else {
            game.batch.draw(exitButtonInactive, exitButton.x,exitButton.y,exitButton.width,exitButton.height);
        }
        if (isMouseIn(playButton)){
            game.batch.draw(playButtonActive, playButton.x, playButton.y, playButton.width, playButton.height);
            if (Gdx.input.isTouched()){
                this.dispose();
                game.setScreen(new GameScreen(game,playerType));
            }
        }else{
            game.batch.draw(playButtonInactive, playButton.x, playButton.y, playButton.width, playButton.height);
        }
        game.batch.end();
    }

    public void setPlayerImage(String side){
        if (side.equals("left")){
            temp--;
        }else{
            temp++;
        }
        if (temp%4 == 0){
            playerType = "Dylan";
            character = new Texture("dylan.png");
        }else if (temp%4 == 1){
            playerType = "Daniel";
            character = new Texture(Gdx.files.internal("daniel.png"));
        }else if (temp%4 == 2){
            playerType = "Jiho";
            character = new Texture(Gdx.files.internal("jiho.png"));
        }else if (temp%4 == 3){
            playerType = "Big Head Joe";
            character = new Texture(Gdx.files.internal("joe.png"));
        }
    }

    public boolean isMouseIn(Rectangle r){
        return Gdx.input.getX() < r.x + r.width && Gdx.input.getX() > r.x && Gdx.graphics.getHeight() - Gdx.input.getY()<r.y +r.height &&Gdx.graphics.getHeight() - Gdx.input.getY()>r.y;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        exitButton.x = (Gdx.graphics.getWidth()/2)-100;
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
        exitButtonInactive.dispose();
        exitButtonActive.dispose();
        playButtonActive.dispose();
        playButtonInactive.dispose();
        character.dispose();
        left.dispose();
        right.dispose();
    }
}
