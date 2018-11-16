var exec = require('cordova/exec');

function SunmiCordova() {

}

SunmiCordova.prototype.print = function (successCallback, errorCallback, config) {
    if (config instanceof Array) {
        // do nothing
    } else {
        if (typeof (config) === 'object') {
            config = [config];
        } else {
            config = [];
        }
    }

    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("SunmiCordova.printText failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("SunmiCordova.printText failure: success callback parameter must be a function");
        return;
    }

    exec(
        function (result) {
            successCallback(result);
        },
        function (error) {
            errorCallback(error);
        },
        'SunmiPlugin',
        'print',
        config
    );
}

var sunmiCordova = new SunmiCordova();
module.exports = sunmiCordova;