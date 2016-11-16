package classes;

import classes.PrecioCanalPrefijo;
import classes.TConsulta;
import classes.TEncuesta;
import classes.TInscripcion;
import classes.TMensaje;
import classes.TSistema;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-16T06:00:33")
@StaticMetamodel(Canal.class)
public class Canal_ { 

    public static volatile SingularAttribute<Canal, String> descripcion;
    public static volatile SingularAttribute<Canal, BigDecimal> precioEnviar;
    public static volatile CollectionAttribute<Canal, TInscripcion> tInscripcionCollection;
    public static volatile SingularAttribute<Canal, String> codigo;
    public static volatile SingularAttribute<Canal, Integer> longitud;
    public static volatile CollectionAttribute<Canal, TEncuesta> tEncuestaCollection;
    public static volatile CollectionAttribute<Canal, TMensaje> tMensajeCollection;
    public static volatile CollectionAttribute<Canal, TSistema> tSistemaCollection;
    public static volatile CollectionAttribute<Canal, PrecioCanalPrefijo> precioCanalPrefijoCollection;
    public static volatile SingularAttribute<Canal, BigDecimal> precioRecibir;
    public static volatile ListAttribute<Canal, TConsulta> tConsultaList;

}