#!/bin/bash
DEVICE=${1?Error: Please specify the emulation device to run}
emulator -avd $DEVICE &

adb wait-for-device

A=$(adb shell getprop sys.boot_completed | tr -d '\r')

while [ "$A" != "1" ]; do
        sleep 2
        A=$(adb shell getprop sys.boot_completed | tr -d '\r')
done


./gradlew installDebug 
adb shell am start -a android.intentOA.action.MAIN -n com.example.demo/.MainActivity
