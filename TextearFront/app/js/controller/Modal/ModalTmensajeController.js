app.controller('ModalCreateTmensajeController', [
    '$scope', '$element', 'abonados', 'grupos', 'bandejas', 'canales', 'close',
    function ($scope, $element, abonados, grupos, bandejas, canales, close) {

        //  Variables asociadas a la seleccion de mensajes con los checkbox

        $scope.selected = {};
        var selectedAction = {};
        var selectedElem = {};
        $scope.selectAll = false;
        var selectOne = false;
        var aboSelected = true;

        $scope.abonados = abonados;
        $scope.grupos = grupos;
        $scope.bandejas = bandejas;
        $scope.canales = canales;


        $scope.selecciono = function (canal, canales) {
            for (var x in canales) {
                if (canales[x].codigo === canal) {
                    $scope.tamano = canales[x].longitud;
                    $scope.tarea.mensaje = "";
                }
            }
        };


        //  Funciones relacionadas a la seleccion de los elementos en la tabla de
//  abonados

        $scope.setChosen = function (bool) {
            aboSelected = bool;
        };

        $scope.toggleAll = function (selectAll, selectedItems) {
            for (var id in selectedItems) {
                selectedItems[id] = selectAll;
                if (selectedAction.hasOwnProperty(id)) {
                    delete selectedAction[id];
                } else {
                    selectedAction[id] = true;
                }
                ;
            }
            selectOne = !jQuery.isEmptyObject(selectedAction);
        };

        $scope.toggleNone = function () {
            for (var id in $scope.selected) {
                if (selectedAction.hasOwnProperty(id)) {
                    delete selectedAction[id];
                    delete selectedElem[id];
                    $scope.selected[id] = false;
                    selectOne = false;
                }
            }
        };

        $scope.addselect = function (id, value) {
            if (selectedAction.hasOwnProperty(id)) {
                delete selectedAction[id];
                delete selectedElem[id];
            } else {
                selectedAction[id] = true;
                selectedElem[id] = value;
            }
            ;
            selectOne = !jQuery.isEmptyObject(selectedAction);
        };






        $('.sidebar-division').hide();



        //  This close function doesn't need to use jQuery or bootstrap, because
        //  the button has the 'data-dismiss' attribute.
        $scope.close = function (valid) {
            if (valid) {
                var ListaFinal = [];
                var i = 0;
                for (var ci in $scope.selected) {
                    if ($scope.selected[ci]) {
                        ListaFinal[i] = selectedElem[ci];
                        i = i + 1;
                    }
                }

                close({
                    destinatarios: ListaFinal,
                    esabonado: aboSelected,
                    mensaje: $scope.tarea
                }, 500);
                $('.modal-backdrop').remove();
                $('.sidebar-division').show();
            }
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
                destinatarios: [],
                esabonado: false,
                mensaje: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);



app.controller('ModalEditTmensajeController', [
    '$scope', '$element', 'mensaje', 'edit', 'abonados', 'grupos', 'bandejas', 'canales', 'close',
    function ($scope, $element, mensaje, edit, abonados, grupos, bandejas, canales, close) {

        //  Variables asociadas a la seleccion de mensajes con los checkbox

        $scope.selected = {};
        var selectedAction = {};
        var selectedElem = {};
        var aboStorage = abonados;
        $scope.selectAll = false;
        $scope.selectOne = false;
        $scope.agregarAbo = false;
        var eliminar = false;


        var aboSelected = true;

        $scope.tarea = mensaje;
        $scope.edit = edit;


        $scope.abonados = abonados;
        $scope.grupos = grupos;
        $scope.bandejas = bandejas;
        $scope.canales = canales;




        //  Funciones relacionadas a la seleccion de los elementos en la tabla de
//  abonados

        $scope.agregarAbonados = function () {
            $scope.agregarAbo = true;
            $scope.toggleNone();
        };

        $scope.elim = function () {
            eliminar = true;
        };

        $scope.cambio = function () {
            $scope.edit = true;
        };

        $scope.setChosen = function (bool) {
            aboSelected = bool;
        };

        $scope.toggleAll = function (selectAll, selectedItems) {
            for (var id in selectedItems) {
                selectedItems[id] = selectAll;
                if (selectedAction.hasOwnProperty(id)) {
                    delete selectedAction[id];
                } else {
                    selectedAction[id] = true;
                }
                ;
            }
            $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
        };

        $scope.toggleNone = function () {
            for (var id in $scope.selected) {
                if (selectedAction.hasOwnProperty(id)) {
                    delete selectedAction[id];
                    delete selectedElem[id];
                    $scope.selected[id] = false;
                    $scope.selectOne = false;
                }
            }
        };

        $scope.addselect = function (id, value) {
            if (selectedAction.hasOwnProperty(id)) {
                delete selectedAction[id];
                delete selectedElem[id];
            } else {
                selectedAction[id] = true;
                selectedElem[id] = value;
            }
            ;
            $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
        };






        $('.sidebar-division').hide();



        //  This close function doesn't need to use jQuery or bootstrap, because
        //  the button has the 'data-dismiss' attribute.
        $scope.close = function () {

            var ListaFinal = [];
            var i = 0;
            for (var ci in $scope.selected) {
                if ($scope.selected[ci]) {
                    ListaFinal[i] = selectedElem[ci];
                    i = i + 1;
                }
            }
            close({
                abonados: aboStorage,
                destinatarios: ListaFinal,
                esabonado: aboSelected,
                mensaje: $scope.tarea,
                eliminar: eliminar
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
                destinatarios: [],
                esabonado: false,
                mensaje: null,
                elimiar: false
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);
