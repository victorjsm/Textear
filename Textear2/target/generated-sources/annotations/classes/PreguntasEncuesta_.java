package classes;

import classes.OpcionesPregunta;
import classes.PreguntasEncuestaPK;
import classes.TEncuesta;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-09T13:06:53")
@StaticMetamodel(PreguntasEncuesta.class)
public class PreguntasEncuesta_ { 

    public static volatile SingularAttribute<PreguntasEncuesta, PreguntasEncuestaPK> preguntasEncuestaPK;
    public static volatile CollectionAttribute<PreguntasEncuesta, OpcionesPregunta> opcionesPreguntaCollection;
    public static volatile SingularAttribute<PreguntasEncuesta, TEncuesta> tEncuesta;
    public static volatile SingularAttribute<PreguntasEncuesta, String> pregunta;

}