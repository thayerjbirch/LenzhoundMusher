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
        motorPanel = new SingleMotorPanel(this);
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
    
    /**
     *
     * @param newPoint
     * @return returns the index where the new waypoint is placed
     */
    public void addWaypoint(Waypoint newPoint){
        /*if(waypointArray.isEmpty()){
            waypointArray.add(newPoint);            
            motorPanel.newListData(prepareArray(waypointArray.toArray()));
            return;
        }*/
        int i = 0;
        while(waypointArray.size() > i && newPoint.compare(waypointArray.get(i)) > 0){
            i++;
        }
        if(waypointArray.size() > i){
            if(newPoint.compare(waypointArray.get(i)) != 0){
                waypointArray.add(i, newPoint);
                motorPanel.newListData(prepareArray(waypointArray.toArray()));
            }
            else
                JOptionPane.showMessageDialog(null,
                        "Error: cannot assign two target positions for the same point in time.",
                        "Alert", JOptionPane.ERROR_MESSAGE);
        }else{
            waypointArray.add(newPoint);            
            motorPanel.newListData(prepareArray(waypointArray.toArray()));
        }
    }
    
    public void removeWaypoint(int index){
        waypointArray.remove(index);
        motorPanel.newListData(prepareArray(waypointArray.toArray()));
    }
    
    private String[] prepareArray(Object[] arrIn){
        String[] arrOut = new String[arrIn.length];
        for(int i = 0; i < arrIn.length; i++){
            arrOut[i] = arrIn[i].toString();
        }
        return arrOut;
    }
    
    public void moveBy(int steps){
        motorPosition+=steps;
        serialWorker.sendMoveCommand(motorPosition);
        System.out.println(motorPosition);
    }
    

    public int getMotorPosition() {
        return motorPosition;
    }
    
    private int motorPosition = 0;
    private int originPosition = 0;
    SerialWorker serialWorker = null;
    SingleMotorPanel motorPanel = null;
    JMenuItem associatedMenuButton;
    public Musher callback = null;
    static ArrayList<String> motorsInUse = new ArrayList<>();
    private final ArrayList<Waypoint> waypointArray = new ArrayList<>();
}
