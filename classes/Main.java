package classes;

import java.lang.reflect.Field;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {

    public static void main(String[] arguments) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {   
        //System.out.print(System.getProperty("user.dir")+"\n\n");
        
        System.setProperty("java.library.path", System.getProperty("user.dir")+"\\slick");
        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        fieldSysPath.set(null, null);
        
        try {
            AppGameContainer app = new AppGameContainer(new MainLoop());
            app.setDisplayMode(800, 600, false);
            //app.setVSync(true);
            app.setFullscreen(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
