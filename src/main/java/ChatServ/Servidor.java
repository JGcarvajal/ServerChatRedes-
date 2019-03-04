package ChatServ;

// Java implementation of  Servidor side 
// It contains two classes : Servidor and ClientHandler 
// Save file as Servidor.java 
import HerramientasServ.Utilidades;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import static java.util.Collections.list;

// Servidor class 
/**
 * https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 * https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
 *
 * @author gusta
 */
public class Servidor {

    // Vector to store active clients 
    static Vector<ClientHandler> conectados = new Vector<>();

    // counter for clients 
    static int i = 0;

    public Servidor() throws IOException {
        // server is listening on port 1234 
        ServerSocket ss = new ServerSocket(8000);
        Utilidades utilidad = new Utilidades();
        Socket socket;
        
          

        // running infinite loop for getting 
        // client request 
        while (true) {
            // Accept the incoming request 
            socket = ss.accept();
            List<Usuario> usuario;

            System.out.println("New client request received : " + socket);

            // obtain input and output streams 
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Creating a new handler for this client...");
            //usuario=utilidad.BuscaUsuario
            // Create a new handler object for handling this request. 
            ClientHandler mtch = new ClientHandler(socket, "c" + i, dis, dos, this);

            //inicia el hilo que envia la lista una sola vez para cada usuario               
            mtch.getListaUsuarios().start();
            
            //Iniciar hilo de comprobacion de solicitudes de amistad
            mtch.getSolicitudesAmistad().start();

            // Create a new Thread with this object. 
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // add this client to active clients list 
            conectados.add(mtch);

            // start the thread. 
            t.start();

            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++;

        }
    }

    public static void main(String[] args) {
        try {
            new Servidor();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String listaUsuarios(String usuario) {
        String usuarios = "";
        //TODO ... enviar unicamente usuarios que sean amigos
        for (ClientHandler c : conectados) {
            usuarios += c.getName() + "&&";
        }
        return usuarios;
    }

    public void enviarUsuariosConectados() {
        enviarMensajeTodos("#LISTA_USUARIOS");
    }

    public void enviarSolicitudesAmistad() {
        enviarMensajeTodos("#SOLICITUDES");
    }

    public void enviarMensajeTodos(String tipo) {
        for (ClientHandler c : conectados) {

            String msj = "";

            if ("#LISTA_USUARIOS".equals(tipo)) {
                msj = tipo + "||" + "tokenOK" + "||" + "server" + "||" + c.getName() + "||" + listaUsuarios(c.getName());
            }
            if ("#SOLICITUDES".equals(tipo)){
                //TODO : Cambiar listaUsuarios por Lista de Solicitudes
                 msj = tipo + "||" + "tokenOK" + "||" + "server" + "||" + c.getName() + "||" + listaUsuarios(c.getName());
            }

            c.enviarMensaje(msj);
        }
    }

}

// ClientHandler class 
class ClientHandler implements Runnable {

    private HiloListaUsuarios listaUsuarios;
    private HiloSolicitudesAmistad solicitudesAmistad;
    private String usuario;

    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    // constructor 
    public ClientHandler(Socket s, String name,
            DataInputStream dis, DataOutputStream dos, Servidor ss) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin = true;
        listaUsuarios = new HiloListaUsuarios(ss);
        solicitudesAmistad = new HiloSolicitudesAmistad(ss);

    }

    public void enviarMensaje(String msj) {
        try {
            System.out.println("[enviarMensaje] ....");
            dos.writeUTF(msj);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @Override
    public void run() {

        String mensaje;
        while (true) {
            try {
                // receive the string 
                mensaje = dis.readUTF();
                System.out.println("received en RUN : \n" + mensaje);

                if (mensaje.equals("logout")) {
                    this.isloggedin = false;
                    this.s.close();
                    break;
                }

                // LEGACY DATA: how are you#client 1
                // DATA ACTUAL "#MSJ||FQLSHP||c0||c2||Hola :)"
                StringTokenizer st = new StringTokenizer(mensaje, "||");
                String accion = st.nextToken();
                String token = st.nextToken();
                String origen = st.nextToken();
                String destino = st.nextToken(); 
                String msj = st.nextToken(); 



                //TODO:
                //1. Comprobar si token es valido para origen
                //2. Noticiar a origen si no esta autorizado
                //3. si no es valido: return;
                // search for the recipient in the connected devices list. 
                // conectados is the vector storing client of active users 
                for (ClientHandler mc : Servidor.conectados) {
                if (destino.equals("servidor")){
                   mc.usuario=msj;
                   break;
                }
                    //Si encuentra el cliente ... entonces conectar
                    if (mc.name.equals(destino) && mc.isloggedin == true) {
                        if(Comandos._ERRORMSJ.equals(accion)){
                            mc.dos.writeUTF(mensaje);
                        }else{
                        mc.dos.writeUTF(mensaje);
                        break;
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchElementException nse) {
                System.out.println("Error en la estructura del mensaje ");
            }

        }
        try {
            // closing resources 
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HiloListaUsuarios getListaUsuarios() {
        return listaUsuarios;
    }

    public HiloSolicitudesAmistad getSolicitudesAmistad() {
        return solicitudesAmistad;
    }

}
