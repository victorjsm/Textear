package classes;

import classes.Abonado;
import classes.Canal;
import classes.TConsultaPK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-16T06:00:33")
@StaticMetamodel(TConsulta.class)
public class TConsulta_ { 

    public static volatile SingularAttribute<TConsulta, String> estado;
    public static volatile SingularAttribute<TConsulta, String> bandeja;
    public static volatile SingularAttribute<TConsulta, Date> fechaEnvio;
    public static volatile SingularAttribute<TConsulta, String> ayuda;
    public static volatile SingularAttribute<TConsulta, TConsultaPK> tConsultaPK;
    public static volatile SingularAttribute<TConsulta, String> bienvenida;
    public static volatile SingularAttribute<TConsulta, Date> fechaExpiracion;
    public static volatile SingularAttribute<TConsulta, Date> fechaCreacion;
    public static volatile SingularAttribute<TConsulta, Abonado> abonado;
    public static volatile SingularAttribute<TConsulta, Canal> codigoCanal;
    public static volatile SingularAttribute<TConsulta, String> respuesta;
    public static volatile SingularAttribute<TConsulta, String> pregunta;

}