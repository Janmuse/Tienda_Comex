package Clases_Simples;

/**
 *
 * @author HP_25
 */

import Clases_Abstractas.PanelABC;
import Interfaces.cargarDatos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PanelABCImper extends PanelABC implements cargarDatos {
    private JTextField txtNombre;
    private JTextField txtTipoImper;
    private JTextField txtPresentacion;
    private JTextField txtCantidadUnidad;
    private JTextField txtUnidadMedida;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JTable tablaImpermeabilizantes;

    public PanelABCImper(JTextField txtNombre, JTextField txtTipo,
                    JTextField txtPresentacion, JTextField txtCantidadUnidad,
                    JTextField txtUnidadMedida, JTextField txtPrecio,
                    JTextField txtCantidad, JTable tabla) {
        super();
        this.txtNombre = txtNombre;
        this.txtTipoImper = txtTipo;
        this.txtPresentacion = txtPresentacion;
        this.txtCantidadUnidad = txtCantidadUnidad;
        this.txtUnidadMedida = txtUnidadMedida;
        this.txtPrecio = txtPrecio;
        this.txtCantidad = txtCantidad;
        this.tablaImpermeabilizantes = tabla;
        cargarDatosIniciales();
    }

    @Override
    protected String getNombreTabla() {
        return "impermeabilizantes";
    }   

    @Override
    protected String getNombreCampoID() {
        return "ID_Impermeabilizante";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{
            "Nombre",
            "Tipo_Imper",
            "Presentacion",
            "Cantidad_Por_Unidad",
            "Unidad_Medida",
            "Precio",
            "Cantidad"
        };
    }

    @Override
    protected boolean validarCampos() {
        if (!campoNoVacio(txtNombre, "Nombre") ||
            !campoNoVacio(txtTipoImper, "Tipo") ||
            !campoNoVacio(txtPresentacion, "Presentación") ||
            !campoNoVacio(txtUnidadMedida, "Unidad de Medida")) {
            return false;
        }
        
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
        txtTipoImper.setText("");
        txtPresentacion.setText("");
        txtCantidadUnidad.setText("");
        txtUnidadMedida.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
    }

    @Override
    protected Object[] getValoresDeCampos() {
        return new Object[] {
            txtNombre.getText().trim(),
            txtTipoImper.getText().trim(),
            txtPresentacion.getText().trim(),
            Integer.parseInt(txtCantidadUnidad.getText()),
            txtUnidadMedida.getText().trim(),
            Integer.parseInt(txtPrecio.getText()),
            Integer.parseInt(txtCantidad.getText())
        };
    }

    @Override
    public void cargarDatos() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            
            con = Conexion.getConexion();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM impermeabilizantes");

            DefaultTableModel modelo = (DefaultTableModel) tablaImpermeabilizantes.getModel();
            
            modelo.setRowCount(0);
            
            int filas = 0;
            while (rs.next()) {
                Object[] columna = new Object[8];

                columna[0] = rs.getInt("ID_Impermeabilizante");
                columna[1] = rs.getString("Nombre");
                columna[2] = rs.getString("Tipo_Imper");
                columna[3] = rs.getString("Presentacion");
                columna[4] = rs.getInt("Cantidad_Por_Unidad");
                columna[5] = rs.getString("Unidad_Medida");
                columna[6] = rs.getInt("Precio");
                columna[7] = rs.getInt("Cantidad");

                modelo.addRow(columna);
                filas++;
            }            
        } catch (Exception e) {
            System.out.println("Error al cargar impermeabilizantes: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al cargar datos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}