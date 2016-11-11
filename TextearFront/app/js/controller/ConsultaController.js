/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


app.controller('ConsultaController', [
    '$scope', '$localStorage', '$resource', '$http', '$q', 'ModalService', 'AuthenticationService',
    function ($scope, $localStorage, $resource, $http, $q, ModalService, DTOptionsBuilder, DTColumnBuilder, DTColumnDefBuilder, AuthenticationService) {


        $scope.Logout = function () {
            AuthenticationService.Logout();
        };
        
        $scope.acceso = function (user, action) {
            if (user === 'basico') {
                return false;
            }
            if (user === 'cliente' || user === 'avanzado') {
                return true;
            } else {
                if (action === 'tarea' || action === 'abo') {
                    return true;
                } else {
                    if (user === 'medio') {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        };

        $scope.selected = {};
        $scope.terminoCrear = false;
        var vm = this;
        $scope.user = $localStorage.currentUser;

        var tconsultaGET = $resource('http://localhost:8080/Textear2/webresources/classes.tconsulta/TconsultaGET/:rif', {rif: '@RIF'});
        var tconsultaPOST = $resource('http://localhost:8080/Textear2/webresources/classes.tconsulta/tconsultaPOST/');

        var tconsultaEditPOST = $resource('http://localhost:8080/Textear2/webresources/classes.tconsulta/tconsultaEditPOST/');

        var GruposGETALL = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposGETALL/:rif', {rif: '@RIF'});
        var GruposGET = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposGETabo/:rif/:nombre',
                {rif: '@RIF', nombre: '@Nombre'});
        var AbonadoGET = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosGET/:rif', {rif: '@RIF'});
        var CanalGET = $resource('http://localhost:8080/Textear2/webresources/classes.canal/canalGET');

        $scope.dtInstance = {};



        $http.get('http://localhost:8080/Textear2/webresources/classes.bandeja/bandejas/'
                + $localStorage.currentUser.empresa.rif + '/salida')
                .success(function (response) {
                    $scope.bandejas = response;
                });


        CanalGET.query().$promise.then(function (temp) {
            $scope.canales = temp;
        });


        //  Query encargado de buscar en el REST todos los abonados pertenecientes la
        //  la empresa con el RIF asociado al usuario


        tconsultaGET.query({rif: $localStorage.currentUser.empresa.rif})
                .$promise.then(function (temp) {
                    $scope.tareas = temp;
                    var lista = temp;
                    for (var i = 0; i < lista.length; i++) {
                        $scope.selected[lista[i].nombre] = false;
                    }
                    ;
                });


        GruposGETALL.query({rif: $localStorage.currentUser.empresa.rif})
                .$promise.then(function (temp) {
                    $scope.grupos = temp;
                });

        AbonadoGET.query({rif: $localStorage.currentUser.empresa.rif})
                .$promise.then(function (temp) {
                    $scope.abonados = temp;

                });



        $scope.InvCreateModal = function (abonados, grupos, bandejas, canales) {

            ModalService.showModal({
                templateUrl: "views/Modal/CreateModals/TareaConsultaModal.html",
                controller: "ModalCreateTconsultaController",
                inputs: {
                    abonados: abonados,
                    grupos: grupos,
                    bandejas: bandejas,
                    canales: canales
                }
            }).then(function (modal) {
                modal.element.modal();
                modal.close.then(function (result) {
                    if (result.mensaje !== null) {
                        for (var i = 0; i < $scope.canales.length; i++) {
                            if ($scope.canales[i].codigo === result.mensaje.canal) {
                                result.mensaje.canal = $scope.canales[i];
                                i = $scope.canales.length;
                            }
                        }
                        ;
                        if (result.esabonado) {
                            crearTareaAbonados(result.destinatarios, result.mensaje);
                        } else {
                            crearTareaGrupos(result.destinatarios, result.mensaje);
                        }
                    }
                });
            });

        };

        $scope.InvEditModal = function (tarea, abonados, grupos, bandejas, canales) {

            ModalService.showModal({
                templateUrl: "views/Modal/EditModals/TareaConsultaModal.html",
                controller: "ModalEditTconsultaController",
                inputs: {
                    tarea: tarea,
                    abonados: abonados,
                    grupos: grupos,
                    bandejas: bandejas,
                    canales: canales
                }
            }).then(function (modal) {
                modal.element.modal();
                modal.close.then(function (result) {
                    if (result.mensaje !== null) {
                        console.log(result);
                        if (result.eliminar) {
                            var tarea = {
                                abonados: result.destinatarios,
                                contenido: result.mensaje
                            };
                            var tareas = [];
                            tareas.push(tarea);
                            eliminarTareaAbonados(tareas);
                        } else {
                            if (result.esabonado) {
                                editarTareaAbonados(result.destinatarios, result.mensaje, result.abonados);
                            } else {
                                editarTareaGrupos(result.destinatarios, result.mensaje, result.abonados);
                            }
                        }
                    }
                });
            });

        };






        function editarTareaAbonados(NuevosAbonados, contenido, ViejosAbonados) {
            $scope.loading = true;
            var temp = new tconsultaEditPOST({
                arreglo: [],
                mensaje: contenido
            });
            var mensaje;
            for (var i = 0; i < NuevosAbonados.length; i++) {
                temp.arreglo.push(NuevosAbonados[i]);
            }
            ;
            for (var i = 0; i < ViejosAbonados.length; i++) {
                temp.arreglo.push(ViejosAbonados[i]);
            }
            ;
            temp.$save(function (response) {
                if (!jQuery.isEmptyObject(response)) {
                    InvModal(response.token['mensaje'], true);
                } else {
                    mensaje = "No se pudo comunicar con el servidor, intente mas tarde";
                    InvModal(mensaje, false);
                }
            });
            $scope.loading = false;
        }
        ;
        function editarTareaGrupos(grupos, contenido, ViejosAbonados) {
            $scope.loading = true;

            var temp = new tconsultaEditPOST({
                arreglo: [],
                mensaje: contenido
            });
            var agregado = {};
            var mensaje;
            for (var i = 0; i < ViejosAbonados.length; i++) {
                temp.arreglo.push(ViejosAbonados[i]);
            }
            ;
            for (var i = 0; i < grupos.length; i++) {
                GruposGET.query({rif: $localStorage.currentUser.empresa.rif, nombre: grupos[i].nombre})
                        .$promise.then(function (temp2) {
                            for (var k = 0; k < temp2.length; k++) {
                                if (!agregado.hasOwnProperty(temp2[k].ci)) {
                                    temp.arreglo.push(temp2[k]);
                                    agregado[temp2[k].ci] = true;
                                }
                            }
                        });
            }
            ;

            temp.$save(function (response) {
                if (!jQuery.isEmptyObject(response)) {
                    InvModal(response.token['mensaje'], true);
                } else {
                    mensaje = "No se pudo comunicar con el servidor, intente mas tarde";
                    InvModal(mensaje, false);
                }
            });
            $scope.loading = false;
        }
        ;



        //  Funciones encargada de crear varios abonados en la base de datos                

        function crearTareaAbonados(abonados, contenido) {
            $scope.loading = true;
            var consulta = contenido;
            var temp = new tconsultaPOST({
                arreglo: [],
                mensaje: contenido
            });
            consulta.abonados = [];
            var mensaje;
            for (var i = 0; i < abonados.length; i++) {
                var temp2 = ({
                    telefono: abonados[i].telefono,
                    ci: abonados[i].ci,
                    nombre: abonados[i].nombre,
                    rif: $localStorage.currentUser.empresa.rif
                });
                temp.arreglo.push(temp2);
                consulta.abonados.push(temp2);
            }
            ;

            temp.$save(function (response) {
                if (!jQuery.isEmptyObject(response)) {
                    InvModal(response.token['mensaje'], true);
                } else {
                    InvModal(mensaje, false);
                }
            });
            $scope.loading = false;
        }
        ;



        function crearTareaGrupos(grupos, contenido) {
            $scope.loading = true;
            var agregado = {};
            var temp = new tconsultaPOST({
                arreglo: [],
                mensaje: contenido
            });
            var mensaje;
            for (var i = 0; i < grupos.length; i++) {
                GruposGET.query({rif: $localStorage.currentUser.empresa.rif, nombre: grupos[i].nombre})
                        .$promise.then(function (temp2) {
                            for (var k = 0; k < temp2.length; k++) {
                                if (!agregado.hasOwnProperty(temp2[k].ci)) {
                                    temp.arreglo.push(temp2[k]);
                                    agregado[temp2[k].ci] = true;
                                }
                            }
                        });
            }
            ;
            temp.$save(function (response) {
                if (!jQuery.isEmptyObject(response)) {
                    InvModal(response.token['mensaje'], true);
//                    $scope.MensajeClass = 'alert-success';
//                    $scope.resultadoMensaje = response.token['mensaje'];
//                    $scope.terminoCrear = true;
                } else {
//                    $scope.MensajeClass = 'alert-danger';
//                    $scope.resultadoMensaje = "No se pudo comunicar con el servidor, intente mas tarde";
//                    mensaje = "No se pudo comunicar con el servidor, intente mas tarde";
//                    $scope.terminoCrear = true;
                    InvModal(mensaje, false);
                }
            });
            $scope.loading = false;
        }
        ;
        function InvModal(mensaje, reload) {

            ModalService.showModal({
                templateUrl: "views/Modal/MensajesModal.html",
                controller: "ModalMensajesController",
                inputs: {
                    mensaje: mensaje,
                    reload: reload
                }
            }).then(function (modal) {
                modal.element.modal();
                modal.close.then(function () {

                });
            });
        }
        ;



    }]);
