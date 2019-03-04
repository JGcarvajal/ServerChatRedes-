/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesServ;
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
public class Autenticacion  extends HttpServlet {
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

        

        Utilidades utilidades = new Utilidades();
        
        String respuesta="";
        
        if (request.getRequestURI().equals("/autenticacion")){
          String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");
            respuesta = utilidades.login(usuario, clave);
          
        }
         if(request.getRequestURI().equals("/LogOut")){
             Integer id=Integer.parseInt(request.getParameter("id"));
         String token=request.getParameter("token");
         respuesta=utilidades.logOut(id, token);
     }
 
   response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(respuesta);
       
    }
}
