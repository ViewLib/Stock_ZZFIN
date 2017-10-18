//
//  Globalconfig.h
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#ifndef Globalconfig_h
#define Globalconfig_h

#define K_FRAME_BASE_WIDTH      [[UIScreen mainScreen]bounds].size.width
#define K_FRAME_BASE_HEIGHT     [[UIScreen mainScreen]bounds].size.height
#define K_FRAME_HEAD_HEIGHT     20
#define K_FRAME_NAVIGATION_BAR_HEIGHT   44
#define K_FRAME_VIEW_ORIGIN_Y   64

#define K_FRAME_CONTENT_HEIGHT  K_FRAME_BASE_HEIGHT-K_FRAME_NAVIGATION_BAR_HEIGHT-K_FRAME_TABBAR_BACKGROUND_HEIGHT-K_FRAME_HEAD_HEIGHT

#define IS_IPHONE_5 ( fabs( ( double )[ [ UIScreen mainScreen ] bounds ].size.height - ( double )568 ) < DBL_EPSILON )

#define rgba(r,g,b,a)[UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:a/1.0]

#define main_bounds [UIScreen mainScreen].bounds

#define MAIN_COLOR              [Utils colorFromHexRGB:@"186DB7"]
#define UP_COLOR                [Utils colorFromHexRGB:@"FA5259"]
#define DOWN_COLOR              [Utils colorFromHexRGB:@"4CB774"]

#define WS(obj) /*autoreleasepool{}*/ __weak typeof(obj) obj##Weak = obj;
#define SS(obj) __strong typeof(obj) obj##Strong = obj;
#define BS(obj) autoreleasepool{} __block typeof(obj) obj##Block = obj;

#define SEARCHBAR_BGIMG         [Utils GetImageWithColor:MAIN_COLOR andHeight:K_FRAME_NAVIGATION_BAR_HEIGHT]

#define isLogin                 [Config shareInstance].islogin

#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define kScreenWidth [UIScreen mainScreen].bounds.size.width
#define kScreenHeight [UIScreen mainScreen].bounds.size.height
#define SCREEN_MAX_LENGTH MAX(kScreenWidth,kScreenHeight)
#define IS_IPHONE_X (IS_IPHONE && SCREEN_MAX_LENGTH == 812.0)

#define SERVICE                 @"http://115.159.31.128:8090"

#endif /* Globalconfig_h */
