package classes;

import java.util.Random;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Actors {

    protected float x, y, vY;
    protected float copyOfY;
    protected int width, height;
    protected Image sprites[];
    protected Animation animation;
    protected Animation animationTable[];
    Random gen = new Random();
    protected int hp, dmg, currentHp, mpGive;
    protected String type;
    protected float VOLUME=1.0f;
    protected int scoreGive;
    
    protected Shape rectangle, attackBox;
    protected Shape bodyTable[];
    
    protected Shape footLine;
    protected int FOOTINT=53;
    protected Stages map;
    

    protected boolean states[], fallen, lookAtRight, attack1, attack2, attack3, immortal, injured, finalFantasy;
    protected boolean summonEffect, doDelete;
    protected int effect;

    protected float gravitationValue;
    protected long immortalTimer;
    

    private final float SCALE = 1.15f;

    public Actors() {
        this.fallen = false;
        this.gravitationValue = 0.0005f;
        this.attackBox = new Rectangle(0, 0, 0, 0);
        this.vY=0;
    }

    public void AI(int delta, float playerX, float playerY) {
    }

    public Animation setAnimation(Image i, int spritesX, int spritesY, int spriteSzerokosc, int spriteWysokosc, int frames, int duration) {
        Animation a = new Animation(false);
        int c = 0;
        for (int y = 0; y < spritesY; y++) {
            for (int x = 0; x < spritesX; x++) {
                if (c < frames) {
                    a.addFrame(i.getSubImage(x * spriteSzerokosc, y * spriteWysokosc, spriteSzerokosc, spriteWysokosc), duration);
                }
                c++;
            }
        }
        return a;
    }

    public void eachOtherFalse(int index) {
        for (int i = 0; i < states.length; i++) {
            states[i] = false;
        }
        states[index] = true;

    }

    public void injured(int obrazenia, int delta) {

    }
    public boolean isLookAtRight() {
        return lookAtRight;
    }
    
   public void spawn(){
       
   }
    
    
    public int getCurrentHp() {
        return currentHp;
    }

    public void setImmortal(boolean state) {
        this.immortal = state;
    }

    public boolean isImmortal() {
        return immortal;
    }

    public long getImmortalTimer() {
        return immortalTimer;
    }
    
    

    @SuppressWarnings("empty-statement")
    public void setTimerStart() {
        long timeMillis = System.currentTimeMillis();
        this.immortalTimer = timeMillis;
    }
    
    
    public String getType() {
        return type;
    }

    public int getMpGive() {
        return mpGive;
    }

    public long getStopTime() {
        long time = System.currentTimeMillis();
        return time;
    }

    public Shape getAttackBox() {
        return attackBox;
    }
  
    public int getScoreGive() {
        return scoreGive;
    }

    public void setScoreGive(int scoreGive) {
        this.scoreGive = scoreGive;
    }

    public boolean[] getStates() {
        return states;
    }

    public boolean isAttack1() {
        return attack1;
    }

    public boolean isAttack2() {
        return attack2;
    }

    public boolean isAttack3() {
        return attack3;
    }

    public void update(int delta) {

    }
    public void render(GameContainer container, Graphics g, boolean show) throws SlickException {
        
    }

    public float getSCALE() {
        return SCALE;
    }

    public void gravitation(int delta) {
        vY += delta * gravitationValue;
    }

    public void setBody(float x, float y, int width, int height) {
        this.rectangle = new Rectangle(x, y, width, height);
    }

    public Shape getRectangle() {
        return rectangle;
    }

    public boolean isSummonEffect() {
        return summonEffect;
    }

    public void setSummonEffect(boolean summonEffect) {
        this.summonEffect = summonEffect;
    }
    public void doEffect(int ktory){
        setSummonEffect(true);
        this.effect=ktory;
    }
    public int getEffect(){
        return effect;
    }

    public void act(int delta) {
    }

    public boolean isFallen() {
        return fallen;
    }

    public void setFallen(boolean fallen) {
        this.fallen = fallen;
    }

    public boolean isDoDelete() {
        return doDelete;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDoDelete(boolean doDelete) {
        this.doDelete = doDelete;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void standAnimation() {
    }

    public boolean isInjured() {
        return injured;
    }

    public void setInjured(boolean injured) {
        this.injured = injured;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    
    public boolean isFinalFantasy() {
        return finalFantasy;
    }

    public void setFinalFantasy(boolean finalFantasy) {
        this.finalFantasy = finalFantasy;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    

    public void setBodyTable(Shape[] bodyTable) {
        this.bodyTable = bodyTable;
    }

}
