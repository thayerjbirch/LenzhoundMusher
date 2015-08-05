/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundmusher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import lenzhoundmusher.panels.SingleMotorPanel;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import jssc.SerialPort;
import lenzhoundmusher.panels.EmptyPanel;
import lenzhoundmusher.panels.SearchPanel;

/**
 *
 * @author Thayer
 */
public class Musher extends javax.swing.JFrame {

    /**
     * Creates new form MusherGui
     */
    public Musher() {
        initComponents();
        setUpListeners();
        masterMotorPanel.setLayout(new BoxLayout(masterMotorPanel, BoxLayout.Y_AXIS));
        addPanel(new SearchPanel(this));
        this.pack();
    }
    
    public final void addMotorController(SerialPort outport){
        int newIndex = controllerArray.size();// .size() not -1 because we're about to increace the array size        
        JMenuItem newButton = new JMenuItem("Remove Motor " + (newIndex + 1));
        newButton.addActionListener((ActionEvent e) -> {
            JMenuItem button = (JMenuItem) e.getSource();
            for(MotorController cont : controllerArray){
                if(cont.associatedMenuButton.equals(button)){
                    removeMotorController(cont);
                    break;
                } else
                    System.out.println("fail to delete");
            }
        });
        MotorController newController = new MotorController(this,new SerialWorker(outport), newButton);
        controllerArray.add(newController);
        menuRemoveMotorMenu.add(newButton);
        addPanel(controllerArray.get(newIndex).motorPanel);
    }
    
    public final void removeMotorController(MotorController controller){
        controller.shutdown();
        removePanel(controller.motorPanel);
        controllerArray.remove(controller);
        if(controllerArray.isEmpty())
            addPanel(new EmptyPanel());
    }
    
    public final void addPanel(JPanel newPanel){
        Musher.masterMotorPanel.add(newPanel);        
        panelList.add(newPanel);        
        this.pack();
    }
    
    public final void removePanel(int index){
        Musher.masterMotorPanel.remove(panelList.get(index));
        panelList.remove(index);
        masterMotorPanel.setPreferredSize(masterMotorPanel.getPreferredSize());
        this.pack();
    }
    
    public final void removePanel(JPanel panel){
        Musher.masterMotorPanel.remove(panel);
        panelList.remove(panel);
        this.pack();
    }
    
    private void setUpListeners(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for(MotorController cont : controllerArray){
                    cont.shutdown();//ensures all serial ports are closed
                }
            }

        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goButton = new javax.swing.JButton();
        masterMotorPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        menuFileButton = new javax.swing.JMenu();
        menuFile_AddButton = new javax.swing.JMenuItem();
        menuRemoveMotorMenu = new javax.swing.JMenu();
        menuFile_ExitButton = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(410, 210));

        goButton.setText("Begin Move");
        goButton.setMinimumSize(new java.awt.Dimension(86, 23));
        goButton.setPreferredSize(null);
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        masterMotorPanel.setMaximumSize(null);
        masterMotorPanel.setMinimumSize(null);

        javax.swing.GroupLayout masterMotorPanelLayout = new javax.swing.GroupLayout(masterMotorPanel);
        masterMotorPanel.setLayout(masterMotorPanelLayout);
        masterMotorPanelLayout.setHorizontalGroup(
            masterMotorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        masterMotorPanelLayout.setVerticalGroup(
            masterMotorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        menuFileButton.setMnemonic('f');
        menuFileButton.setText("File");

        menuFile_AddButton.setMnemonic('n');
        menuFile_AddButton.setText("Add New Motor");
        menuFile_AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFile_AddButtonActionPerformed(evt);
            }
        });
        menuFileButton.add(menuFile_AddButton);

        menuRemoveMotorMenu.setText("Remove Motor");
        menuFileButton.add(menuRemoveMotorMenu);

        menuFile_ExitButton.setMnemonic('x');
        menuFile_ExitButton.setText("Exit");
        menuFile_ExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFile_ExitButtonActionPerformed(evt);
            }
        });
        menuFileButton.add(menuFile_ExitButton);

        menuBar.add(menuFileButton);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(masterMotorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(goButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(154, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(masterMotorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuFile_ExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFile_ExitButtonActionPerformed
        for(MotorController cont : controllerArray){
                    cont.shutdown();//ensures all serial ports are closed
                }
        System.exit(0);
    }//GEN-LAST:event_menuFile_ExitButtonActionPerformed

    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
        
    }//GEN-LAST:event_goButtonActionPerformed

    private void menuFile_AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFile_AddButtonActionPerformed
        if(panelList.get(0) instanceof EmptyPanel){
            removePanel(0);
            addPanel(new SearchPanel(this));
            return;
        }
        if(!(panelList.get(panelList.size() - 1) instanceof SearchPanel))
            addPanel(new SearchPanel(this));
    }//GEN-LAST:event_menuFile_AddButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Musher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Musher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Musher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Musher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Musher().setVisible(true);
            }
        });
    }

    protected ArrayList<MotorController> controllerArray = new ArrayList<>();
    private ArrayList<JPanel> panelList = new ArrayList<>();
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton goButton;
    protected static javax.swing.JPanel masterMotorPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuFileButton;
    private javax.swing.JMenuItem menuFile_AddButton;
    private javax.swing.JMenuItem menuFile_ExitButton;
    private javax.swing.JMenu menuRemoveMotorMenu;
    // End of variables declaration//GEN-END:variables
}
