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

pointer.controller('pointerctrl', ['$scope','socket', function($scope, socket) {
    $scope.height =$(window).height()/2;
    $scope.width = $(window).width()/2;
    $scope.size = 20;
    socket.on('position', function (data) {
      $scope.coor = data;

      $scope.height -= parseFloat(data.y);
      $scope.width -= parseFloat(data.x);

      var h_aux = $scope.height;
      var w_aux = $scope.width;

      if( h_aux < 0)
        $scope.height = 0;

      if( h_aux > $(window).height())
        $scope.height = $(window).height();

      if( w_aux < 0)
        $scope.width = 0;

      if( w_aux > $(window).width())
        $scope.width = $(window).width();

      //$scope.size += parseFloat(data.z);
    });
}]);
