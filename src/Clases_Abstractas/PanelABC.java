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
import Interfaces.cargarDatos;
import javax.swing.JTextField;
      
/**
 *
 * @author HP_25
 */
public abstract class PanelABC extends JPanel implements cargarDatos {
    
//VARIABLES PARA ATRAPAR ERRORES
    protected boolean seCargaronDatos = false;
    protected boolean fueModificado = false;
    protected String mensajeError = "";
//FIN 
    
/*-----------------------------------------------
MÉTODOS ABSTRACTOS, DEBE HACERLES OVERRIDE
PARA QUE RETORNEN LO QUE DICE EL METODO

Los hicimos protected para mantener modularidad 
-------------------------------------------------*/
    
//Poner el nombre de lo que indica al hacer override, los métodos concretos los usan
    protected abstract String getNombreTabla();
    protected abstract String getNombreCampoID();
    
    protected abstract String[] getNombresColumnas();
    //No se incluye el ID

    //Poner los nombres de todos los campos en estos tres
    protected abstract boolean validarCampos();
    protected abstract void limpiarCampos();
    protected abstract Object[] getValoresDeCampos();
    //usamos object porque recibe distintos tipos de datos
//FIN DE LOS MÉTODOS ABSTRACTOS
    
/*----------------------------------
METODOS CONCRETOS, NO SE LES HACE OVERRIDE
----------------------------------*/
//MÉTODOS ABC 
    public boolean Alta() {
        if (!validarCampos()) {
            mostrarError("Validación falló: " + mensajeError);
            return false;
        }

        try {
            // Construir SQL INSERT dinámico
            String sql = construirSQLInsert();
            System.out.println("Ejecutando: " + sql);

            // Aquí iría la ejecución real en BD
            boolean exito = ejecutarSQL(sql);
            if (exito) {
                mostrarMensaje("Registro agregado exitosamente");
                limpiarCampos();
                cargarDatos(); // Recargar datos después de insertar
                return true;
            }
        } catch (Exception e) {
            mostrarError("Error en alta: " + e.getMessage());
        }
        return false;
    }

    public boolean Baja(int id) {
        if (id <= 0) {
            mostrarError("ID inválido para eliminar");
            return false;
        }
        
        try {
            String sql = "DELETE FROM " + getNombreTabla() +
                        " WHERE " + getNombreCampoID() + " = " + id;
            System.out.println("Ejecutando: " + sql);
            
            // Ejecución en BD
            boolean exito = true; // Simulado
            
            if (exito) {
                mostrarMensaje("Registro eliminado exitosamente");
                cargarDatos(); // Recargar datos
                return true;
            }
        } catch (Exception e) {
            mostrarError("Error en baja: " + e.getMessage());
        }
        return false;
    }
    
     public boolean Cambio(int id) {
        if (!validarCampos()) {
            mostrarError("Validación falló: " + mensajeError);
            return false;
        }
        
        try {
            String sql = construirSQLUpdate(id);
            System.out.println("Ejecutando: " + sql);
            
            // Ejecución en BD
            boolean exito = true; // Simulado
            
            if (exito) {
                mostrarMensaje("Registro actualizado exitosamente");
                cargarDatos(); // Recargar datos
                return true;
            }
        } catch (Exception e) {
            mostrarError("Error en cambio: " + e.getMessage());
        }
        return false;
    }
//FIN ABC

//FUNCIONES AUXILIARES AL ABC
    protected boolean ejecutarSQL(String sqlStmt ){
        try (
            Connection con = Clases_Simples.Conexion.getConexion(); 
            Statement stmt = con.createStatement()) {
            int filasAfectadas = stmt.executeUpdate(sqlStmt);
            return filasAfectadas > 0; 
            
        } catch (SQLException e) {
            mensajeError = "Error SQL: " + e.getMessage();
            return false;
        }
    } 
    protected String construirSQLInsert() {
        String[] columnas = getNombresColumnas();
        Object[] valores = getValoresDeCampos();

        String sqlStmt = "INSERT INTO " + getNombreTabla() + " (";

        for (int i = 0; i < columnas.length; i++) {
            sqlStmt = sqlStmt + columnas[i];  // ← CONCATENAR con +
            if (i < columnas.length - 1) {
                sqlStmt = sqlStmt + ", ";
            }
        }

        sqlStmt = sqlStmt + ") VALUES (";

        for (int i = 0; i < valores.length; i++) {
            if (valores[i] instanceof String) {
                sqlStmt = sqlStmt + "'" + valores[i] + "'";  // Strings con comillas
            } else {
                sqlStmt = sqlStmt + valores[i];  //Los demás sin comillas
            }

            if (i < valores.length - 1) {
                sqlStmt = sqlStmt + ", ";
            }
        }
        sqlStmt = sqlStmt + ")";
        return sqlStmt;
    }

    protected String construirSQLUpdate(int id) {
        String[] columnas = getNombresColumnas();
        Object[] valores = getValoresDeCampos();
        
        // Usando + para concatenar
        String sqlStmt = "UPDATE " + getNombreTabla() + " SET ";
        
        // Agregar pares columna=valor
        for (int i = 0; i < columnas.length; i++) {
            sqlStmt += columnas[i] + " = ";
            
            if (valores[i] instanceof String) {
                sqlStmt += "'" + valores[i] + "'";
            } else {
                sqlStmt += valores[i];
            }
            
            if (i < columnas.length - 1) sqlStmt += ", ";
        }
        
        sqlStmt += " WHERE " + getNombreCampoID() + " = " + id;
        return sqlStmt;
    }
//FIN MÉTODOS AUXILIARES AL ABC

//INICIO MÉTODOS DE VALIDACIÓN
    protected boolean campoNoVacio(JTextField campo, String nombreCampo) {
        if (campo.getText() == null || campo.getText().trim().isEmpty()) {
            mensajeError = "Campo '" + nombreCampo + "' no puede estar vacío";
            return false;
        }
        return true;
    }
    
    protected boolean esNumeroValido(JTextField campo, String nombreCampo) {
        try {
            Double.parseDouble(campo.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            mensajeError = "Campo '" + nombreCampo + "' debe ser un número válido";
            return false;
        }
    }
    
    protected boolean esEnteroValido(JTextField campo, String nombreCampo) {
        try {
            Integer.parseInt(campo.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            mensajeError = "Campo '" + nombreCampo + "' debe ser un número entero válido";
            return false;
        }
    }
//FIN MÉTODOS DE VALIDACIÓN

//INICIO MÉTODOS DE MENSAJE
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
//FIN MÉTODOS DE MENSAJE
}




   
