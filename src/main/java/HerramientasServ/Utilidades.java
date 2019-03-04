/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HerramientasServ;

import ChatServ.Comandos;
import ChatServ.Usuario;
import ConexionesServ.Conexion;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Array;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.List;
import org.sql2o.Connection;

/**
 *
 * @author gusta
 */
public class Utilidades {
    

    public static HashMap<String, String> parametros(String mensaje) {
        HashMap<String, String> res = new HashMap<>();
        Usuario usuario;

        //System.out.println("[Parametros] str " + mensaje);
        StringTokenizer st = new StringTokenizer(mensaje, "||");
        
        String accion = st.nextToken();
        res.put("tipo", accion);
        res.put("token", st.nextToken());
        res.put("origen", st.nextToken());
        res.put("destino", st.nextToken());

        if (Comandos._MSJ.equals(accion)) {
            res.put("msj", st.nextToken());
        } else if (Comandos._ARCHIVO.equals(accion)) {
            res.put("n_archivo", st.nextToken());
            res.put("base64", st.nextToken());
        } else if (Comandos._LISTAUSUARIOS.equals(accion)) {
            res.put("listado", st.nextToken());
        } else if (Comandos._SOLICITUDES.equals(accion)) {
            res.put("listado", st.nextToken());}
//        }else if (Comandos._REG_USUARIO.equals(accion)) {
//            res.put("listado", st.nextToken());
//            ///#REG_USUARIO||Token||YO||SERV||USUARIO           
//            
//            String nombre="";
//           String apodo="";
//           String correo="";
//           String uss="";
//           String clave="";
//            
//            usuario =new Usuario(nombre, apodo, correo, uss, clave);
//            
//            registrar
//        }else if (Comandos._ENVIAR_SOLICITUD.equals(accion)) {
//            res.put("listado", st.nextToken());
//            ///#REG_USUARIO||Token||YO||SERV||USUARIO           
//            
//            String nombre="";
//           String apodo="";
//           String correo="";
//           String uss="";
//           String clave="";
//            
//            usuario =new Usuario(nombre, apodo, correo, uss, clave);
//            
//            registrar
//        }

        System.out.println("Parametros Recibidos " + res.size());

        return res;
    }

    public String registrarUsuario(Usuario usuario) {
        int res = -1;
        final String insertQuery
                = "INSERT INTO usuarios (nombre,correo,usuario,clave) "
                + "VALUES (:nombre, :correo,:usuario,:clave)";

        try (Connection con = Conexion.getSql2o().beginTransaction()) {
            res =con.createQuery(insertQuery, true)
                    .addParameter("nombre", usuario.getNombre())
                    .addParameter("correo", usuario.getCorreo())
                    .addParameter("usuario", usuario.getUsuario())
                    .addParameter("clave", usuario.getClave())
                    .executeUpdate()
                    .getResult();
            con.commit();
        }
        return Integer.toString(res);
    }

    public String login(String usuario, String clave) {
        List<Usuario> resultado;
        String ramdon = "-1";
        ramdon = generateRandomText();
        String respuesta="-1";
        try (Connection con = Conexion.getSql2o().open()) {
            final String query
                    = "SELECT id, nombre , apodo , correo, usuario, clave "
                    + "FROM usuarios WHERE usuario = :usuario3 AND clave = :clave";

            resultado = con.createQuery(query)
                    .addParameter("usuario3", usuario)
                    .addParameter("clave", clave)
                    .executeAndFetch(Usuario.class);
            System.out.println("resultado " + resultado.size());
            if (resultado.size() == 1) {
                respuesta="1";
                ingresarAutenticados(resultado.get(0).getId(), ramdon);                
            }
        }
        return respuesta+";"+ ramdon;
    }

    public List<Usuario> BuscaUsuario(String usuario) {

        try (Connection con = Conexion.getSql2o().open()) {
            final String query
                    = "SELECT id, nombre , apodo , correo, usuario, clave "
                    + "FROM usuarios WHERE usuario = :usuario3 AND clave = :clave";

            return con.createQuery(query)
                    .addParameter("usuario3", usuario)
                    .executeAndFetch(Usuario.class);
        }

    }

    public int ingresarAutenticados(Integer id, String token) {
        System.out.println("id token "+id+"----"+token);
        try (Connection con = Conexion.getSql2o().open()) {
            final String query
                    = "insert into autenticados (id,token) values(:id,:token)";

            return con.createQuery(query)
                    .addParameter("id", id)
                    .addParameter("token", token)
                    .executeUpdate()
                    .getResult();
        }

    }
     public String validaToken(String token, String id) {

        try (Connection con = Conexion.getSql2o().open()) {
           List<String> usuarios;
            
            final String query
                    = "Select aut.token from autenticados  "
                    + " where token= :token and id= :id";

            usuarios= con.createQuery(query)
                    .addParameter("token", token)
                    .addParameter("id", id)
                     .executeAndFetch(String.class);
            return usuarios.get(0);
        }
        

    }
    
        public Usuario BuscaAutenticados(String usuario, String clave) {

        try (Connection con = Conexion.getSql2o().open()) {
           List<Usuario> usuarios;
            
            final String query
                    = "Select aut.token from autenticados aut inner join usuarios usu on aut.id=usu.id  "
                    + " where usu.usuario= :usuario and usu.clave= :clave";

            usuarios= con.createQuery(query)
                    .addParameter("usuario", usuario)
                    .addParameter("clave", clave)
                     .executeAndFetch(Usuario.class);
            return usuarios.get(0);
        }
        

    }

    public int actualizarUsuario(Usuario usuario) {
        int res = -1;

        final String updateQuery
                = "UPDATE usuario SET :nombre, :apodo,:correo,:usuario,:clave,:token) WHERE id = :id";

        try (Connection con = Conexion.getSql2o().open()) {
            res = con.createQuery(updateQuery, true)
                    .bind(usuario)
                    .executeUpdate()
                    .getResult();
        }

        return res;
    }

    public String logOut(Integer id, String token) {
        int res = -1;
        System.out.println("Logout "+id+token);
        final String updateQuery
                = "delete from autenticados where id=:id and token =:token";

        try (Connection con = Conexion.getSql2o().open()) {
            res = con.createQuery(updateQuery, true)
                    .addParameter("id", id)
                    .addParameter("token", token)
                    .executeUpdate()
                    .getResult();
        }

        return Integer.toString(res);
    }
    
    

    public ArrayList AmigosConectados(String id, String token) {
        ArrayList res = new ArrayList();

        final String updateQuery
                = "SELECT am.id FROM  usuarios us inner join amigos am on us.id=am.id "
                + "where us.conectado=true and us.id=:id";

        try (Connection con = Conexion.getSql2o().open()) {
            con.createQuery(updateQuery, true)
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult();
        }

        return res;
    }
    public String EnviarSolicitud(Integer id,Integer codUsuario,String mensaje ) {
        int res = -1;
        final String insertQuery
                = "INSERT INTO SolicitudesAmistad (id, codUsuario,mensaje) "
                + "VALUES (:id, :codUsuario,:mensaje)";

        try (Connection con = Conexion.getSql2o().beginTransaction()) {
            res = con.createQuery(insertQuery, true)
                    .addParameter("id", id)
                    .addParameter("codUsuario", codUsuario)
                    .addParameter("mensaje", mensaje)
                    .executeUpdate()
                    .getResult();
            con.commit();
            
            System.out.println("Envio solicitud?  "+res);
        }
        return Integer.toString(res);
    }

    public static String generateRandomText() {
        SecureRandom random = new SecureRandom();
        String text = new BigInteger(40, random).toString(32);
        return text;
    }
}
