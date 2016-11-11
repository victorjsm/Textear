/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author aganius
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(classes.NewCrossOriginResourceSharingFilter.class);
        resources.add(service.AbonadoFacadeREST.class);
        resources.add(service.BandejaFacadeREST.class);
        resources.add(service.CanalFacadeREST.class);
        resources.add(service.EmpresaFacadeREST.class);
        resources.add(service.GrupoFacadeREST.class);
        resources.add(service.MensajeRecFacadeREST.class);
        resources.add(service.PrecioCanalPrefijoFacadeREST.class);
        resources.add(service.PrefijoFacadeREST.class);
        resources.add(service.TConsultaFacadeREST.class);
        resources.add(service.TEncuestaFacadeREST.class);
        resources.add(service.TInscripcionFacadeREST.class);
        resources.add(service.TMensajeFacadeREST.class);
        resources.add(service.TSistemaFacadeREST.class);
        resources.add(service.UsuarioFacadeREST.class);
    }
    
}
