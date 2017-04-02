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

public class Monster extends Actors {

    private Graphics g;
    private boolean runRight;
    private Sound puf, jumpS;

    public Monster(Stages map,Image table[],Sound p, Sound j) {
        this.puf=p;
        this.jumpS=j;
        this.sprites=table;
        hp = currentHp = 90;
        dmg = 10;
        mpGive = 1;
        scoreGive=1;
        this.map = map;
        animationTable = new Animation[15];
        states = new boolean[7];
        width = height = 100;
        lookAtRight = true;
        type = "monster";
        footLine = new Rectangle(x + FOOTINT, y + height, 10, 7);
        setAnimationTable();
        eachOtherFalse(0);
        stand();
        animation.setCurrentFrame(gen.nextInt(3));
    }

    public void setAnimationTable() {
        animationTable[0] = setAnimation(sprites[0], 4, 1, 100, 100, 4, 200);
        animationTable[1] = setAnimation(sprites[1], 4, 1, 100, 100, 4, 200);
        animationTable[2] = setAnimation(sprites[2], 6, 1, 100, 100, 6, 150);
        animationTable[3] = setAnimation(sprites[3], 6, 1, 100, 100, 6, 150);
        animationTable[4] = setAnimation(sprites[4], 1, 1, 100, 100, 1, 100);
        animationTable[5] = setAnimation(sprites[5], 1, 1, 100, 100, 1, 100);
        animationTable[6] = setAnimation(sprites[6], 1, 1, 100, 100, 1, 100);
        animationTable[7] = setAnimation(sprites[7], 1, 1, 100, 100, 1, 100);
        animationTable[8] = setAnimation(sprites[8], 2, 1, 100, 100, 2, 300);
        animationTable[9] = setAnimation(sprites[9], 2, 1, 100, 100, 2, 300);

        animationTable[10] = setAnimation(sprites[10], 4, 1, 100, 100, 4, 210);
        animationTable[11] = setAnimation(sprites[11], 4, 1, 100, 100, 4, 210);
        /*
        animationTable[12] = setAnimation(sprites[12], 4, 1, 100, 100, 4, 105);
        animationTable[13] = setAnimation(sprites[13], 4, 1, 100, 100, 4, 105);
         */
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
            jump(delta);
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
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
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
        x += delta * 0.2;
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        lookAtRight = true;
    }

    public void runLeft(int delta) {
        animation = animationTable[3];
        x -= delta * 0.2;
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        lookAtRight = false;
    }

    public void jump(int delta) {
        fallen = true;
        if (!puf.playing() && !jumpS.playing()) {
            jumpS.play();
        }
        if (lookAtRight) {
            //w prawo
            animation = animationTable[4];
            rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
            x += delta * 0.2;
        } else {
            //w lewo
            animation = animationTable[5];
            rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
            x -= delta * 0.2;
        }
        if (y > copyOfY - 125) {
            y -= delta * 0.5 + gravitationValue;
        } else if (y < copyOfY - 120) {
            eachOtherFalse(4);
        }
    }

    public void falling(int delta) {
        if (lookAtRight) {
            animation = animationTable[6];
            x += delta * 0.2;
        } else {
            animation = animationTable[7];
            x -= delta * 0.2;
        }
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
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
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
    }

    public void attack1(int delta) {
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        if (lookAtRight) {
            animation = animationTable[10];
            x += delta * 0.05;
            if (animation.getFrame() == 1) {
                attackBox = new Rectangle(70 + x, 68 + y, 33, 15);
            }
        } else {
            animation = animationTable[11];
            x -= delta * 0.05;
            if (animation.getFrame() == 1) {
                attackBox = new Rectangle(13 + x, 68 + y, 33, 15);
            }
        }
        if (animation.getFrame() == 3) {
            animation.restart();
            eachOtherFalse(0);

        }
    }

    @Override
    public void spawn() {
        puf.play();
        doEffect(1);
    }

    ////////////////////////////////////////
    @Override
    public void AI(int delta, float playerX, float playerY) {

        int pom = gen.nextInt(1000);
        int szansa = 950;

        ////bieganie od lewa do prawa
        if (!injured && !attack1) {
            if (!runRight && !fallen) { //w lewo
                if (pom < szansa + 47) {
                    eachOtherFalse(2);
                } else {
                    lookAtRight = false;
                    copyOfY = y;
                    eachOtherFalse(3); //'Random' Jump
                }
            }
            if (runRight && !fallen) { //w prawo
                if (pom < szansa + 47) {
                    eachOtherFalse(1);
                } else {
                    lookAtRight = true;//'Random' Jump
                    copyOfY = y;
                    eachOtherFalse(3);

                }
            }
            if (map.getMapBody().getMinX() > x + 45 && !runRight) {
                runRight = true;
                eachOtherFalse(0);
            }
            if (map.getMapBody().getMaxX() < x + 73 && runRight) {
                runRight = false;
                eachOtherFalse(0);
            }
        }
        //Atak
        if (!fallen && !injured) {
            if (runRight && ((x < playerX && x + 50 > playerX && y < playerY + 50 && y > playerY-10) && pom > szansa + 10)) {
                attack1 = true;
                eachOtherFalse(6);
            } else if (!runRight && ((x > playerX && x < playerX + 50) && pom > szansa+10) && y < playerY + 50 && y > playerY-10) {
                attack1 = true;
                eachOtherFalse(6);
            }
        }
    }
}
