package classes;


import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Stages extends Actors {
    private Image map;
    private Shape mapBody, platform, platform2;
    
    public Stages(GameContainer container) throws SlickException{
        sprites = new Image[6];
        init(container);
    }
    public void init(GameContainer container) throws SlickException {
    sprites[0]=new Image("maps/map1/map1_1.png");
    sprites[1]=new Image("maps/map1/map1_2.png");
    sprites[2]=new Image("maps/map1/map1_3.png");
    sprites[3]=new Image("maps/map1/map1_4.png");
    sprites[4]=new Image("maps/map1/map1_5.png");
    sprites[5]=new Image("maps/map1/map1_6.png");
    animation= new Animation(sprites,200);

    
    float tableOfPoints[]= {0,0,
                            0,235,
                            225,235,
                            225,242,
                            0,242,
                            0,389,
                            240,389,
                            240,396,
                            0,396,
                            0,570,
                            800,570,
                            800,398,
                            653,398,
                            653,391,
                            800,391,
                            800,242,
                            678,242,
                            678,235,
                            800,235,
                            800,0,//nawrot
                            800,570,
                            0,570,
                            0,600,
                            800,600,
                            800,0
    }; 
    mapBody=new Polygon(tableOfPoints);
    platform=new Rectangle(296,504,315,5);
    platform2=new Rectangle(323,330,248,5);
    }
    
    
    public void update(GameContainer container, int delta) throws SlickException {
    animation.update(delta);
    }
    @Override
    public void render(GameContainer container, Graphics g, boolean show) throws SlickException {
        animation.draw(0, 0);
        if (show) {
            g.setColor(Color.yellow);
            g.draw(mapBody);
            g.draw(platform);
            g.draw(platform2);
        }
    }
    
    public boolean collisionWith(Shape s){
        return mapBody.intersects(s) || platform.intersects(s) || platform2.intersects(s);
    }
    
    public Shape getMapBody() {
        return mapBody;
    }
    
}
