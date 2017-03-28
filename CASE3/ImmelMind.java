import org.w2mind.net.*;
import java.util.*;
import java.awt.*;
//import java.lang.*;
 
public class ImmelMind implements Mind {
//====== Mind must respond to these methods: ==========================================================
//  newrun(), endrun()
//  getaction()
//======================================================================================================
    Point ghostOne = new Point();
    Point ghostTwo = new Point();
    Point ghostThree = new Point();
         
    Point pacman = new Point();
    int action;
     
    public void newrun()  throws RunError { }
    public void endrun()  throws RunError { }
     
    public Action getaction(State state) {
        // parse state:
 
        String s = state.toString();
        // parsed into x[0], x[1], ...
        String [] x = s.split(",");
         
        // getting everything we need from the state for each sprite
        ghostOne.x = Integer.parseInt(x[0]);
        ghostOne.y = Integer.parseInt(x[1]);
        ghostTwo.x = Integer.parseInt(x[2]);
        ghostTwo.y = Integer.parseInt(x[3]);
        ghostThree.x = Integer.parseInt(x[4]);
        ghostThree.y = Integer.parseInt(x[5]);
        pacman.x = Integer.parseInt(x[6]);
        pacman.y = Integer.parseInt(x[7]);
         
        // array for convenience
        Point [] ghosts = {ghostOne, ghostTwo, ghostThree};
         
        // array of illegal positions
         
        Point [] borders = PacSurvive.get_borders();
        int borderCount = 0;
        for(int i = 0; i < PacSurvive.get_borderlength(); i++) {
            if(null != borders[i])
                borderCount++;
        }
        int [] actions = new int[3];
        Random rand = new Random(); 
         
        for(int i = 0; i < 3; i++) {
            // try to move up first, if you can't then down
            // first checks if a ghost is on the same x axis as you
            // if you can't then left - taking into consideration
            // if there is a ghost within three places of Pacman
            // similar for right
            // if these are not important then try to move up/down/left/right
            // check every movement against the borders of the world
            // borders = walls, cannot move into them
            // if for some reason it gets through all of these with no action
            // pick a random action
            for(int j = 0; j < borderCount; j++) {
                /*if((pacman.y - 1) >= 0) && (pacman.y == ghosts[i].y) && ((pacman.y - 1) != borders[j].y) && ((pacman.y - 1) == ghosts[i].y))
                    actions[i] = PacSurvive.ACTION_UP;
                else if(((pacman.y + 1) <= 15) && (pacman.y == ghosts[i].y) && ((pacman.y + 1) != borders[j].y) && ((pacman.y + 1) == ghosts[i].y))
                    actions[i] = PacSurvive.ACTION_DOWN;
                else if(((pacman.x - 1) >= 0) && ((pacman.x - 1) != borders[j].x) && ((pacman.x - 1) == ghosts[i].x) || ((pacman.x - 1) >= 0) && ((pacman.x - 1) != borders[j].x) && (ghosts[i].y - pacman.y <= 3) && ((pacman.x - 1) == ghosts[i].x))
                    actions[i] = PacSurvive.ACTION_LEFT;
                else if(((pacman.x + 1) <= 15) && (pacman.y - ghosts[i].y <= 3) && ((pacman.x + 1) == ghosts[i].x) || ((pacman.x + 1) <= 15) && (pacman.y - ghosts[i].y <= 3) && ((pacman.x + 1) != borders[j].x) && ((pacman.x + 1) == ghosts[i].x))
                    actions[i] = PacSurvive.ACTION_RIGHT;
                else*/ if(((pacman.y - 1) >= 0) && (pacman.y == ghosts[i].y) && ((pacman.y - 1) != borders[j].y))
                    if((((pacman.y - 1) == ghosts[i].y) && ((pacman.y + 1) == ghosts[0].y)) || (((pacman.y - 1) == ghosts[i].y) && ((pacman.y + 1) == ghosts[1].y)) || (((pacman.y - 1) == ghosts[i].y) && ((pacman.y + 1) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_DOWN; //loaded as int = 2 in PacSurvive
                    else if((((pacman.y - 1) == ghosts[i].y) && ((pacman.x - 1) == ghosts[0].x)) || (((pacman.y - 1) == ghosts[i].y) && ((pacman.x - 1) == ghosts[1].x)) || (((pacman.y - 1) == ghosts[i].y) && ((pacman.x - 1) == ghosts[2].x)))
                        actions[i] = PacSurvive.ACTION_LEFT;
                    else if((((pacman.y - 1) == ghosts[i].y) && ((pacman.x + 1) == ghosts[0].x)) || (((pacman.y - 1) == ghosts[i].y) && ((pacman.x + 1) == ghosts[1].x)) || (((pacman.y - 1) == ghosts[i].y) && ((pacman.x + 1) == ghosts[2].x)))
                        actions[i] = PacSurvive.ACTION_RIGHT;
                    else if((pacman.y - 1) == ghosts[i].y)
                        actions[i] = PacSurvive.ACTION_UP;
                else if(((pacman.y + 1) <= 15) && (pacman.y == ghosts[i].y) && ((pacman.y + 1) != borders[j].y))
                    if((((pacman.y + 1) == ghosts[i].y) && ((pacman.x - 1) == ghosts[0].x)) || (((pacman.y + 1) == ghosts[i].y) && ((pacman.x - 1) == ghosts[1].x)) || (((pacman.y + 1) == ghosts[i].y) && ((pacman.x - 1) == ghosts[2].x)))
                        actions[i] = PacSurvive.ACTION_RIGHT; //loaded as int = 3 in PacSurvive
                    else if((((pacman.y + 1) == ghosts[i].y) && ((pacman.y - 1) == ghosts[0].y)) || (((pacman.y + 1) == ghosts[i].y) && ((pacman.y - 1) == ghosts[1].y)) || (((pacman.y + 1) == ghosts[i].y) && ((pacman.y - 1) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_UP;
                    else if((((pacman.y + 1) == ghosts[i].y) && ((pacman.x + 1) == ghosts[0].x)) || (((pacman.y + 1) == ghosts[i].y) && ((pacman.x + 1) == ghosts[1].x)) || (((pacman.y + 1) == ghosts[i].y) && ((pacman.x + 1) == ghosts[2].x)))
                        actions[i] = PacSurvive.ACTION_LEFT;
                    else if((pacman.y + 1) == ghosts[i].y)
                        actions[i] = PacSurvive.ACTION_DOWN;
                else if(((pacman.x - 1) >= 0) && ((pacman.x - 1) != borders[j].x) || ((pacman.x - 1) >= 0) && ((pacman.x - 1) != borders[j].x) && (ghosts[i].y - pacman.y <= 3))
                    if((((pacman.x - 1) == ghosts[i].x) && ((pacman.y - 1) == ghosts[0].y)) || (((pacman.x - 1) == ghosts[i].x) && ((pacman.y - 1) == ghosts[1].y)) || (((pacman.x - 1) == ghosts[i].x) && ((pacman.y - 1) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_RIGHT;
                    else if((((pacman.x - 1) == ghosts[i].x) && ((pacman.y + 1) == ghosts[0].y)) || (((pacman.x - 1) == ghosts[i].x) && ((pacman.y + 1) == ghosts[1].y)) || (((pacman.x - 1) == ghosts[i].x) && ((pacman.y + 1) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_UP;
                    else if((((pacman.x - 1) == ghosts[i].x) && ((pacman.y - 1) == ghosts[0].y)) || (((pacman.x - 1) == ghosts[i].x) && ((pacman.y - 1) == ghosts[1].y)) || (((pacman.x - 1) == ghosts[i].x) && ((pacman.y +-1) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_DOWN;
                    else if((pacman.x - 1) == ghosts[i].x)
                        actions[i] = PacSurvive.ACTION_LEFT; //loaded as int = 0 in PacSurvive
                else if(((pacman.x + 1) <= 15) && (pacman.y - ghosts[i].y <= 3) || ((pacman.x + 1) <= 15) && (pacman.y - ghosts[i].y <= 3) && ((pacman.x + 1) != borders[j].x))
                    if((((pacman.x + 1) == ghosts[i].x) && ((pacman.y + 1) == ghosts[0].y) || (((pacman.x + 1) == ghosts[i].x) && ((pacman.y + 1) == ghosts[1].y)) || (((pacman.x + 1) == ghosts[i].x) && ((pacman.y + 1)) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_LEFT;
                    else if((((pacman.x + 1) == ghosts[i].x) && ((pacman.y + 1) == ghosts[0].y) || (((pacman.x + 1) == ghosts[i].x) && ((pacman.y + 1) == ghosts[1].y)) || (((pacman.x + 1) == ghosts[i].x) && ((pacman.y + 1)) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_UP;
                    else if((((pacman.x + 1) == ghosts[i].x) && ((pacman.y - 1) == ghosts[0].y) || (((pacman.x + 1) == ghosts[i].x) && ((pacman.y - 1) == ghosts[1].y)) || (((pacman.x + 1) == ghosts[i].x) && ((pacman.y - 1)) == ghosts[2].y)))
                        actions[i] = PacSurvive.ACTION_DOWN;
                    else if((pacman.x + 1) == ghosts[i].x)
                        actions[i] = PacSurvive.ACTION_RIGHT; //loaded as int = 1 in PacSurvive
                else if(((pacman.y - 1) >= 0) && (pacman.y == ghosts[i].y) && ((pacman.y - 1) != borders[j].y))
                    actions[i] = PacSurvive.ACTION_UP;
                else if(((pacman.y + 1) <= 15) && (pacman.y == ghosts[i].y) && ((pacman.y + 1) != borders[j].y))
                    actions[i] = PacSurvive.ACTION_DOWN; 
                else if(((pacman.x - 1) >= 0) && ((pacman.x - 1) != borders[j].x) || ((pacman.x - 1) >= 0) && ((pacman.x - 1) != borders[j].x) && (ghosts[i].y - pacman.y <= 3))
                    actions[i] = PacSurvive.ACTION_LEFT; 
                else if(((pacman.x + 1) <= 15) && (pacman.y - ghosts[i].y <= 3) || ((pacman.x + 1) <= 15) && (pacman.y - ghosts[i].y <= 3) && ((pacman.x + 1) != borders[j].x))
                    actions[i] = PacSurvive.ACTION_RIGHT;
                else if((pacman.y != 0) && (pacman.y < 7) && ((pacman.y - 1) != borders[j].y))
                    actions[i] = PacSurvive.ACTION_UP;
                else if((pacman.y != 14) && (pacman.y < 7) && ((pacman.y + 1) != borders[j].y))
                    actions[i] = PacSurvive.ACTION_DOWN;
                else if((pacman.x != 0) && (pacman.x < 7) && ((pacman.x - 1) != borders[j].x))
                    actions[i] = PacSurvive.ACTION_LEFT;
                else if((pacman.x != 14) && (pacman.x > 7) && ((pacman.x + 1) != borders[j].x))
                    actions[i] = PacSurvive.ACTION_RIGHT;
                else {
                    int random = rand.nextInt(PacSurvive.NO_ACTIONS); // NO_ACTIONS is int = 4
                    actions[i] = random;
                }
            }
        }
         
        int [] finalActions = new int[3];
         
        int index = 0;
        // double check it doesn't fall into illegal area
        for(int j = 0; j < 3; j++) {
            for(int i = 0; i < borderCount; i++) {
                if(actions[j] == PacSurvive.ACTION_UP) {
                    if((pacman.y - 1) == borders[i].y) {}
                }
                else if(actions[j] == PacSurvive.ACTION_DOWN) {
                    if((pacman.y + 1) == borders[i].y) {}
                }
                else if(actions[j] == PacSurvive.ACTION_LEFT) {
                    if((pacman.x - 1) == borders[i].x) {}
                }
                else if(actions[j] == PacSurvive.ACTION_LEFT) {
                    if((pacman.x + 1) == borders[i].x) {}
                }
                else {
                    if(index < 3) {
                        finalActions[index] = actions[j];
                        index++;
                    }
                    else {}
                }
            }
        }
         
        // get the first action from the arraylist and return that as an action
        action = finalActions[0];
         
        String a = String.format("%d", action);
        return new Action(a);
    }
}
