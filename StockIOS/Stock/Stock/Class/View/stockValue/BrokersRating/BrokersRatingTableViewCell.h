//
//  BrokersRatingTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ValueCollection.h"

@interface BrokersRatingTableViewCell : UITableViewCell<UICollectionViewDataSource,UICollectionViewDelegate>

@property (weak, nonatomic) IBOutlet UICollectionView *menuCollection;
@property (weak, nonatomic) IBOutlet UIView *valueView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *valueViewHigh;

@property (strong, nonatomic) NSArray   *titleAry;
@property (strong, nonatomic) NSArray   *stockGradeAry;
@property (strong, nonatomic) NSString   *stockCode;

@property (strong, nonatomic) ValueCollection *pjbhView;


@end
