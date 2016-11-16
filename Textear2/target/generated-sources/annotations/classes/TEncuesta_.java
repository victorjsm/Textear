package classes;

import classes.Abonado;
import classes.Canal;
import classes.PreguntasEncuesta;
import classes.TEncuestaPK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-16T06:00:33")
@StaticMetamodel(TEncuesta.class)
public class TEncuesta_ { 

    public static volatile SingularAttribute<TEncuesta, TEncuestaPK> tEncuestaPK;
    public static volatile SingularAttribute<TEncuesta, String> estado;
    public static volatile SingularAttribute<TEncuesta, String> bandeja;
    public static volatile SingularAttribute<TEncuesta, Date> fechaEnvio;
    public static volatile SingularAttribute<TEncuesta, String> ayuda;
    public static volatile SingularAttribute<TEncuesta, String> bienvenida;
    public static volatile SingularAttribute<TEncuesta, Date> fechaExpiracion;
    public static volatile SingularAttribute<TEncuesta, Date> fechaCreacion;
    public static volatile CollectionAttribute<TEncuesta, PreguntasEncuesta> preguntasEncuestaCollection;
    public static volatile SingularAttribute<TEncuesta, Abonado> abonado;
    public static volatile SingularAttribute<TEncuesta, Canal> codigoCanal;

}