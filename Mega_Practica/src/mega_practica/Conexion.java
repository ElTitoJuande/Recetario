/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mega_practica;

import java.sql.*;
import java.util.Scanner;
/**
 *
 * @author EAG
 */
public class Conexion {

    //Atributos
    private String url = "jdbc:oracle:thin:@//localhost:1521/xe";
    private String username = "aula";
    private String pass = "aula";
    private Connection con;

    //Constructor
    public Conexion() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DriverManager.getConnection(url, username, pass);
        con.setAutoCommit(true);
        System.out.println("Conexion Oracle establecida.\n");
    }

    public ResultSet select(String a) throws SQLException {
        Statement s = con.createStatement();
        return s.executeQuery(a);
    }

    public void insert(String a) throws SQLException {
        Statement s = con.createStatement();
        s.executeUpdate(a);

        //con.commit();
    }

    public void cerrar() throws SQLException {
        con.close();
    }

    public void puntuacion(Scanner sc, int opc, int cont, Conexion c, ResultSet rs) throws SQLException {
        do {
            System.out.println("*¿Quieres introducir una puntuación? [0] NO [1] SÍ");
            try {
                opc = sc.nextInt();
            } catch (Exception e) { //NO FUNCIONA
                System.out.println("Introduce [0] o [1]");
                //System.out.println(e.getLocalizedMessage());
                opc = -1;
                sc.nextLine();
            }

        } while (opc < 0 || opc > 1);

        if (opc == 1) {
            System.out.println("*Introduce una puntuación: ");
            float puntuacion = sc.nextFloat();

            String consulta_puntuacion = "update recetas set punt_tot= punt_tot+" + puntuacion + ", n_punt=n_punt+1 where ident = " + cont;
            try {
                c.insert(consulta_puntuacion);
                rs.next();

            } catch (SQLException e) {
                System.out.println("Ha fallado la consulta:");
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public void menus(Scanner sc, int eleccion3, String nombre, Conexion c, ResultSet rs, int tipo_normal, int opc, int cont) throws SQLException {

        System.out.println("........................................");
        System.out.println(".............MENU DE BUSQUEDA...........");
        System.out.println("........................................");
        System.out.println("..............POR AUTOR [1].............");
        System.out.println("........POR NOMBRE DE RECETA [2]........");
        System.out.println("............POR ETIQUETAS [3]...........");
        System.out.println("........................................");

        eleccion3 = sc.nextInt();
        sc.nextLine();
        switch (eleccion3) {
            case 1:
                System.out.println("....BUSCADOR POR NOMBRE DE CHEF....");
                nombre = sc.nextLine();
                String consulta_buscador1 = "Select * from recetas where creador='" + nombre + "'";
                rs = c.select(consulta_buscador1);
                while (rs.next()) {
                    System.out.println("[" + rs.getString(1) + "] " + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));  
                }
                if (tipo_normal != 3) {
                      c.puntuacion(sc, opc, cont, c, rs);
                }
                         
                
                break;
            case 2:
                System.out.println("....BUSCADOR POR NOMBRE DE RECETA....");
                nombre = sc.nextLine();
                String consulta_buscador2 = "Select * from recetas where nombre='" + nombre + "'";
                rs = c.select(consulta_buscador2);
                while (rs.next()) {
                    System.out.println("[" + rs.getString(1) + "] " + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));
                }
                if (tipo_normal != 3) {
                      c.puntuacion(sc, opc, cont, c, rs);
                }
                break;
            case 3:
                rs = c.select("select count(*) from etiquetas");
                rs.next();
                System.out.println("....BUSCADOR POR ETIQUETA DE RECETA....");
                while (rs.next()) {
                    System.out.println("[" + rs.getString(1) + "] " + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));
                }
                

                String consul = "select nombre, creador, p_descripcion, ingredientes, descripcion from recetas, rece_eti where recetas.ident = rece_eti.ident and ident_e =";
                int eti = 0;

                for (int i = 0; i < 26 && eti != -1; i++) {
                    System.out.println("Seleccione el ID de la etiqueta que quieras añadir [-1] SALIR");
                    eti = sc.nextInt();

                    if (i == 0) {
                        consul += eti;
                    } else if (eti != -1) {

                        consul = consul + " and recetas.ident in (select r.ident from recetas r, rece_eti where r.ident = rece_eti.ident and ident_e =" + eti + ")";

                    }
                    System.out.println("ETIQUETA: " + eti);

                }
                System.out.println(consul);

                rs = c.select(consul);
                while (rs.next()) {
                    System.out.println("[" + rs.getString(1) + "] " + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));
                }
                if (tipo_normal != 3) {
                      c.puntuacion(sc, opc, cont, c, rs);
                }

                break;
        }

    }
}