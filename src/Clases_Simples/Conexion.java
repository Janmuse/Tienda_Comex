/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package Clases_Simples;

/**
 *
 * @author HP_25
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

public static Connection con;

public static Connection getConexion() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/tienda_pinturas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        "root",
        "zapato123"
    );
    
}
}


