/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mega_practica;

import java.sql.*;
/**
 *
 * @author EAG
 */
public class Conexion {
    //Atributos
    private    String url = "jdbc:oracle:thin:@//localhost:1521/xe";
    private    String username = "aula";
    private    String pass = "aula";
    private    Connection con;
    //Constructor
    public Conexion() throws ClassNotFoundException, SQLException{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DriverManager.getConnection(url,username,pass);
        con.setAutoCommit(true);
        System.out.println("Conexion Oracle establecida.\n");
    }
    
    public ResultSet select(String a) throws SQLException{
        Statement s = con.createStatement();
        return s.executeQuery(a);
    }
    public void insert(String a) throws SQLException{
        Statement s = con.createStatement();
        s.executeUpdate(a);
    
        //con.commit();
    }
    public void cerrar() throws SQLException{
        con.close();
    }
    
}
