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
        try{
            port.openPort();
            port.setParams(SerialPort.BAUDRATE_9600, 
                           SerialPort.DATABITS_8,
                           SerialPort.STOPBITS_1,
                           SerialPort.PARITY_NONE);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public boolean hasFailed(){
        return errorThrown;
    }
    
    public static ArrayList<SerialPort> findMotors(){
        ArrayList<SerialPort> devicePorts = new ArrayList<>();
        String[] allPorts = SerialPortList.getPortNames();
        for(String s: allPorts){
            SerialPort testPort = new SerialPort(s);
            try{
                testPort.writeBytes("0;".getBytes());
                byte[] buffer = testPort.readBytes(18, 250);
                String returnedString = new String(buffer, "UTF-8");
                testPort.closePort();
                if(returnedString.contains("Dogbone")){
                    System.out.println("Check success!\n");
                    devicePorts.add(testPort);
                }
            }catch(Exception e){
                System.out.println("Port " + s + " has failed.");
            }
        }
        return devicePorts;
    }
    
    private SerialPort port;
    boolean errorThrown = false;
}
