/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundmusher.util;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.BitSet;
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lenzhoundmusher.panels.SingleMotorPanel;

/**
 *
 * @author Thayer
 */
public class ManualDrive extends javax.swing.JDialog {

    /**
     * Creates new form ManualDrive
     */
    public ManualDrive(java.awt.Frame parent, boolean modal, SingleMotorPanel callback) {
        super(parent, modal);
        initComponents();
        pack();
        setLocationRelativeTo(parent);
        this.callback = callback;
        t= new Timer(20 , new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               callback.parent.moveBy(stepsToMove);
               setStatusText();
            }
         });
        setUpListeners();
        setStatusText();
        
        //required setup for focus independent keyboard contorl
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fastBackButton = new javax.swing.JButton();
        recordButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        fastForwardButton = new javax.swing.JButton();
        forwardButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        fastBackButton.setText("<<");
        fastBackButton.setToolTipText("Hotkey: A");

        recordButton.setText("Record Position");
        recordButton.setToolTipText("Hotkey: Spacebar");
        recordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordButtonActionPerformed(evt);
            }
        });

        backButton.setText("<");
        backButton.setToolTipText("Hotkey: S");

        fastForwardButton.setText(">>");
        fastForwardButton.setToolTipText("Hotkey: F");
        fastForwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastForwardButtonActionPerformed(evt);
            }
        });

        forwardButton.setText(">");
        forwardButton.setToolTipText("Hotkey: D");

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fastBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(recordButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(forwardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fastForwardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fastForwardButton)
                    .addComponent(forwardButton)
                    .addComponent(recordButton)
                    .addComponent(backButton)
                    .addComponent(fastBackButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fastForwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastForwardButtonActionPerformed
        
    }//GEN-LAST:event_fastForwardButtonActionPerformed

    private void recordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordButtonActionPerformed
        record();
    }//GEN-LAST:event_recordButtonActionPerformed
    
    private void record(){
        String inString = JOptionPane.showInputDialog("When do you want to arrive at this point:");
        double time = -1;
        while(time<0){
            try{
                if(inString.equals(""))
                    return;
                time = Double.parseDouble(inString);
                if(time<0)
                    inString = JOptionPane.showInputDialog("Time value must be non-negative:");                
            }catch(Exception e){
                if(e instanceof NullPointerException)//lets you cancel out
                    return;
                inString = JOptionPane.showInputDialog("Must enter a numeric value:");
                continue;
            }
        }
        if(time == 0)
            originPosition = callback.parent.getMotorPosition();
        callback.parent.addWaypoint(new Waypoint(time,callback.parent.getMotorPosition()));
    }
    
    private void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_A:
                stepsToMove = -10;
                keysDown.set(0);
                break;
            case KeyEvent.VK_F:
                stepsToMove = 10;
                keysDown.set(1);
                break;
            case KeyEvent.VK_S:
                stepsToMove = -5;
                keysDown.set(2);
                break;
            case KeyEvent.VK_D:
                stepsToMove = 5;
                keysDown.set(3);
                break;
            case KeyEvent.VK_SPACE:
                record();
                break;
            default:
                stepsToMove = 0;
        }
        t.start();
    }
    
    private void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_A:
                keysDown.clear(0);
                break;
            case KeyEvent.VK_F:
                keysDown.clear(1);
                break;
            case KeyEvent.VK_S:
                keysDown.clear(2);
                break;
            case KeyEvent.VK_D:
                keysDown.clear(3);
                break;
        }
        if(keysDown.isEmpty())
            t.stop();
    }
    
    private void setStatusText(){
        statusLabel.setText(String.format("%s is at position % 6d", callback.getMotorName(), callback.parent.getMotorPosition()));
    }
    
    private void setUpListeners(){
        forwardButton.addChangeListener(new ChangeListener() {
            ButtonModel buttonModel = forwardButton.getModel();
            @Override
            public void stateChanged(ChangeEvent cEvt) {
               if(buttonModel.isPressed()){
                   stepsToMove = 5;
                   t.start();
               }else{
                   t.stop();
               }
            }
        });
        fastForwardButton.addChangeListener(new ChangeListener() {
            ButtonModel buttonModel = fastForwardButton.getModel();
            @Override
            public void stateChanged(ChangeEvent cEvt) {
               if(buttonModel.isPressed()){
                   stepsToMove = 10;
                   t.start();
               }else{
                   t.stop();
               }
            }
        });
        backButton.addChangeListener(new ChangeListener() {
            ButtonModel buttonModel = backButton.getModel();
            @Override
            public void stateChanged(ChangeEvent cEvt) {
               if(buttonModel.isPressed()){
                   stepsToMove = -5;
                   t.start();
               }else{
                   t.stop();
               }
            }
        });
        fastBackButton.addChangeListener(new ChangeListener() {
            ButtonModel buttonModel = fastBackButton.getModel();
            @Override
            public void stateChanged(ChangeEvent cEvt) {
               if(buttonModel.isPressed()){
                   stepsToMove = -10;
                   t.start();
               }else{
                   t.stop();
               }
            }
        });
        
    }
    
    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                keyPressed(e);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                keyReleased(e);
            }
            return false;
        }
    }
    
    private final SingleMotorPanel callback;
    int originPosition = 0;
    int stepsToMove = 0;
    final Timer t;
    BitSet keysDown = new BitSet(4);
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton fastBackButton;
    private javax.swing.JButton fastForwardButton;
    private javax.swing.JButton forwardButton;
    private javax.swing.JButton recordButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
