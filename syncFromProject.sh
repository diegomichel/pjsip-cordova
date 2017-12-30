# Script to copy files from project back to plugin
# Reason: Plugin development is easier on android studio.
B
# Instructions: Change PROJECT_DIR to a cordova project folder.

PROJECT_DIR="/Users/diego/cordovaplayground/myapp/"
JAVA_DIR="${PROJECT_DIR}platforms/android/src/org/nov/myplugin/"
JS_DIR="${PROJECT_DIR}platforms/android/platform_www/plugins/org.nov.pjsip-cordova/www/"

cp $JAVA_DIR* src/android/
cp $JS_DIR* www/
