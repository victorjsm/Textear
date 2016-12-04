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
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author yecheverria
 */
public class Mensajes_DataManage extends DBSingleManager {

    public Mensajes_DataManage(DBSinglePool dbPool) {
        super(dbPool);
    }

    public Boolean checkConsulta(String respuesta, String telefono, String clave, String acronimo, String canal) {
        ResultSet rs = null;
        String rif;
        Connection con = null;
        Boolean result = false;

        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_mensaje WHERE estado = 'ENVIADO' AND rif = ? AND telefono = ? AND nombre = ?");

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

    public Boolean checkInscripcion(String respuesta, String telefono, String clave, String acronimo, String canal) {
        ResultSet rs = null;
        String rif;
        Connection con = null;
        Boolean result = false;

        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_inscripcion WHERE estado = 'ENVIADO' AND rif = ? AND telefono = ? AND nombre = ?");

            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_rec (acronimo, clave, canal, mensaje, telefono)"
                    + "VALUES (?,?,?,?,?)");

            PreparedStatement ins2 = con.prepareStatement(
                    "INSERT INTO abonado (rif_empresa, nombre, telefono, ci, negra)"
                    + "VALUES (?,?,?,?,?)");

            rif = GetRif(acronimo, con);
            ps.setString(1, rif);
            ps.setString(2, telefono);
            ps.setString(3, clave);
            ps.execute();
            rs = ps.getResultSet();
            if (rs.next()) {

                result = true;

                ins.setString(1, acronimo);
                ins.setString(2, clave);
                ins.setString(3, canal);
                ins.setString(5, telefono);
                ins.setString(4, respuesta);
                ins.executeUpdate();

                String[] a = respuesta.split("\\s+");

                ins2.setString(1, rif);
                ins2.setString(2, a[0]);
                ins2.setString(3, telefono);
                ins2.setString(4, a[1]);
                ins2.setBoolean(5, true);

                ins2.executeUpdate();

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

    public Boolean checkEncuesta(String respuesta, String telefono, String clave, String acronimo, String canal) {
        ResultSet rs = null;
        String rif;
        Connection con = null;
        Boolean result = false;

        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_encuesta WHERE estado != 'EXPIRO' AND estado != 'CANCELADO' "
                    + "AND rif = ? AND telefono = ? AND nombre = ?");

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

                estado = "PROGRESO" + Integer.toString(numero + 1);

                up2.setString(1, estado);
                up2.setString(2, clave);
                up2.setString(3, rif);
                up2.setString(4, telefono);

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
