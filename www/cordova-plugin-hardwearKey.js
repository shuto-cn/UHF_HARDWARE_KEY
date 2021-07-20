var exec = require('cordova/exec');

var coolMethod = function () {};

coolMethod.startListen = function (success, error) {
    exec(success, error, 'hardwearKey', 'startListen', []);
}

coolMethod.stopListen = function (success, error) {
    exec(success, error, 'hardwearKey', 'stopListen', []);
}

module.exports = coolMethod;
