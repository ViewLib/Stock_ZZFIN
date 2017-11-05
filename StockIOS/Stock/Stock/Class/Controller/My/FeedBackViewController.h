//
//  FeedBackViewController.h
//  Stock
//
//  Created by mac on 2017/11/5.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FeedBackViewController : UIViewController

@property (weak, nonatomic) IBOutlet UITextView *value;

@property (weak, nonatomic) IBOutlet UIView *secView;

@property (weak, nonatomic) IBOutlet UIView *thrView;

@property (weak, nonatomic) IBOutlet UITextField *numTextField;
@end
