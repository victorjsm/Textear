app.controller('BandejaController', [
    '$scope', '$http', '$localStorage', 'AuthenticationService',
    function ($scope, $http, $localStorage, AuthenticationService) {

        $scope.Logout = function () {
            AuthenticationService.Logout();
        };

        $scope.tipos = ['entrada', 'salida'];
        $scope.bandeja = {};
        $scope.crearbandeja = crearbandeja;


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


        function crearbandeja() {
            $http.post('http://localhost:8080/Textear2/webresources/classes.bandeja/createbandeja',
                    {tipo: $scope.bandeja.tipo, nombre: $scope.bandeja.nombre, rif: $localStorage.currentUser.empresa.rif}).success(function (response) {
                if (response.token) {
                    $scope.mensaje = response.token.mensaje;
                } else {
                    $scope.mensaje = "No se pudo comunicar con el servidor, intente mas tarde";
                }
            });
        }
        ;
    }]);