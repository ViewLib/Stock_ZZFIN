//
//  StockValueViewController.m
//  Stock
//
//  Created by mac on 2017/9/23.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockValueViewController.h"

@interface StockValueViewController ()

@end

@implementation StockValueViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (IBAction)clickReturnBtn:(UIButton *)sender {
    CATransition *animation = [CATransition animation];
    
    animation.duration = .5;
    
    animation.timingFunction = UIViewAnimationCurveEaseInOut;
    
    animation.type = kCATransitionPush;
    
    animation.subtype = kCATransitionFromLeft;
    
    [self.view.window.layer addAnimation:animation forKey:nil];
    [self dismissViewControllerAnimated:NO completion:nil];
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
