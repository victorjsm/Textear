/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('RecargasController', [
    '$scope', '$http', '$localStorage', 'AuthenticationService',
    function ($scope, $http, $localStorage, AuthenticationService) {

        $scope.Logout = function () {
            AuthenticationService.Logout();
        };

        $scope.recargas = [];
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
    }]);



