//
//  HomeTableViewCell.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "HomeTableViewCell.h"

@implementation HomeTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _baseWidth.constant = K_FRAME_BASE_WIDTH;
    
    UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc]initWithTarget:self action:@selector(longPressAction:)];
    [self addGestureRecognizer:longPress];
}

- (void)updateCell:(StockObjEntity *)entity {
    self.name.text = entity.name;
    self.code.text = entity.code;
    self.price.text = entity.currentprice;
    
    NSString *PN = @"+";
    UIColor *color = UP_COLOR;
    if ([entity.pricefluctuation hasPrefix:@"-"]) {
        PN = @"-";
        _percentage = [[entity.pricefluctuation stringByReplacingOccurrencesOfString:@"-" withString:@""] floatValue]/10;
        _downValue.text = [entity.pricefluctuation stringByAppendingString:@"%"];
        color = DOWN_COLOR;
    } else {
        NSString *value = [entity.pricefluctuation stringByReplacingOccurrencesOfString:@"+" withString:@""];
        _percentage = [value floatValue]/10;
        _upValue.text = [entity.pricefluctuation stringByAppendingString:@"%"];
    }
    
    if (_percentage == 0) {
        PN = @"";
    }
    [self reloadright];

    self.pView = [[UIView alloc] init];
    self.pView.backgroundColor = color;
    
    if ([PN isEqual:@"+"]) {
        self.upView.hidden = NO;
        CGRect rect = [self.upValue.text boundingRectWithSize:CGSizeMake(1000, 25) options:NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:15]} context:nil];
        if (rect.size.width < _percentage * [self viewx]) {
            self.upValue.textColor = [UIColor whiteColor];
        }
    } else if ([PN isEqual:@"-"]) {
        self.downView.hidden = NO;
        CGRect rect = [self.downValue.text boundingRectWithSize:CGSizeMake(1000, 25) options:NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:15]} context:nil];
        if (rect.size.width < _percentage * [self viewx]) {
            self.downValue.textColor = [UIColor whiteColor];
        }
    }
    
    if ([entity.zsprice floatValue] == 0) {
        self.stopLabel.hidden = NO;
    }
}

- (float)viewx {
    float rightViewWidth = CGRectGetWidth(self.stopLabel.frame);
    float viewX = rightViewWidth/2;
    return viewX;
}

- (void)layoutSubviews {
    for (UIControl *control in self.subviews){
        if ([control isMemberOfClass:NSClassFromString(@"UITableViewCellEditControl")]){
            for (UIView *v in control.subviews)
            {
                if ([v isKindOfClass: [UIImageView class]]) {
                    UIImageView *img=(UIImageView *)v;
                    if (self.selected) {
                        img.image=[UIImage imageNamed:@"icon_pitch on"];
                    }else {
                        img.image=[UIImage imageNamed:@"icon_pitch off"];
                    }
                }
            }
        }
        if ([control isMemberOfClass:NSClassFromString(@"UITableViewCellContentView")]) {
            for (UIView *v in control.subviews)
            {
                if ([v isKindOfClass: [UIView class]]) {
                    v.backgroundColor = [UIColor clearColor];
                    if (v.subviews.count > 0) {
                        [v.subviews enumerateObjectsUsingBlock:^(__kindof UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                            if (obj.tag != 32133 ) {
                                if (self.editing) {
                                    obj.hidden = YES;
                                } else {
                                    obj.hidden = NO;
                                }
                            }
                            if (obj.tag == 32133) {
                                obj.backgroundColor = [UIColor whiteColor];
                            }
                            if (obj.tag == 32135) {
                                obj.hidden = NO;
                                obj.backgroundColor = [Utils colorFromHexRGB:@"D8D8D8"];
                            }
                        }];
                    }
                }
            }
        }
        if ([control isMemberOfClass:NSClassFromString(@"_UITableViewCellSeparatorView")]) {
            control.backgroundColor = [UIColor clearColor];
        }
    }
    
    [super layoutSubviews];
    
    if (!self.upView.hidden) {
        self.upWidth.constant = _percentage * CGRectGetWidth(self.upValue.frame);
        [self.upView addSubview:self.pView];
    } else {
        self.downWidth.constant = _percentage * CGRectGetWidth(self.downValue.frame);
        [self.downView addSubview:self.pView];
    }
    
}

- (void)reloadright {
    self.upView.hidden = YES;
    self.downView.hidden = YES;
    self.stopLabel.hidden = YES;
}

- (void)longPressAction:(UILongPressGestureRecognizer *)longPress {
    if (longPress.state == UIGestureRecognizerStateBegan) {
        NSLog(@"出现弹框");
        [self becomeFirstResponder];
        
//        UIMenuItem * copyItem=[[UIMenuItem alloc] initWithTitle:@"置顶" action:@selector(myTop:)];
        UIMenuItem * deleateItem=[[UIMenuItem alloc] initWithTitle:@"删除" action:@selector(myDeleate:)];
        
        //   获取UIMenuController单例
        UIMenuController * menuControl=[UIMenuController sharedMenuController];
        //   塞进UIMenuController中
        [menuControl setMenuItems:[NSArray arrayWithObjects:deleateItem ,nil]];
        //   设置要显示的位置
        [menuControl setTargetRect:CGRectMake(self.centerX, 10, 100, 100) inView:self.contentView];
        //   显示出来
        [menuControl setMenuVisible:YES animated:YES];
        
    }
}

//-(void)myTop:(id)sender
//{
//    NSLog(@"myCopy");
//}

-(void)myDeleate:(id)sender
{
    [Utils removeStock:self.code.text utilRequest:nil];
    if (self.clickMenuBlock) {
        self.clickMenuBlock();
    }
}

- (BOOL)canPerformAction:(SEL)action withSender:(id)sender
{
    NSLog(@"%@", NSStringFromSelector(action));
    if (action == @selector(myDeleate:)) {//action == @selector(myTop:)||
        return YES; // YES ->  代表我们只监听 cut: / copy: / paste:方法
    }
    return NO; // 除了上面的操作，都不支持
}

-(BOOL)canBecomeFirstResponder{
    return YES;
}

//适配第一次图片为空的情况
- (void)setEditing:(BOOL)editing animated:(BOOL)animated
{
    [super setEditing:editing animated:animated];
    for (UIControl *control in self.subviews){
        if ([control isMemberOfClass:NSClassFromString(@"UITableViewCellEditControl")]){
            for (UIView *v in control.subviews)
            {
                if ([v isKindOfClass: [UIImageView class]]) {
                    UIImageView *img=(UIImageView *)v;
                    if (!self.selected) {
                        img.image=[UIImage imageNamed:@"icon_pitch off"];
                    } else {
                        img.image=[UIImage imageNamed:@"icon_pitch on"];
                    }
                }
            }
        }
    }
    
}

@end
