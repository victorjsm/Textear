/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('CanalController',
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
                var selectedCanal = {};
                $scope.selectAll = false;
                $scope.selectOne = false;
                $scope.user = $localStorage.currentUser;
//  Variables asociadas a mensajes de exito o fallo en las
//  diferentes operaciones

                $scope.loading = false;
                var arregloprefijos = [];


//  Variables asociadas a la comunicacion con el REST

                var CanalGET = $resource('http://localhost:8080/Textear2/webresources/classes.canal/canalGET');
                var PrefijosGET = $resource('http://localhost:8080/Textear2/webresources/classes.prefijo/prefijosGET');
                var CanalPOST = $resource('http://localhost:8080/Textear2/webresources/classes.canal/canalPOST/');
                var CanalEDIT = $resource('http://localhost:8080/Textear2/webresources/classes.canal/canalEDIT/');
                var CanalDELETE = $resource('http://localhost:8080/Textear2/webresources/classes.canal/canalesDELETE/');



//  Query encargado de buscar en el REST todos los abonados pertenecientes la
//  la empresa con el RIF asociado al usuario

                CanalGET.query().$promise.then(function (temp) {
                    $scope.canales = temp;
                });

                PrefijosGET.query().$promise.then(function (temp) {
                    $scope.prefijos = temp;
                    for (var x in temp) {
                        arregloprefijos[temp[x].codigo] = temp[x];
                    }
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
                $scope.addselect = function (ci, canal) {
                    if (selectedAction.hasOwnProperty(ci)) {
                        delete selectedAction[ci];
                        delete selectedCanal[ci];
                    } else {
                        selectedAction[ci] = true;
                        selectedCanal[ci] = canal;
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

                $scope.InvEditModal = function (canal) {

                    ModalService.showModal({
                        templateUrl: "views/Modal/EditModals/CanalModal.html",
                        controller: "ModalEditCanalController",
                        inputs: {
                            canal: canal
                        }
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result.canal !== null) {
                                editCanal(result.canal);
                            }
                        });
                    });

                };
                $scope.InvCreateModal = function (prefijos) {


                    ModalService.showModal({
                        templateUrl: "views/Modal/CreateModals/CanalModal.html",
                        controller: "ModalCreateCanalController",
                        inputs: {
                            prefijos: prefijos,
                        }
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result.canal !== null) {
                                crearCanal(result.canal, result.precios);
                            }

                        });
                    });

                };
//  Funciones encargada de crear varios abonados en la base de datos                


                function crearCanal(canal, precios) {
                    $scope.loading = true;
                    var mensaje;
                    var canal = {
                        codigo: canal.codigo,
                        descripcion: canal.descripcion,
                        longitud: canal.longitud,
                        precioEnviar: canal.precioEnviar,
                        precioRecibir: canal.precioRecibir
                    };
                    var temp = new CanalPOST({
                        canal: canal,
                        arreglo: []
                    });

                    for (var cod in precios) {
                        var temp2 = {
                            precio: precios[cod],
                            prefijo: arregloprefijos[cod]

                        };
                        temp.arreglo.push(temp2);
                    }
                    temp.$save(function (response) {
                        if (!jQuery.isEmptyObject(response)) {
                            mensaje = response.token['mensaje'];
                        } else {
                            mensaje = "No se pudo comunicar con el servidor, intente mas tarde";

                        }
                        InvModal(mensaje, true);
                    });
                }
                ;

                function editCanal(canal) {
                    $scope.loading = true;
                    var mensaje;
                    var temp = new CanalEDIT({
                        codigo: canal.codigo,
                        descripcion: canal.descripcion,
                        longitud: canal.longitud,
                        precioEnviar: canal.precioEnviar,
                        precioRecibir: canal.precioRecibir,
                        prefijos: canal.Prefijos
                    });
                    console.log(canal);
                    temp.$save(function (response) {
                        if (!jQuery.isEmptyObject(response)) {
                            mensaje = response.token['mensaje'];
                        } else {
                            mensaje = "No se pudo comunicar con el servidor, intente mas tarde";

                        }
                        InvModal(mensaje, true);
                    });
                }
                ;



                $scope.borrar = function () {
                    $scope.loading = true;
                    var temp = new CanalDELETE({
                        arreglo: []
                    });
                    for (var codigo in selectedAction) {
                        var temp2 = ({
                            codigo: codigo
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

