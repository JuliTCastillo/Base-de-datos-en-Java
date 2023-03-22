package negocio;

import datos.Archivo;

/**
 *
 * @author julie
 */
public class Tabla {
    String directorio = "Documentos/Usuario/";
    public Tabla(String nomUser){
        directorio = directorio + nomUser;
        System.out.println("La dirección del archivo es: " + directorio);
    };
    
    public boolean crearTabla(String[] campo, int[] carac, String file){
        Archivo oArch = new Archivo();
        oArch.nombreFile = directorio + "/" + file;
        System.out.println("Direccion de carpeta " + oArch.nombreFile);
        boolean val = oArch.crearTabla(campo, carac);
        System.out.println("La creación de la tabla es: "+val );
        return val;
    }
    public String validarCampos(String[] campos, String file){
        Archivo oArch = new Archivo();
        oArch.nombreFile = directorio + "/" + file;
        String columna="";
        int cant = oArch.obtenerColumnas();
        if(cant == -1){
            return "La tabla no existe";
        }
        else{
            if(!(campos.length > cant)){
                boolean val = false; 
                int[] entidad = oArch.obtenerTamaño(cant);
                for(int i=0; i < campos.length; i++){
                    if(campos[i].length() < entidad[i]){
                        columna = campos[i];
                        val = true;
                    }
                }
                if(!val){
                    System.out.println("Error con : " + columna);
                    return "sobrepasa el limite de la columna";
                }
                oArch.espacio = true;
                oArch.nuevoRegistro(campos, entidad);
            }
            else{
                return "Hay campos demás";
            }
        }
        return "";
    }
    public String verTabla(String file){
        directorio = directorio + "/" + file;
        System.out.println("directorio " + directorio);
        Archivo oArch = new Archivo();
        oArch.nombreFile = directorio;
        return oArch.verTabla();
    }
    public String elimnarTabla(String file){
        directorio = directorio + "/" + file;
        System.out.println("directorio " + directorio);
        Archivo oArch = new Archivo();
        oArch.nombreFile = directorio;
        return oArch.eliminarTabla();
    }
}
