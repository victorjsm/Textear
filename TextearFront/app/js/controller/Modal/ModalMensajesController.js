app.controller('ModalMensajesController', [
    '$scope', 'mensaje', 'reload', 'close', '$window', '$timeout',
    function ($scope, mensaje, reload, close, $window, $timeout) {
        $scope.mensaje = mensaje;
        $('.sidebar-division').hide();

        $timeout(function () {
            Cerrar();
        }, 1500);


        function Cerrar() {

            close(); // close, but give 500ms for bootstrap to animate
            $('.modal-backdrop').remove();
            $('.sidebar-division').show();
            if (reload) {
                $window.location.reload();
            }
        }
        ;



    }]);


