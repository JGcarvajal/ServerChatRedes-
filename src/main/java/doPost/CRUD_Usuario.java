/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doPost;
import ConexionesServ.*;
import ChatServ.Usuario;
import HerramientasServ.Utilidades;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
/**
 *
 * @author jgcar
 */
public class CRUD_Usuario  extends HttpServlet {
     /**
     * protected void doGet(HttpServletRequest request, HttpServletResponse
     * response) throws ServletException, IOException {
     * System.out.println("HttpServletRequest : " + request);
     * System.out.println("request.getMethod() : " + request.getMethod());
     * System.out.println("request.getQueryString() " +
     * request.getQueryString()); response.setContentType("application/json");
     * response.setStatus(HttpServletResponse.SC_OK);
     * response.getWriter().println("{ \"status\": \"ok\"}");
    }
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Parametro recibido = " + request.getParameter("usuario"));
        System.out.println("Parametro recibido = " + request.getParameter("clave"));
        
        String nombre =request.getParameter("nombre");
        String correo =request.getParameter("correo");
        String uss =request.getParameter("usuario");
        String clave =request.getParameter("clave");
        Utilidades utilidades = new Utilidades();
        
        String respuesta="-1";
        
     Usuario usuario=new Usuario(nombre, correo, uss, clave);
       
     if (request.getRequestURI().equals("/RegistrarUsuario")){
        respuesta=utilidades.registrarUsuario(usuario);
         System.out.println("Respuesta de ingreso "+respuesta);
     }
        
        
        
        
        
        System.out.println("Autenticado");
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(respuesta); 
        
 

       
    }
}
