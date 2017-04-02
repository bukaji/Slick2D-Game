//Created Jakub Drożdż, Norbert Rojek
package classes;

import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class MainLoop extends BasicGame {
    //sterowanie 
    
    private Stages stage;
    public ArrayList actorsList;
    private Random gen;
    private Player player,player2;
    
    private boolean setVisibleBody = false, pause = false, secureSpawn=false;
    private int dt, spawnNumber, fatNinjaSpawn;
    private GameOver gameOver;
    private Image iface,iface2,font;
    private Image numbers[];
    private Effects chakra, ff;
    private long startSpawnTime;
    private Music themeMusic,theme1,theme2;
    private Sound ffS;
    private int score=0,copyOfScore=0, hpscore=0;
    //sterowanie
    private int keyboardPlayer[]={Input.KEY_LEFT,Input.KEY_RIGHT,200,40,38,39};
    private int keyboardPlayer2[]={Input.KEY_F,Input.KEY_H,20,32,30,31};
    
    //zmienne do klas
    //Monster
    private Image spritesMonster[];
    private Sound pufMonster,jumpSMonster;
    
    //
               
    public MainLoop() {
        super("Bukaji game");
    }
    
    @Override
    public void init(GameContainer container) throws SlickException {
        startSpawnTime = System.currentTimeMillis();
        fatNinjaSpawn=477;
        
        //wczytywanie
        spritesMonster = new Image[15]; 
        loadAll();
        //
        numbers = new Image[10];              
        setNumbersTable();
        gen = new Random();
        if (gen.nextInt(2) == 1) {
            themeMusic = theme1;
        } else {
            themeMusic = theme2;
        }
        themeMusic.loop();
        stage = new Stages(container);
        player = new Player(container, stage,keyboardPlayer);
        player2 = new Player(container, stage,keyboardPlayer2);
        
        player2.setX(400);
        player2.setY(250);       
        actorsList = new ArrayList();
        gameOver = new GameOver();
        chakra = new Effects(45, 24, player.getCurrentMp() + 1);
        ff = new Effects(0, 0, 7);
        
        ///////////////////////////
        for (int i = 0; i < 2; i++) {
            Monster m = new Monster(stage,spritesMonster,pufMonster,jumpSMonster);
            m.setX((float) gen.nextInt(650));
            m.setY((float) gen.nextInt(150)+100);
            m.spawn();
            actorsList.add(m);
        }
        Fatmonster fm=new Fatmonster(stage);
        fm.setX((float) gen.nextInt(650));
        fm.setY(fatNinjaSpawn);
        fm.spawn();
        actorsList.add(fm);
        Tpmonster tm=new Tpmonster(stage);
        actorsList.add(tm);
    }

    void loadAll() {
        try {
            theme1 = new Music("sounds/theme/theme1.wav");
            theme2 = new Music("sounds/theme/theme2.wav");
            ffS = new Sound("sounds/ff.wav");
            iface = new Image("sprites/effects/interface.png");
            iface2 = new Image("sprites/effects/interface2.png");
            font = new Image("sprites/effects/font.png");
        } catch (SlickException e) {
            System.out.print("Nie udalo sie wczytac Interfejsu: " + e.getMessage() + "\n");
        }
        
        try {
            pufMonster = new Sound("sounds/puf.wav");
            jumpSMonster = new Sound("sounds/jump.wav");
            spritesMonster[0] = new Image("sprites/SoundNinja/SoundNinjaStand.png");
            spritesMonster[1] = new Image("sprites/SoundNinja/SoundNinjaStandLeft.png");
            spritesMonster[2] = new Image("sprites/SoundNinja/SoundNinjaRun.png");
            spritesMonster[3] = new Image("sprites/SoundNinja/SoundNinjaRunLeft.png");
            spritesMonster[4] = new Image("sprites/SoundNinja/SoundNinjaJump.png");
            spritesMonster[5] = new Image("sprites/SoundNinja/SoundNinjaJumpLeft.png");
            spritesMonster[6] = new Image("sprites/SoundNinja/SoundNinjaFall.png");
            spritesMonster[7] = new Image("sprites/SoundNinja/SoundNinjaFallLeft.png");
            spritesMonster[8] = new Image("sprites/SoundNinja/SoundNinjaInjured.png");
            spritesMonster[9] = new Image("sprites/SoundNinja/SoundNinjaInjuredLeft.png");
            spritesMonster[10] = new Image("sprites/SoundNinja/SoundNinjaAttack.png");
            spritesMonster[11] = new Image("sprites/SoundNinja/SoundNinjaAttackLeft.png");
        } catch (SlickException e) {
            System.out.print("Nie udalo sie wczytac SoundNinja: " + e.getMessage() + "\n");
        }
    }
    
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (player.currentHp > 0 && !pause && !player.isFinalFantasy()) {
            if (2000 < System.currentTimeMillis() - startSpawnTime && spawnNumber > 0) {
                startSpawnTime = System.currentTimeMillis();
                //System.out.print("4 Sec!\n");
                spawn();
            }
            this.dt = delta;
            stage.update(container, delta);
            player.keyListener(container, delta);
            player.update(delta);
            
            player2.keyListener(container, delta);
            player2.update(delta);
            
            chakra.getAnimation().update(delta);
            if (player.isSummonEffect()) {
                Effects e = new Effects(player.getX(), player.getY(), player.getEffect());
                actorsList.add(e);
                player.setSummonEffect(false);
            }
            if (player2.isSummonEffect()) {
                Effects e = new Effects(player2.getX(), player2.getY(), player2.getEffect());
                actorsList.add(e);
                player2.setSummonEffect(false);
            }
            
            for (int i = 0; i < actorsList.size(); i++) {
                Actors m = (Actors) actorsList.get(i);
                checkCollision(m, delta);
                if (m.isSummonEffect()) {
                    Effects e = new Effects(m.getX(), m.getY(), m.getEffect());
                    actorsList.add(e);
                    m.setSummonEffect(false);
                }
                if (m.isDoDelete()) {
                    if (m.getType() == "monster") {
                        spawnNumber++;
                        score+=m.getScoreGive();
                        copyOfScore+=m.getScoreGive();
                        hpscore+=m.getScoreGive();
                        if(hpscore>=10){
                            hpscore-=10;
                            player.addHp(20);
                        }
                        if(copyOfScore>=20){
                            copyOfScore-=20;
                            spawnNumber++;
                        }
                    }
                    actorsList.remove(i);
                    player.addMp(m.getMpGive());
                }
                m.getAnimation().update(delta);
                m.update(delta);
                m.AI(delta, player.getX(), player.getY());
                m.AI(delta, player2.getX(), player2.getY());
            }
            //FINAL ULTIMATE
        } else if (player.isFinalFantasy() && !pause) {
            themeMusic.pause();
            if(!ffS.playing()){
                ffS.play();
            }
            if (!secureSpawn) {
                secureSpawn = true;
                for (int i = 0; i < actorsList.size(); i++) {
                    Actors m = (Actors) actorsList.get(i);
                    if (m.getType() == "monster") {
                        spawnNumber++;
                        score += m.getScoreGive();
                        copyOfScore += m.getScoreGive();
                        hpscore+= m.getScoreGive();
                        if (hpscore > 10) {
                            hpscore -= 10;
                            player.addHp(20);
                        }
                        if(copyOfScore>=20){
                            copyOfScore-=20;
                            spawnNumber++;
                        }
                    }
                }
                actorsList = new ArrayList();
            }

            if (ff.getAnimation().getFrame() == 35) {
                ff.getAnimation().restart();
                themeMusic.resume();
                secureSpawn=false;
                player.setFinalFantasy(false);
            }
            ff.getAnimation().update(delta);
        } else if (!pause) {
            if (!gameOver.isDoDelete()) {
                if (player.getCurrentHp() <= player2.getCurrentHp()) {
                    gameOver.setX(player.getX() - 3);
                    gameOver.setY(player.getY() + 9);
                } else {
                    gameOver.setX(player2.getX() - 3);
                    gameOver.setY(player2.getY() + 9);
                }
            }
            gameOver.update(delta);
        }
    }
    
    public void checkCollision(Actors m, int delta) {
        //reakcja na dmg monsters
        if (player.getAttackBox().intersects(m.getRectangle()) && !m.isImmortal() ) {
            m.injured(player.getDmg(), delta);
            m.setInjured(true);
            m.setImmortal(true);
            m.setTimerStart();
        } else if (250 < m.getStopTime() - m.getImmortalTimer() && m.isImmortal()) {
            m.setImmortal(false);
            m.setInjured(false);
            m.eachOtherFalse(0);            
        }
        ////////na player 2
        if (player2.getAttackBox().intersects(m.getRectangle()) && !m.isImmortal()) {
            m.injured(player2.getDmg(), delta);
            m.setInjured(true);
            m.setImmortal(true);
            m.setTimerStart();
        } else if (250 < m.getStopTime() - m.getImmortalTimer() && m.isImmortal()) {
            m.setImmortal(false);
            m.setInjured(false);
            m.eachOtherFalse(0);
        }
        //reakcja na dmg player
        if (m.getAttackBox().intersects(player.getRectangle()) && !player.isImmortal()) {
            player.injured(m.getDmg(), delta);
            player.setInjured(true);
            player.setImmortal(true);
            player.setTimerStart();
        } else if (250 < player.getStopTime() - player.getImmortalTimer() && player.isImmortal()) {
            player.setImmortal(false);
            player.setInjured(false);
            player.eachOtherFalse(0);
        }
        ////////player 2 reakcja na dmg
        if (m.getAttackBox().intersects(player2.getRectangle()) && !player2.isImmortal()) {
            player2.injured(m.getDmg(), delta);
            player.diffHp(m.getDmg());
            player2.setInjured(true);
            player2.setImmortal(true);
            player2.setTimerStart();
        } else if (250 < player2.getStopTime() - player2.getImmortalTimer() && player2.isImmortal()) {
            player2.setImmortal(false);
            player2.setInjured(false);
            player2.eachOtherFalse(0);
        }
    }
    
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        if(player.isFinalFantasy()){
            g.scale((float)2.5,(float) 2.5);
            g.drawAnimation(ff.getAnimation(), 0, 0);
        }else if (player.currentHp > 0) {
            //malowanie mapy
            stage.render(container, g, setVisibleBody);

            //malowanie listy aktorow
            player.render(container, g, setVisibleBody);
            player.getAnimation().draw(player.getX(), player.getY(), player.getHeight() * player.getSCALE(), player.getHeight() * player.getSCALE());
            
            player2.render(container, g, setVisibleBody);
            player2.getAnimation().draw(player2.getX(), player2.getY(), player2.getHeight() * player2.getSCALE(), player2.getHeight() * player2.getSCALE());
            
            for (int i = 0; i < actorsList.size(); i++) {
                Actors m = (Actors) actorsList.get(i);
                m.getAnimation().draw(m.getX(), m.getY(), m.getHeight() * m.getSCALE(), m.getWidth() * m.getSCALE());
                m.render(container, g, setVisibleBody);
            }
            //malowanie interfejsu
            iface.draw(30, 30);
            g.setColor(Color.green);
            g.fillRect(85, 64, (float) (player.getCurrentHp()/1.8), 15);
            iface2.draw(30, 30);
            
            g.scale(0.5f, 0.5f);
            printScore(g,1580,10);
            g.scale(2,2);
            if (player.getCurrentMp() == 5) {
                chakra.swapAnimation(2);
            } else if (player.getCurrentMp() == 3 || player.getCurrentMp()==4) {
                chakra.swapAnimation(3);
            } else if (player.getCurrentMp() == 1 || player.getCurrentMp()==2) {
                chakra.swapAnimation(4);
            } else if (player.getCurrentMp() == 0) {
                chakra.swapAnimation(5);
            }
            chakra.getAnimation().draw(chakra.getX(), chakra.getY());
        } else {
           gameOver.getAnimation().draw(gameOver.getX(), gameOver.getY(), gameOver.getAnimation().getHeight(), gameOver.getAnimation().getWidth());            
           if(gameOver.getAnimation().getHeight()>200){
               gameOver.setX(231);
               gameOver.setY(-100);
               int pom=(Integer.toString(score).length()-1)*49;
               printScore(g,424+pom/2,470);
           }                   
        }
    }
    
    public void printScore(Graphics g,int pozX, int pozY){
        for(int i=0;i<Integer.toString(score).length();i++){
            int liczbaCyfr=Integer.toString(score).length()*49;
            int iterator=Character.getNumericValue(Integer.toString(score).charAt(i));
            //System.out.print(iterator+"\n");
            g.drawImage(numbers[iterator], i*49+pozX-liczbaCyfr, pozY);
        }
    }
    
    @Override
    public void keyPressed(int key, char c) {
       System.out.print(key+"\n");
        player.keyListenerPressed(key, dt);
        player2.keyListenerPressed(key, dt);
        
        if (key == 59 && setVisibleBody) {
            setVisibleBody = false;
        } else if (key == 59) {
            setVisibleBody = true;
        }
        if (key == 78) {
            Monster m = new Monster(stage,spritesMonster,pufMonster,jumpSMonster);
            m.setX((float) gen.nextInt(650));
            m.setY((float) gen.nextInt(150)+100);
            m.spawn();
            actorsList.add(m);
        }
        if (key == 60 && pause) {
            pause = false;
        } else if (key == 60) {
            pause = true;
        }
        if(key==63){
            reset();
        }
    }
    
    @Override
    public void keyReleased(int key, char c) {
        player.keyListenerReleased(key, dt);
        player2.keyListenerReleased(key, dt);
    }

    public void spawn() {
        for (int i = 0; i < spawnNumber; i++) {
            int random = gen.nextInt(100);
            if (random >= 85) {
                Tpmonster tm = new Tpmonster(stage);
                actorsList.add(tm);
            } else if (random < 85 && random >= 70) {
                Fatmonster m = new Fatmonster(stage);
                m.setX((float) gen.nextInt(650));
                m.setY(fatNinjaSpawn);
                m.spawn();
                actorsList.add(m);
            } else {
                Monster m = new Monster(stage,spritesMonster,pufMonster,jumpSMonster);
                m.setX((float) gen.nextInt(650));
                m.setY((float) gen.nextInt(150) + 100);
                m.spawn();
                actorsList.add(m);

            }
        }
        spawnNumber = 0;
    }
    public void setNumbersTable(){
        for(int i=0;i<10;i++){
            numbers[i]=font.getSubImage(i*49,62, 49, 62);
        }      
    }
    
    public void reset(){     
        //czyszczenie listy aktorow,a potem init
        actorsList = new ArrayList();
        startSpawnTime = System.currentTimeMillis();
        score=0;
        copyOfScore=0;
        hpscore=0;
        spawnNumber=0;
        themeMusic.stop();
        gameOver.setDefault();
        if (gen.nextInt(2) == 1) {
            themeMusic = theme1;
        } else {
            themeMusic = theme2;
        }
        themeMusic.loop();
        for (int i = 0; i < 2; i++) {
            Monster m = new Monster(stage,spritesMonster,pufMonster,jumpSMonster);
            m.setX((float) gen.nextInt(650));
            m.setY((float) gen.nextInt(150) + 100);
            m.spawn();
            actorsList.add(m);
        }
        Fatmonster fm = new Fatmonster(stage);
        fm.setX((float) gen.nextInt(650));
        fm.setY(fatNinjaSpawn);
        fm.spawn();
        actorsList.add(fm);
        Tpmonster tm = new Tpmonster(stage);
        actorsList.add(tm);
        player.setDefault();
        
        player2.setDefault();
        player2.setX(400);
        player2.setY(250);  
    }
}
