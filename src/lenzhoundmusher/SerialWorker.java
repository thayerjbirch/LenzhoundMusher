/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundmusher;

import java.util.ArrayList;
import jssc.*;
/**
 *
 * @author Thayer
 */
public class SerialWorker {
    public SerialWorker(SerialPort inPort){
        port = inPort;
    }
    
    public boolean hasFailed(){
        return errorThrown;
    }
    
    public void shutdown(){
        try {
            if(port.isOpened())
                port.closePort();
        } catch (SerialPortException ex) {
            System.out.println(ex.toString());
        }
        MotorController.removeInUse(port.getPortName());
    }
    
    public void sendMoveCommand(int target){
        try {
            port.openPort();
            port.writeBytes(("9," + target + ";").getBytes());
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
        try {
            port.closePort();
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
    
    public static ArrayList<SerialPort> findMotors(){
        ArrayList<SerialPort> devicePorts = new ArrayList<>();
        String[] allPorts = SerialPortList.getPortNames();
        for(String s: allPorts){
            if(MotorController.checkInUse(s)){//if a motor already has an assigned controller, skip itcontinue;
                System.out.println(s + " is already in use.");
                continue;
            }
            SerialPort testPort = new SerialPort(s);
            try{
                testPort.openPort();
                testPort.setParams(9600, 8, 1, 0);
                testPort.writeBytes("0;".getBytes());
                try{
                    Thread.sleep(350);
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                String returnedString = new String(testPort.readBytes());//new String(buffer, "UTF-8");
                testPort.closePort();
                if(returnedString.contains("Dogbone")){
                    System.out.println("Check success on port " + s + "!\n");
                    devicePorts.add(testPort);
                }
            }catch(Exception e){
                try {
                    testPort.closePort();
                    System.out.println(e);
                    System.out.println("Port " + s + " has failed.");
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
        return devicePorts;
    }
    
    private SerialPort port;
    boolean errorThrown = false;
}
