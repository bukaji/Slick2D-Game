package classes;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Bukaji-PC
 */
public class Effects extends Actors {
    private int endAnimation;
    private int WIDTH = 28, HEIGHT = 50;  //75x  
    private int TPX = 45, TPY = 13;

    public Effects(float x, float y, int ktora) throws SlickException {
        mpGive=0;
        sprites = new Image[15];
        animationTable = new Animation[15];
        type="effect";   
        setXY(ktora,x,y);
        try {
            sprites[0] = new Image("sprites/effects/dmg.png");
            sprites[1] = new Image("sprites/effects/puf.png");
            sprites[2] = new Image("sprites/effects/chakraRed.png");
            sprites[3] = new Image("sprites/effects/chakraGreen.png");
            sprites[4] = new Image("sprites/effects/chakraBlue.png");
            sprites[5] = new Image("sprites/effects/chakraGrey.png");
            sprites[6] = new Image("sprites/effects/puf2.png");
            sprites[7] = new Image("sprites/effects/ff.png");
            sprites[8] = new Image("sprites/TpNinja/tp.png");
            sprites[9] = new Image("sprites/TpNinja/tpLeft.png");
        } catch (SlickException e) {
            System.out.print("Nie udalo sie wczytac efektow: " + e.getMessage() + "\n");
        }
        rectangle = new Rectangle(1, 1, 1, 1);
        setAnimationTable();
        setEnd(ktora);
        animation = animationTable[ktora];
    }
    
    public void setXY(int i, float x, float y){
        if(i==1){
            width = height = 100;
            this.x = x;
            this.y = y;
        } else if(i==0) {
            width = height = 100;
            this.x = x + gen.nextInt(20);
            this.y = y + 15 + gen.nextInt(20);
        }
         else if(i==2 || i==3 || i==4 || i==5) {
             width = height = 100;
            this.x = x;
            this.y = y;
        }else if(i==6){
            width = 100;
            height = 115;
            this.x = x;
            this.y = y;
        }else if(i==7){
            width = 600;
            height = 800;
            this.x=0;
            this.y=0;
        }else if(i==8 || i==9){
            width = height = 100;   
            //System.out.print("X= "+x+" Y= "+y+" ");
            if(x<700 && y<120 && x>680 && y>110){
               this.x =185-WIDTH-TPX;
               this.y=232-(HEIGHT*2)-TPY;
            }
            if(x<670 && y<280 && x>660 && y>270){
                this.x=718-WIDTH;
                this.y=232-(HEIGHT*2)-TPY;
            }
            if(x<130 && y<280 && x>120 && y>270){
                this.x=693-WIDTH;
                this.y=388-(HEIGHT*2)-TPY;
            }
            if(x<120 && y<120 && x>110 && y>110){
                this.x=200-WIDTH-TPX;
                this.y=388-(HEIGHT*2)-TPY;
            }   
        }
    }
    
    public void setEnd(int i){
        if(i==0){
            endAnimation=2;
        }else if(i==1){
            endAnimation=3;
        }
        else if(i==2 || i==3 || i==4 || i==5){
            endAnimation=5;
        }else if(i==6){
            endAnimation=4;
        }else if(i==7){
            endAnimation=35;
        }else if(i==8 || i==9){
            endAnimation=3;
        }
    }
    
      public void update(int delta) {
          if(animation.getFrame()>=endAnimation){
              setDoDelete(true);
          }
      }

    public void setAnimationTable() {
        animationTable[0] = setAnimation(sprites[0], 3, 1, 50, 50, 4, 75);
        animationTable[1] = setAnimation(sprites[1], 4, 1, 100, 100, 4, 75);
        animationTable[2] = setAnimation(sprites[2], 6, 1, 50, 80, 6, 80);
        animationTable[3] = setAnimation(sprites[3], 6, 1, 50, 80, 6, 80);
        animationTable[4] = setAnimation(sprites[4], 6, 1, 50, 80, 6, 80);
        animationTable[5] = setAnimation(sprites[5], 6, 1, 50, 80, 6, 80);
        animationTable[6] = setAnimation(sprites[6], 5, 1, 100, 115, 5,75);
        animationTable[7] = setAnimation(sprites[7], 1, 36, 320, 256, 36, 40);
        animationTable[8] = setAnimation(sprites[9], 4, 1, 100, 100, 4, 150);       
        animationTable[9] = setAnimation(sprites[8], 4, 1, 100, 100, 4, 150); 
    }
    public void swapAnimation(int i){
        animation=animationTable[i];
    }
}
