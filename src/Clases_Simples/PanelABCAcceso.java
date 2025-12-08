package Clases_Simples;
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
public class PanelABCAcceso extends PanelABC implements cargarDatos {
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JTextField txtTipoAcceso;
    private JTable tablaAccesorios;
    
    public PanelABCAcceso(JTextField txtNombre, JTextField txtTipoAcceso, JTextField txtPrecio,
                    JTextField txtCantidad, JTable tabla) {
        super();
        this.txtNombre = txtNombre;
        this.txtTipoAcceso = txtTipoAcceso;
        this.txtPrecio = txtPrecio;
        this.txtCantidad = txtCantidad;
        this.tablaAccesorios = tabla;
        cargarDatosIniciales();
    }
    
    @Override
    protected String getNombreTabla() {
        return "accesorios";
    }   
    
    @Override
    protected String getNombreCampoID() {
        return "ID_Accessorios";
    }
    
    @Override
    protected String[] getNombresColumnas() {
        return new String[]{
            "Nombre",
            "Tipo_Acceso",
            "Precio",
            "Cantidad"
        };
    }
    
    @Override
    protected boolean validarCampos() {
        if (!campoNoVacio(txtNombre, "Nombre") ||
           !campoNoVacio(txtTipoAcceso, "Tipo_Acceso")) {
            return false;
        }
        
        if (!esEnteroValido(txtPrecio, "Precio") ||
            !esEnteroValido(txtCantidad, "Cantidad")) {
            return false;
        }
        return true;    
    }
    
    @Override
    protected void limpiarCampos() {
        txtNombre.setText("");
        txtTipoAcceso.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
    }
    
    @Override
    protected Object[] getValoresDeCampos() {
        return new Object[] {
            txtNombre.getText().trim(),
            txtTipoAcceso.getText().trim(),
            Integer.parseInt(txtPrecio.getText().trim()),
            Integer.parseInt(txtCantidad.getText().trim())
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
            rs = st.executeQuery("SELECT * FROM accesorios");
            DefaultTableModel modelo = (DefaultTableModel) tablaAccesorios.getModel();
            
            modelo.setRowCount(0);
            
            int filas = 0;
            while (rs.next()) {
                Object[] columna = new Object[5];
                columna[0] = rs.getInt("ID_Accessorios");
                columna[1] = rs.getString("Nombre");
                columna[2] = rs.getString("Tipo_Acceso");
                columna[3] = rs.getInt("Precio");
                columna[4] = rs.getInt("Cantidad");
                modelo.addRow(columna);
                filas++;
            }   
        } catch (Exception e) {
            System.out.println("Error al cargar accesorios: " + e.getMessage());
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