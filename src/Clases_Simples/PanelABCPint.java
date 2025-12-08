package Clases_Simples;

import Clases_Abstractas.PanelABC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PanelABCPint extends PanelABC {
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
        cargarDatosIniciales();
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
        return new Object[] {
            txtNombre.getText().trim(),
            txtTipoPintura.getText().trim(),
            txtAcabado.getText().trim(),
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
            rs = st.executeQuery("SELECT * FROM pinturas_y_recubrimientos");

            DefaultTableModel modelo = (DefaultTableModel) tablaPinturas.getModel(); 
            
            // Limpiar tabla
            modelo.setRowCount(0);
            
            int filas = 0;
            while (rs.next()) {
                Object[] columna = new Object[9];

                columna[0] = rs.getInt("ID_Pintura");
                columna[1] = rs.getString("Nombre");
                columna[2] = rs.getString("Tipo_pintura");
                columna[3] = rs.getString("Acabado");
                columna[4] = rs.getString("Presentacion");
                columna[5] = rs.getInt("Cantidad_Por_Unidad");  
                columna[6] = rs.getString("Unidad_Medida");
                columna[7] = rs.getInt("Precio");  
                columna[8] = rs.getInt("Cantidad");

                modelo.addRow(columna);
                filas++;
            }            
        } catch (Exception e) {
            System.out.println("Error al cargar pinturas: " + e.getMessage());
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