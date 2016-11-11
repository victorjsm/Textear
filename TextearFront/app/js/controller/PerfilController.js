/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller('PerfilController', [
    '$scope', '$http', '$localStorage', '$resource', 'AuthenticationService',
    function ($scope, $http, $localStorage, $resource, AuthenticationService) {

        var UsuarioEDIT = $resource('http://localhost:8080/Textear2/webresources/classes.usuario/usuarioEDIT/');

        $scope.Logout = function () {
            AuthenticationService.Logout();
        };

        $scope.user = $localStorage.currentUser;
        $scope.edit = false;
        $scope.edit2 = false;
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

        $scope.cambio = function () {
            $scope.edit = !$scope.edit;
        };
        $scope.cambio2 = function () {
            $scope.edit2 = !$scope.edit2;
        };


        $scope.guardarUser = function () {
            var mensaje;
            var temp = new UsuarioEDIT($scope.user);
            temp.$save(function (response) {
                if (!jQuery.isEmptyObject(response)) {
                    mensaje = response.token['mensaje'];
                } else {
                    mensaje = "No se pudo comunicar con el servidor, intente mas tarde";

                }
                InvModal(mensaje, true);
            });
        };
    }]);
