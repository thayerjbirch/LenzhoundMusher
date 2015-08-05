/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundmusher;

import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import lenzhoundmusher.panels.SingleMotorPanel;
import lenzhoundmusher.util.Waypoint;

/**
 *
 * @author Thayer
 */
public class MotorController {
    public MotorController (Musher cb, SerialWorker outWorker, JMenuItem removeButton){
        callback = cb;
        serialWorker = outWorker;
        associatedMenuButton = removeButton;
    }
    public void shutdown(){
        serialWorker.shutdown();
    }
    static public boolean checkInUse(String motor){
        return motorsInUse.contains(motor);
    }
    
    static public void addInUse(String motor){
        motorsInUse.add(motor);
    }
    
    static public void removeInUse(String motor){
        motorsInUse.remove(motor);
    }
    
    public void addWaypoint(Waypoint newPoint){
        int i = 0;
        while(newPoint.compare(waypointList.get(i)) > 0){
            i++;
        }
        if(newPoint.compare(waypointList.get(i)) != 0)
            waypointList.add(i, newPoint);
        else
            JOptionPane.showMessageDialog(null,
                    "Error: cannot assign two target positions for the same point in time.",
                    "Alert", JOptionPane.ERROR_MESSAGE);
    }
    
    public void removeWaypoint(int index){
        waypointList.remove(index);
    }
    
    SerialWorker serialWorker = null;
    SingleMotorPanel motorPanel = new SingleMotorPanel(this);
    JMenuItem associatedMenuButton;
    Musher callback = null;
    static ArrayList<String> motorsInUse = new ArrayList<>();
    private final ArrayList<Waypoint> waypointList = new ArrayList<>();
}
