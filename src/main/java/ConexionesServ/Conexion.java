/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesServ;

import org.sql2o.Sql2o;

/**
 *
 * @author jgcar
 */
public class Conexion {

    private static Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/ChatRedes2", "redes2", "123456");

    public static Sql2o getSql2o() {
        return sql2o;
    }
}
