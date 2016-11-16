/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textearback;

import com.tedexis.commons.db.DBSingleManager;
import com.tedexis.commons.db.DBSinglePool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author yecheverria
 */
public class Mensajes_DataManage extends DBSingleManager {

    public Mensajes_DataManage(DBSinglePool dbPool) {
        super(dbPool);
    }

    public Boolean checkConsulta(String respuesta,String telefono,String clave,String acronimo, String canal) {
        ResultSet rs = null;
        String rif;
        Connection con = null;
        Boolean result = false;
        
        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_mensaje WHERE estado = 'ENVIADO' AND rif = ? AND telefono = ? AND nombre = ?");
            PreparedStatement up = con.prepareStatement(
                    "UPDATE t_mensaje SET respuesta = ? WHERE nombre = ? AND rif_empresa = ? AND telefono = ?");
            
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_rec (acronimo, clave, canal, mensaje, telefono)"
                    + "VALUES (?,?,?,?,?)");
            
            rif = GetRif(acronimo, con);
            ps.setString(1, rif);
            ps.setString(2, telefono);
            ps.setString(3, clave);
            ps.execute();
            rs = ps.getResultSet();
            if (rs.next()) {
                
                

                up.setString(1, respuesta);
                up.setString(2, clave);
                up.setString(3, rif);
                up.setString(4, telefono);

                up.executeUpdate();
                result = true;
                
                ins.setString(1, acronimo);
                ins.setString(2, clave);
                ins.setString(3, canal);
                ins.setString(5, telefono);
                ins.setString(4, respuesta);
                ins.executeUpdate();
                
            }
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(con);
        }
        
        
        
        return false;
    }
    public Boolean checkEncuesta(String respuesta,String telefono,String clave,String acronimo, String canal) {
        ResultSet rs = null;
        String rif;
        Connection con = null;
        Boolean result = false;
        
        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_encuesta WHERE estado != 'EXPIRO' AND estado != 'CANCELADO' "
                            + "AND rif = ? AND telefono = ? AND nombre = ?");
            PreparedStatement up = con.prepareStatement(
                    "UPDATE preguntas_encuesta SET respuesta = ? WHERE t_nombre = ? AND t_rif = ? "
                            + "AND t_telefono = ? AND t_id = ?");
            PreparedStatement up2 = con.prepareStatement(
                    "UPDATE t_encuesta SET estado = ? WHERE t_nombre = ? AND t_rif = ? "
                            + "AND t_telefono = ? ");
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_rec (acronimo, clave, canal, mensaje, telefono)"
                    + "VALUES (?,?,?,?,?)");
            
            rif = GetRif(acronimo, con);
            ps.setString(1, rif);
            ps.setString(2, telefono);
            ps.setString(3, clave);
            ps.execute();
            rs = ps.getResultSet();
            if (rs.next()) {
                
                String estado = rs.getString("estado");
                Integer numero = Integer.parseInt(estado.substring(9));
                
                estado = "PROGRESO" +  Integer.toString(numero + 1);

                up.setString(1, respuesta);
                up.setString(2, clave);
                up.setString(3, rif);
                up.setString(4, telefono);
                up.setInt(5, numero);
                
                up2.setString(1, estado);
                up2.setString(2, clave);
                up2.setString(3, rif);
                up2.setString(4, telefono);
                
                up.executeUpdate();
                up2.executeUpdate();
                
                ins.setString(1, acronimo);
                ins.setString(2, clave);
                ins.setString(3, canal);
                ins.setString(5, telefono);
                ins.setString(4, respuesta);
                ins.executeUpdate();
                
                result = true;
            }
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(con);
        }
        
        
        
        return false;
    }
    
    private String GetRif(String acro, Connection con) {

        ResultSet rs = null;

        try {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM empresa WHERE acronimo = ?");
            ps.setString(1, acro);
            ps.execute();
            rs = ps.getResultSet();

            if (rs.next()) {
                return rs.getNString("rif");
            } else {
                return "";
            }

        } catch (Exception e) {
            return "";
        } finally {
            close(rs);
        }
    }

}
