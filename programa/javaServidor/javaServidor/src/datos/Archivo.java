package datos;

import java.io.*;
import java.net.UnknownHostException;

public class Archivo {
    public String directorio = "";
    public String nombreFile = "";
    public boolean crearCarpeta = false;
    public boolean espacio = false; 
    
    public boolean nuevoRegistro(String[] vcampos, int[] n){
        //Instaciamos las siguiente clases para escribir en el archivo
        FileWriter fw = null; BufferedWriter bw = null;
        try{
            //Le asignamos al campo[0] una id 
            vcampos[0] = nuevaId(vcampos, n);
            System.out.println(vcampos[0]);
            String carpeta = directorio+nombreFile;
            //instanciamos la clase e indicamos true para que no se sobreescriba lo que ingresamos
            fw = new FileWriter(carpeta, true);
            bw = new BufferedWriter(fw);
            //Realizamos un for para escribir los datos ingresados
            for(int i = 0; i < vcampos.length; i++){
                System.out.println("Campos: " + vcampos[i]);
                //Le asignamos un limite de caracteres a cada campo
                String carac = "%-"+String.valueOf(n[i])+"s";
                //Le asignamos nuevamente al vector de campo ya con su limite de caracteres
                vcampos[i] = String.format(carac, vcampos[i]);
                //Lo escribimos en el archivo txt
                bw.write(vcampos[i]);
            }
            if(espacio){
                bw.write("\n");
            }
            //Cerramos la clase
            bw.close(); fw.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }return false;
    }
    public String nuevaId(String[] campos, int [] n){
        FileReader fr;
        
        try{
            String carpeta = directorio + nombreFile;
            File arch = new File(carpeta); 
            if(!arch.exists()){ //Si no exite el archivo lo crea
                arch.createNewFile();
            }
            if(arch.length() == 0){ //si el archivo se encuntra vacio
                return "1"; //devolvemos 1, ya que serìa nuestra primera id
            }
            else{
                fr = new FileReader(carpeta);
                
                int reader = fr.read(); //Se lee todo el archivo
                int espacio = 0; //Se guardara el espacio que ocupa cada registro 
                String id = ""; //Se guardara la id del registro
                int repeticiones=0; //Contara cada caracter que se lee 
                for(int i = 0; i< n.length; i++){
                    espacio = espacio + n[i]; //Guardamos el espacio de cada registro
                }
                String txt = ""; char caracter;
                while(reader != -1){ //Si llega a -1 quiere decir que llego al final del archivo
                    caracter = (char)reader;
                    txt = txt + String.valueOf(caracter);
                    reader = fr.read();
                    repeticiones+=1;
                    if(repeticiones == n[0]){ //si la repeticio es igual n[0](limite de caractere de id) 
                        id = txt; //Se guarda la id
                        System.out.println("dentro del if: "+id);
                    }//Si repeticiones es igual espacio significa que se leyo un registro de dato completo
                    if(repeticiones == espacio){ 
                        System.out.println(txt);
                        txt=""; //Vaciamos la variable para leer el siguiente registro
                        repeticiones = 0; //declaramos de nuevo 0
                    }
                }
                System.out.println("afuera del if: "+id); 
                if(convertirInt(id.trim())){ //Si se puede convertir en un int 
                    return String.valueOf(Integer.valueOf(id.trim())+1);
                }
                else{ //Si no puede esta leyendo una entidad
                    return "1"; //devuelve 1
                }
            }        
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "nada";
    }
    public int buscarCampo(String campo, int[] n){
        FileReader fr = null;
        BufferedReader br = null;
        try{
            File arch = new File(nombreFile);
            if(!arch.exists()){ //Si el archivo no existe
                return -1;
            }
            fr = new FileReader(nombreFile);
            br = new BufferedReader(fr);
            String lectura; int espacio = 0;
            for(int i=0; i < n.length; i++){ espacio = espacio + n[i];}
            lectura = br.readLine(); //Leamos el archivo

                int cant = lectura.length()/espacio;
                String[] registro = new String[cant];
                //Separa los registros
                for(int i = 0; i < registro.length; i++){
                    registro[i]= lectura.substring(0, espacio);
                    lectura = lectura.substring(espacio);
                }
                for(int i=0; i< registro.length; i++){
                    String reg = registro[i]; //guardamos el registro en una variable

                    for(int x=0; x < n.length; x++){
                        String leer = reg.substring(0, n[x]); //leemos el campo segun el tamaño de la "tabla"
                        reg = reg.substring(n[x]); //eliminamos el tamaño del campo
                        if((leer.trim()).equals(campo)){
                            return 1;
                        }
                    }
                }
            return 0;
        }
        catch(Exception e){
            e.printStackTrace();
        }
            return 0;   
    }
    public int crearDirectorio(){
        File carpeta = new File(directorio); //Pasamos la dirección de nuestro directorio
        if (carpeta.exists()==false) { //si las carpeta no exiten
            if (carpeta.mkdirs()) {
                System.out.println("Directorio creado");
                return 1;
            } 
            else {
                System.out.println("Error al crear directorio");
                return 2;
            }
        }
        else{
            System.out.println("La carpeta existe");
            return 3;
        }
    }
    public boolean crearTabla(String[] vcampos, int[] n){
        try{
            File arch = new File(nombreFile); 
            if(!arch.exists()){ //Si no exite el archivo lo crea
                arch.createNewFile();
            }
            FileWriter fw = new FileWriter(nombreFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            //Realizamos un for para escribir los datos ingresados
            for(int i = 0; i < vcampos.length; i++){
                System.out.println("Campos: " + vcampos[i]);
                //Le asignamos un limite de caracteres a cada campo
                String carac = "%-"+String.valueOf(n[i])+"s";
                //Le asignamos nuevamente al vector de campo ya con su limite de caracteres
                vcampos[i] = String.format(carac, vcampos[i]);
                //Lo escribimos en el archivo txt
                bw.write(vcampos[i]);
            }
            bw.write("\n");
            //Cerramos la clase
            bw.close();
            fw.close();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("No se pudo crear la tabla");
        }
        return false;
    }
    public int obtenerColumnas(){
        try{
            File arch = new File(nombreFile); 
            if(!arch.exists()){ //La tabla no existe
                return -1;
            }
            FileReader fr = new FileReader(nombreFile);
            BufferedReader br = new BufferedReader(fr);
            String txt = ""; char caracter;
            String linea = br.readLine();
            System.out.println(linea);
            String[] columna = linea.split("\\s+");

            return columna.length;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public int[] obtenerTamaño(int tamanio){
        int[] colTamanio = new int[tamanio]; 
        try{
            File arch = new File(nombreFile); 
            FileReader fr = new FileReader(nombreFile);
            BufferedReader br = new BufferedReader(fr);
            
            String linea = br.readLine();
            String txt = ""; int index = 0;
            int i;
            for (i=0; i < linea.length()-1; i++) {
                if(linea.charAt(i) == 32){ //Si se encuentra un espacio
                    //Verificamos que encuentre otro espacio
                    if(linea.charAt(i+1) == 32 || (linea.charAt(i) >= 65 && linea.charAt(i) <= 90)){ 
                        txt = txt + String.valueOf(linea.charAt(i));
                    }
                    else{
                        if(index <= colTamanio.length){
                            colTamanio[index] = txt.length()+1;
                            txt = "";
                        }
                        index+=1;
                    }
                }
                else if((linea.charAt(i) >= 65 && linea.charAt(i) <= 90)){ //Si se encuentra una letra
                    //Verificamos que encuentre otro letra 
                    if((linea.charAt(i+1) >= 65 && linea.charAt(i+1) <= 90) || linea.charAt(i+1) == 32){ 
                        txt = txt + String.valueOf(linea.charAt(i)); //La guardamos 
                    }
                }
            }
            colTamanio[index] = txt.length()+1;
            return colTamanio;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private boolean convertirInt(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }
    public String verTabla(){
        try{
            File arch = new File(nombreFile); 
            if(!arch.exists()){ //La tabla no existe
                return "la tabla no existe"; //No se puede visualizar ninguna tabla
            }
            FileReader fw = new FileReader(nombreFile);
            BufferedReader bw = new BufferedReader(fw);
            
            String contenido; String tabla = "";
            
            while((contenido = bw.readLine()) != null){
                tabla = tabla + contenido + "\n";
            }
            //Cerramos la clase
            bw.close();
            fw.close();
            return tabla;
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("No se pudo crear la tabla");
        }
        return "ocurrio un error";
    }
    public String eliminarTabla(){
        try{
            File archivo = new File(nombreFile);
            boolean estatus = archivo.delete();
            if (!estatus) {
                return "Error no se ha podido eliminar el  archivo";
            }
            else{
                return "Se ha eliminado la Tabla exitosamente";
            }
        }
        catch(Exception e){
            
        }
        return "no se encontro la Tabla";
    }
}
