package classes;

import classes.Canal;
import classes.PrecioCanalPrefijoPK;
import classes.Prefijo;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-16T06:00:33")
@StaticMetamodel(PrecioCanalPrefijo.class)
public class PrecioCanalPrefijo_ { 

    public static volatile SingularAttribute<PrecioCanalPrefijo, PrecioCanalPrefijoPK> precioCanalPrefijoPK;
    public static volatile SingularAttribute<PrecioCanalPrefijo, BigDecimal> precio;
    public static volatile SingularAttribute<PrecioCanalPrefijo, Prefijo> prefijo;
    public static volatile SingularAttribute<PrecioCanalPrefijo, Canal> canal;

}