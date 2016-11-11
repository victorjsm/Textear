app.controller('RegisterController', ['$scope','$location','RegisterService','AuthenticationService', function ($scope, $location, RegisterService,AuthenticationService) {
        $scope.register = register;
        function register() {
          RegisterService.Register($scope.emp, $scope.usuario, function(result){
              if (result === true) {
                AuthenticationService.Login($scope.usuario.username, $scope.usuario.password, function(result){
                    if (result === true) {
                        $location.path('/buzon_entrada');
                    } else {
                        $scope.error = 'Username or password is incorrect';
                        $scope.loading = false;
                    }
                });
              } else {
                $location.path('/registro');
              }
          });
        };
}]);
