package Clases_Simples;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import Clases_Abstractas.PanelABC;
import Interfaces.cargarDatos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP_25
 */


public class PanelABCPint extends PanelABC implements cargarDatos{
    private JTextField txtNombre;
    private JTextField txtTipoPintura;
    private JTextField txtAcabado;
    private JTextField txtPresentacion;
    private JTextField txtCantidadUnidad;
    private JTextField txtUnidadMedida;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JTable tablaPinturas;
    
        public PanelABCPint(JTextField txtNombre, JTextField txtTipo,
                        JTextField txtAcabado, JTextField txtPresentacion,
                        JTextField txtCantidadUnidad, JTextField txtUnidadMedida,
                        JTextField txtPrecio, JTextField txtCantidad, JTable tabla) {
        super();
        this.txtNombre = txtNombre;
        this.txtTipoPintura = txtTipo;
        this.txtAcabado = txtAcabado;
        this.txtPresentacion = txtPresentacion;
        this.txtCantidadUnidad = txtCantidadUnidad;
        this.txtUnidadMedida = txtUnidadMedida;
        this.txtPrecio = txtPrecio;
        this.txtCantidad = txtCantidad;
        this.tablaPinturas = tabla;
        configurarModeloTabla();
        cargarDatos();
    }
        
    private void configurarModeloTabla() {
        if (tablaPinturas.getModel() == null || 
            tablaPinturas.getModel().getColumnCount() == 0) {
            
            DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
            };
            
            String[] nombresColumnas = {
                "ID_Pintura",    
                "Nombre",        
                "Tipo_pintura",  
                "Acabado",       
                "Presentacion",  
                "Cantidad_Por_Unidad", 
                "Unidad_Medida", 
                "Precio",       
                "Cantidad"       
            };
            
            for (String nombreColumna : nombresColumnas) {
                modelo.addColumn(nombreColumna);
            }
            tablaPinturas.setModel(modelo);
        }
    }
    @Override
    protected String getNombreTabla() {
        return "pinturas_y_recubrimientos"; 
    }

    @Override
    protected String getNombreCampoID() {
        return "ID_Pintura";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{
            "Nombre",
            "Tipo_pintura", 
            "Acabado",
            "Presentacion",
            "Cantidad_Por_Unidad",
            "Unidad_Medida",
            "Precio",
            "Cantidad"
        };
    }

@Override
protected boolean validarCampos() {
    // Validar campos vacíos
    if (!campoNoVacio(txtNombre, "Nombre") ||
        !campoNoVacio(txtTipoPintura, "Tipo") ||
        !campoNoVacio(txtAcabado, "Acabado") ||
        !campoNoVacio(txtPresentacion, "Presentación") ||
        !campoNoVacio(txtUnidadMedida, "Unidad de Medida")) {
        return false;
    }
    
    // Validar números
    if (!esEnteroValido(txtCantidadUnidad, "Cantidad por Unidad") ||
        !esEnteroValido(txtPrecio, "Precio") ||
        !esEnteroValido(txtCantidad, "Cantidad")) {
        return false;
    }
    
    return true;
    }



    @Override
    protected void limpiarCampos() {
        txtNombre.setText("");
        txtTipoPintura.setText("");
        txtAcabado.setText("");
        txtPresentacion.setText("");
        txtCantidadUnidad.setText("");
        txtUnidadMedida.setText("");
        txtPrecio.setText("");
        txtCantidad.setText(""); 
    }

    @Override
    protected Object[] getValoresDeCampos() {
        // Obtener valores en el MISMO ORDEN que getNombresColumnas()
        return new Object[] {
            txtNombre.getText().trim(),                    // String
            txtTipoPintura.getText().trim(),               // String  
            txtAcabado.getText().trim(),                   // String
            txtPresentacion.getText().trim(),              // String
            Integer.parseInt(txtCantidadUnidad.getText()), // int
            txtUnidadMedida.getText().trim(),              // String
            Integer.parseInt(txtPrecio.getText()),       // double
            Integer.parseInt(txtCantidad.getText())        // int
        };
    } 
    @Override  
    public void cargarDatos() {
        try {
            Connection con = (Connection) Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM pinturas_y_recubrimientos");

            DefaultTableModel modelo = (DefaultTableModel) tablaPinturas.getModel(); 
            
            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] columna = new Object[9];

                columna[0] = rs.getInt("ID_Pintura");
                columna[1] = rs.getString("Nombre");
                columna[2] = rs.getString("Tipo_pintura");
                columna[3] = rs.getString("Acabado");
                columna[4] = rs.getString("Presentacion");
                columna[5] = rs.getString("Cantidad_Por_Unidad");
                columna[6] = rs.getString("Unidad_Medida");
                columna[7] = rs.getDouble("Precio");
                columna[8] = rs.getInt("Cantidad");

                modelo.addRow(columna);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
       }
    }
}
    
