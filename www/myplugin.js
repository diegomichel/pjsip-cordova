var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'myplugin', 'coolMethod', [arg0]);
};

debug = function(s){
    console.log(s);
}

exports.coolMethod("Something", debug, debug)