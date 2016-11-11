/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.factory('RegisterService', Service);

function Service($http, $localStorage) {
    var service = {};

    service.Register = Register;

    return service;

    function Register(empresa, usuario, callback) {
        $http.post('http://localhost:8080/Textear2/webresources/classes.empresa/createEmp',
            {
                direccion: empresa.direccion, 
                nit: empresa.nit, 
                rif: empresa.rif, 
                nombre: empresa.nombre,
                pagina: empresa.pagina, 
                telefono: empresa.telefono
            })
                .success(function (response) {
                    if (response.token) {
                        $http.post('http://localhost:8080/Textear2/webresources/classes.usuario/createuser',
                            {
                                id: usuario.username, 
                                email: usuario.email, 
                                fax: usuario.fax, 
                                nombre: usuario.nombre,
                                password: usuario.password, 
                                empresa: 
                                        {
                                            direccion: empresa.direccion,
                                            nit: empresa.nit,
                                            rif: empresa.rif,
                                            nombre: empresa.nombre,
                                            pagina: empresa.pagina,
                                            telefono: empresa.telefono
                                        },
                                telefono: usuario.telefono, 
                                tipo: 'cliente'
                            })
                                .success(function (response2) {
                                    $localStorage.currentUser = response2.token.usuario;
                                   
                                    // Se agrega el token jwt token al header para todos los request hechos por el $http service
                                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.token;

                                    // Se hace callback true por la llamada exitosa y respuesta exitosa del servidor
                                    callback(true);
                                });

                    } else {
                        callback(false);
                    };
                });
    }
};



