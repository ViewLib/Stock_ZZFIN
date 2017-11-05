//
//  FeedBackViewController.m
//  Stock
//
//  Created by mac on 2017/11/5.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "FeedBackViewController.h"

@interface FeedBackViewController ()

@end

@implementation FeedBackViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.value.layer.borderColor = [Utils colorFromHexRGB:@"979797"].CGColor;
    self.value.layer.borderWidth = 1;
    
    self.secView.layer.borderColor = [Utils colorFromHexRGB:@"979797"].CGColor;
    self.secView.layer.borderWidth = 1;
    
    self.thrView.layer.borderColor = [Utils colorFromHexRGB:@"979797"].CGColor;
    self.thrView.layer.borderWidth = 1;
}


- (IBAction)clickReturnBtn:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
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
