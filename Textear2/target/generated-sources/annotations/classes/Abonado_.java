package classes;

import classes.AbonadoPK;
import classes.Empresa;
import classes.Grupo;
import classes.TConsulta;
import classes.TEncuesta;
import classes.TInscripcion;
import classes.TMensaje;
import classes.TSistema;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-09T13:06:53")
@StaticMetamodel(Abonado.class)
public class Abonado_ { 

    public static volatile CollectionAttribute<Abonado, TInscripcion> tInscripcionCollection;
    public static volatile ListAttribute<Abonado, Grupo> grupoList;
    public static volatile SingularAttribute<Abonado, Boolean> negra;
    public static volatile SingularAttribute<Abonado, String> ci;
    public static volatile SingularAttribute<Abonado, AbonadoPK> abonadoPK;
    public static volatile CollectionAttribute<Abonado, TSistema> tSistemaCollection;
    public static volatile ListAttribute<Abonado, TEncuesta> tEncuestaList;
    public static volatile ListAttribute<Abonado, TMensaje> tMensajeList;
    public static volatile SingularAttribute<Abonado, Empresa> empresa;
    public static volatile ListAttribute<Abonado, TConsulta> tConsultaList;
    public static volatile SingularAttribute<Abonado, String> nombre;

}