app.controller('InboxController', [
    '$location', '$scope', '$http', '$localStorage', 'AuthenticationService',
    function ($location, $scope, $http, $localStorage, AuthenticationService) {
        $scope.rec_messages = [];
        $scope.env_messages = [];

        $scope.ban_entrada = [];
        $scope.ban_salida = [];
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

        $http.get('http://localhost:8080/Textear2/webresources/classes.mensajerec/rec_messages/' + $localStorage.currentUser.telefono)
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

    }]);