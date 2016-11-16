app.controller('ModalCreateAbonadoController', [
    '$scope', '$element', 'close', '$localStorage',
    function ($scope, $element, close, $localStorage) {

        $('.sidebar-division').hide();
        $scope.abonados = [];
        $scope.abonado = null;

        $scope.clear = function () {
            $scope.abonado = null;
        };


        $scope.close = function () {
            if ($scope.abonado !== null) {
                $scope.abonado.rifEmpresa = $localStorage.currentUser.empresa.rif;
                $scope.abonado.negra = false;
                $scope.abonados[0] = $scope.abonado;
            } else {
                for (var i = 0; i < $scope.fileContent.length; i++) {
                    var temp2 = ({
                        telefono: $scope.fileContent[i].Telefono,
                        ci: $scope.fileContent[i].CI,
                        nombre: $scope.fileContent[i].Nombre,
                        rifEmpresa: $localStorage.currentUser.empresa.rif,
                        negra: false
                    });
                    $scope.abonados[i] = temp2;
                }
                ;
            }

            close({
                abonados: $scope.abonados
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
            close(null, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);



app.controller('ModalEditAbonadoController', [
    '$scope', '$element', 'abonado', 'close',
    function ($scope, $element, abonado, close) {

        $scope.edit = false;
        $scope.cambio = function () {
            $scope.edit = true;
        };

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
