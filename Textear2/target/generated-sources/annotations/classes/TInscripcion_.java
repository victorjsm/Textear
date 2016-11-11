package classes;

import classes.Abonado;
import classes.Canal;
import classes.TInscripcionPK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-09T13:06:53")
@StaticMetamodel(TInscripcion.class)
public class TInscripcion_ { 

    public static volatile SingularAttribute<TInscripcion, String> estado;
    public static volatile SingularAttribute<TInscripcion, String> bandeja;
    public static volatile SingularAttribute<TInscripcion, Date> fechaEnvio;
    public static volatile SingularAttribute<TInscripcion, Date> fechaExpiracion;
    public static volatile SingularAttribute<TInscripcion, Date> fechaCreacion;
    public static volatile SingularAttribute<TInscripcion, String> respuesta;
    public static volatile SingularAttribute<TInscripcion, Abonado> abonado;
    public static volatile SingularAttribute<TInscripcion, Canal> codigoCanal;
    public static volatile SingularAttribute<TInscripcion, TInscripcionPK> tInscripcionPK;

}