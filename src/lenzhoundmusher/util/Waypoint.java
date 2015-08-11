/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundmusher.util;

/**
 *
 * @author Thayer
 */
public class Waypoint {
    public Waypoint(double tarTime, int tarPosition, int vel, int acc){
        target = tarPosition;
        
        if(tarTime < 0)
            tarTime*=-1;
        time = tarTime;
        miliTime = (int) time * 1000;
        
        if(vel < 0)
            vel*=-1;
        if(vel > MAX_VELOCITY)
            vel = MAX_VELOCITY;
        velocity = vel;
        
        if(acc<0)
            acc*=-1;
        if(acc > MAX_ACCELERATION)
            acc = MAX_ACCELERATION;
        acceleration = acc;
    }
    
    public Waypoint(double tarTime, int tarPosition){
        this(tarTime, tarPosition, MAX_VELOCITY, MAX_ACCELERATION);
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        if(velocity < 0)
            velocity*=-1;
        if(velocity > MAX_VELOCITY)
            velocity = MAX_VELOCITY;
        this.velocity = velocity;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        if(acceleration<0)
            acceleration*=-1;
        if(acceleration > MAX_ACCELERATION)
            acceleration = MAX_ACCELERATION;
        this.acceleration = acceleration;
    }
    
    public int compare(Waypoint rh){
        if(time < rh.time)
            return -1;
        else if(time > rh.time)
            return 1;
        else return 0;
    }
    
    @Override
    public String toString(){
        return String.format("Position % 6d at time % 5.2f",target,time);
    }
    
    private int acceleration;    
    public final int target;
    public final double time;
    public final int miliTime;
    private int velocity;

    /**
     *The maximum allowed velocity of the motor
     */
    public static final int MAX_VELOCITY = 32768;

    /**
     *The maximum allowed acceleration of the motor
     */
    public static final int MAX_ACCELERATION = 1024;
}
