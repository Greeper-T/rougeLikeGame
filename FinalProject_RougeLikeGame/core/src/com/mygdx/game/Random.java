package com.mygdx.game;

public class Random {
    public static int getRandomNumber(int lowest, int highest){
        highest -= lowest -1;
        return (int)(Math.random()*highest+lowest);
    }
}
