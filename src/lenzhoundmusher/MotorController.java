/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundmusher;

import lenzhoundmusher.panels.SingleMotorPanel;

/**
 *
 * @author Thayer
 */
public class MotorController {
    public MotorController (Musher cb){
        callback = cb;
        //callback.
    }
    SingleMotorPanel motorPanel = new SingleMotorPanel();
    Musher callback = null;
}
