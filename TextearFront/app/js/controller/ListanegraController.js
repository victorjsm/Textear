/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('ListanegraController',
        ['$scope', '$localStorage', '$resource', '$location', 'AuthenticationService', 'ModalService',
            function ($scope, $localStorage, $resource, $location, AuthenticationService, ModalService) {

                $scope.Logout = function () {
                    AuthenticationService.Logout();
                };

                $scope.user = $localStorage.currentUser;
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

                var AbonadosNegraGET = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosNegraGET/:rif', {rif: '@RIF'});
                var AbonadoDELETE = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosDELETE/');



//  Query encargado de buscar en el REST todos los abonados pertenecientes la
//  la empresa con el RIF asociado al usuario




                AbonadosNegraGET.query({rif: $localStorage.currentUser.empresa.rif})
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
                $scope.addselect = function (ci) {
                    if (selectedAction.hasOwnProperty(ci)) {
                        delete selectedAction[ci];
                    } else {
                        selectedAction[ci] = true;
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
                    for (var ci in selectedAction) {
                        var temp2 = ({
                            telefono: '',
                            ci: ci,
                            nombre: '',
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
                            if (result !== null) {

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

            }]);

