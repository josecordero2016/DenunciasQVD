package com.foodi.Clases;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;

public class clsConexionBd {
    public Connection connection;
    private boolean okConnection;
    private String ok;

    private Statement str=null;
    private String estado;

    public clsConexionBd()
    {
        connection = null;

        try{
            try {
                Class.forName("org.postgresql.Driver");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                connection = DriverManager.getConnection("jdbc:postgresql://26.91.27.80:5432/denunciasqvd","postgres", "pa1998");
                okConnection = connection.isValid(1000);
                System.out.println(okConnection ? "TEST OK" : "TEST FAIL");
                ok = okConnection ? "OK" : "TEST FAIL";
            } catch (ClassNotFoundException ex) {
                ok = ex.getMessage();
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
        }
        catch (SQLException ex)
        {
            System.out.println("Error: " + ex);
            ok = ex.getMessage();
        }
    }

    public ResultSet consultar(String sentencia)
    {
        try
        {
            str = this.connection.createStatement();
            ResultSet res = str.executeQuery(sentencia);  // Ejecuta la linea de c.
            return res;
        }
        catch(SQLException e)
        {
            estado = "Error "+e;
        }
        return null;
    }

    public void ejecutarPs(PreparedStatement ps){
        try {
            str = this.connection.createStatement();
            ps.executeUpdate();
            ps.close();
        }
        catch (Exception e){
            estado = "Error "+e;
        }
    }
    public String ejecutarPs_mensaje(PreparedStatement ps){
        String mensaje=null;
        ResultSet resultado;
        try {
            str = this.connection.createStatement();
            resultado=ps.executeQuery();
            while (resultado.next()) {
                mensaje = resultado.getString(1);
            }
            ps.close();
        }
        catch (Exception e){
            estado = "Error "+e;
            mensaje="Error";
        }
        return mensaje;
    }

    public void ejecutar(String sentencia) {
        try
        {
            str = this.connection.createStatement();
            str.execute(sentencia);
        }
        catch (SQLException e)
        {
            estado = "Error "+e;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isOkConnection() {
        return okConnection;
    }

    public void setOkConnection(boolean okConnection) {
        this.okConnection = okConnection;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }
    public void cerrarConexion()
    {
        try
        {
            //Cierra la conexion de la Base de Datos
            connection.close();
            System.out.println("Cerrada la conexion con la Base de Datos");
        }
        catch(SQLException e)
        {
            System.out.println("Error al cerrar la conexion con la Base de datos. \n"+e);
        }
    }
}
