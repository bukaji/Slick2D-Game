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
public class GameOver extends Actors {
    private int endAnimation=15;
    private Sound diedS;
    private boolean doItOne=true;

    public GameOver() {
        sprites = new Image[3];
        animationTable = new Animation[3];
        try {
            diedS = new Sound("sounds/narutoDied.wav");
            sprites[0] = new Image("sprites/effects/blood.png");
            sprites[1] = new Image("sprites/effects/youDied.jpg");
        } catch (SlickException e) {
            System.out.print("Nie udalo sie wczytac gameOver Effect: " + e.getMessage() + "\n");
        }
        rectangle = new Rectangle(1, 1, 1, 1);
        setAnimationTable();
        animation = animationTable[0];
    }   
    
      public void update(int delta) {
          animation.update(delta);
          if(!diedS.playing() && doItOne){
              doItOne=false;
              diedS.play();
          }
          if(animation.getFrame()==endAnimation){
              animation=animationTable[1];
              setDoDelete(true);
          }
      }
    public void setDefault(){
        doItOne=true;
        animation = animationTable[0];
        //animation.restart();
    }
      
    public void setAnimationTable() {
        animationTable[0] = setAnimation(sprites[0], 4, 4, 128, 128, 16, 75);
        animationTable[1] = setAnimation(sprites[1], 1, 1, 575, 350, 1, 200);
    }
}
