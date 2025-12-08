package Clases_Simples;

import Clases_Abstractas.PanelABC;
import Interfaces.cargarDatos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PanelABCSella extends PanelABC implements cargarDatos {
    private JTextField txtNombre;
    private JTextField txtTipo;
    private JTextField txtPresentacion;
    private JTextField txtCantidadUnidad;
    private JTextField txtUnidadMedida;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JTable tablaSelladores;

    public PanelABCSella(JTextField txtNombre, JTextField txtTipo,
                    JTextField txtPresentacion, JTextField txtCantidadUnidad,
                    JTextField txtUnidadMedida, JTextField txtPrecio,
                    JTextField txtCantidad, JTable tabla) {
        super();
        this.txtNombre = txtNombre;
        this.txtTipo = txtTipo;
        this.txtPresentacion = txtPresentacion;
        this.txtCantidadUnidad = txtCantidadUnidad;
        this.txtUnidadMedida = txtUnidadMedida;
        this.txtPrecio = txtPrecio;
        this.txtCantidad = txtCantidad;
        this.tablaSelladores = tabla;
        cargarDatosIniciales();
    }

    @Override
    protected String getNombreTabla() {
        return "selladores";
    }   

    @Override
    protected String getNombreCampoID() {
        return "ID_Sellador";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{
            "Nombre",
            "Tipo",
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
            !campoNoVacio(txtTipo, "Tipo") ||
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
        txtTipo.setText("");
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
            txtTipo.getText().trim(),
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
            // CORRECCIÓN: Sin cast innecesario
            con = Conexion.getConexion();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM selladores");

            DefaultTableModel modelo = (DefaultTableModel) tablaSelladores.getModel();
            
            // Limpiar tabla antes de cargar
            modelo.setRowCount(0);
            
            while (rs.next()) {
                Object[] columna = new Object[8];

                columna[0] = rs.getInt("ID_Sellador");
                columna[1] = rs.getString("Nombre");
                columna[2] = rs.getString("Tipo");
                columna[3] = rs.getString("Presentacion");
                columna[4] = rs.getInt("Cantidad_Por_Unidad");
                columna[5] = rs.getString("Unidad_Medida");
                columna[6] = rs.getInt("Precio");
                columna[7] = rs.getInt("Cantidad");

                modelo.addRow(columna);
            }
            
            System.out.println("Datos cargados exitosamente. Filas: " + modelo.getRowCount());
            
        } catch (Exception e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
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