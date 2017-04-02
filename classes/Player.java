package classes;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

public class Player extends Actors {

    private int leftControl,rightControl;
    int jumpControl,tpControl,ultimateControl,attackControl;
    private int maxMp, currentMp;
    private boolean relase;
    private Sound runS, jumpS, injuredS, attack1S, attack2S;

    public Player(GameContainer container, Stages map,int keyboardTable[]) throws SlickException {
        this.leftControl=keyboardTable[0];
        this.rightControl=keyboardTable[1];
        this.jumpControl=keyboardTable[2];
        this.tpControl=keyboardTable[3];
        this.ultimateControl=keyboardTable[4];
        this.attackControl=keyboardTable[5];
        maxMp = 5;
        currentMp = 1;
        hp = currentHp = 200;
        dmg = 30;
        relase = true;
        sprites = new Image[16];
        animationTable = new Animation[16];
        //0-stand; 1-runRight; 2-runLeft; 3-jump; 4-falling; 5-attack1; 6-attack2; 7-injured;
        states = new boolean[11];
        x = 350;
        y = 210;
        width = height = 105;
        lookAtRight = true;
        footLine = new Rectangle(x + FOOTINT, y + height, 10, 7);
        this.map = map;
        init(container);
        eachOtherFalse(0);
        stand();
    }
    
    public void setDefault(){
        x = 350;
        y = 210;
        //width = height = 105;
        lookAtRight = true;
        currentMp = 1;
        currentHp = hp;
        relase = true;
    }

    public void init(GameContainer container) throws SlickException {
        try {
            runS = new Sound("sounds/run.wav");
            jumpS = new Sound("sounds/jump.wav");
            injuredS = new Sound("sounds/narutoInjured.wav");
            attack1S = new Sound("sounds/narutoAttack1.wav");
            attack2S = new Sound("sounds/narutoAttack2.wav");
            sprites[0] = new Image("sprites/naruto/NarutoStand.png");
            sprites[1] = new Image("sprites/naruto/NarutoStandLeft.png");
            sprites[2] = new Image("sprites/naruto/NarutoRun.png");
            sprites[3] = new Image("sprites/naruto/NarutoRunLeft.png");
            sprites[4] = new Image("sprites/naruto/NarutoJump.png");
            sprites[5] = new Image("sprites/naruto/NarutoJumpLeft.png");
            sprites[6] = new Image("sprites/naruto/NarutoFall.png");
            sprites[7] = new Image("sprites/naruto/NarutoFallLeft.png");
            sprites[8] = new Image("sprites/naruto/NarutoAttack1.png");
            sprites[9] = new Image("sprites/naruto/NarutoAttack1Left.png");
            sprites[10] = new Image("sprites/naruto/NarutoAttack2.png");
            sprites[11] = new Image("sprites/naruto/NarutoAttack2Left.png");
            sprites[12] = new Image("sprites/naruto/NarutoAttack3.png");
            sprites[13] = new Image("sprites/naruto/NarutoAttack3Left.png");
            sprites[14] = new Image("sprites/naruto/NarutoInjured.png");
            sprites[15] = new Image("sprites/naruto/NarutoInjuredLeft.png");
        } catch (SlickException e) {
            System.out.print("Nie udalo sie wczytac Naruto: " + e.getMessage() + "\n");
        }
        //AnimationTable        
        animationTable[0] = setAnimation(sprites[0], 6, 1, 100, 100, 6, 100);
        animationTable[1] = setAnimation(sprites[1], 6, 1, 100, 100, 6, 100);
        animationTable[2] = setAnimation(sprites[2], 6, 1, 100, 100, 6, 100);
        animationTable[3] = setAnimation(sprites[3], 6, 1, 100, 100, 6, 100);
        animationTable[4] = setAnimation(sprites[4], 2, 1, 100, 100, 2, 100);
        animationTable[5] = setAnimation(sprites[5], 2, 1, 100, 100, 2, 100);
        animationTable[6] = setAnimation(sprites[6], 2, 1, 100, 100, 2, 100);
        animationTable[7] = setAnimation(sprites[7], 2, 1, 100, 100, 2, 100);
        animationTable[8] = setAnimation(sprites[8], 4, 1, 100, 100, 4, 110);
        animationTable[9] = setAnimation(sprites[9], 4, 1, 100, 100, 4, 110);
        animationTable[10] = setAnimation(sprites[10], 4, 1, 100, 100, 4, 105);
        animationTable[11] = setAnimation(sprites[11], 4, 1, 100, 100, 4, 105);
        animationTable[12] = setAnimation(sprites[12], 4, 1, 100, 100, 4, 105);
        animationTable[13] = setAnimation(sprites[13], 4, 1, 100, 100, 4, 105);
        animationTable[14] = setAnimation(sprites[14], 2, 1, 100, 100, 2, 300);
        animationTable[15] = setAnimation(sprites[15], 2, 1, 100, 100, 2, 300);
    }
    
    public void addHp(int ile){
        if(currentHp+ile>=hp){
            currentHp=hp;
        }
        else if (currentHp < hp) {
            currentHp += ile;
        }
    }
    public void diffHp(int ile){
        currentHp-=ile;
    }

    @Override
    public void update(int delta) {
        getAnimation().update(delta);
        gravitation(delta);
        if (states[0]) {
            attackBox = new Rectangle(0, 0, 0, 0);
            attack1 = attack2 = attack3 = false;
            stand();
            dmg = 30;
        }
        if (states[1]) {
            try {
                runRight(delta);
            } catch (SlickException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (states[2]) {
            runLeft(delta);
        }
        if (states[3]) {
            jump(delta);
        }
        if (states[4]) {
            falling();
        }
        if (states[5]) {
            attack1(delta);
        }
        if (states[6]) {
            attack2(delta);
        }
        if (states[7]) {
            attack3(delta);
        }
        if (states[8]) {
            //injured
        }
        if (states[9]) {
            escape(delta);
        }
        if (states[10]) {
            setFinalFantasy(true);
        }
    }

    @Override
    public void render(GameContainer container, Graphics g, boolean show) throws SlickException {
        if (show) {
            g.setColor(Color.blue);
            g.draw(getRectangle());
            g.setColor(Color.red);
            g.draw(getAttackBox());
            int k = 50;
            //Color.yellow
            g.drawString("x= " + Float.toString(getX()), 630 + k, 20);
            g.drawString("y= " + Float.toString(getY()), 630 + k, 40);

            g.setColor(Color.blue);
            g.drawString("x= " + Float.toString(getRectangle().getX()), 480 + k, 20);
            g.drawString("y= " + Float.toString(getRectangle().getY()), 480 + k, 40);
            g.drawString("width= " + Float.toString(getRectangle().getWidth()), 480 + k, 60);
            g.drawString("height= " + Float.toString(getRectangle().getHeight()), 480 + k, 80);

            g.setColor(Color.green);
            g.drawString("fallen= " + Boolean.toString(isFallen()), 300 + k, 20);
            g.drawString("lookAtRight= " + Boolean.toString(isLookAtRight()), 300 + k, 40);
            g.drawString("attack1= " + Boolean.toString(isAttack1()), 300 + k, 60);
            g.drawString("attack2= " + Boolean.toString(isAttack2()), 300 + k, 80);
            g.drawString("attack3= " + Boolean.toString(isAttack3()), 300 + k, 100);
            g.draw(footLine);
        }
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

    public void keyListener(GameContainer container, int delta) {
        Input input = container.getInput();
        if (!injured) {
            if (input.isKeyDown(rightControl) && !input.isKeyDown(leftControl) && !attack1) {
                states[1] = true;
            }
            if (input.isKeyDown(leftControl) && !input.isKeyDown(rightControl) && !attack1) {
                states[2] = true;
            }
        }
    }

    public void keyListenerPressed(int key, int delta) {
        //("Key= " + key + "\n");
        if (!injured) {
            if (key == jumpControl) {
                if (!fallen) {
                    copyOfY = y;
                    eachOtherFalse(3);
                }
            } else if (key == attackControl) {
                if (!attack1 && !fallen) {
                    attack1 = true;
                    eachOtherFalse(5);
                } else if (attack1 && !attack2 && !fallen) {
                    attack2 = true;
                    eachOtherFalse(6);
                } else if (attack1 && attack2 && !attack3 && !fallen) {
                    attack3 = true;
                    eachOtherFalse(7);
                }
            }
        }
        if (key == ultimateControl && currentMp == 5) {
            currentMp -= 5;
            eachOtherFalse(10);
        }
        if (key == tpControl) {
            eachOtherFalse(9);
        }
    }

    public void keyListenerReleased(int key, int delta) {
        // System.out.print("KeyRelased= "+key+"\n");
        if (fallen) {
            eachOtherFalse(4);
        } else if (!attack1) {
            eachOtherFalse(0);
        }
        if (key == 48) {
            relase = true;
        }
    }

    ////STATES
    public void stand() {
        footLine.setLocation(x + FOOTINT, y + height + 5);
        if (lookAtRight) {
            animation = animationTable[0];
            rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        }//lookatright=false
        else {
            animation = animationTable[1];
        }
    }

    public void runRight(int delta) throws SlickException {
        if (x < map.getMapBody().getMaxX() - 75) {
            animation = animationTable[2];
            if (animation.getFrame() == 1 && !fallen) {
                if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing()) {
                    runS.play();
                }
            } else if (animation.getFrame() == 3 && !fallen) {
                if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing()) {
                    runS.play();
                }
            }
            x += delta * 0.3;
            rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
            footLine.setLocation(x + FOOTINT, y + height + 5);
            lookAtRight = true;
            if (fallen) {
                falling();
            }
        }
    }

    public void runLeft(int delta) {
        if (x + 44 > map.getMapBody().getMinX()) {
            animation = animationTable[3];
            if (animation.getFrame() == 1 && !fallen) {
                if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing()) {
                    runS.play();
                }
            } else if (animation.getFrame() == 3 && !fallen) {
                if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing()) {
                    runS.play();
                }
            }
            x -= delta * 0.3;
            rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
            footLine.setLocation(x + FOOTINT, y + height + 5);
            lookAtRight = false;
        }
        if (fallen) {
            falling();
        }
    }

    public void jump(int delta) {
        attackBox = new Rectangle(0, 0, 0, 0);
        if (animation.getFrame() == 1) {
            if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing()) {
                jumpS.play();
            }
        }
        footLine.setLocation(x + FOOTINT, y + height + 5);
        if (lookAtRight) {
            animation = animationTable[4];
            rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        } else {
            animation = animationTable[5];
            rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        }
        if (y > copyOfY - 120) {
            y -= delta * 0.7 + gravitationValue;
        } else if (y < copyOfY - 115) {
            eachOtherFalse(4);
        }
    }

    public void falling() {
        if (lookAtRight) {
            animation = animationTable[6];
        } else {
            animation = animationTable[7];
        }
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        footLine.setLocation(x + FOOTINT, y + height + 5);
        if (!fallen) {
            eachOtherFalse(0);
        }
    }

    public void attack1(int delta) {
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing() && animation.getFrame() < 2) {
            if (gen.nextInt(2) == 1) {
                attack1S.play();
            } else {
                attack2S.play();
            }
        }
        if (lookAtRight) {
            animation = animationTable[8];
            if (x < map.getMapBody().getMaxX() - 75) {
                x += delta * 0.05;
            }

            if (animation.getFrame() == 1) {
                attackBox = new Rectangle(80 + x, 73 + y, 23, 10);
            }
        } else {
            animation = animationTable[9];
            if (x + 44 > map.getMapBody().getMinX()) {
                x -= delta * 0.05;
            }
            if (animation.getFrame() == 1) {
                attackBox = new Rectangle(23 + x, 73 + y, 23, 10);
            }
        }
        if (animation.getFrame() == 3) {
            animation.restart();
            eachOtherFalse(0);

        }
    }

    public void attack2(int delta) {
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing() && animation.getFrame() < 2) {
            if (gen.nextInt(2) == 1) {
                attack1S.play();
            } else {
                attack2S.play();
            }
        }
        if (lookAtRight) {
            animation = animationTable[10];
            if (x < map.getMapBody().getMaxX() - 75) {
                x += delta * 0.05;
            }
            if (animation.getFrame() == 2) {
                attackBox = new Rectangle(80 + x, 73 + y, 23, 20);
            }
        } else {
            animation = animationTable[11];
            if (x + 44 > map.getMapBody().getMinX()) {
                x -= delta * 0.05;
            }

            if (animation.getFrame() == 2) {
                attackBox = new Rectangle(26 + x, 73 + y, 23, 20);
            }
        }
        if (animation.getFrame() == 3) {
            animation.restart();
            eachOtherFalse(0);
        }
    }

    public void attack3(int delta) {
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
        if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing() && animation.getFrame() < 2) {
            if (gen.nextInt(2) == 1) {
                attack1S.play();
            } else {
                attack2S.play();
            }
        }
        if (lookAtRight) {
            animation = animationTable[12];
            if (x < map.getMapBody().getMaxX() - 75) {
                x += delta * 0.05;
            }
            if (animation.getFrame() == 2) {
                attackBox = new Rectangle(80 + x, 63 + y, 20, 33);
            }
        } else {
            animation = animationTable[13];
            if (x + 44 > map.getMapBody().getMinX()) {
                x -= delta * 0.05;
            }

            if (animation.getFrame() == 2) {
                attackBox = new Rectangle(29 + x, 63 + y, 20, 33);
            }
        }
        if (animation.getFrame() == 3) {
            animation.restart();
            eachOtherFalse(0);
        }
    }

    @Override
    public void injured(int obrazenia, int delta) {
        attackBox = new Rectangle(0, 0, 0, 0);
        if (!runS.playing() && !jumpS.playing() && !injuredS.playing() && !attack1S.playing() && !attack2S.playing()) {
            injuredS.play();
        }
        doEffect(0);
        currentHp = currentHp - obrazenia;
        eachOtherFalse(8);
        if (lookAtRight) {
            animation = animationTable[14];
        } else {
            animation = animationTable[15];
        }
        animation.setCurrentFrame(gen.nextInt(2));
        rectangle = new Rectangle(45 + x, 60 + y, 28, 50);
    }

    ////END STATES
    public void escape(int delta) {
        if (currentMp > 0 && relase) {
            relase = false;
            doEffect(1);
            currentMp -= 1;
            if (lookAtRight) {
                if ((x + delta * 80.0) < map.getMapBody().getMaxX() - 75) {
                    x += delta * 80.0;
                } else {
                    x = map.getMapBody().getMaxX() - 77;
                }
            } else {
                if (((x + 44) - delta * 80.0) > map.getMapBody().getMinX()) {
                    x -= delta * 80.0;
                } else {
                    x = map.getMapBody().getMinX() - 42;
                }
            }
        }
    }

    public void addMp(int i) {
        if (currentMp < 5) {
            currentMp += i;
        }
    }

    public int getCurrentMp() {
        return currentMp;
    }

    public void setCurrentMp(int currentMp) {
        this.currentMp = currentMp;
    }
}
