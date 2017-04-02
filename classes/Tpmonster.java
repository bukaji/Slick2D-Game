package classes;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

public class Tpmonster extends Actors {

    private Graphics g;
    private Sound puf, runS, jumpS, wziu;
    private int POZX=45, POZY=60;
    private int WIDTH=28, HEIGHT=50;
    private int TPX=45, TPY=13;
    private long wait,attackCd;
    
    private boolean tp;
    private int token;

    public Tpmonster(Stages map) {
        hp = currentHp = 60;
        dmg = 15;
        mpGive = 1;
        scoreGive = 3;
        this.map = map;
        sprites = new Image[15];
        animationTable = new Animation[15];
        states = new boolean[7];
        width = height = 100;
        lookAtRight = true;
        //System.out.print(height+" | "+HEIGHT+"\n");
        type = "monster";
        footLine = new Rectangle(x + FOOTINT, y + height, 10, 7);
        tp=true;
        try {
            puf = new Sound("sounds/puf.wav");
            runS = new Sound("sounds/run.wav");
            jumpS = new Sound("sounds/jump.wav");
            wziu = new Sound("sounds/wziu.wav");
            sprites[0] = new Image("sprites/TpNinja/stance.png");
            sprites[1] = new Image("sprites/TpNinja/stanceLeft.png");
            sprites[2] = new Image("sprites/TpNinja/tp.png");
            sprites[3] = new Image("sprites/TpNinja/tpLeft.png");
            sprites[8] = new Image("sprites/TpNinja/injured.png");
            sprites[9] = new Image("sprites/TpNinja/injuredLeft.png");
            sprites[10] = new Image("sprites/TpNinja/attack.png");
            sprites[11] = new Image("sprites/TpNinja/attackLeft.png");
        } catch (SlickException e) {
            System.out.print("Nie udalo sie wczytac FatNinja: " + e.getMessage() + "\n");
        }
        token=gen.nextInt(4);
        tp();
        wait=System.currentTimeMillis();
        attackCd=System.currentTimeMillis();
        setAnimationTable();
        eachOtherFalse(0);
        stand();
        animation.setCurrentFrame(gen.nextInt(3));
    }

    public void setAnimationTable() {
        animationTable[0] = setAnimation(sprites[0], 3, 1, 100, 100, 3, 200);
        animationTable[1] = setAnimation(sprites[1], 3, 1, 100, 100, 3, 200);
        animationTable[2] = setAnimation(sprites[2], 4, 1, 100, 100, 4, 200);       
        animationTable[3] = setAnimation(sprites[3], 4, 1, 100, 100, 4, 200);  
        animationTable[6] = setAnimation(sprites[2], 4, 1, 100, 100, 4, 200);       
        animationTable[7] = setAnimation(sprites[3], 4, 1, 100, 100, 4, 200); 
        animationTable[8] = setAnimation(sprites[8], 2, 1, 100, 100, 2, 300);
        animationTable[9] = setAnimation(sprites[9], 2, 1, 100, 100, 2, 300);
        animationTable[10] = setAnimation(sprites[10], 8, 1, 100, 100, 8, 180);
        animationTable[11] = setAnimation(sprites[11], 8, 1, 100, 100, 8, 180);
    }

    @Override
    public void gravitation(int delta) {
        super.gravitation(delta);
        footLine.setLocation(x + FOOTINT, y + height + 5);
        if (!map.collisionWith(footLine)) {
            y += vY * delta;
            setFallen(true);
        } else {
            setFallen(false);
            vY = 0;
        }
    }

    @Override
    public void update(int delta) {
        getAnimation().update(delta);
        gravitation(delta);
        if (currentHp <= 0) {
            doEffect(1);
            setDoDelete(true);
        }
        if (states[0]) {
            attackBox = new Rectangle(0, 0, 0, 0);
            tp=false;
            attack1 = false;
            stand();
        }
        if (states[1]) {
            tp();
        }
        if (states[2]) {
            //runLeft(delta);
        }
        if (states[3]) {
            //jump
        }
        if (states[4]) {
            falling(delta);
        }
        if (states[5]) {
            //injured
        }
        if (states[6]) {
            attack1(delta);
        }
    }

    @Override
    public void render(GameContainer container, Graphics g, boolean show) throws SlickException {
        if (show) {
            g.setColor(Color.blue);
            g.draw(getRectangle());
            g.setColor(Color.red);
            g.draw(getAttackBox());
            g.drawString("m.immortal= " + Boolean.toString(isImmortal()), 100, 20);
            g.drawString("m.immortalTimer= " + Long.toString(getStopTime() - getImmortalTimer()), 100, 40);
            g.drawString("m.fallen= " + Boolean.toString(isFallen()), 100, 60);
            g.drawString("m.injured= " + Boolean.toString(isInjured()), 100, 80);
            g.drawString("m.x= " + Float.toString(getX()), 100, 100);
            g.setColor(Color.green);
            g.draw(footLine);
        }
    }

    ////////////////////////////////////////////////////
    public void stand() {
        rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
        if (lookAtRight) {
            if (fallen) {
                animation = animationTable[6];
            } else {
                animation = animationTable[0];
                animation.setPingPong(true);
            }
        }//lookatright=false
        else {
            if (fallen) {
                animation = animationTable[7];
            } else {
                animation = animationTable[1];
                animation.setPingPong(true);
            }
        }
    }

    public void falling(int delta) {
        if (lookAtRight) {
            animation = animationTable[0];
            animation.setPingPong(true);
        } else {
            animation = animationTable[1];
            animation.setPingPong(true);
        }
        rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
        if (!fallen) {
            eachOtherFalse(0);
        }
    }

    @Override
    public void injured(int obrazenia, int delta) {
        currentHp = currentHp - obrazenia;
        doEffect(0);
        attackBox = new Rectangle(0, 0, 0, 0);
        eachOtherFalse(5);
        if (lookAtRight) {
            animation = animationTable[8];
        } else {
            animation = animationTable[9];
        }
        animation.setCurrentFrame(gen.nextInt(2));
        rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
    }

    public void attack1(int delta) {
        if(1000<System.currentTimeMillis()-attackCd){
                  rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
        if (lookAtRight) {
            animation = animationTable[10];
            if (animation.getFrame() == 0) {
                attackBox = new Rectangle(70 + x, 68 + y, 33, 15);
            }
        } else {
            animation = animationTable[11];
            if (animation.getFrame() == 0) {
                attackBox = new Rectangle(13 + x, 68 + y, 33, 15);
            }
        }
        if (animation.getFrame() == 7) {
            attackCd=System.currentTimeMillis();
            animation.restart();
            eachOtherFalse(0);

        }  
        }
    }
    
    public void tp(){
        if(tp){
            wziu.play();
            token++;
            //System.out.print("Token= "+token+"\n");
            if (token == 4) {
                token = 0;
            }
            if(token==0){
                animation=animationTable[2];
                doEffect(9);
                lookAtRight=true;
                x=185-WIDTH-TPX;
                y=232-(HEIGHT*2)-TPY;
                rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
            }else if(token==1){
                animation=animationTable[3];
                doEffect(9);
                lookAtRight=false;
                x=718-WIDTH;
                y=232-(HEIGHT*2)-TPY;
                rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
            }else if(token==2){
                doEffect(8);
                animation=animationTable[3];
                lookAtRight=false;
                x=693-WIDTH;
                y=388-(HEIGHT*2)-TPY;
                rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
            }else if(token==3){
                doEffect(8);
                animation=animationTable[2];
                lookAtRight=true;
                x=200-WIDTH-TPX;
                y=388-(HEIGHT*2)-TPY;
                rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
            }
            eachOtherFalse(0);
        }
    }

    ////////////////////////////////////////
    @Override
    public void AI(int delta, float playerX, float playerY) {

        int pom = gen.nextInt(1000);
        int szansa = 950;
        ////bieganie od lewa do prawa
        if (!injured && !attack1) {
            if (!fallen && 2000 < System.currentTimeMillis() - wait && !tp) { //w lewo
                tp=true;
                wait=System.currentTimeMillis();
                eachOtherFalse(1);
            }
        }

        //Atak
        if (!fallen && !injured) {
            if (lookAtRight && ((x < playerX && x + 50 > playerX && y < playerY + 50 && y > playerY-10) && pom > szansa + 10)) {
                attack1 = true;
                eachOtherFalse(6);
            } else if (!lookAtRight && ((x > playerX && x < playerX + 50) && pom > szansa + 10) && y < playerY + 50 && y > playerY-10) {
                attack1 = true;
                eachOtherFalse(6);
            }
        }
    }
}
