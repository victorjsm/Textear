/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('ModalCreatePrefijoController', [
    '$scope', '$element', 'close',
    function ($scope, $element, close) {

        $('.sidebar-division').hide();

        $scope.precios = [];
        $scope.prefijo = {};


        $scope.close = function () {

            close({
                prefijo: $scope.prefijo
            }, 500);
            $('.modal-backdrop').remove();
            $('.sidebar-division').show();
        };

        //  This cancel function must use the bootstrap, 'modal' function because
        //  the doesn't have the 'data-dismiss' attribute.
        $scope.cancel = function () {

            //  Manually hide the modal.
            $element.modal('hide');
            $('.modal-backdrop').remove();
            $('.sidebar-division').show();
            //  Now call close, returning control to the caller.
            close({
                prefijo: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);

app.controller('ModalEditPrefijoController', [
    '$scope', '$element', 'prefijo', 'close',
    function ($scope, $element, prefijo, close) {

        $scope.prefijo = prefijo;
        $('.sidebar-division').hide();
        //  This close function doesn't need to use jQuery or bootstrap, because
        //  the button has the 'data-dismiss' attribute.
        $scope.close = function () {

            close({
                prefijo: $scope.prefijo,
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
                prefijo: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);