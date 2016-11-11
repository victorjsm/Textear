app.controller('ModalCreateGrupoController', [
    '$scope', '$element', 'abonados', 'close',
    function ($scope, $element, abonados, close) {

        //  Variables asociadas a la seleccion de mensajes con los checkbox

        $scope.selected = {};
        var selectedAction = {};
        var selectedAbo = {};
        $scope.selectAll = false;
        var selectOne = false;
        
        //  Funciones relacionadas a la seleccion de los elementos en la tabla de
//  abonados

        $scope.toggleAll = function (selectAll, selectedItems) {
            for (var ci in selectedItems) {
                selectedItems[ci] = selectAll;
                if (selectedAction.hasOwnProperty(ci)) {
                    delete selectedAction[ci];
                } else {
                    selectedAction[ci] = true;
                }
                ;
            }
            selectOne = !jQuery.isEmptyObject(selectedAction);
        };
        
        $scope.toggleNone = function () {
            for (var ci in $scope.selected) {
                if (selectedAction.hasOwnProperty(ci)) {
                    delete selectedAction[ci];
                    delete selectedAbo[ci];
                    $scope.selected = false;
                    selectOne = false;
                }
            }
        };
        
        $scope.addselect = function (ci,abonado) {
            if (selectedAction.hasOwnProperty(ci)) {
                delete selectedAction[ci];
                delete selectedAbo[ci];
            } else {
                selectedAction[ci] = true;
                selectedAbo[ci] = abonado;
            }
            ;
            selectOne = !jQuery.isEmptyObject(selectedAction);
        };





        $scope.abonados = abonados;
        $('.sidebar-division').hide();
        
        
        
        //  This close function doesn't need to use jQuery or bootstrap, because
        //  the button has the 'data-dismiss' attribute.
        $scope.close = function () {
            
            var abonadosFinal = [];
            if (selectOne){
                var i = 0;
                for (var ci in $scope.selected) {
                    if (selectedAbo.hasOwnProperty(ci)) {
                        abonadosFinal[i] = selectedAbo[ci];
                        i = i+1;
                    }
                }
            } else {
                for (var i = 0; i < $scope.fileContent.length; i++) {
                    var temp2 = ({
                        telefono: $scope.fileContent[i].Telefono,
                        ci: $scope.fileContent[i].CI,
                        nombre: $scope.fileContent[i].Nombre
                    });
                    abonadosFinal[i] = temp2;
                };
            }
            close({
                    abonados: abonadosFinal,
                    nombre: $scope.nombre
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
                abonados: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);


app.controller('ModalEditGrupoController', [
    '$scope', '$element', '$resource', '$localStorage', 'grupo', 'close',
    function ($scope, $element, $resource, $localStorage, grupo, close) {

        $scope.nombre = grupo.nombre;

        var seagrega = false;


        var GruposGET = $resource('http://localhost:8080/Textear2/webresources/classes.grupo/gruposGETabo/:rif/:nombre',
                {rif: '@RIF', nombre: '@Nombre'});

        GruposGET.query({rif: $localStorage.currentUser.empresa.rif, nombre: grupo.nombre})
                .$promise.then(function (temp) {
                    $scope.abonados = temp;
                });


        var AbonadoGET = $resource('http://localhost:8080/Textear2/webresources/classes.abonado/abonadosGET/:rif', {rif: '@RIF'});

        AbonadoGET.query({rif: $localStorage.currentUser.empresa.rif})
                .$promise.then(function (temp) {
                    $scope.abonados2 = temp;
                    var lista = temp;
                    for (var i = 0; i < lista.length; i++) {
                        $scope.selected[lista[i].ci] = false;
                    }
                    ;
                });

//  Variables asociadas a la seleccion de mensajes con los checkbox

        $scope.selected = {};
        var selectedAction = {};
        var selectedAbo = {};
        $scope.selectAll = false;
        $scope.selectOne = false;
        var agregar = false;

//  Variables asociadas a mensajes de exito o fallo en las
//  diferentes operaciones

        $scope.loading = false;


//  Funciones relacionadas a la seleccion de los elementos en la tabla de
//  abonados

        $scope.toggleAll = function (selectAll, selectedItems, secambia) {
            for (var ci in selectedItems) {
                selectedItems[ci] = selectAll;
                if (selectedAction.hasOwnProperty(ci)) {
                    delete selectedAction[ci];
                    delete selectedAbo[ci];
                } else {
                    selectedAction[ci] = true;
                    selectedAbo[ci] = abonado;
                }
                ;
            }
            agregar = !jQuery.isEmptyObject(selectedAction);
            if (secambia) {
                $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
            }
        };

        $scope.toggleNone = function () {
            for (var ci in $scope.selected) {
                if (selectedAction.hasOwnProperty(ci)) {
                    delete selectedAction[ci];
                    delete selectedAbo[ci];
                    $scope.selected[ci] = false;
                    $scope.selectOne = false;
                }
            }
        };


        $scope.addselect = function (ci, abonado, secambia) {
            if (selectedAction.hasOwnProperty(ci)) {
                delete selectedAction[ci];
                delete selectedAbo[ci];
            } else {
                selectedAction[ci] = true;
                selectedAbo[ci] = abonado;
            }
            ;
            agregar = !jQuery.isEmptyObject(selectedAction);
            if (secambia) {
                $scope.selectOne = !jQuery.isEmptyObject(selectedAction);
            }


        };



        $('.sidebar-division').hide();


//  This close function doesn't need to use jQuery or bootstrap, because
//  the button has the 'data-dismiss' attribute.
        $scope.close = function () {
            var temp = {
                arreglo: [],
                agrego: seagrega
            };
            if (seagrega) {
                if (agregar) {
                    for (var ci in $scope.selected) {
                        if (selectedAbo.hasOwnProperty(ci)) {
                            temp.arreglo.push(selectedAbo[ci]);
                        }
                    }
                } else {
                    for (var i = 0; i < $scope.fileContent.length; i++) {
                        var temp2 = ({
                            telefono: $scope.fileContent[i].Telefono,
                            ci: $scope.fileContent[i].CI,
                            nombre: $scope.fileContent[i].Nombre
                        });
                        temp.arreglo.push(temp2);
                    }
                    ;
                }
            } else {
                for (var ci in selectedAction) {
                    var temp2 = ({
                        telefono: '',
                        ci: ci,
                        nombre: '',
                        rif: $localStorage.currentUser.empresa.rif
                    });
                    temp.arreglo.push(temp2);
                }
                ;
            }

            close({
                arreglo: temp.arreglo
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
                resultado: null
            }, 500); // close, but give 500ms for bootstrap to animate
        };


//  Funciones utiles
        $scope.agregando = function () {
            seagrega = true;
        };

    }]);
