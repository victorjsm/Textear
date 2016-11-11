app.directive('sidebar', function() { 
  return { 
    restrict: 'E', 
    templateUrl: 'directives/sidebar.html' 
  }; 
});

app.directive('navbar', function() { 
  return { 
    restrict: 'E', 
    templateUrl: 'directives/navbar.html' 
  }; 
});

app.directive('fileReader', function () {
    return {
        scope: {
            fileReader: "="
        },
        link: function (scope, element) {
            $(element).on('change', function (changeEvent) {
                var files = changeEvent.target.files;
                if (files.length) {
                    var r = new FileReader();
                    r.onload = function (e) {
                        var contents = e.target.result;
                        scope.$apply(function () {
                            
                            scope.fileReader = JSON.parse(csvJSON(contents));
                        });
                    };

                    r.readAsText(files[0]);
                }
            });
        }
    };
});
function csvJSON(csv) {

    var lines = csv.split("\n");

    var result = [];

    var headers = lines[0].split(",");

    for (var i = 1; i < lines.length-1; i++) {

        var obj = {};
        var currentline = lines[i].split(",");

        for (var j = 0; j < headers.length; j++) {
            obj[headers[j]] = currentline[j];
        }

        result.push(obj);

    }

    //return result; //JavaScript object
    return JSON.stringify(result); //JSON
};
