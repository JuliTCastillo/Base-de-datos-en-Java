import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Sentencia;

public class Hilo extends Thread {
    static Semaphore usuario = new Semaphore(3);
    private DataInputStream vin;
    private DataOutputStream vout;
    private String vmsg;
    
    Hilo(DataInputStream in, DataOutputStream out) {
        vin = in;
        vout = out;
    }

    public void run() {
        try {
            realizarAccion();
        } 
        catch (InterruptedException ex) {
            Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void realizarAccion() throws IOException, InterruptedException{
        
        while(true){
            try {
                usuario.acquire();
                vmsg = vin.readUTF().toUpperCase(); //pasamos el mensaje a mayuscula
                if(vmsg.equals("EXIT")){ //Si el mensaje es EXIT
                    break; //Salimos del while
                }
                
                Sentencia os = new Sentencia(); //instanciamos la clase sentacia
                
                String[] palabraClave = vmsg.split("\\s+"); //Separamos las palabra 
                String rta = os.validarSentencia(palabraClave); //validamos 
                vout.writeUTF(rta); //Le enviamos la respuesta de la consulta al cliente 
                
            } 
            catch (IOException ex) {
                Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        usuario.release();
    }
}
