import negocio.Sentencia;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    String directorio;
    
    public Servidor(){
        try {
            ServerSocket server = new ServerSocket(5000); //Se declara el puerto del servidor
            Socket sv;
            System.out.println("Servidor iniciado"); 
            while(true){
                sv = server.accept(); //Aceptamos al usuario que se conecto
                //Instanciamos las entradas de salida y entrada
                DataInputStream in = new DataInputStream(sv.getInputStream());
                DataOutputStream out = new DataOutputStream(sv.getOutputStream());
                
                //Instanciamos el hilo
                Hilo hiloUno = new Hilo(in, out);
                hiloUno.start();
                                
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args)throws IOException, InterruptedException{
        new Servidor();
    }
    
}
