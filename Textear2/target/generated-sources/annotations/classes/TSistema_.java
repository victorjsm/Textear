package classes;

import classes.Abonado;
import classes.Canal;
import classes.TSistemaPK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-09T13:06:53")
@StaticMetamodel(TSistema.class)
public class TSistema_ { 

    public static volatile SingularAttribute<TSistema, String> estado;
    public static volatile SingularAttribute<TSistema, String> bandeja;
    public static volatile SingularAttribute<TSistema, Date> fechaEnvio;
    public static volatile SingularAttribute<TSistema, Date> fechaExpiracion;
    public static volatile SingularAttribute<TSistema, Date> fechaCreacion;
    public static volatile SingularAttribute<TSistema, TSistemaPK> tSistemaPK;
    public static volatile SingularAttribute<TSistema, Abonado> abonado;
    public static volatile SingularAttribute<TSistema, Canal> codigoCanal;

}