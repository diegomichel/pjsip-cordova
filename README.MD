Plugin that allows your Cordova app to recieve calls from a SIP server.

Config is an array containing in order id, user, password and hostname.

cordova.plugins.myplugin.coolMethod(config, successCallBack, errorCallBack);

Example of usage:

onDeviceReady add the following code.

logger = (x) => console.log(x);
cordova.plugins.myplugin.coolMethod(['1000','1000','secretpassword','fs.myhost.com'], logger, logger);
