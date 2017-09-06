//
//  SearchViewController.m
//  Stock
//
//  Created by mac on 2017/9/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "SearchViewController.h"

@interface SearchViewController ()

@end

@implementation SearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.definesPresentationContext = YES;
    
}

#pragma mark - SearchController

- (void)willPresentSearchController:(UISearchController *)searchController {
    UIView *searchBarTextField = nil;
    if ([[UIDevice currentDevice].systemVersion floatValue] > 10.0) {
        UIView *searchBackground = [[searchController.searchBar.subviews.firstObject subviews] objectAtIndex:0];
        searchBackground.alpha = 0;
        searchController.view.backgroundColor = MAIN_COLOR;
        searchBarTextField = [[searchController.searchBar.subviews.firstObject subviews] objectAtIndex:1];
    } else {
        searchController.searchBar.backgroundImage = SEARCHBAR_BGIMG;
        searchBarTextField = [[searchController.searchBar.subviews.firstObject subviews] lastObject];
    }
    searchBarTextField.backgroundColor = MAIN_COLOR;
}

- (void)updateSearchResultsForSearchController:(UISearchController *)searchController {
    searchController.searchResultsController.view.hidden = NO;
}

#pragma mark - UISearchBarDelegate
- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar{
    
    return YES;
}
- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    
}
- (BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar {
    
    
    return YES;
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar {
    
}
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
    
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
