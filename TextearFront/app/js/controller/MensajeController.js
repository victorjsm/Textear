/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('MensajeController', [
    '$scope', '$localStorage', '$resource', '$http', 'ModalService', 'AuthenticationService',
    function ($scope, $localStorage, $resource, $http, ModalService, AuthenticationService) {

        //  Variables asociadas a la seleccion de mensajes con los checkbox

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



        $scope.user = $localStorage.currentUser;


        //  Variables asociadas a mensajes de exito o fallo en las
        //  diferentes operaciones

        $scope.loading = false;



        //  Variables asociadas a la comunicacion con el REST

        var tmensajeGET = $resource('http://localhost:8080/Textear2/webresources/classes.tmensaje/tmensajeGET/:rif', {rif: '@RIF'});
        var GruposGETALL = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposGETALL/:rif', {rif: '@RIF'});
        var GruposGET = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposGETabo/:rif/:nombre',
                {rif: '@RIF', nombre: '@Nombre'});
        var AbonadoGET = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosGET/:rif', {rif: '@RIF'});
        var CanalGET = $resource('http://localhost:8080/Textear2/webresources/classes.canal/canalGET');
        var tmensajePOST = $resource('http://localhost:8080/Textear2/webresources/classes.tmensaje/tmensajePOST');
        var tmensajeEditPOST = $resource('http://localhost:8080/Textear2/webresources/classes.tmensaje/tmensajeEditPOST');
        var tmensajeElimPOST = $resource('http://localhost:8080/Textear2/webresources/classes.tmensaje/tmensajeElimPOST');
        
        $scope.numero_usuarios = $localStorage.currentUser.numero_usuarios;
        $scope.numero_abonados = $localStorage.currentUser.numero_abonados;
        $scope.numero_recibidos = $localStorage.currentUser.numero_recibidos;
        $scope.numero_enviados = $localStorage.currentUser.numero_enviados;


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


        tmensajeGET.query({rif: $localStorage.currentUser.empresa.rif})
                .$promise.then(function (temp) {
                    $scope.tmensajes = temp;
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


        // Funciones para el manejo de los modals

        $scope.InvCreateModal = function (abonados, grupos, bandejas, canales) {

            ModalService.showModal({
                templateUrl: "views/Modal/CreateModals/TareaMensajeModal.html",
                controller: "ModalCreateTmensajeController",
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

        $scope.InvEditModal = function (mensaje, edit, abonados, grupos, bandejas, canales) {

            ModalService.showModal({
                templateUrl: "views/Modal/EditModals/TareaMensajeModal.html",
                controller: "ModalEditTmensajeController",
                inputs: {
                    mensaje: mensaje,
                    edit: edit,
                    abonados: abonados,
                    grupos: grupos,
                    bandejas: bandejas,
                    canales: canales
                }
            }).then(function (modal) {
                modal.element.modal();
                modal.close.then(function (result) {
                    if (result.mensaje !== null) {
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
            var temp = new tmensajeEditPOST({
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

            var temp = new tmensajeEditPOST({
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
            var temp = new tmensajePOST({
                arreglo: [],
                mensaje: contenido
            });
            var mensaje;
            for (var i = 0; i < abonados.length; i++) {
                var temp2 = ({
                    telefono: abonados[i].telefono,
                    ci: abonados[i].ci,
                    nombre: abonados[i].nombre,
                    rif: $localStorage.currentUser.empresa.rif
                });
                temp.arreglo.push(temp2);
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



        function crearTareaGrupos(grupos, contenido) {
            $scope.loading = true;
            var agregado = {};
            var temp = new tmensajePOST({
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
                } else {
                    mensaje = "No se pudo comunicar con el servidor, intente mas tarde";
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
        
        $scope.selected = {};
        var selectedAction = {};
        $scope.selectAll = false;
        $scope.selectOne = false;

        //  Funciones relacionadas a la seleccion de los elementos en la tabla de
//  abonados

//        $scope.toggleAll = function (selectAll, selectedItems) {
//            for (var ci in selectedItems) {
//                selectedItems[ci] = selectAll;
//                if (selectedAction.hasOwnProperty(ci)) {
//                    delete selectedAction[ci];
//                } else {
//                    selectedAction[ci] = true;
//                }
//                ;
//            }
//            $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
//        };
        $scope.addselect = function (id, obj) {
            if (selectedAction.hasOwnProperty(id)) {
                delete selectedAction[id];
            } else {
                selectedAction[id] = obj;
            }
            ;
            $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
        };

        $scope.borrar = function () {
            $scope.loading = true;
            var tareas = [];

            for (var ci in $scope.selected) {
                if ($scope.selected[ci]) {
                    var tarea = {
                        abonados: selectedAction[ci].abonados,
                        contenido: selectedAction[ci]
                    }
                    tareas.push(tarea);
                }
            }
            eliminarTareaAbonados(tareas);

        };

        function eliminarTareaAbonados(Tareas) {
            $scope.loading = true;
            var final = new tmensajeElimPOST({
                tareas: []
            });
            for (var id in Tareas) {
                var temp = {
                    arreglo: [],
                    mensaje: Tareas[id].contenido
                };
                var mensaje;
                for (var i = 0; i < Tareas[id].abonados.length; i++) {
                    var temp2 = ({
                        telefono: Tareas[id].abonados[i].telefono,
                        ci: Tareas[id].abonados[i].ci,
                        nombre: Tareas[id].abonados[i].nombre,
                        rifEmpresa: $localStorage.currentUser.empresa.rif
                    });
                    temp.arreglo.push(temp2);
                }
                ;
                final.tareas.push(temp);
            }

            final.$save(function (response) {
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



    }]);

