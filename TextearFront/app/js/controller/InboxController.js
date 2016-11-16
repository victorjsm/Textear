app.controller('InboxController', [
    '$location', '$scope', '$http', '$localStorage', 'AuthenticationService', '$resource',
    function ($location, $scope, $http, $localStorage, AuthenticationService, $resource) {
        $scope.ban_entrada = [];
        $scope.ban_salida = [];
        
        $scope.selected = {};
        var selectedAction = {};
        $scope.selectAll = false;
        $scope.selectOne = false;
        
        $scope.change = function (){
            $scope.filters1.Bandeja = '';
            $scope.selected = {};
            selectedAction = {};
            $scope.selectAll = false;
            $scope.selectOne = false;
        }
        
        
        $scope.filters1 = {};
        $scope.user = $localStorage.currentUser;
        $scope.cambioVista = function (vista) {
            $location.path(vista);
        };

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

        $http.get('http://localhost:8080/Textear2/webresources/classes.mensajeenv/mensajesGET/' + $localStorage.currentUser.empresa.acronimo)
                .success(function (response) {
                    $scope.env_messages = response;
                });
        $http.get('http://localhost:8080/Textear2/webresources/classes.mensajerec/mensajesGET/' + $localStorage.currentUser.empresa.acronimo)
                .success(function (response) {
                    $scope.rec_messages = response;
                });
        $http.get('http://localhost:8080/Textear2/webresources/classes.bandeja/bandejas/'
                + $localStorage.currentUser.empresa.rif + '/entrada')
                .success(function (response) {
                    $scope.ban_entrada = response;
                });
        $http.get('http://localhost:8080/Textear2/webresources/classes.bandeja/bandejas/'
                + $localStorage.currentUser.empresa.rif + '/salida')
                .success(function (response) {
                    $scope.ban_salida = response;
                });
                
                
        var AbonadoCOUNT = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosCOUNT/:rif', {rif: '@RIF'});

        AbonadoCOUNT.get({rif: $localStorage.currentUser.empresa.rif})
                .$promise.then(function (temp) {
                    $scope.numero_abonados = temp.numero;
                    $localStorage.currentUser.numero_abonados = temp.numero;
                });
                
        var UsuarioCOUNT = $resource('http://localhost:8080/Textear2/webresources/classes.usuario/usuariosCOUNT/:rif', {rif: '@RIF'});

        UsuarioCOUNT.get({rif: $localStorage.currentUser.empresa.rif})
                .$promise.then(function (temp) {
                    $scope.numero_usuarios = temp.numero;
                    $localStorage.currentUser.numero_usuarios = temp.numero;
                });
                
        var MensajeRecCOUNT = $resource('http://localhost:8080/Textear2/webresources/classes.mensajerec/mensajesCOUNT/:acro', {acro: '@ACRO'});

        MensajeRecCOUNT.get({acro: $localStorage.currentUser.empresa.acronimo})
                .$promise.then(function (temp) {
                    $scope.numero_recibidos = temp.numero;
                    $localStorage.currentUser.numero_recibidos = temp.numero;
                });
                
        var MensajeEnvCOUNT = $resource('http://localhost:8080/Textear2/webresources/classes.mensajeenv/mensajesCOUNT/:acro', {acro: '@ACRO'});

        MensajeEnvCOUNT.get({acro: $localStorage.currentUser.empresa.acronimo})
                .$promise.then(function (temp) {
                    $scope.numero_enviados = temp.numero;
                    $localStorage.currentUser.numero_enviados = temp.numero;
                });
                



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

    }]);