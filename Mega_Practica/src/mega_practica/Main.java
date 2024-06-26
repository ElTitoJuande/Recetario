/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mega_practica;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
///////////////////////////////////////////////////////////////////★
/**
 *
 * @author EAG
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Conexion c = new Conexion();

        Scanner sc = new Scanner(System.in, "ISO-8859-1");

        boolean esta = true;

        //Selectores de switch
        int eleccion = 0;
        int eleccion2 = 0;
        int eleccion3 = 0;

        //Datos de los usuarios
        String usuario = "";
        String mail = "";
        String contraseña = "";
        String backup = "";
        int tipo_normal = 3;

        //Datos de las recetas
        String nombre_receta;
        String breve_desc;
        String ingredientes;
        String preparacion;
        ResultSet rs = null;

        //Datos del Buscador
        String nombre = "";
        int opc = 0;
        int cont = 0;

        do {
            System.out.println("-------MENU DE INICIO--------");
            System.out.println(".............................");
            System.out.println("---REGISTRO DE USUARIO [1]---");
            System.out.println("-----INICIO DE SESION [2]----");
            System.out.println("------BUSCAR RECETAS [3]-----");
            System.out.println(".............................");

            eleccion = sc.nextInt();

            sc.nextLine();
            switch (eleccion) {
                case 1:
                    //1ºregistro
                    System.out.println("----------REGISTRO----------");
                    //COMPROBADOR DE NOMBRE
                    do {
                        System.out.println("Introduce el nombre de usario que vas usar.");
                        usuario = sc.next();

                        //Comprobamos que no exista el usuario. Si la consulta devuelve más de 0, se vuelve a pedir el nombre para que no se repita.
                        String consulta_registro = "select count(*) from usuarios where nombre_usu='" + usuario + "'";

                        try {
                            rs = c.select(consulta_registro);
                            rs.next();
                            int resultado = rs.getInt(1);
                            esta = resultado == 1;
                        } catch (SQLException e) {
                            System.out.println("Ha fallado la consulta:");
                            System.out.println(e.getLocalizedMessage());
                        }
                    } while (esta);

                    System.out.println("--------DATO VALIDO----------");

                    //COMPROBADOR DE MAIL
                    do {
                        System.out.println("Introduce el mail que vas usar.");
                        mail = sc.next();

                        //bucle que comprueba el mail con los que hay en la base de datos para ver si esta libre
                        String consulta_registro = "select count(*) from usuarios where mail='" + mail + "'";

                        try {
                            rs = c.select(consulta_registro);
                            rs.next();
                            int resultado = rs.getInt(1);
                            esta = resultado == 1;
                        } catch (SQLException e) {
                            System.out.println("Ha fallado la consulta:");
                            System.out.println(e.getLocalizedMessage());
                        }

                    } while (esta);
                    System.out.println("--------DATO VALIDO----------");
                    //System.out.println(mail);

                    //COMPROBADOR DE CONTRASEÑA
                    sc.nextLine();
                    do {
                        do {
                            System.out.println("Introduce tu contraseña");
                            contraseña = sc.nextLine();

                            System.out.println("Vuelve a introducir la contraseña");
                            backup = sc.nextLine();

                        } while (!(contraseña.equals(backup)));
                    } while (contraseña.length() < 5);

                    //COMPROBADOR DE TIPO
                    tipo_normal = 2;

                    String consulta_meter = "insert into usuarios values('" + usuario + "','" + contraseña + "','" + mail + "','" + tipo_normal + "')";
                    try {
                        c.insert(consulta_meter);
                    } catch (SQLException e) {
                        System.out.println("Ha fallado la consulta:");
                        System.out.println(e.getLocalizedMessage());
                    }

                    break;

                case 2:
                    System.out.println("--------INICIO DE SESION---------");
                    do {
                        System.out.println("Introduce el nombre .");
                        usuario = sc.next();

                        //Comprobamos que exista el usuario. Si la consulta devuelve 0, se vuelve a pedir el nombre.
                        String consulta_registro = "select count(*) from usuarios where nombre_usu='" + usuario + "'";

                        try {
                            rs = c.select(consulta_registro);
                            rs.next();
                            int resultado = rs.getInt(1);
                            esta = resultado == 1;
                        } catch (SQLException e) {
                            System.out.println("Ha fallado la consulta:");
                            System.out.println(e.getLocalizedMessage());
                        }
                    } while (!esta);
                    sc.nextLine();

                    //COMPROBAR QUE LA CONTRASEÑA SEA VALIDA
                    String consulta_contraseña;
                    int res = 0;

                    do {
                        System.out.println("Introduce contraseña");
                        backup = sc.next();
                        //System.out.println(backup);
                        consulta_contraseña = "select count(*) from usuarios where nombre_usu='" + usuario + "' and contraseña='" + backup + "'";
                        rs = c.select(consulta_contraseña);
                        rs.next();
                        res = rs.getInt(1);
                        //System.out.println(res);

                    } while (res == 0);

                    do {
                        System.out.println("-------MENU DE ACCION--------");
                        System.out.println(".............................");
                        System.out.println("---REGISTRA TUS RECETAS [1]---");
                        System.out.println("---MODIFICAR TU RECETA [2]---");
                        System.out.println("----MIRA MAS RECETAS [3]----");
                        System.out.println(".............................");

                        eleccion2 = sc.nextInt();
                        sc.nextLine();
                        switch (eleccion2) {
                            case 1:
                                System.out.println("Inscriba el nombre de su receta");
                                nombre_receta = sc.nextLine();
                                System.out.println("................................................");
                                System.out.println("La receta " + nombre_receta + " añadida por " + usuario);
                                System.out.println("................................................");

                                System.out.println("Describa brevemente su plato en esta seccion");
                                breve_desc = sc.nextLine();

                                System.out.println("Inscriba los nombres de los ingredientes: ");
                                ingredientes = sc.nextLine();

                                System.out.println("Describa el proceso de preparación");
                                preparacion = sc.nextLine();
                                String consulta_meter_receta = "insert into recetas (nombre, creador, p_descripcion, ingredientes, descripcion) values('" + nombre_receta + "','" + usuario + "','" + breve_desc + "','" + ingredientes + "','" + preparacion + "')";
                                int id = 0;
                                try {
                                    c.insert(consulta_meter_receta);
                                    rs = c.select("select ident from recetas where nombre='" + nombre_receta + "'");
                                    rs.next();
                                    id = rs.getInt(1);

                                } catch (SQLException e) {
                                    System.out.println("Ha fallado la consulta:");
                                    System.out.println(e.getLocalizedMessage());
                                }
                                System.out.println(id);
                                System.out.println("añadir etiquetas");
                                String consul = "select * from etiquetas";
                                rs = c.select(consul);

                                do {
                                    while (rs.next()) {
                                        System.out.println(rs.getString(1) + " - " + rs.getString(2));
                                    }
                                    System.out.println("Seleccione el numero de la etiqueta que quieras añadir");
                                    System.out.println("Si no quieres añadir mas seleccione [-1]");
                                    cont = sc.nextInt();
                                    if (cont != -1) {
                                        String consulta_meter_receta_etiqueta = "insert into rece_eti values('" + id + "','" + cont + "')";
                                        try {
                                            c.insert(consulta_meter_receta_etiqueta);
                                        } catch (SQLException e) {
                                            System.out.println("Ha fallado la consulta:");
                                            System.out.println(e.getLocalizedMessage());
                                        }
                                    }
                                } while (cont != -1);
                                break;
                            case 2:
                                System.out.println("MODIFICAR TUS RECETAS");
                                String consulta = "select tipo from usuarios where nombre_usu = '" + usuario + "'";
                                rs = c.select(consulta);
                                rs.next();
                                int resultado = rs.getInt(1);

                                String consul_rece;
                                do {
                                    if (resultado == 1) {
                                        consul_rece = "select * from recetas order by ident asc";
                                    } else {
                                        consul_rece = "select * from recetas where creador = '" + usuario + "' order by ident asc";
                                    }

                                    rs = c.select(consul_rece);
                                    while (rs.next()) {
                                        System.out.println("[" + rs.getString(1) + "] " + rs.getString(2));
                                    }
                                    System.out.println("");
                                    System.out.println("*SELECCIONA EL [ID] DE LA RECETA A MODIFICAR");
                                    System.out.println("*SALIR [-1]");
                                    cont = sc.nextInt();

                                    if (cont != -1) {
                                        opc = -1;
                                        String consulta_ver_receta = "Select * from recetas where ident=" + cont + "";
                                        try {
                                            c.select(consulta_ver_receta);
                                            rs = c.select(consulta_ver_receta);
                                            rs.next();
                                            System.out.println("");
                                            System.out.println("[" + rs.getString(1) + "] " + rs.getString(2));
                                            System.out.println("    [1].Descripción: " + rs.getString(4));
                                            System.out.println("    [2].Ingredientes: " + rs.getString(5));
                                            System.out.println("    [3].Puntuación: " + rs.getString(6));
                                            System.out.println("");
                                            try {
                                                System.out.println("¿Qué quieres modificar?");
                                                opc = sc.nextInt();
                                            } catch (Exception e) { //NO FUNCIONA
                                                System.out.println("Introduce un número válido");
                                                opc = sc.nextInt();
                                            }
                                        } catch (SQLException e) {
                                            System.out.println("Ha fallado la consulta:");
                                            System.out.println(e.getLocalizedMessage());
                                        }

                                        switch (opc) {
                                            case 1:
                                                System.out.println("Introduce una nueva descripción: ");
                                                sc.nextLine();
                                                String descripcion = sc.nextLine();
                                                String consulta_modificar = "update recetas set p_descripcion ='" + descripcion + "' where ident = " + cont;

                                                try {
                                                    c.insert(consulta_modificar);
                                                    rs.next();

                                                } catch (SQLException e) {
                                                    System.out.println(e.getLocalizedMessage());
                                                }
                                                break;

                                            case 2:
                                                System.out.println("Introduce los nuevos ingredientes: ");
                                                sc.nextLine();
                                                ingredientes = sc.nextLine();
                                                consulta_modificar = "update recetas set ingredientes ='" + ingredientes + "' where ident = " + cont;

                                                try {
                                                    c.insert(consulta_modificar);
                                                    rs.next();

                                                } catch (SQLException e) {
                                                    System.out.println(e.getLocalizedMessage());
                                                }
                                                break;

                                            case 3:
                                                System.out.println("Introduce una puntuación: ");
                                                sc.nextLine();
                                                ingredientes = sc.nextLine();
                                                consulta_modificar = "update recetas set ingredientes ='" + ingredientes + "' where ident = " + cont;

                                                try {
                                                    c.insert(consulta_modificar);
                                                    rs.next();

                                                } catch (SQLException e) {
                                                    System.out.println(e.getLocalizedMessage());
                                                }
                                                break;

                                        }
                                    }
                                } while (cont != -1);

                                break;

                            case 3:
                                System.out.println("");
                                System.out.println("LISTA DE RECETAS");

                                c.menus(sc, eleccion3, nombre, c, rs, tipo_normal, opc, cont);

                                break;

                        }
                    } while (eleccion2 != -1);

                    break;
                case 3:
                    tipo_normal = 3;
                    c.menus(sc, eleccion3, nombre, c, rs, tipo_normal, opc, cont);
                    
                    break;
            }

        } while (eleccion != -1);
        c.cerrar();
    }
}
