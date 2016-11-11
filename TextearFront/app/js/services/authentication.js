/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.factory('AuthenticationService', Service);

function Service($http, $localStorage, $location) {
    var service = {};

    service.Login = Login;
    service.Logout = Logout;

    return service;

    function Login(username, password, callback) {
        $http.post('http://localhost:8080/Textear2/webresources/classes.usuario/authenticate', {username: username, password: password})
                .success(function (response) {
                    // Si tiene exito un token es recibidio en respuesta desde el backend
                    if (response.token) {
                        // Se guarda el username y un token en el local storage para que el usuario se mantenga 
                        // en sesion aunque cambie de ventana o refresque el explorador

                        $localStorage.currentUser = response.token.usuario;
                        $localStorage.token = response.token;
//
//                    // Se agrega el token jwt token al header para todos los request hechos por el $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.token;
//
//                    // Se hace callback true por la llamada exitosa y respuesta exitosa del servidor
                        callback(true);
//                    });
                    } else {
                        // Se hace callback con false indicando el inicio de sesion fallido
                        callback(false);
                    }
                });
    }

    function Logout() {
        // Se remueve el usuario del header
        delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
        $location.path('/');
    }
}
;

