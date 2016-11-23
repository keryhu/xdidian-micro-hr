/**
 * Created by hushuming on 2016/11/21.
 */

angular.module('app', [])
.controller('loginCtl',['$scope',function ($scope) {
    // 当ip blocked 的时候，隐藏 input submit
    $scope.hiddenForm=msg!=null;
}]);