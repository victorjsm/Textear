app.controller('UsuarioController',
        ['$scope', '$localStorage', '$resource', '$window', 'ModalService', '$location', 'AuthenticationService',
            function ($scope, $localStorage, $resource, $window, ModalService, $location, AuthenticationService) {

                $scope.user = $localStorage.currentUser;
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

//  Variables asociadas a mensajes de exito o fallo en las
//  diferentes operaciones

                $scope.loading = false;


//  Variables asociadas a la comunicacion con el REST

                var UsuarioGET = $resource('http://localhost:8080/Textear2/webresources/classes.usuario/usuariosGET/:rif', {rif: '@RIF'});
                var UsuarioAdminGET = $resource('http://localhost:8080/Textear2/webresources/classes.usuario/usuariosadminGET');
                var UsuarioPOST = $resource('http://localhost:8080/Textear2/webresources/classes.usuario/createuser');
                var UsuarioEDIT = $resource('http://localhost:8080/Textear2/webresources/classes.usuario/usuarioEDIT/');
                var UsuarioDELETE = $resource('http://localhost:8080/Textear2/webresources/classes.usuario/usuarioDELETE/');



//  Query encargado de buscar en el REST todos los abonados pertenecientes la
//  la empresa con el RIF asociado al usuario


                if ($localStorage.currentUser.tipo === 'cliente') {

                    UsuarioGET.query({rif: $localStorage.currentUser.empresa.rif})
                            .$promise.then(function (temp) {
                                $scope.usuarios = temp;
                                var lista = temp;
                                for (var i = 0; i < lista.length; i++) {
                                    $scope.selected[lista[i].ci] = false;
                                }
                                ;
                            });
                }
                ;

                if ($localStorage.currentUser.tipo === 'admin') {

                    UsuarioAdminGET.query()
                            .$promise.then(function (temp) {
                                $scope.usuarios = temp;
                                var lista = temp;
                                for (var i = 0; i < lista.length; i++) {
                                    $scope.selected[lista[i].ci] = false;
                                }
                                ;
                            });
                }
                ;



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

                $scope.InvEditModal = function (usuario) {

                    ModalService.showModal({
                        templateUrl: "views/Modal/EditModals/UsuarioModal.html",
                        controller: "ModalEditUsuarioController",
                        inputs: {
                            usuario: usuario
                        }
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result.usuario !== null) {
                                editUsuario(result.usuario);
                            }
                        });
                    });

                };
                $scope.InvCreateModal = function (prefijos) {

                    ModalService.showModal({
                        templateUrl: "views/Modal/CreateModals/UsuarioModal.html",
                        controller: "ModalCreateUsuarioController",
                        inputs: {}
                    }).then(function (modal) {
                        modal.element.modal();
                        modal.close.then(function (result) {
                            if (result.usuario !== null) {
//                                console.log(result.usuario);
                                crearUsuario(result.usuario);
                            }

                        });
                    });

                };
//  Funciones encargada de crear varios abonados en la base de datos                


                function crearUsuario(user) {
                    $scope.loading = true;
                    var mensaje;
                    var temp = new UsuarioPOST(user);
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

                function editUsuario(user) {
                    $scope.loading = true;
                    var mensaje;
                    var temp = new UsuarioEDIT(user);
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
                    var temp = new UsuarioDELETE({
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