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
import Jama.Matrix;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

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
    
    public double getEndTime(){
        return waypointArray.get(waypointArray.size() - 1).miliTime;
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
        createThreePointInterpolater(getXArray(),getYArray());
    }
    
    public void removeWaypoint(int index){
        waypointArray.remove(index);
        motorPanel.newListData(prepareArray(waypointArray.toArray()));
        if(waypointArray.size() < 2){
           interpolater = null;
        }
    }
    
    public void advance(double time){        
        int target;
        try {
            target = (int)interpolater.value(time);            
            serialWorker.sendMoveCommand(target);
            System.out.println(target);
        } catch (Exception e) {
            System.out.println(e);
            //System.out.println("Not enough points to interpolate");
        }
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
    }
    
    public int getMotorPosition() {
        return motorPosition;
    }
    
    public double[] getXArray(){
        double[] x = new double[waypointArray.size()];
        for(int i = 0; i < waypointArray.size(); i++){
            x[i] = waypointArray.get(i).miliTime;
        }
        return x;
    }
    
    public double[] getYArray(){
        double[] y = new double[waypointArray.size()];
        for(int i = 0; i < waypointArray.size(); i++){
            y[i] = waypointArray.get(i).target;
        }
        return y;
    }
    
    public void createThreePointInterpolater(double[] x, double[] y){
        if(x.length != y.length || x.length < 2){
            System.out.println("Error: Invalid Parameters");
            return;
        }
        PolynomialFunction[] polynomials = new PolynomialFunction[x.length - 1];
        
        //special case for the first polynomial
        double[] xPoints = {x[0],x[1]};
        double[] yPoints = {y[0],y[1]};
        polynomials[0] = new PolynomialFunction(findPolynomialFactors(xPoints,yPoints));
        
        if(x.length >=3){
            int index = 1;
            while(index < x.length - 1){
                xPoints = new double[]{x[index-1],x[index],x[index+1]};
                yPoints = new double[]{y[index-1],y[index],y[index+1]};
                double[] f = findPolynomialFactors(xPoints,yPoints);
                polynomials[index] = new PolynomialFunction(f);
                System.out.println(f[0] + "x^2 + " + f[1] + "x + " + f[2]);
                index++;
            }        
        }
        
        interpolater = new SplineInterpolater(x,polynomials);
    }
    
    @Override
    public String toString(){
        if(interpolater!=null){
            return interpolater.toString();
        }
        return "null";
    }
    
    public static double[] findPolynomialFactors(double[] x, double[] y)
        throws RuntimeException{
        int n = x.length;
        double[][] data = new double[n][n];
        double[]   rhs  = new double[n];

        for (int i = 0; i < n; i++) {
          double v = 1;
          for (int j = 0; j < n; j++) {
              data[i][n-j-1] = v;
              v *= x[i];
          }

          rhs[i] = y[i];
        }
        Matrix m = new Matrix(data);
        Matrix b = new Matrix(rhs, n);

        Matrix s = m.solve(b);
        
        double[] returnArray = s.getRowPackedCopy();
        ArrayUtils.reverse(returnArray);
        return returnArray;
    }
    
    private int motorPosition = 0;
    SerialWorker serialWorker = null;
    SingleMotorPanel motorPanel = null;
    JMenuItem associatedMenuButton;
    public Musher callback = null;
    static ArrayList<String> motorsInUse = new ArrayList<>();
    private final ArrayList<Waypoint> waypointArray = new ArrayList<>();
    private SplineInterpolater interpolater;
    
    private class SplineInterpolater {
        public SplineInterpolater(double[] knots,PolynomialFunction[] funcs){
            this.knots = knots;
            this.funcs = funcs;
        }

        public double value(double x) throws Exception{
            if(x < knots[0] || x > knots[knots.length - 1]){
                throw new Exception("Value is out of bounds");
            }
            int index = 1;
            while(x > knots[index] && index < funcs.length)
                index++;
            return funcs[index - 1].value(x);
        }
        
        @Override
        public String toString(){
            return Arrays.toString(funcs);
        }

        double[] knots;
        PolynomialFunction[] funcs;
    }
}
