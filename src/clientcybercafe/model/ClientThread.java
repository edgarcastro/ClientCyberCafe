/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientcybercafe.model;

import clientcybercafe.controllers.masterController;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author edgar
 */
public class ClientThread extends Thread{
    private String ipServer;
    private int port;
    private Socket sk;
    private Boolean state;
    private String nameClient;
    private Boolean bloqued;

    public ClientThread(String ipServer, int port) {
        this.ipServer = ipServer;
        this.port = port;
        this.bloqued = false;
    }
    
    public void connect(){
        try{
            sk = new Socket(ipServer, port);
            state = true;
            JOptionPane.showMessageDialog(null, "CONECTADO AL SERVIDOR");
            System.out.println("CONECTADO AL SERVIDOR");
            catchName();
        }catch(IOException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: No se puede conectar al Servidor");
            System.out.println("ERROR: No se puede conectar al Servidor");
        }
    }
    
    public void catchName(){
        Thread hiloFlujoEntrada = new Thread() {
            @Override
            public void run() {
                while (nameClient == null) {                    
                    DataInputStream datosEntrada = null;
                    try {
                        datosEntrada = new DataInputStream(sk.getInputStream());
                        nameClient = datosEntrada.readUTF();
                        masterController.updateNamePC(nameClient);
                        catchBloqued();
                    } catch (IOException ex) {
                        System.out.println("ERROR: De escritura y lectura en la conexion");
                        state = false;
                    }
                }
            }
        };
        hiloFlujoEntrada.start();  
    };
    
    public void catchBloqued(){
        Thread hiloFlujoEntrada = new Thread() {
            @Override
            public void run() {
                while (bloqued == false) {                    
                    DataInputStream datosEntrada = null;
                    try {
                        datosEntrada = new DataInputStream(sk.getInputStream());
                        if(datosEntrada.readUTF().equals("BLOCK")){
                            bloqued = true;
                            masterController.bloquedPC();
                            catchUnBloqued();
                            System.out.println("Este pc "+nameClient+" esta bloqueado");
                        }
                    } catch (IOException ex) {
                        System.out.println("ERROR: De escritura y lectura en la conexion");
                        state = false;
                    }
                }
            }
        };
        hiloFlujoEntrada.start();
    }
    
    public void catchUnBloqued(){
        Thread hiloFlujoEntrada = new Thread() {
            @Override
            public void run() {
                while (bloqued == true) {                    
                    DataInputStream datosEntrada = null;
                    try {
                        datosEntrada = new DataInputStream(sk.getInputStream());
                        if(datosEntrada.readUTF().equals("UNBLOCK")){
                            bloqued = false;
                            masterController.unBloquedPC();
                            catchBloqued();
                            System.out.println("Este pc "+nameClient+" esta desbloqueado");
                        }
                    } catch (IOException ex) {
                        System.out.println("ERROR: De escritura y lectura en la conexion");
                        state = false;
                    }
                }
            }
        };
        hiloFlujoEntrada.start();
    }
    
    
    
    @Override
    public void run() {
        super.run();
        connect();
    }
    
}


