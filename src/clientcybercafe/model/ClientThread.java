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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    //establece la coneccion con el servidor
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
    //Por este metodo se recive el nombre asignado por el servidor
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
                        catchAction();
                    } catch (IOException ex) {
                        System.out.println("ERROR: De escritura y lectura en la conexion");
                        state = false;
                    }
                }
            }
        };
        hiloFlujoEntrada.start();  
    };
    
    //Mantiene la coneccion con el servidor
    //Recibe los mensajes del servidor
    public void catchAction(){
        Thread hiloFlujoEntrada = new Thread(){
            boolean run = true;  
            String mensaje;
            public void run(){
                DataInputStream datosEntrada = null;
                try {
                    datosEntrada = new DataInputStream(sk.getInputStream());   
                    mensaje=datosEntrada.readUTF();
                    ejecutar(mensaje);
                    catchAction();
                } catch (IOException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }               
        };
        hiloFlujoEntrada.start();
    }
    
    //ejecuta la accion que recibe como prametro (mensaje)
    public void ejecutar(String mensaje){      
                    if((mensaje.equals("BLOCK"))&&(bloqued==false)){
                        bloqued = true;
                        masterController.bloquedPC();
                        System.out.println("Este pc "+nameClient+" esta bloqueado");
                        
                    }else if((mensaje.equals("UNBLOCK"))&&(bloqued==true)){
                        bloqued = false;
                        masterController.unBloquedPC();
                        System.out.println("Este pc "+nameClient+" esta desbloqueado");
                        
                    }else if(mensaje.equals("APAGAR")){
                        masterController.apagarPC();
                    }else if(mensaje.equals("REINICIAR")){
                        masterController.reiniciarPc();
                    }else if(mensaje.equals("CANCELAR")){
                        masterController.cancelar();
                    }     
    }
    
    @Override
    public void run() {
        super.run();
        connect();
    }
    
}


