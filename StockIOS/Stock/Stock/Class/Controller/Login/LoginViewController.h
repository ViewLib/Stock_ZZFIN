//
//  LoginViewController.h
//  Stock
//
//  Created by 王凯 on 2017/8/24.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController : UIViewController

//手机号码输入框
@property (weak, nonatomic) IBOutlet UITextField *phoneText;

@property (weak, nonatomic) IBOutlet UIView *VerificationView;

@property (weak, nonatomic) IBOutlet UILabel *Verification1;
@property (weak, nonatomic) IBOutlet UILabel *Verification2;
@property (weak, nonatomic) IBOutlet UILabel *Verification3;
@property (weak, nonatomic) IBOutlet UILabel *Verification4;
@property (weak, nonatomic) IBOutlet UILabel *Verification5;
@property (weak, nonatomic) IBOutlet UILabel *Verification6;
@property (weak, nonatomic) IBOutlet UIButton *againBtn;


@end
