/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServ;

import ConexionesServ.ServerJetty;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author jgcar
 */
public class Main {

    public static void main(String args[]) throws IOException, Exception {
        
         ServerJetty serverJetty = new ServerJetty();
        serverJetty.start();
        
        Servidor servidor;
        servidor=new Servidor();
       
        
        
    }
}
