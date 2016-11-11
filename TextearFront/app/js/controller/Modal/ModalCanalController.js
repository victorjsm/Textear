/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('ModalCreateCanalController', [
    '$scope', '$element', 'prefijos', 'close',
    function ($scope, $element, prefijos, close) {

        $('.sidebar-division').hide();

        $scope.prefijos = prefijos;
        $scope.precios = [];
        $scope.canal = {};


        $scope.close = function () {
            
            close({
                canal: $scope.canal,
                precios: $scope.precios
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
                canal: null,
                precios: []
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);

app.controller('ModalEditCanalController', [
    '$scope', '$element', 'canal', 'close',
    function ($scope, $element, canal, close) {

        $scope.canal = canal;
        $scope.canal2 = {
            descripcion:null,
            longitud:null,
            precioRecibir:null,
            precioEnviar:null
        };
        $scope.prefijos = {};
        $('.sidebar-division').hide();
        //  This close function doesn't need to use jQuery or bootstrap, because
        //  the button has the 'data-dismiss' attribute.
        $scope.close = function () {

            close({
                canal: $scope.canal,
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
                canal: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);
