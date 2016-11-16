/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('GruposController',
        ['$scope', '$localStorage', '$resource', '$window', 'ModalService', '$location', 'AuthenticationService',
            function ($scope, $localStorage, $resource, $window, ModalService, $location, AuthenticationService) {

                $scope.Logout = function () {
                    AuthenticationService.Logout();
                };
                $scope.cambioVista = function (vista) {
                    $location.path(vista);
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
                
                $scope.numero_usuarios = $localStorage.currentUser.numero_usuarios;
                $scope.numero_abonados = $localStorage.currentUser.numero_abonados;
                $scope.numero_recibidos = $localStorage.currentUser.numero_recibidos;
                $scope.numero_enviados = $localStorage.currentUser.numero_enviados;

//  Variables asociadas a la seleccion de mensajes con los checkbox

                $scope.selected = {};
                var selectedAction = {};
                $scope.selectAll = false;
                $scope.selectOne = false;
                $scope.user = $localStorage.currentUser;

//  Variables asociadas a mensajes de exito o fallo en las
//  diferentes operaciones

                $scope.loading = false;


//  Variables asociadas a la comunicacion con el REST

                var GruposGETALL = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposGETALL/:rif', {rif: '@RIF'});
                var GrupoPOST = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/grupoPOST/');
                var AbonadoGET = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosGET/:rif', {rif: '@RIF'});
                var AbonadoGrupoDELETE = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposABODELETE');
                var GrupoDELETE = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposDELETE');


//  Query encargado de buscar en el REST todos los abonados pertenecientes la
//  la empresa con el RIF asociado al usuario


                GruposGETALL.query({rif: $localStorage.currentUser.empresa.rif})
                        .$promise.then(function (temp) {
                            $scope.grupos = temp;
                        });

                AbonadoGET.query({rif: $localStorage.currentUser.empresa.rif})
                        .$promise.then(function (temp) {
                            $scope.abonados = temp;

                        });
//  Funciones relacionadas a la seleccion de los elementos en la tabla de
//  abonados

                $scope.toggleAll = function (selectAll, selectedItems) {
                    for (var ci in selectedItems) {
                        selectedItems[ci] = selectAll;
                        if (selectedAction.hasOwnProperty(ci)) {
                            delete selectedAction[ci];
                        } else {
                            selectedAction[ci] = true;
                        }
                        ;
                    }
                    $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
                };
                $scope.addselect = function (ci) {
                    if (selectedAction.hasOwnProperty(ci)) {
                        delete selectedAction[ci];
                    } else {
                        selectedAction[ci] = true;
                    }
                    ;
                    $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
                };

// Funciones para el manejo del modal

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
                            if (reload) {
                                $window.location.reload();
                            }

                        });
                    });
                }
                ;

                $scope.InvEditModal = function (grupo) {

                    ModalService.showModal({
                        templateUrl: "views/Modal/EditModals/GrupoModal.html",
                        controller: "ModalEditGrupoController",
                        inputs: {
                            grupo: grupo
                        }
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result !== null) {
                                if (result.grupos.agrego) {
                                    crearGrupo(result.arreglo, grupo.nombre);
                                } else {
                                    deleteAboGrupo(result.arreglo, grupo.nombre);
                                }

                            }
                        });
                    });

                };
                $scope.InvCreateModal = function (abonados) {

                    ModalService.showModal({
                        templateUrl: "views/Modal/CreateModals/GrupoModal.html",
                        controller: "ModalCreateGrupoController",
                        inputs: {
                            abonados: abonados
                        }
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result.abonados !== null) {
                                crearGrupo(result.abonados, result.nombre);
                            }
                        });
                    });

                };
//  Funciones encargada de crear varios abonados en la base de datos                

                function crearGrupo(abonados, nombre) {
                    $scope.loading = true;
                    var temp = new GrupoPOST({
                        arreglo: [],
                        nombre: nombre
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


                function deleteAboGrupo(abonados, nombre) {
                    var temp = new AbonadoGrupoDELETE({
                        arreglo: []
                    });
                    for (var abo in abonados) {
                        var temp2 = ({
                            ci: abonados[abo].ci,
                            nombre: nombre,
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
                            InvModal(mensaje, true);
                        }
                    });

                }
                ;

                $scope.borrar = function () {
                    $scope.loading = true;
                    var temp = new GrupoDELETE({
                        arreglo: []
                    });
                    for (var nombre in selectedAction) {
                        var temp2 = ({
                            nombre: nombre,
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
                            InvModal(mensaje, true);
                        }
                    });
                    $scope.loading = false;

                };




            }]);

