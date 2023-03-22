package presentacion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Cliente extends JFrame implements ActionListener{
    Scanner input = new Scanner(System.in);
    InetAddress ipHost;
    DataInputStream in = null;
    DataOutputStream out = null;
    Socket sv  = null;
    
    private JMenu meArchivo, meConexion, meAyuda;
    private JMenuItem miArcGuardar, miArcGuardar2, miArcAbrir, miArcCerrar, miConIniciar, miConCerrar,miAyu;
    private JToolBar tbHerramienta;
    private JTextField txtusuario;
    private JButton btnrun;
    private JTextArea txtScript;
    private JScrollPane stxtScript;
    private JMenuBar mBarra;
    
    String u;
    
    public Cliente() throws UnknownHostException, IOException{
        //input.useDelimiter("\n");
        ipHost = InetAddress.getLocalHost();
        sv = new Socket(ipHost.getHostAddress(), 5000);
        this.setVisible(true);
        setSize(800, 600); //tamaño
        setTitle("Base de datos"); //Titulo
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Si se preciona la X se cierra el programa por completo
        setLocationRelativeTo(null);
        //Desarrollo de la barra de herramienta
        mBarra = new JMenuBar(); //Declaramos la barra de herramienta
        //Declaramos a los subMenu de la barra de herramienta
        meArchivo = new JMenu("Archivo");
            //Añadimos los distintos items en el menu de archivo
            miArcGuardar = new JMenuItem("Guardar archivo");
            miArcGuardar2 = new JMenuItem("Guardar como...");
            miArcAbrir = new JMenuItem("Abrir archivo");
            miArcCerrar = new JMenuItem("Cerrar archivo");
            meArchivo.add(miArcGuardar);
            meArchivo.add(miArcGuardar2);
            meArchivo.add(miArcAbrir);
            meArchivo.add(miArcCerrar);
        meConexion = new JMenu("Conexion");
            //Añadimos los distintos items en el menu Conexion
            miConIniciar = new JMenuItem("Iniciar usuario");
            miConCerrar = new JMenuItem("Cerrar usuario");
            meConexion.add(miConIniciar);
            meConexion.add(miConCerrar);
        meAyuda = new JMenu("Ayuda");
            //Añadimos los items en el menu de ayuda
            miAyu = new JMenuItem("Sintaxsis");
            meAyuda.add(miAyu);
        
        mBarra.add(meArchivo);
        mBarra.add(meConexion);
        mBarra.add(meAyuda);
        
        setJMenuBar(mBarra);//Añadimos la barra de herramienta en la ventana
        
        //Creamos nuestra barra de herramientas
        tbHerramienta = new JToolBar();
        txtusuario = new JTextField("Servidor en linea");
        btnrun = new JButton("run");
        txtusuario.setMaximumSize(new Dimension(200, 50));
        txtusuario.setHorizontalAlignment(JTextField.CENTER);
        txtusuario.setEditable(false);
        
        tbHerramienta.add(txtusuario);
        tbHerramienta.add(Box.createHorizontalStrut(10));
        tbHerramienta.add(btnrun);
        //Desarrollo de interfaz
        Container contenedor = getContentPane();
        
        contenedor.setLayout(new BorderLayout());
        
        txtScript = new JTextArea();
        stxtScript = new JScrollPane(txtScript); //Ponemos el scroll al Area del Texto
        
        //Ubicación de los controles en la ventana principal
        contenedor.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        contenedor.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        contenedor.add(stxtScript, BorderLayout.CENTER);
        contenedor.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        contenedor.add(tbHerramienta, BorderLayout.NORTH);
        
        in = new DataInputStream(sv.getInputStream());
        out = new DataOutputStream(sv.getOutputStream());
        
        
        btnrun.addActionListener(this);
        miConIniciar.addActionListener(this);
    } 
    public void mensaje(String txt){
        System.out.println("mensaje del servidor: " + txt);
        JOptionPane.showMessageDialog(null, txt);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnrun){
            try {
                String mensaje = txtScript.getText();
                out.writeUTF(mensaje);
                if(!mensaje.equals("EXIT")){
                    mensaje(in.readUTF());
                }
                else{
                    dispose();
                    System.exit(0);
                }
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
     }
    
    public static void main(String[] args) throws IOException{
        new Cliente();
    }

}
