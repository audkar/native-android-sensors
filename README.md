# Native Android sensors example

Ugly fugly example of android sensors working in background for long period of time. 
Data collected of Bluetooth, Location and Light sensors. 
Foreground service is used to ensure background work is not interrupted by system.

#### Key aspects
 - Data is being collected in background independently if Android Activity exists or not.
 - Data from all providers is shown in system notifications bar.
 - After resuming to app (after Activity destory) new activity shows all collected data while application was working in background.
 
#### Possible scenarios to check backround work correctness
 - Press `back` button while activity is in Foreground.
 - Turn-on `Do not keep activities` in Dev settings.
 - Running on low resources device for longer period of time / filling memory from other apps.

#### Recorded video & expected result:
<img src="https://github.com/audkar/native_android_sensors/raw/master/app_record.gif" width="200" />
