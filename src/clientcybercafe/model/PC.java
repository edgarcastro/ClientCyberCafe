/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientcybercafe.model;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

/**
 *
 * @author Luis David SAHER
 */
public class PC {
    JFrame jframe=null;
    public PC(JFrame jframe){
        this.jframe=jframe; //Se recibe una instacia de la clase JFrame
                            //y se trabaja con la misma insatancia
    }
    
 /**
 * ejecuta una tarea cada "n" tiempo
 * Para evitar que el usuario utilice las teclas (WINDOWS + D)(TAB) y asi perder el foco
 * de la aplicación, cada 50 milisegundos se envia el JFrame al frente y se cambia su propiedad a maximizado
 */
    public void block()
    {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate( 
            new Runnable() 
            {
                @Override
                public void run() {                   
                    front(); 
                }
            }, 500, 50 , TimeUnit.MILLISECONDS ); //comienza dentro de 1/2 segundo y luego se repite cada N segundos
        jframe.setVisible(true);
    }

    /**
 * 
 */
    private void front()
    {
        jframe.setExtendedState( JFrame.MAXIMIZED_BOTH );//maximizado
        jframe.toFront();
    }
    

    
    public void unBlock(){
        jframe.dispose();
    }
    
    //Ejecuta los comandos (apagar, reiniciar) o cancecela los mismos
    //dependiendo de lo que resiva como parametro
    private void exec(String cmd){
        try { 
            Runtime.getRuntime().exec(cmd); 
        }catch (IOException e) { 
            System.out.println("Failed");         
        } 
    }
    
    public void apagar(){
        exec("shutdown -s -t 30");//apagar en 30 segundos
    }
    
    public void reiniciar(){
        exec("shutdown -r -t 30");//reinicar en 30 segundos
    }
    
    public void cancelar(){
        exec("shutdown -a");//cancela cualquier operacion de apagado o reinicio
    }
    
    
}
