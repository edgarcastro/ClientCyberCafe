/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientcybercafe.controllers;

import clientcybercafe.model.ClientThread;
import clientcybercafe.model.PC;
import clientcybercafe.views.BloquedView;
import clientcybercafe.views.MainView;
import javax.swing.JOptionPane;

/**
 *
 * @author edgar
 */
public class masterController {
    
    public static MainView mv;
    public static BloquedView bv;
    public static PC pc;
    
    public static void init(){
        bv = new BloquedView();
        mv = new MainView();
        mv.setLocationRelativeTo(null);
        mv.setVisible(true);
        pc = new PC(bv);
    }
    
    public static void initClient(String ipAddress, int port){
        ClientThread ct = new ClientThread(ipAddress, port);
        ct.connect();
    }
    
    public static void updateNamePC(String namePC){
        mv.updateNamePC(namePC);
    }
    
    public static void bloquedPC(){
        pc.block();
    }
    
    public static void unBloquedPC(){
        pc.unBlock();
    }
    
    public static void apagarPC(){
        pc.apagar();
    }
    
    public static void reiniciarPc(){
        pc.reiniciar();
    }
    
    public static void cancelar(){
        pc.cancelar();
    }
}
