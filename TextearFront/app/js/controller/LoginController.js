app.controller('LoginController',  ['$location','AuthenticationService',function($location, AuthenticationService) {
        var vm =this;
        vm.login = login;
        initController();
        
        function initController(){
            AuthenticationService.Logout();
        };
        function login() {
          vm.loading = true;
          AuthenticationService.Login(vm.username, vm.password, function(result){
              if (result === true) {
                  $location.path('/buzon_entrada');
              } else {
                  vm.error = 'Username or password is incorrect';
                  vm.loading = false;
              }
          });
        };
}]);