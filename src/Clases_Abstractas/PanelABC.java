/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases_Abstractas;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import Clases_Simples.Conexion;

/**
 *
 * @author HP_25
 */
public abstract class PanelABC extends JPanel {
    
    //VARIABLES PARA ATRAPAR ERRORES
    protected boolean seCargaronDatos = false;
    protected boolean fueModificado = false;
    protected String mensajeError = "";
    //FIN 
    
    
    //MÉTODOS ABSTRACTOS, DEBE HACERLES OVERRIDE
    //PARA QUE RETORNEN LO QUE DICE EL METODO
    //Los hicimos protected para que tenga modularidad 
    //Poner el nombre de lo que pide, los métodos concretos los usan
    protected abstract String getNombreTabla();
    protected abstract String getNombreCampoID();
    
    protected abstract String[] getNombresColumnas();
    //No se incluye el ID

    //Poner los nombres de todos los campos en estos tres
    protected abstract boolean validarCampos();
    protected abstract void limpiarCampos();
    protected abstract Object[] obtenerValoresDeCampos();
    //usamos object porque recibe distintos tipos de datos
    //FIN DE LOS OVERRIDES
    
    //METODOS CONCRETOS, NO SE TOCAN
    //Métodos ABC
    protected boolean ejecutarSQL(String sql) {
    try (Connection con = Clases_Simples.Conexion.getConexion();
         Statement stmt = con.createStatement()) {
        
        int filasAfectadas = stmt.executeUpdate(sql);
        return filasAfectadas > 0; // ← CLAVE: Verifica que ALGO cambió
        
    } catch (SQLException e) {
        mensajeError = "Error SQL: " + e.getMessage();
        return false;
    }
   }
    
    
   
    
   
 
    
    //Métodos auxiliares
    //Estos metodos usan funciones incluidas en Java
    protected void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    protected boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, 
            mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
    }




   
