/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller('ModalCreateUsuarioController', [
    '$scope', '$element', '$localStorage', 'close',
    function ($scope, $element, $localStorage, close) {

        $('.sidebar-division').hide();

        $scope.tipos = ['basico', 'medio', 'avanzado'];
        $scope.u = {
            empresa: $localStorage.currentUser.empresa
        };


        $scope.close = function () {

            close({
                usuario: $scope.u
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
               usuario: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);

app.controller('ModalEditUsuarioController', [
    '$scope', '$element', 'usuario', 'close',
    function ($scope, $element, usuario, close) {

        $scope.usuario = usuario;
        $scope.cambio = function () {
            $scope.edit = true;
        };
        $scope.tipos = ['basico', 'medio', 'avanzado'];
        $('.sidebar-division').hide();
        //  This close function doesn't need to use jQuery or bootstrap, because
        //  the button has the 'data-dismiss' attribute.
        $scope.close = function () {

            close({
                usuario: $scope.usuario,
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
                usuario: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);