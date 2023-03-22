
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class ClienteHilo extends Thread{
    private DataInputStream vin;
    private DataOutputStream vout;
    
    public ClienteHilo(DataInputStream in, DataOutputStream out){
        vin = in;
        vout = out;
    }
    
    public void run(){
        
    }
}
