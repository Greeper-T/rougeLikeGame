package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Floor {
    private Room[][] rooms = new Room[20][20];
    private boolean allEnemiesDead = false;
    private int numRooms;
    /*
     01234
    0XXXXX
    1XXXXX
    2XXOXX
    3XXXXX
    4XXXXX
     */
    public Floor(int numRooms, Player player){
        rooms[(rooms.length-1)/2][(rooms[0].length-1)/2] = new Room(0);
        rooms[(rooms.length-1)/2][(rooms[0].length-1)/2].setPlayer(player);
        rooms[(rooms.length-1)/2][(rooms[0].length-1)/2].beenTo();
        rooms[(rooms.length-1)/2][(rooms[0].length-1)/2].beenSeen();
        this.numRooms = numRooms;
        for (int i = 0; i <numRooms -1; i++){
            createRoom();
        }
        setDoorsForRooms();
        checkForSeenDoors();
    }

    public void checkForSeenDoors(){
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                if (rooms[i][j]!= null){
                    if (rooms[i][j].isBeenTo()){
                        if (rooms[i+1][j] != null){
                            rooms[i+1][j].beenSeen();
                        }
                        if (rooms[i-1][j] != null){
                            rooms[i-1][j].beenSeen();
                        }
                        if (rooms[i][j+1] != null){
                            rooms[i][j+1].beenSeen();
                        }
                        if (rooms[i][j-1] != null){
                            rooms[i][j-1].beenSeen();
                        }
                    }
                }
            }
        }
    }

    public void setDoorsForRooms(){
        boolean endRow,startRow, startCol, endCol;
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] != null){
                    endRow = false;
                    startRow = false;
                    endCol = false;
                    startCol = false;
                    if (i == 0){
                        startRow = true;
                    }
                    if (i == rooms.length -1){
                        endRow = true;
                    }
                    if ( j == 0){
                        startCol = true;
                    }
                    if (j == rooms[0].length-1){
                        endCol = true;
                    }
                    if (!startRow){
                        if (rooms[i-1][j] != null){
                            rooms[i][j].addDoor(new Door("top"));
                        }
                    }
                    if (!endRow){
                        if (rooms[i+1][j] != null){
                            rooms[i][j].addDoor(new Door("bottom"));
                        }
                    }
                    if (!startCol){
                        if (rooms[i][j-1] != null){
                            rooms[i][j].addDoor(new Door("left"));
                        }
                    }
                    if (!endCol){
                        if (rooms[i][j+1] != null){
                            rooms[i][j].addDoor(new Door("right"));
                        }
                    }
                }
            }
        }
    }

    public void createRoom(){

        ArrayList<Integer> is = new ArrayList<>();
        ArrayList<Integer> js = new ArrayList<>();
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                if (rooms[i][j] == null){
                    if (i != 0){
                        if (rooms[i-1][j] != null){
                            is.add(i);
                            js.add(j);
                            System.out.println("added");
                        }
                    }
                    if (j != 0){
                        if (rooms[i][j-1] != null){
                            is.add(i);
                            js.add(j);
                            System.out.println("added");
                        }
                    }
                    if (i != rooms.length -1){
                        if (rooms[i+1][j] != null){
                            is.add(i);
                            js.add(j);
                            System.out.println("added");
                        }
                    }
                    if (j != rooms[0].length -1){
                        if (rooms[i][j+1] != null){
                            is.add(i);
                            js.add(j);
                            System.out.println("added");
                        }
                    }
                }
            }
        }
        if (!is.isEmpty()) {
            int random = getRandomNumber(0, is.size()-1);
            rooms[is.get(random)][js.get(random)] = new Room(getRandomNumber(1, 5));
        }
    }
    public boolean render(Batch batch, Player player){
        int numEmptyRooms = 0;
        Door doorInterseption;
        for (int i = 0; i < rooms.length; i++){
            for (int j = 0; j < rooms[0].length; j++){
                if (rooms[i][j] != null){
                    if (rooms[i][j].getEnemies().isEmpty()){
                        numEmptyRooms+=1;
                    }
                    if (numEmptyRooms == numRooms){
                        allEnemiesDead = true;
                    }
                    if (rooms[i][j].getPlayer()!=null){
                        rooms[i][j].render(batch);
                        doorInterseption = rooms[i][j].interceptDoor();
                        if (doorInterseption != null && rooms[i][j].isCanLeave()){
                            if (doorInterseption.getDirection().equals("left")){
                                rooms[i][j].setPlayer(null);
                                rooms[i][j-1].setPlayer(player);
                                rooms[i][j-1].beenTo();
                                player.getPlayerData().x = Gdx.graphics.getWidth() - doorInterseption.getDoorData().width - player.getPlayerData().width;
                                return true;
                            }
                            if (doorInterseption.getDirection().equals("right")){
                                rooms[i][j].setPlayer(null);
                                rooms[i][j+1].setPlayer(player);
                                rooms[i][j+1].beenTo();
                                player.getPlayerData().x = doorInterseption.getDoorData().width;
                                return true;
                            }
                            if (doorInterseption.getDirection().equals("top")){
                                rooms[i][j].setPlayer(null);
                                rooms[i-1][j].setPlayer(player);
                                rooms[i-1][j].beenTo();
                                player.getPlayerData().y = doorInterseption.getDoorData().height;
                                return true;
                            }
                            if (doorInterseption.getDirection().equals("bottom")){
                                rooms[i][j].setPlayer(null);
                                rooms[i+1][j].setPlayer(player);
                                rooms[i+1][j].beenTo();
                                player.getPlayerData().y = Gdx.graphics.getHeight() - doorInterseption.getDoorData().height - player.getPlayerData().height;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Room[][] getRooms() {
        return rooms;
    }

    public int getRandomNumber(int lowest, int highest){
        highest -= lowest -1;
        return (int)(Math.random()*highest+lowest);
    }

    public boolean isAllEnemiesDead() {
        return allEnemiesDead;
    }
    public void dispose(){
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] != null){
                    rooms[i][j].dispose();
                }
            }
        }
    }
}
