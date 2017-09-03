//
//  LoginViewController.m
//  Stock
//
//  Created by 王凯 on 2017/8/24.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <SMS_SDK/SMSSDK.h>
#import "LoginViewController.h"

static int num = 60;

@interface LoginViewController ()<UITextFieldDelegate>
{
    NSTimer *time;
}

//手机号码输入框
@property (weak, nonatomic) IBOutlet UITextField *phoneText;

@property (weak, nonatomic) IBOutlet UIView *Verification;

@property (weak, nonatomic) IBOutlet UILabel *phoneNum;

@property (weak, nonatomic) IBOutlet UIView *VerificationView;

@property (weak, nonatomic) IBOutlet UITextField *codeText;

@property (weak, nonatomic) IBOutlet UILabel *Verification1;
@property (weak, nonatomic) IBOutlet UILabel *Verification2;
@property (weak, nonatomic) IBOutlet UILabel *Verification3;
@property (weak, nonatomic) IBOutlet UILabel *Verification4;
@property (weak, nonatomic) IBOutlet UILabel *Verification5;
@property (weak, nonatomic) IBOutlet UILabel *Verification6;

@property (weak, nonatomic) IBOutlet UIButton *againBtn;

@property (nonatomic, strong)   NSArray     *verificationLabs;

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    _verificationLabs = @[_Verification1,_Verification2,_Verification3,_Verification4,_Verification5,_Verification6];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
}

- (IBAction)clickNextBtn:(UIButton *)sender {
    if (_phoneText.text.length > 0 && [Utils validateNum:_phoneText.text]) {
        [self showSecondView];
//        [SMSSDK getVerificationCodeByMethod:SMSGetCodeMethodSMS phoneNumber:_phoneText.text zone:@"86" result:^(NSError *error) {
//            if (!error) {
//                [self showHint:@"验证码发送成功"];
//                [self showSecondView];
//            } else {
//                // error
//            }
//        }];
    }
}

/**
 退出登录页面的方法
 */
- (IBAction)clickCancelBtn:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

/**
 显示Verification页面
 */
- (void)showSecondView {
    [self.view endEditing:YES];
    [_phoneNum setText:_phoneText.text];
    [_codeText addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    [UIView animateWithDuration:0.4 animations:^{
        _Verification.hidden = NO;
        [self startTime];
    }];
}

/**
 退到输入手机号的页面
 */
- (IBAction)clickVerificationViewCancelBtn:(UIButton *)sender {
    [UIView animateWithDuration:0.4 animations:^{
        _Verification.hidden = YES;
    }];
}

//开始倒计时
- (void)startTime {
    self.againBtn.userInteractionEnabled = NO;
    time = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(updateBtnTitle) userInfo:nil repeats:YES];
}

- (void)updateBtnTitle {
    [self.againBtn setTitle:[NSString stringWithFormat:@"%d",num] forState:UIControlStateSelected];
    if (num > 0) {
        num --;
    } else if (num == 0) {
        num = 60;
        [time invalidate];
        time = nil;
        [self.againBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        self.againBtn.userInteractionEnabled = YES;
    }
}

#pragma mark - UITextFieldDelegate

- (void)textFieldDidChange:(UITextField *)textField {
    
    if (textField.text.length > 6) {
        textField.text = [textField.text substringToIndex:6];
    }
    for (UILabel *label in _verificationLabs) {
        [label setText:@""];
    }
    for (int i = 0; i < textField.text.length; i++) {
        UILabel *lab = _verificationLabs[i];
        NSString * value = [textField.text substringWithRange:NSMakeRange(i, 1)];
        lab.text = value;
    }
    textField.alpha = 0;
    NSLog(@"ShouldBegin = %@",textField.text);
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
    if (textField.text.length == 6) {
        [self commitCode:textField.text];
    }
    textField.text = @"";
    textField.alpha = 1;
}

- (void)commitCode:(NSString *)string {
    
    [SMSSDK commitVerificationCode:string phoneNumber:_phoneNum.text zone:@"86" result:^(NSError *error) {
        if (!error) {
            [self showHint:@"登录成功"];
            [[Config shareInstance] setIslogin:YES];
            if (_login_success) {
                _login_success();
            }
            [self dismissViewControllerAnimated:YES completion:nil];
        } else {
            
        }
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
