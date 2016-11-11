/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('TareasInteractivasController', [
    '$scope', '$http', '$localStorage', 'AuthenticationService',
    function ($scope, $http, $localStorage, AuthenticationService) {
        $scope.Logout = function () {
            AuthenticationService.Logout();
        };

        $scope.canales = [];
        $scope.abonados = [];
        $scope.grupos = [];
        $scope.tareas = ['Todas', 'Consulta', 'Encuesta'];
        $scope.labels = [];
        $scope.series = [];
        $scope.data = [];
        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };
        $scope.datasetOverride = [{yAxisID: 'y-axis-1'}, {yAxisID: 'y-axis-2'}];
        $scope.options = {
            scales: {
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    },
                    {
                        id: 'y-axis-2',
                        type: 'linear',
                        display: true,
                        position: 'right'
                    }
                ]
            }
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
    }]);


