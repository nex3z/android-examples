//
//  RnDeviceInfo.m
//  RnNativeModule
//
//  Copyright © 2018 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RnDeviceInfo.h"

@implementation RnDeviceInfo

RCT_EXPORT_MODULE();

- (NSDictionary *) constantsToExport {
  UIDevice *currentDevice = [UIDevice currentDevice];
  return @{
           @"system": currentDevice.systemName,
           @"systemVersion": currentDevice.systemVersion,
           @"language": self.deviceLanguage,
           @"appVersion": [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"],
           };
}

- (NSString *) deviceLanguage {
  NSString *language = [[NSLocale preferredLanguages] objectAtIndex:0];
  return language;
}

//- (NSString *) deviceCountry {
//  NSString *country = [[NSLocale currentLocale] objectForKey: NSLocaleCountryCode];
//  return country;
//}

@end
