package classes;

import classes.Abonado;
import classes.Bandeja;
import classes.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-09T13:06:53")
@StaticMetamodel(Empresa.class)
public class Empresa_ { 

    public static volatile ListAttribute<Empresa, Usuario> usuarioList;
    public static volatile SingularAttribute<Empresa, String> paginaweb;
    public static volatile SingularAttribute<Empresa, String> nit;
    public static volatile SingularAttribute<Empresa, String> direccion;
    public static volatile ListAttribute<Empresa, Abonado> abonadoList;
    public static volatile ListAttribute<Empresa, Bandeja> bandejaList;
    public static volatile SingularAttribute<Empresa, Float> saldo;
    public static volatile SingularAttribute<Empresa, String> telefono;
    public static volatile SingularAttribute<Empresa, String> nombre;
    public static volatile SingularAttribute<Empresa, String> rif;

}