app.controller('ModalEditAbonadoController', [
    '$scope', '$element', 'abonado', 'close',
    function ($scope, $element, abonado, close) {

        $scope.abonado = abonado;
        $('.sidebar-division').hide();
        //  This close function doesn't need to use jQuery or bootstrap, because
        //  the button has the 'data-dismiss' attribute.
        $scope.close = function () {
            
            close({
                abonado: $scope.abonado
            }, 500); 
            $('.sidebar-division').show();
        };

        //  This cancel function must use the bootstrap, 'modal' function because
        //  the doesn't have the 'data-dismiss' attribute.
        $scope.cancel = function () {

            //  Manually hide the modal.
            $element.modal('hide');
            $('.sidebar-division').show();
            //  Now call close, returning control to the caller.
            close({
                abonado: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);
