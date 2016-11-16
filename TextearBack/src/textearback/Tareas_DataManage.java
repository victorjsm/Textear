/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textearback;

import com.tedeixs.utils.jms.QueueHelper;
import com.tedexis.commons.db.DBSingleManager;
import com.tedexis.commons.db.DBSinglePool;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author yecheverria
 */
public class Tareas_DataManage extends DBSingleManager {

    public Tareas_DataManage(DBSinglePool dbPool) {
        super(dbPool);
    }
    
    public void checkExpired(QueueHelper qHelper) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Connection con = null;

        try {

            con = getConnection();

            expireMensaje(con, "t_mensaje", timestamp);
            expireMensaje(con, "t_consulta", timestamp);
            expireMensaje(con, "t_inscripcion", timestamp);
            expireMensaje(con, "t_sistema", timestamp);
            expireMensaje(con, "t_encuesta", timestamp);
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con);
        }
    }
    
    private void expireMensaje (Connection con, String target, Timestamp time) {
        ResultSet rs = null;
        PreparedStatement ps;
        PreparedStatement up;

        try {            
            ps = con.prepareStatement(
                    "SELECT * FROM " +target + " WHERE estado != 'ENVIADO' AND fecha_expiracion <= ?");
            up = con.prepareStatement(
                    "UPDATE " +target + " SET estado = 'EXPIRO' WHERE nombre = ? AND rif_empresa = ? AND telefono = ?");
            ps.setTimestamp(1, time);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {

                up.setString(1, rs.getString("nombre"));
                up.setString(2, rs.getString("rif_empresa"));
                up.setString(3, rs.getString("telefono"));

                up.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
        }
        
    }
    
    

    public void checkMensaje( QueueHelper qHelper) {
        ResultSet rs = null;
        String acro,result;
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Connection con = null;
        
        try {
            
            con = getConnection();
            
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_mensaje WHERE estado = 'PENDIENTE' AND fecha_envio <= ?");
            PreparedStatement up = con.prepareStatement(
                    "UPDATE t_mensaje SET estado = 'ENVIADO' WHERE nombre = ? AND rif_empresa = ? AND telefono = ?");
            
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_env (acronimo, clave, canal, mensaje, telefono)"
                            + "VALUES (?,?,?,?,?)");
                  
  
            ps.setTimestamp(1, timestamp);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                
                acro = GetAcronimo(rs.getNString("rif_empresa"),con);
                
                Mensaje men = new Mensaje();
                
                men.setAcronimo(acro);
                men.setClave(rs.getString("nombre"));
                men.setCanal(rs.getString("codigo_canal"));
                men.setMensaje(rs.getString("mensaje"));
                men.setTelefono(rs.getString("telefono"));
                
                qHelper.send(men);
                

                up.setString(1, rs.getString("nombre"));
                up.setString(2, rs.getString("rif_empresa"));
                up.setString(3, rs.getString("telefono"));
                
                up.executeUpdate();
                
                ins.setString(1, acro);
                ins.setString(2, rs.getString("nombre"));
                ins.setString(3, rs.getString("codigo_canal"));
                ins.setString(4, rs.getString("mensaje"));
                ins.setString(5, rs.getString("telefono"));
                
                ins.executeUpdate();
                
                
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(con);
        }
    }
    
    public void checkInscripcion(QueueHelper qHelper) {
        ResultSet rs = null;
        String acro, result;
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Connection con = null;

        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_inscripcion WHERE estado = 'PENDIENTE' AND fecha_envio <= ?");
            PreparedStatement up = con.prepareStatement(
                    "UPDATE t_inscripcion SET estado = 'ENVIADO' WHERE nombre = ? AND rif_empresa = ? AND telefono = ?");
            
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_env (acronimo, clave, canal, mensaje, telefono)"
                    + "VALUES (?,?,?,?,?)");
            
            ps.setTimestamp(1, timestamp);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {

                acro = GetAcronimo(rs.getNString("rif_empresa"), con);

                String mensaje = "Por favor envie su nombre y cedula de respuesta para inscribirlo al sistema de mensajeria";
                
                Mensaje men = new Mensaje();

                men.setAcronimo(acro);
                men.setClave(rs.getString("nombre"));
                men.setCanal(rs.getString("codigo_canal"));
                men.setMensaje(mensaje);
                men.setTelefono(rs.getString("telefono"));
               
                qHelper.send(men);

                up.setString(1, rs.getString("nombre"));
                up.setString(2, rs.getString("rif_empresa"));
                up.setString(3, rs.getString("telefono"));

                up.executeUpdate();
                
                ins.setString(1, acro);
                ins.setString(2, rs.getString("nombre"));
                ins.setString(3, rs.getString("codigo_canal"));
                ins.setString(4, mensaje);
                ins.setString(5, rs.getString("telefono"));

                ins.executeUpdate();

                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(con);
        }
    }
    
    
    public void checkSistema(QueueHelper qHelper) {
        ResultSet rs = null;
        String acro, result;
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Connection con = null;

        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_sistema WHERE estado = 'PENDIENTE' AND fecha_envio <= ?");
            PreparedStatement up = con.prepareStatement(
                    "UPDATE t_sistema SET estado = 'ENVIADO' WHERE nombre = ? AND rif_empresa = ? AND telefono = ?");
            
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_env (acronimo, clave, canal, mensaje, telefono)"
                    + "VALUES (?,?,?,?,?)");
            
            ps.setTimestamp(1, timestamp);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {

                acro = GetAcronimo(rs.getNString("rif_empresa"), con);

                Mensaje men = new Mensaje();
                
                String mensaje = "Mensaje sistema a sistema";

                men.setAcronimo(acro);
                men.setClave(rs.getString("nombre"));
                men.setCanal(rs.getString("codigo_canal"));
                men.setMensaje(mensaje);
                men.setTelefono(rs.getString("telefono"));
               
                qHelper.send(men);

                up.setString(1, rs.getString("nombre"));
                up.setString(2, rs.getString("rif_empresa"));
                up.setString(3, rs.getString("telefono"));

                up.executeUpdate();
                
                ins.setString(1, acro);
                ins.setString(2, rs.getString("nombre"));
                ins.setString(3, rs.getString("codigo_canal"));
                ins.setString(4, mensaje);
                ins.setString(5, rs.getString("telefono"));

                ins.executeUpdate();

                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(con);
        }
    }
    
    
    public void checkConsulta(QueueHelper qHelper) {
        ResultSet rs = null;
        String acro, result;
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Connection con = null;

        try {

            con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_consulta WHERE estado = 'PENDIENTE' AND fecha_envio <= ?");
            PreparedStatement up = con.prepareStatement(
                    "UPDATE t_consulta SET estado = 'ENVIADO' WHERE nombre = ? AND rif_empresa = ? AND telefono = ?");
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_env (acronimo, clave, canal, mensaje, telefono)"
                    + "VALUES (?,?,?,?,?)");
            
            ps.setTimestamp(1, timestamp);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {

                acro = GetAcronimo(rs.getNString("rif_empresa"), con);
                
                Mensaje men = new Mensaje();
                
                men.setAcronimo(acro);
                men.setClave(rs.getString("nombre"));
                men.setCanal(rs.getString("codigo_canal"));    
                men.setTelefono(rs.getString("telefono"));
                
                ins.setString(1, acro);
                ins.setString(2, rs.getString("nombre"));
                ins.setString(3, rs.getString("codigo_canal"));
                ins.setString(5, rs.getString("telefono"));

                String ayuda = "\"mensaje\":\"" + rs.getString("ayuda") + "\"}";
                String bienvenida = "\"mensaje\":\"" + rs.getString("bienvenida") + "\"}";
                String pregunta = "\"mensaje\":\"" + rs.getString("pregunta") + "\"}";

                men.setMensaje(bienvenida);

                qHelper.send(men);
                
                ins.setString(4, bienvenida);
                ins.executeUpdate();
                
                Thread.sleep(5000);
                
                men.setMensaje(ayuda);
                
                qHelper.send(men);
                
                ins.setString(4, ayuda);
                ins.executeUpdate();
                
                Thread.sleep(5000);
                
                men.setMensaje(pregunta);
                
                qHelper.send(men);
                
                ins.setString(4, pregunta);
                ins.executeUpdate();
                

                up.setString(1, rs.getString("nombre"));
                up.setString(2, rs.getString("rif_empresa"));
                up.setString(3, rs.getString("telefono"));

                up.executeUpdate();

                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(con);
        }
    }
    
    
    public void checkEncuesta(QueueHelper qHelper) {
        ResultSet rs = null;
        String acro, result;
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        try {

            Connection con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM t_encuesta WHERE estado != 'ENVIADO' AND fecha_envio <= ?");
            PreparedStatement up = con.prepareStatement(
                    "UPDATE t_encuesta SET estado = ? WHERE nombre = ? AND rif_empresa = ? AND telefono = ?");
            
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO mensaje_env (acronimo, clave, canal, mensaje, telefono)"
                    + "VALUES (?,?,?,?,?)");
            
            ps.setTimestamp(1, timestamp);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                
                Mensaje men = new Mensaje();

                acro = GetAcronimo(rs.getNString("rif_empresa"), con);
                String estado = rs.getString("estado");
                
                ins.setString(1, acro);
                ins.setString(2, rs.getString("nombre"));
                ins.setString(3, rs.getString("codigo_canal"));
                ins.setString(5, rs.getString("telefono"));
                
                men.setAcronimo(acro);
                men.setClave(rs.getString("nombre"));
                men.setCanal(rs.getString("codigo_canal"));    
                men.setTelefono(rs.getString("telefono"));
                
                
                PreparedStatement p = con.prepareStatement(
                        "SELECT * FROM preguntas_encuesta "
                        + "WHERE t_nombre = ? "
                        + "AND t_rif = ? "
                        + "AND t_telefono = ? "
                        + "AND id = 0");

                p.setString(1, rs.getString("nombre"));
                p.setString(2, rs.getString("rif_empresa"));
                p.setString(3, rs.getString("telefono"));
                
                PreparedStatement o = con.prepareStatement(
                        "SELECT * FROM opciones_pregunta "
                        + "WHERE t_nombre = ? "
                        + "AND t_rif = ? "
                        + "AND t_telefono = ? "
                        + "AND pregunta_id = 0");

                o.setString(1, rs.getString("nombre"));
                o.setString(2, rs.getString("rif_empresa"));
                o.setString(3, rs.getString("telefono"));
                
                if (estado.equals("PENDIENTE")) {
                    
                    String ayuda = "\"mensaje\":\"" + rs.getString("ayuda") + "\"}";
                    String bienvenida = "\"mensaje\":\"" + rs.getString("bienvenida") + "\"}";
                    
                    men.setMensaje(rs.getString("bienvenida"));
                    qHelper.send(men);
                    
                    ins.setString(4, rs.getString("bienvenida"));
                    ins.executeUpdate();
                    
                    Thread.sleep(5000);
                    
                    men.setMensaje(rs.getString("ayuda"));
                    qHelper.send(men);
                    
                    ins.setString(4, rs.getString("ayuda"));
                    ins.executeUpdate();
                    
                    Thread.sleep(5000);
                    
                    p.setInt(4, 0);
                    p.execute();

                    o.setInt(4, 0);
                    o.execute();
                    
                    ResultSet pregunta = p.getResultSet();
                    ResultSet opciones = o.getResultSet();
                    
                    if (pregunta.next()) {
                        
                        String mensaje = pregunta.getString("pregunta") +". Opciones: ";
                        
                        while(opciones.next()){
                            
                            mensaje = mensaje +  Integer.toString(opciones.getInt("id")) + 
                                    ") " + opciones.getNString("opcion") ;
                            
                        }
                        
                        men.setMensaje(mensaje);
                        qHelper.send(men);

                        ins.setString(4, mensaje);
                        ins.executeUpdate();
                        
                        up.setString(1,"PROGRESO" + pregunta.getNString("id"));
                        
                        
                        
                    };
                    
                } else {
                    
                    p.setInt(4, Integer.parseInt(estado.substring(9)));
                    p.execute();

                    o.setInt(4, Integer.parseInt(estado.substring(9)));
                    o.execute();

                    ResultSet pregunta = p.getResultSet();
                    ResultSet opciones = o.getResultSet();

                    if (pregunta.next()) {

                        String mensaje = pregunta.getString("pregunta") +". Opciones: ";
                        
                        while(opciones.next()){
                            
                            mensaje = mensaje +  Integer.toString(opciones.getInt("id")) + 
                                    ") " + opciones.getNString("opcion") ;
                            
                        }
                        
                        men.setMensaje(mensaje);
                        qHelper.send(men);
                        
                        ins.setString(4, mensaje);
                        ins.executeUpdate();
                        
                        up.setString(1, "PROGRESO" + Integer.toString(pregunta.getInt("id")));

                    };
                    
                }

                up.setString(2, rs.getString("nombre"));
                up.setString(3, rs.getString("rif_empresa"));
                up.setString(4, rs.getString("telefono"));

                up.executeUpdate();
                Thread.sleep(5000);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
        }
    }
    
    
    
    
    private String GetAcronimo(String rif, Connection con){
        
        ResultSet rs = null;
        
        try {
        
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM empresa WHERE rif = ?");
            ps.setString(1, rif);
            ps.execute();
            rs = ps.getResultSet();
            
            if (rs.next()){
                return rs.getNString("acronimo");
            } else{
                return "";
            }
            
            
        } catch (Exception e) {
            return "";
        } finally {
            close(rs);
        }
    }

}
