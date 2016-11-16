var app = angular.module("Textear", 
    ['ui.router','ngResource','ngMessages','ngStorage',
        'angularUtils.directives.dirPagination', 'ui.bootstrap',
        'chart.js','datatables','angularModalService']);
app.config(function ($stateProvider, $urlRouterProvider) {
    // default route
    $urlRouterProvider.otherwise("/");
    
    // app routes
    $stateProvider
            .state('Login', {
                url: '/',
                templateUrl: 'views/login.html',
                controller: 'LoginController',
                controllerAs: 'vm'
            })
            .state('Inbox', {
                url: '/buzon_entrada',
                templateUrl: 'views/inbox.html',
                controller: 'InboxController',
                controllerAs: 'vm'
            })
            .state('Register', {
                url: '/registro',
                templateUrl: 'views/register.html',
                controller: 'RegisterController',
                controllerAs: 'vm'
            })
            .state('TareaMensaje', {
                url: '/tarea_mensaje',
                templateUrl: 'views/TareaMensaje.html',
                controller: 'MensajeController',
                controllerAs: 'vm'
            })
            .state('TareaEncuesta', {
                url: '/tarea_encuesta',
                templateUrl: 'views/TareaEncuesta.html',
                controller: 'EncuestaController',
                controllerAs: 'vm'
            })
            .state('TareaConsulta', {
                url: '/tarea_consulta',
                templateUrl: 'views/TareaConsulta.html',
                controller: 'ConsultaController',
                controllerAs: 'vm'
            })
            .state('TareaSistema', {
                url: '/tarea_sistema',
                templateUrl: 'views/TareaSistema.html',
                controller: 'SistemaController',
                controllerAs: 'vm'
            })
            .state('Abonados', {
                url: '/abonados',
                templateUrl: 'views/Abonados.html',
                controller: 'AbonadosController',
                controllerAs: 'vm'
            })
            .state('Grupos', {
                url: '/grupos',
                templateUrl: 'views/Grupos.html',
                controller: 'GruposController',
                controllerAs: 'vm'
            })
            .state('CrearGrupo', {
                url: '/crear_grupos',
                templateUrl: 'views/CrearGrupos.html',
                controller: 'GruposController',
                controllerAs: 'vm'
            })
            .state('Recargas', {
                url: '/recargas',
                templateUrl: 'views/Recargas.html',
                controller: 'RecargasController',
                controllerAs: 'vm'
            })
            .state('Perfil', {
                url: '/perfil',
                templateUrl: 'views/Perfil.html',
                controller: 'PerfilController',
                controllerAs: 'vm'
            })
            .state('Contrasena', {
                url: '/cambio_clave',
                templateUrl: 'views/Contrasena.html',
                controller: 'PerfilController',
                controllerAs: 'vm'
            })
            .state('Listanegra', {
                url: '/lista_negra',
                templateUrl: 'views/Listanegra.html',
                controller: 'ListanegraController',
                controllerAs: 'vm'
            })
            .state('ReporteMensajes', {
                url: '/reporte_mensajes',
                templateUrl: 'views/ReporteMensajes.html',
                controller: 'ReporteController',
                controllerAs: 'vm'
            })
            .state('TareasInteractivas', {
                url: '/tareas_interactivas',
                templateUrl: 'views/TareasInteractivas.html',
                controller: 'TareasInteractivasController',
                controllerAs: 'vm'
            })
            .state('CrearBandeja', {
                url: '/crear_bandeja',
                templateUrl: 'views/CrearBandeja.html',
                controller: 'BandejaController',
                controllerAs: 'vm'
            })
            .state('Canal', {
                url: '/canal',
                templateUrl: 'views/Canal.html',
                controller: 'CanalController',
                controllerAs: 'vm'
            })
            .state('Prefijo', {
                url: '/prefijo',
                templateUrl: 'views/Prefijo.html',
                controller: 'PrefijoController',
                controllerAs: 'vm'
            })
            .state('Usuarios', {
                url: '/usuarios',
                templateUrl: 'views/Usuarios.html',
                controller: 'UsuarioController',
                controllerAs: 'vm'
            })
            .state('TareaInscripcion', {
                url: '/tarea_inscripcion',
                templateUrl: 'views/TareaInscripcion.html',
                controller: 'InscripcionController',
                controllerAs: 'vm'
            });
});
app.run( function ($rootScope, $http, $location, $localStorage) {
    // keep user logged in after page refresh
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    // redirect to login page if not logged in and trying to access a restricted page
    $rootScope.$on('$locationChangeStart', function (event, next, current) {
        var publicPages = ['/','/registro'];
        var restrictedPage = publicPages.indexOf($location.path()) === -1;
        if (restrictedPage && !$localStorage.currentUser) {
            $location.path('/');
        }
    });
});