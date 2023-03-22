package negocio;

import datos.Archivo;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sentencia {
    String sintaxis[]=null;
    String sentencia[]=null;
    String bdd;
    String tabla;
    String nomUser;
    String password;
    String columna[] = null;
    int tamanio[] = null;
    String directorio = "";
    
    public Sentencia() throws IOException{
        File arch = new File("Documentos/Sintaxis/Sentencia.txt");
        if(!arch.exists()){ //Si el archivo no existe
            arch.createNewFile(); //Lo crea
        }
    }
    
    public int cantidadRegistro(){
        FileReader fr = null;
        BufferedReader br = null;
        try{
            fr = new FileReader("Documentos/Sintaxis/Sentencia.txt"); //Indicamos el archivo de texto 
            br = new BufferedReader(fr); //Leemos todo el registro
            String txt=""; int reg=0;
            while((txt = br.readLine()) != null){ //Leamos cada linea
                reg+=1; //Contamos cada linea del archivo
            }
            llenarSintaxis(reg);
            return reg;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public void llenarSintaxis(int i){
        FileReader fr = null;
        BufferedReader br = null;
        try{
            fr = new FileReader("Documentos/Sintaxis/Sentencia.txt"); //Indicamos el archivo de texto 
            br = new BufferedReader(fr); //Leemos todo el registro

            sintaxis = new String[i]; //Le damos el tamaño al vector
            
            String texto=""; int index=0;
            
            while((texto = br.readLine()) != null){ //leamos la lineas del archivo
                if(index < sintaxis.length){ //Hacemos una condición para no se soprepase al tamaño del vector
                    sintaxis[index] = texto; //Guardamos la sintaxis en un vector
                    index+=1;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
  
    public String validarSentencia(String consulta[]){
        try {
            int i = consulta.length-1; //Guardamos el tamaño del vector
            if((consulta[i]).equals(';')){ //Preguntamos si es igual a un ;
                return "Se esperaba un ;"; //Sino lo es la sentencia no es validad
            }
            cantidadRegistro();
            int sent = separarPalabras(consulta); //Separamos la consulta del cliente
            Usuario oUser = new Usuario();
            boolean usuario;
            System.out.println("Numero de sentencia " + sent);
            switch(sent){ //Sent guarda el numero de la sintaxis
                case 0: //El 0 se conecta un usuario
                    /*NO SE USA*/
                    break;
                case 1: //El 1 Se crea un usuario
                    usuario = oUser.validarUsuario(nomUser);
                    if(usuario == false){
                        String msg = oUser.nuevoRegistro(nomUser, password);
                        return msg;
                    }
                    return "El nombre del usuario no es valido, eliga otro";
                case 2: //El 2 crea tabla en una base de datos
                    usuario = oUser.validarUsuario(nomUser);
                    if(usuario){
                        Tabla oTabla = new Tabla(nomUser);
                        String file = bdd + "/" + tabla + ".txt";
                        oTabla.crearTabla(columna, tamanio, file );
                        columna = null;
                        tamanio = null;
                    }
                    else{
                        return "No existe el usuario indicado";
                    }
                    break;
                case 3: //El 3 crea una base de datos
                    usuario = oUser.validarUsuario(nomUser);
                    if(usuario){
                        String dic = "Documentos/Usuario/" + nomUser + "/" + bdd;
                        if(oUser.crearCarpeta(dic)!=3){
                            return "Se creo la base de datos con exito!";
                        }
                        else{
                            return "No puede haber base de datos con el mismo nombre";
                        }
                    }
                    break;
                case 4: //El 4 inserta los datos en la tabla
                    Tabla oTabla = new Tabla(nomUser);
                        String file = bdd + "/" + tabla + ".txt";
                        String msg = oTabla.validarCampos(columna, file);
                        columna = null;
                        tamanio = null;
                        return msg;
                case 5: //El 5 seleccion la tabla para verla
                    file = bdd + "/" + tabla + ".txt";
                    oTabla = new Tabla(nomUser);
                    return "Tabla " + tabla + "\n" + oTabla.verTabla(file);
                case 6: //El 6 elimina una tabla
                    file = bdd + "/" + tabla + ".txt";
                    oTabla = new Tabla(nomUser);
                    return oTabla.elimnarTabla(file);
                case -1:
                    return "La sentencia no es correcta";
                case -2:
                    return "Indique el tamaño de su tabla correctamente \nNo se aceptan palabras o letras";
            }
            return "se termino";
        } catch (IOException ex) {
            Logger.getLogger(Sentencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Hubo un problema";
    }
    
    public int separarPalabras(String palabra[]){
        int col=0; int tam = 0; //Numero de columna - numero de elemento del tamaño de las columnas
        int ubicacion = 0;
        for(int i=0; i < sintaxis.length; i++){
            int cant = 0; int a= 0;
            String pal[] = sintaxis[i].split("\\s+"); //Separamos la sintaxis por palabra
            while(cant!=palabra.length){ //Mientra nos nos pasemos del tamaño limite de la consulta de cliente
                switch(pal[cant]){ //según la palabra de la sintaxis  
                    case "/*nombreDeUsuario*/": //Se esta indicando el nombre de un usuario
                        nomUser = palabra[a]; //Guardamos el dato en una variable global
                        cant+=1;
                        a+=1;
                        break;
                    case "/*contraseña*/": //Información del usuario
                        password = palabra[a]; //Lo guardamos en una variable global
                        cant+=1;
                        a+=1;
                        break;
                    case "/*nombreDeLaTabla*/": //Se indica el nombre de una tabla
                        tabla = palabra[a]; //Lo guardamos en una variable global
                        //Crear tabla
                        cant+=1;
                        a+=1;
                        break;
                    case "/*tamaño*/":
                        a = ubicacion;
                        int index = ubicacion; //Le damos a indix el mismo valor de a
                        while(!palabra[a].equals(")")){ //Ponemos la cantidad de columnas que tenemos
                            if(palabra[a+1].equals(")") || palabra[a+1].equals(",")){
                                tam+=1;
                            }
                            a+=1;
                        }
                        tamanio = new int[tam]; int c=0; int t=0; //declaración de variables
                        while(!palabra[index].equals(")")){ //Mientras que en la consulta del cliente no se leea un )
                            if(palabra[index+1].equals(",") || palabra[index+1].equals(")")){
                                if(convertirInt(palabra[index])){
                                    int valor = Integer.valueOf(palabra[index]);
                                    tamanio[t] = valor;
                                    t+=1;
                                }
                                else{
                                    return -2;
                                }
                            }
                            index+=1;
                        }
                        cant+=1;
                        break;
                    case "/*col,col*/": //Se indica las columnas de una tabla
                        ubicacion = a;
                        index = a; //Le damos a indix el mismo valor de a
                        if(i == 4){
                            while(!palabra[a].equals(")")){ //Ponemos la cantidad de columnas que tenemos
                                if(palabra[a+1].equals(")") || palabra[a+1].equals(",")){ //Comparamos si es igual a una , o )
                                    col +=1; //Guardamos la cantidad de columnas que se encontraron
                                }
                                a+=1;
                            }
                            columna = new String[col]; //Le asigamos a un vector la cantidad de columnas que tenemos
                            c=0; t=0;
                            while(!palabra[index].equals(")")){ //Mientras que en la consulta del cliente no se leea un )
                                if(palabra[index+1].equals(")") || palabra[index+1].equals(",") ){ //Preguntamos la palabra es igual , o )
                                    columna[c] = palabra[index]; //Guardamos el nombre de la columna
                                    c+=1;
                                }
                                index+=1;
                            }
                            cant+=1;
                        }
                        if(i == 2){
                            while(!palabra[a].equals(")")){ //Ponemos la cantidad de columnas que tenemos
                                if(palabra[a+1].equals(":")){ //Comparamos si es igual a una , o )
                                    col +=1; //Guardamos la cantidad de columnas que se encontraron
                                }
                                a+=1;
                            }
                            columna = new String[col]; //Le asigamos a un vector la cantidad de columnas que tenemos
                            c=0; t=0;
                            while(!palabra[index].equals(")")){ //Mientras que en la consulta del cliente no se leea un )
                                if(palabra[index+1].equals(":")){ //Preguntamos la palabra es igual , o )
                                    columna[c] = palabra[index]; //Guardamos el nombre de la columna
                                    c+=1;
                                }
                                index+=1;
                            }
                            cant+=2;
                        }
                        break;
                    case "/*nombreDeBaseDeDatos*/": //Indicamos el nombre de una base de datos
                        bdd = palabra[a]; //Lo guardamos en una variable global
                        cant+=1;
                        a+=1;
                        break;
                    case ";": //Si se lee un ; se termino la sentencia
                        System.out.println("Se termino la sentencia " + i);
                        return i; //Mandamos el numero de la sintaxis 
                    default:
                        if(pal[cant].equals(palabra[a])){
                            //System.out.println("Entro al if de pal " + pal[cant] + " y de palabra " + palabra[a]);
                            cant+=1;
                            a+=1;
                        }
                        else{
                            //System.out.println("No entro al if de pal " + pal[cant] + " y de palabra " + palabra[a]);
                            cant = palabra.length; //La palabra no coinsiden pasamos al siguiente registro
                        }
                        break;
                }      
            }
        }
        return -1;
    }
    
    public boolean convertirInt(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }
}