//
//  LoginViewController.m
//  Stock
//
//  Created by 王凯 on 2017/8/24.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <SMS_SDK/SMSSDK.h>
#import "LoginViewController.h"
#import "loginEntity.h"

static int num = 60;

@interface LoginViewController ()<UITextFieldDelegate,UIPickerViewDelegate,UIPickerViewDataSource>

//手机区号选择
@property (weak, nonatomic) IBOutlet UIButton *phoneCode;
//手机号码输入框
@property (weak, nonatomic) IBOutlet UITextField *phoneText;

@property (weak, nonatomic) IBOutlet UIView *picBgView;

@property (weak, nonatomic) IBOutlet UIButton *oneNextBtn;

@property (weak, nonatomic) IBOutlet UIPickerView *pickerView;

@property (weak, nonatomic) IBOutlet UIView *Verification;

@property (weak, nonatomic) IBOutlet UILabel *phoneNum;

@property (weak, nonatomic) IBOutlet UIView *VerificationView;

@property (weak, nonatomic) IBOutlet UITextField *codeText;

@property (weak, nonatomic) IBOutlet UILabel *Verification1;
@property (weak, nonatomic) IBOutlet UILabel *Verification2;
@property (weak, nonatomic) IBOutlet UILabel *Verification3;
@property (weak, nonatomic) IBOutlet UILabel *Verification4;

@property (weak, nonatomic) IBOutlet UIButton *againBtn;

@property (strong, nonatomic)   NSTimer    *timer;

@property (nonatomic, strong)   NSArray     *verificationLabs;

@property (nonatomic, strong)   NSString    *btnTitle;

@property (nonatomic, strong)   NSString    *key;


@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    _verificationLabs = @[_Verification1,_Verification2,_Verification3,_Verification4];
    [self.phoneCode ImgRightTextLeft];
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
    [self racOneNextBtn];
}

- (IBAction)clickPhoneCode:(UIButton *)sender {
    self.picBgView.hidden = !self.picBgView.hidden;
}

#pragma mark - UIPickerViewDelegateAndDataSourece
//指定pickerview有几个表盘
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

//指定每个表盘上有几行数据
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [Config shareInstance].areacode.count;
}

- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component
{
    return K_FRAME_BASE_WIDTH;
}

- (CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
{
    return 40;
}

//指定每行如何展示数据（此处和tableview类似）
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    NSDictionary *dic = [Config shareInstance].areacode[row];
    NSString * title = dic[@"key"];
    return title;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    NSDictionary *dic = [Config shareInstance].areacode[row];
    self.btnTitle = dic[@"key"];
    self.key = dic[@"value"];
}

- (IBAction)clickSuccessBtn:(UIButton *)sender {
    self.picBgView.hidden = YES;
    [self.phoneCode setTitle:[NSString stringWithFormat:@"+%@",self.key] forState:UIControlStateNormal];
}

- (IBAction)clickHIddenBtn:(UIButton *)sender {
    self.picBgView.hidden = YES;
}

- (IBAction)clickNextBtn:(UIButton *)sender {
    if (_phoneText.text.length > 0 && [Utils validateNum:_phoneText.text]) {
//        [self showSecondView];
        [SMSSDK getVerificationCodeByMethod:SMSGetCodeMethodSMS phoneNumber:_phoneText.text zone:self.key result:^(NSError *error) {
            if (!error) {
                [self showHint:@"验证码发送成功"];
                [self showSecondView];
            } else {
                [self showHint:@"验证码发送失败"];
            }
        }];
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
    self.timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(updateBtnTitle) userInfo:nil repeats:YES];
}

- (void)updateBtnTitle {
    [self.againBtn setTitle:[NSString stringWithFormat:@"%d",num] forState:UIControlStateNormal];
    if (num > 0) {
        num --;
    } else if (num == 0) {
        num = 60;
        [self.timer invalidate];
        self.timer = nil;
        [self.againBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        self.againBtn.userInteractionEnabled = YES;
    }
}

#pragma mark - UITextFieldDelegate

- (void)textFieldDidChange:(UITextField *)textField {
    
    if (textField.text.length > 4) {
        textField.text = [textField.text substringToIndex:4];
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
    if (textField.text.length == 4) {
        [self commitCode:textField.text];
    }
    textField.text = @"";
    textField.alpha = 1;
}

- (void)commitCode:(NSString *)string {
    [self showHudInView:self.Verification hint:@"正在登录"];
    [SMSSDK commitVerificationCode:string phoneNumber:_phoneNum.text zone:self.key result:^(NSError *error) {
        if (!error) {
            [self login];
        } else {
            [self hideHud];
            [self showHint:@"验证码认证失败"];
        }
    }];
}

- (void)login {
    [[HttpRequestClient sharedClient] registerWithPhone:[NSString stringWithFormat:@"%@_%@",_key ,_phoneNum.text] request:^(NSString *resultMsg, id dataDict, id error) {
        [self hideHud];
        if (dataDict) {
            [[Config shareInstance] setIslogin:YES];
            [Config shareInstance].login = [[loginEntity alloc] initWithDictionary:dataDict];
            [[NSUserDefaults standardUserDefaults] setValue:[NSString stringWithFormat:@"%@_%@",_key ,_phoneNum.text] forKey:UserLogin];
            if (_login_success) {
                _login_success();
            }
            [self showHint:@"注册/登录成功"];
            [self dismissViewControllerAnimated:YES completion:nil];
        } else {
            [self showHint:resultMsg];
        }
    }];
}

- (void)racOneNextBtn {
    NSMutableArray *ary = [NSMutableArray arrayWithObject:_phoneText];
    
    WS(self)
    RACSignal *newSignal = [RACSignal rac_tfs:ary];
    [newSignal subscribeNext:^(id x) {
        selfWeak.oneNextBtn.userInteractionEnabled = [x boolValue];
        if (selfWeak.oneNextBtn.userInteractionEnabled) {
            [selfWeak.oneNextBtn setBackgroundColor:MAIN_COLOR];
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
