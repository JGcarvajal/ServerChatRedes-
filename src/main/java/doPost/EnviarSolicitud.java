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
public class EnviarSolicitud  extends HttpServlet {
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
        System.out.println("Parametro recibido = " + request.getParameter("id"));
        System.out.println("Parametro recibido = " + request.getParameter("codUsuario"));
        System.out.println("Parametro recibido = " + request.getParameter("mensaje"));

        
        Integer id = Integer.parseInt( request.getParameter("id"));
        Integer codUsuario =Integer.parseInt( request.getParameter("codUsuario"));        
        String mensaje =request.getParameter("mensaje");
        
        Utilidades utilidades = new Utilidades();
        
        String respuesta="-1";
        
       
     if (request.getRequestURI().equals("/EnviarSolicitud")){
        respuesta=utilidades.EnviarSolicitud(id, codUsuario, mensaje);
        
        
     }
    
   
        System.out.println("Autenticado");
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(respuesta); 
        
 

       
    }
}
