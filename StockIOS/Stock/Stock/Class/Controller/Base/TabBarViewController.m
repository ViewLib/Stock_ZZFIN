//
//  TabBarViewController.m
//  Stock
//
//  Created by fred on 2017/8/29.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "TabBarViewController.h"

@interface TabBarViewController ()

@end

@implementation TabBarViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.view setBackgroundColor:[UIColor whiteColor]];
    
    [self addTabControllers];
}

- (void)addTabControllers{
    UIViewController *HomeVC = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Home"];
    HomeVC.tabBarItem.title = @"自选";
    HomeVC.tabBarItem.image = [UIImage imageNamed:@"home_off"];
    HomeVC.tabBarItem.selectedImage = [UIImage imageNamed:@"home_on"];
    
    UIViewController *FindVC = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Find"];
    FindVC.tabBarItem.title = @"发现";
    FindVC.tabBarItem.image = [UIImage imageNamed:@"find_off"];
    FindVC.tabBarItem.selectedImage = [UIImage imageNamed:@"find_on"];
    
    UIViewController *myVC = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"My"];
    myVC.tabBarItem.title = @"我的";
    myVC.tabBarItem.image = [UIImage imageNamed:@"my_off"];
    myVC.tabBarItem.selectedImage = [UIImage imageNamed:@"my_on"];
    
    NSArray *navsArr = [NSArray arrayWithObjects:HomeVC,FindVC,myVC,nil];//导航控制器
    
    [self setSelectedIndex:0];
    [self setViewControllers:navsArr];
    
    [[UITabBar appearance]setTintColor:MAIN_COLOR];
    
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
