package classes;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-09T13:06:53")
@StaticMetamodel(MensajeRec.class)
public class MensajeRec_ { 

    public static volatile SingularAttribute<MensajeRec, String> telefonoReceptor;
    public static volatile SingularAttribute<MensajeRec, Date> fecha;
    public static volatile SingularAttribute<MensajeRec, String> bandeja;
    public static volatile SingularAttribute<MensajeRec, String> telefonoEmisor;
    public static volatile SingularAttribute<MensajeRec, Integer> id;
    public static volatile SingularAttribute<MensajeRec, Integer> canal;
    public static volatile SingularAttribute<MensajeRec, String> mensaje;

}