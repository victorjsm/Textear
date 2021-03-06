/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('AbonadosController',
        ['$scope', '$localStorage', '$resource', 'ModalService', '$location', 'AuthenticationService',
            function ($scope, $localStorage, $resource, ModalService, $location, AuthenticationService) {


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

                var AbonadoGET = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosGET/:rif', {rif: '@RIF'});
                var AbonadoPOST = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosPOST/');
                var AbonadoUPDATE = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosUPDATE/');
                var AbonadoDELETE = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosDELETE/');

                var abonadosNEGRA = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosNEGRA/');



//  Query encargado de buscar en el REST todos los abonados pertenecientes la
//  la empresa con el RIF asociado al usuario




                AbonadoGET.query({rif: $localStorage.currentUser.empresa.rif})
                        .$promise.then(function (temp) {
                            $scope.abonados = temp;
                            var lista = temp;
                            for (var i = 0; i < lista.length; i++) {
                                $scope.selected[lista[i].ci] = false;
                            }
                            ;
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
                $scope.addselect = function (ci, abonado) {
                    if (selectedAction.hasOwnProperty(ci)) {
                        delete selectedAction[ci];
                    } else {
                        selectedAction[ci] = abonado;
                    }
                    ;
                    $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
                };


//  Funciones encargada de borrar un abonado de la base de datos             
                $scope.borrar = function () {
                    $scope.loading = true;
                    var temp = new AbonadoDELETE({
                        arreglo: []
                    });
                    for (var tel in selectedAction) {
                        temp.arreglo.push(selectedAction[tel]);
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


                $scope.banear = function () {
                    $scope.loading = true;
                    var mensaje;
                    $localStorage.falla = "carajo";
                    var temp = new abonadosNEGRA({
                        arreglo: []
                    });
                    for (var tel in selectedAction) {
                        selectedAction[tel].negra = true;
                        temp.arreglo.push(selectedAction[tel]);
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

//  Funciones encargada de crear varios abonados en la base de datos                

                function crearabonados(abonados) {
                    $scope.loading = true;
                    var temp = new AbonadoPOST({
                        arreglo: abonados
                    });
                    var mensaje;
                    temp.$save(function (response) {
                        if (!jQuery.isEmptyObject(response)) {
                            InvModal(response.token['mensaje'], true);
                        } else {
                            mensaje = "No se pudo comunicar con el servidor, intente mas tarde";
                            InvModal(mensaje, false);
                        }
                    });
                    $scope.loading = false;
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

                        });
                    });
                }
                ;

                $scope.InvEditModal = function (abonado) {

                    ModalService.showModal({
                        templateUrl: "views/Modal/EditModals/AbonadoModal.html",
                        controller: "ModalEditAbonadoController",
                        inputs: {
                            abonado: abonado
                        }
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result.abonado !== null) {
                                
                                var temp = new AbonadoUPDATE(result.abonado);

                                temp.$save(function (response) {
                                    if (!jQuery.isEmptyObject(response)) {
                                        InvModal(response.token['mensaje'], true);
                                    } else {
                                        mensaje = "No se pudo comunicar con el servidor, intente mas tarde";
                                        InvModal(mensaje, true);
                                    }
                                });
                            }
                        });
                    });

                };
                
                $scope.InvCreateModal = function () {

                    ModalService.showModal({
                        templateUrl: "views/Modal/CreateModals/AbonadoModal.html",
                        controller: "ModalCreateAbonadoController",
                        inputs: {}
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result !== null) {
                                crearabonados(result.abonados);
                            }
                        });
                    });

                };

            }]);