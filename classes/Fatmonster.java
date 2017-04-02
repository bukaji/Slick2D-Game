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

public class Fatmonster extends Actors {

    private Graphics g;
    private boolean runRight;
    private Sound puf, runS, jumpS;
    private int POZX=33, POZY=25;
    private int WIDTH=45, HEIGHT=60;
    private long wait;

    public Fatmonster(Stages map) {
        hp = currentHp = 150;
        dmg = 10;
        mpGive = 1;
        scoreGive = 2;
        this.map = map;
        sprites = new Image[15];
        animationTable = new Animation[15];
        states = new boolean[7];
        width = height = 100;
        lookAtRight = true;
        type = "monster";
        footLine = new Rectangle(x + FOOTINT, y + height, 10, 7);
        try {
            puf = new Sound("sounds/puf.wav");
            runS = new Sound("sounds/run.wav");
            jumpS = new Sound("sounds/jump.wav");
            sprites[0] = new Image("sprites/FatNinja/stand.png");
            sprites[1] = new Image("sprites/FatNinja/standLeft.png");
            sprites[2] = new Image("sprites/FatNinja/walk.png");
            sprites[3] = new Image("sprites/FatNinja/walkLeft.png");
            sprites[8] = new Image("sprites/FatNinja/injured.png");
            sprites[9] = new Image("sprites/FatNinja/injuredLeft.png");
            sprites[10] = new Image("sprites/FatNinja/attack.png");
            sprites[11] = new Image("sprites/FatNinja/attackLeft.png");
        } catch (SlickException e) {
            System.out.print("Nie udalo sie wczytac FatNinja: " + e.getMessage() + "\n");
        }
        setAnimationTable();
        eachOtherFalse(0);
        stand();
        if(gen.nextInt(2)==1){
            runRight=true;
        }else{
            runRight=false;
        }
        animation.setCurrentFrame(gen.nextInt(3));
    }

    public void setAnimationTable() {
        animationTable[0] = setAnimation(sprites[0], 4, 1, 100, 100, 4, 200);
        animationTable[1] = setAnimation(sprites[1], 4, 1, 100, 100, 4, 200);
        animationTable[2] = setAnimation(sprites[2], 5, 1, 100, 100, 5, 150);
        animationTable[3] = setAnimation(sprites[3], 5, 1, 100, 100, 5, 150);
        animationTable[6] = setAnimation(sprites[0], 1, 1, 100, 100, 1, 100);
        animationTable[7] = setAnimation(sprites[1], 1, 1, 100, 100, 1, 100);
        animationTable[8] = setAnimation(sprites[8], 2, 1, 100, 100, 2, 300);
        animationTable[9] = setAnimation(sprites[9], 2, 1, 100, 100, 2, 300);

        animationTable[10] = setAnimation(sprites[10], 5, 1, 100, 100, 5, 130);
        animationTable[11] = setAnimation(sprites[11], 5, 1, 100, 100, 5, 130);
    }

    @Override
    public void gravitation(int delta) {
        super.gravitation(delta);
        footLine.setLocation(x + FOOTINT, y + height-20 + 5);
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
            doEffect(6);
            setDoDelete(true);
        }
        if (states[0]) {
            attackBox = new Rectangle(0, 0, 0, 0);
            attack1 = false;
            stand();
        }
        if (states[1]) {
            runRight(delta);
        }
        if (states[2]) {
            runLeft(delta);
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
            }
        }//lookatright=false
        else {
            if (fallen) {
                animation = animationTable[7];
            } else {
                animation = animationTable[1];
            }
        }
    }

    public void runRight(int delta) {
        animation = animationTable[2];
        if (animation.getFrame() == 1 && !fallen) {
            if (!runS.playing() && !puf.playing()) {
                runS.play();
            }
        } else if (animation.getFrame() == 3 && !fallen) {
            if (!runS.playing() && !puf.playing()) {
                runS.play();
            }
        }
        x += delta * 0.1;
        rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
        lookAtRight = true;
    }

    public void runLeft(int delta) {
        animation = animationTable[3];
        if (animation.getFrame() == 1 && !fallen) {
            if (!runS.playing() && !puf.playing()) {
                runS.play();
            }
        } else if (animation.getFrame() == 3 && !fallen) {
            if (!runS.playing() && !puf.playing()) {
                runS.play();
            }
        }
        x -= delta * 0.1;
        rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
        lookAtRight = false;
    }

    public void falling(int delta) {
        if (lookAtRight) {
            animation = animationTable[6];
            x += delta * 0.2;
        } else {
            animation = animationTable[7];
            x -= delta * 0.2;
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
        rectangle = new Rectangle(POZX + x, POZY + y, WIDTH, HEIGHT);
        if (lookAtRight) {
            animation = animationTable[10];
            //x += delta * 0.05;
            if (animation.getFrame() == 1) {
                attackBox = new Rectangle(x+70, 38 + y, 33, 15);
            }
        } else {
            animation = animationTable[11];
            //x -= delta * 0.05;
            if (animation.getFrame() == 1) {
                attackBox = new Rectangle(20+x, 38 + y, 33, 15);
            }
        }
        if (animation.getFrame() == 4) {
            animation.restart();
            eachOtherFalse(0);

        }
    }

    @Override
    public void spawn() {
        puf.play();
        doEffect(6);
    }

    ////////////////////////////////////////
    @Override
    public void AI(int delta, float playerX, float playerY) {

        int pom = gen.nextInt(1000);
        int szansa = 950;
        ////bieganie od lewa do prawa
        if (!injured && !attack1) {
            if (!runRight && !fallen && 2000 < System.currentTimeMillis() - wait) { //w lewo
                eachOtherFalse(2);
                lookAtRight = false;
            }
            if (runRight && !fallen  && 2000 < System.currentTimeMillis() - wait) { //w prawo
                eachOtherFalse(1);
                lookAtRight = true;
            }

            if (map.getMapBody().getMinX() > x + 5 && !runRight) {
                wait= System.currentTimeMillis();
                runRight = true;
                eachOtherFalse(0);
            }
            if (map.getMapBody().getMaxX() < x + 113 && runRight) {
                wait= System.currentTimeMillis();
                runRight = false;
                eachOtherFalse(0);
            }
        }

        //Atak
        if (!fallen && !injured) {
            if (runRight && ((x < playerX && x + 50 > playerX && y < playerY + 50 && y > playerY) && pom > szansa)) {
                attack1 = true;
                eachOtherFalse(6);
            } else if (!runRight && ((x > playerX && x < playerX + 50) && y < playerY + 50 && y > playerY) && pom > szansa) {
                attack1 = true;
                eachOtherFalse(6);
            }
        }
    }
}
