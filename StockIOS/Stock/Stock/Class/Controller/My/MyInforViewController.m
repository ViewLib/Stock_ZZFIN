//
//  MyInforViewController.m
//  Stock
//
//  Created by mac on 2017/11/5.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "MyInforViewController.h"
#import "XHInputView.h"

@interface MyInforViewController ()<XHInputViewDelagete>

@end

@implementation MyInforViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    if ([Config shareInstance].login) {
        
    }
    
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//点击头像修改
- (IBAction)clickHeaderImgBtn:(UIButton *)sender {
    
}

//点击昵称修改
- (IBAction)clickNickNameBtn:(UIButton *)sender {
    [self showXHInputViewWithStyle:InputViewStyleLarge AndValueLabel:_nickName];
}

//点击性别修改
- (IBAction)clickSexBtn:(UIButton *)sender {
    [self sexChangeActionSheet:^(NSString *sex) {
        _sex.text = sex;
    }];
}

//点击地址修改
- (IBAction)clickAddressBtn:(UIButton *)sender {
    [self showXHInputViewWithStyle:InputViewStyleLarge AndValueLabel:_address];
}

//点击返回
- (IBAction)clickReturnBtn:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)showXHInputViewWithStyle:(InputViewStyle)style AndValueLabel:(UILabel *)label{
    
    [XHInputView showWithStyle:style configurationBlock:^(XHInputView *inputView) {
        /** 请在此block中设置inputView属性 */
        
        /** 代理 */
        inputView.delegate = self;
        /** 占位符文字 */
        inputView.placeholder = label.text;
        /** 设置最大输入字数 */
        inputView.maxCount = 50;
        /** 输入框颜色 */
        inputView.textViewBackgroundColor = [UIColor groupTableViewBackgroundColor];
        
        /** 更多属性设置,详见XHInputView.h文件 */
        
    } sendBlock:^BOOL(NSString *text) {
        if(text.length){
            NSLog(@"输入的信息为:%@",text);
            label.text = text;
            return YES;//return YES,收起键盘
        }else{
            NSLog(@"显示提示框-请输入要评论的的内容");
            return NO;//return NO,不收键盘
        }
    }];
    
}

#pragma mark - XHInputViewDelagete
/** XHInputView 将要显示 */
-(void)xhInputViewWillShow:(XHInputView *)inputView{
    
    /** 如果你工程中有配置IQKeyboardManager,并对XHInputView造成影响,请在XHInputView将要显示时将其关闭 */
    
    [IQKeyboardManager sharedManager].enableAutoToolbar = NO;
    [IQKeyboardManager sharedManager].enable = NO;
    
}

/** XHInputView 将要影藏 */
-(void)xhInputViewWillHide:(XHInputView *)inputView{
    
    /** 如果你工程中有配置IQKeyboardManager,并对XHInputView造成影响,请在XHInputView将要影藏时将其打开 */
    
    [IQKeyboardManager sharedManager].enableAutoToolbar = YES;
    [IQKeyboardManager sharedManager].enable = YES;
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
