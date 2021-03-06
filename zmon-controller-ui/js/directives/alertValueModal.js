angular.module('zmon2App').directive('alertValueModal', [ '$uibModal', 'APP_CONST', function($uibModal, APP_CONST) {
    return {
        restrict: 'A',
        scope: {
            name: '=',
            value: '=',
            open: '&'
        },
        link: function(scope, elem, attrs) {

            if (typeof scope.value === 'string') {
                try {
                    scope.value = JSON.parse(scope.value);
                } catch(e) {}
            }

            var modalCtrl = function($scope, $uibModalInstance, name, value) {
                $scope.filter = '';
                $scope.alert = {
                    name: name,
                    value: JSON.stringify(value, null, APP_CONST.INDENT),
                    ts: new Date()
                };

                $scope.close = function() {
                    $uibModalInstance.dismiss();
                };

                $scope.$watch('filter', function(filter) {
                    if (!filter) {
                        $scope.alert.value = JSON.stringify(value, null, APP_CONST.INDENT);
                        return;
                    }

                    $scope.valid = false;

                    var jp = [];
                    try {
                        jp = jsonpath.nodes(value, filter);
                    } catch (e) {
                        return;
                    }

                    // here's were the magic happens, adds parent keys to jsonpath nodes
                    var filteredValue = _.map(jp, function(n) {
                        var node = n.value;         // avoid jsonpath's root level: {'$': ... }
                        if (n.path.length > 1) {
                            node =  {};
                            node[n.path[n.path.length-1]] = n.value;
                        }
                        return node;
                    }, []);

                    if (filteredValue.length) {
                        filteredValue = filteredValue.length === 1 ? filteredValue[0] : filteredValue;
                        $scope.alert.value = JSON.stringify(filteredValue, null, APP_CONST.INDENT);
                        $scope.valid = true;
                    }
                });
            };

            var open = function() {
                var modalInstance = $uibModal.open({
                    templateUrl: '/templates/alertValueModal.html',
                    controller: modalCtrl,
                    backdrop: false,
                    windowClass: 'alert-value-modal-window',
                    resolve: {
                        name: function() {
                            return scope.name;
                        },
                        value: function() {
                            return scope.value;
                        }
                    }
                });
            };

            elem.on('click', open);
        }
    };
}]);
