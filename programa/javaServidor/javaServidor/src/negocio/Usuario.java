package negocio;

import datos.Archivo;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Usuario {
    private int carac[] = new int[4];
    private String campo[] = new String[4];
    private String nombreFile;
    private String directorio;
    public String regUsuario[] = null;
    
    public Usuario() throws IOException{
        //Cantidad de caracteres
        carac[0] = 6;   //id
        carac[1] = 20; //nombre
        carac[2] = 25; //contraseña
        carac[3] = 25; //fecha

        //llenamos los campos
       for(int i = 0; i < campo.length; i++){
           campo[i] = "";
       }
        directorio = "Documentos/Administrado/";
        nombreFile = "Usuario.txt";
    }
    public String nuevoRegistro(String nomUsuario, String password) throws IOException{
        Archivo oArch = new Archivo(); 
        campo[1] = nomUsuario;
        campo[2] = password;
        campo[3] = obtenerFecha();
        
        
        oArch.nombreFile = nombreFile;
        oArch.directorio = directorio;
        oArch.crearDirectorio();
        boolean val = oArch.nuevoRegistro(campo, carac);
        if(val){
            String direccion = "Documentos/Usuario/";
            oArch.directorio = direccion + nomUsuario;
            oArch.crearDirectorio();
            return "Se creo su Usuario";
        }
        return "No se pudo realizar la sentencia";
    }
    public boolean validarUsuario(String nombre){
        if(!nombre.equals(" ")){
            Archivo oArch = new Archivo();
            oArch.nombreFile = directorio + nombreFile;
            int val = oArch.buscarCampo(nombre, carac);
            switch(val){
                case -1:
                    return false;
                case 0:
                    return false;
                case 1:
                    return true;
            }
        }
        return false;
    }
    public int crearCarpeta(String dic){
        Archivo oArch = new Archivo();
        oArch.directorio = dic;
        int val = oArch.crearDirectorio();
        return val;
    }
    public String obtenerFecha(){
        DateTimeFormatter fecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String sFecha = fecha.format(LocalDateTime.now());
        return sFecha;
    }
}
