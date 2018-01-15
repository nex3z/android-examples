import {NativeModules} from 'react-native';

export default {
    'system': NativeModules.RnDeviceInfo.system,
    'systemVersion': NativeModules.RnDeviceInfo.systemVersion,
    'language': NativeModules.RnDeviceInfo.language,
    'appVersion': NativeModules.RnDeviceInfo.appVersion
}
