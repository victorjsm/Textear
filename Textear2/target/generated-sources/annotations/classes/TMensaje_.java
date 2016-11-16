package classes;

import classes.Abonado;
import classes.Canal;
import classes.TMensajePK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-16T06:00:33")
@StaticMetamodel(TMensaje.class)
public class TMensaje_ { 

    public static volatile SingularAttribute<TMensaje, TMensajePK> tMensajePK;
    public static volatile SingularAttribute<TMensaje, String> estado;
    public static volatile SingularAttribute<TMensaje, String> bandeja;
    public static volatile SingularAttribute<TMensaje, Date> fechaEnvio;
    public static volatile SingularAttribute<TMensaje, Date> fechaExpiracion;
    public static volatile SingularAttribute<TMensaje, Date> fechaCreacion;
    public static volatile SingularAttribute<TMensaje, String> mensaje;
    public static volatile SingularAttribute<TMensaje, Abonado> abonado;
    public static volatile SingularAttribute<TMensaje, Canal> codigoCanal;

}