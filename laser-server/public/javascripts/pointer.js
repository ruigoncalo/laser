var pointer = angular.module('pointer', []);

pointer.factory('socket', function ($rootScope) {
  var socket = io.connect(window.location.protocol + "//" + window.location.hostname  + ":33001");
  return {
    on: function (eventName, callback) {
      socket.on(eventName, function () {
        var args = arguments;
        $rootScope.$apply(function () {
          callback.apply(socket, args);
        });
      });
    },
    emit: function (eventName, data, callback) {
      socket.emit(eventName, data, function () {
        var args = arguments;
        $rootScope.$apply(function () {
          if (callback) {
            callback.apply(socket, args);
          }
        });
      })
    }
  };
});

pointer.controller('pointer', ['$scope','socket', function($scope, socket) {
    $scope.height =$(window).height()/2;
    $scope.width = $(window).width()/2;
    $scope.size = 20;
    socket.on('position', function (data) {
      $scope.coor = data;

      var h_aux = $scope.height+(-parseFloat(data.y));
      var w_aux = $scope.width+(-parseFloat(data.x));

      if( h_aux < $(window).height() && h_aux > 0)
        $scope.height+=-parseFloat(data.y);
      if( w_aux < $(window).width() && w_aux > 0)
        $scope.width+=-parseFloat(data.x);

      //$scope.size += parseFloat(data.z);
    });
}]);
