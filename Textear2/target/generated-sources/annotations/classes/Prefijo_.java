package classes;

import classes.PrecioCanalPrefijo;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-16T06:00:33")
@StaticMetamodel(Prefijo.class)
public class Prefijo_ { 

    public static volatile SingularAttribute<Prefijo, String> codigo;
    public static volatile SingularAttribute<Prefijo, BigDecimal> costomt;
    public static volatile CollectionAttribute<Prefijo, PrecioCanalPrefijo> precioCanalPrefijoCollection;

}