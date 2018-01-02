var exec = require('cordova/exec');

exports.coolMethod = function (config, success, error) {
    exec(success, error, 'MyPlugin', 'coolMethod', config);
};

debug = function(s){
    console.log(s);
}
