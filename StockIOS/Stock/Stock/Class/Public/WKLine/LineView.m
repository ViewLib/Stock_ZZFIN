//
//  LineView.m
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import "LineView.h"
#import "LineBgScrollView.h"
#import <Masonry/Masonry.h>
#import "WKSLineView.h"
#import "WKButtomView.h"
#import "WKMaskView.h"

@interface LineView(){
    //图表最大的价格
    CGFloat maxValue;
    //图表最小的价格
    CGFloat minValue;
}

@property (nonatomic, strong) LineBgScrollView *stockScrollView;

@property (nonatomic, strong) WKSLineView *Line;

@property (nonatomic, strong) NSArray *listNews;

@property (nonatomic, strong) WKButtomView *ButtomView;

@property (nonatomic, strong) NSArray *valueList;

/**
 当前绘制在屏幕上的数据源数组
 */
@property (nonatomic, strong) NSMutableArray *drawLineModels;

/**
 当前绘制在屏幕上的处理后的数据源数组
 */
@property (nonatomic, strong) NSArray *drawLines;



@end


@implementation LineView

- (id)initWithFrame:(CGRect)frame andViewData:(NSArray *)viewData {
    if (self = [super initWithFrame:frame]) {
        [self init_UIScrallView];
        if (viewData.count > 0) {
            self.valueList = viewData;
            self.drawLines = [NSMutableArray array];
            self.drawLineModels = [NSMutableArray array];
            [self reDrawWithLineModels];
        }
    }
    return self;
}

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];

    if (self.valueList.count > 0) {
        //更新绘制的数据源
        [self updateDrawModels];
        //更新背景线
        [self.stockScrollView setNeedsDisplay];
        //绘制K线上部分
        
        self.drawLines = [self.Line drawViewWithXPosition:[self xPosition] drawModels:self.drawLineModels maxValue:maxValue minValue:minValue];
        
        [self.ButtomView drawViewWithXPosition:[self xPosition] drawModels:self.drawLineModels linePositionModels:self.drawLines];
        
        [self drawLeftDesc];
    }
}

/*
scrollView滑动重绘页面
*/
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    [self setNeedsDisplay];
}

/**
 更新需要绘制的数据源
 */
- (void)updateDrawModels {
    
    NSInteger startIndex = [self startIndex];
    NSInteger drawLineCount = (self.stockScrollView.frame.size.width) / (LINEGAP +  LINE);
    
    [self.drawLineModels removeAllObjects];
    NSInteger length = startIndex+drawLineCount < self.valueList.count ? drawLineCount+1 : self.valueList.count - startIndex;
    [self.drawLineModels addObjectsFromArray:[self.valueList subarrayWithRange:NSMakeRange(startIndex, length)]];
    
    //更新最大值最小值-价格
    CGFloat max =  [[[self.drawLineModels valueForKeyPath:@"close"] valueForKeyPath:@"@max.floatValue"] floatValue];
    
    CGFloat min =  [[[self.drawLineModels valueForKeyPath:@"close"] valueForKeyPath:@"@min.floatValue"] floatValue];
    
    max = max+max/20;
    
    CGFloat average = (min+max) / 2;
    maxValue = max;
    minValue = average * 2 - maxValue;
}

//算每次的初始X
- (NSInteger)xPosition {
    NSInteger leftArrCount = [self startIndex];
    CGFloat startXPosition = (leftArrCount + 1) * LINEGAP + leftArrCount * LINE + LINE/2;
    return startXPosition;
}

//初始的index
- (NSInteger)startIndex {
    CGFloat offsetX = self.stockScrollView.contentOffset.x < 0 ? 0 : self.stockScrollView.contentOffset.x;

    NSUInteger leftCount = ABS(offsetX) / (LINEGAP + LINE);
    
    if (leftCount > self.valueList.count) {
        leftCount = self.valueList.count - 1;
    }
    return leftCount;
}

- (void)init_UIScrallView {
    _stockScrollView = [LineBgScrollView new];
    _stockScrollView.backgroundColor = [UIColor colorWithWhite:1 alpha:0.2];//[UIColor yellowColor];
    _stockScrollView.showsHorizontalScrollIndicator = NO;
    _stockScrollView.delegate = self;
    
    [self addSubview:_stockScrollView];
    [_stockScrollView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self);
        make.left.equalTo(self).offset(45);
        make.top.equalTo(self).offset(25);
        make.right.equalTo(self).offset(-12);
    }];
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(controlViewClick_1:)];
    [_stockScrollView addGestureRecognizer:tap];
    
    _Line = [WKSLineView new];
    _Line.backgroundColor = [UIColor clearColor];
    [_stockScrollView.contentView addSubview:_Line];
    [_Line mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.equalTo(_stockScrollView.contentView);
        make.height.equalTo(_stockScrollView.contentView).multipliedBy(LINERADIO);
    }];
    
    //加载VolumeView
    _ButtomView = [WKButtomView new];
    _ButtomView.backgroundColor = [UIColor clearColor];
    _ButtomView.parentScrollView = _stockScrollView;
    [_stockScrollView.contentView addSubview:_ButtomView];
    [_ButtomView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.bottom.equalTo(_stockScrollView.contentView);
        make.height.equalTo(_stockScrollView.contentView).multipliedBy(BUTTOMRADIO);
    }];
    
}

- (void)drawLeftDesc {
    
    NSDictionary *attribute = @{NSFontAttributeName:[UIFont systemFontOfSize:9],NSForegroundColorAttributeName:[UIColor redColor]};
    CGSize textSize = [self rectOfNSString:[NSString stringWithFormat:@"%.2f",(maxValue + minValue)/2.f] attribute:attribute].size;
    CGFloat unit = self.stockScrollView.frame.size.height * LINERADIO / 5.f;
    CGFloat unitValue = (maxValue - minValue)/4.f;
    //顶部间距
    for (int i = 0; i < 6; i++) {
        NSString *text = [NSString stringWithFormat:@"%.2f",maxValue - unitValue * i];
        CGPoint drawPoint = CGPointMake((45 - textSize.width)/2, unit * i + 25 - textSize.height/2.f);
        [text drawAtPoint:drawPoint withAttributes:attribute];
    }
}

/**
 重绘视图
 
 @param lineModels  数据源
 */
- (void)reDrawWithLineModels {
    
    [self layoutIfNeeded];
    [self updateScrollViewContentWidth];
    [self setNeedsDisplay];
    if (self.valueList.count > 0) {
        self.stockScrollView.contentOffset = CGPointMake(self.stockScrollView.contentSize.width - self.stockScrollView.bounds.size.width, self.stockScrollView.contentOffset.y);
    }
}

- (void)reloadNewView:(NSArray *)news {
    
    self.listNews = news;
    if (self.listNews.count > 0) {
        WS(self)
        self.Line.getNewsData = ^BOOL (NSString *string) {
            __block BOOL isHave;
            __block NSMutableString *time = string.mutableCopy;
            [time insertString:@"-" atIndex:4];
            [time insertString:@"-" atIndex:7];
            [selfWeak.listNews enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                if ([obj[@"eventDate"] isEqual:time]) {
                    isHave = YES;
                }
            }];
            return isHave;
        };
    }
    [self reDrawWithLineModels];
}

- (CGFloat)updateScrollViewContentWidth {
    //根据数据的个数和间隔和宽度计算出self的宽度，并设置contentsize
    CGFloat kLineViewWidth = self.valueList.count * LINE + (self.valueList.count + 1) * LINEGAP;
    
    if(kLineViewWidth < self.stockScrollView.bounds.size.width) {
        kLineViewWidth = self.stockScrollView.bounds.size.width;
    }
    
    //更新scrollview的contentsize
    self.stockScrollView.contentSize = CGSizeMake(kLineViewWidth, self.stockScrollView.contentSize.height);
    return kLineViewWidth;
}

//计算string的宽度
- (CGRect)rectOfNSString:(NSString *)string attribute:(NSDictionary *)attribute {
    CGRect rect = [string boundingRectWithSize:CGSizeMake(MAXFLOAT, 0)
                                       options:NSStringDrawingTruncatesLastVisibleLine |NSStringDrawingUsesLineFragmentOrigin |
                   NSStringDrawingUsesFontLeading
                                    attributes:attribute
                                       context:nil];
    return rect;
}

//点击事件
- (void)controlViewClick_1:(UITapGestureRecognizer *)tap {
    NSLog(@"进入点击");
    NSLog(@"%f", [tap locationInView:self.stockScrollView].x - self.stockScrollView.contentOffset.x);
    
    static CGFloat oldPositionX = 0;
    
    CGPoint location = [tap locationInView:self.stockScrollView];
    if (location.x < 0 || location.x > self.stockScrollView.contentSize.width) return;
    
    //暂停滑动
    oldPositionX = location.x;
    NSInteger startIndex = (NSInteger)((oldPositionX - [self xPosition] + (LINE+LINEGAP)/2.f) / (LINE+LINEGAP));
    
    if (startIndex < 0) startIndex = 0;
    if (startIndex >= self.drawLineModels.count) startIndex = self.drawLineModels.count - 1;
    
    //长按位置没有数据则退出
    if (startIndex < 0) {
        return;
    }
    NSArray *ary;
    if (startIndex+1 < self.drawLineModels.count && startIndex > 1) {
        ary = [self.drawLineModels subarrayWithRange:NSMakeRange(startIndex-1, 2)];
    } else if (startIndex > 1) {
        ary = [self.drawLineModels subarrayWithRange:NSMakeRange(startIndex-1, 2)];
    } else {
        ary = [self.drawLineModels subarrayWithRange:NSMakeRange(0, 1)];
    }
    
    NSMutableArray *dayStrs = [NSMutableArray array];
    [ary enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        NSMutableString *str = [NSMutableString stringWithString:obj[@"day"]];
        [str insertString:@"-" atIndex:4];
        [str insertString:@"-" atIndex:7];
        [dayStrs addObject:str];
    }];
    
    [self.listNews enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([dayStrs indexOfObject:obj[@"eventDate"]] != NSNotFound) {
            WKMaskView *mask = [[WKMaskView alloc] initWithFrame:self.bounds Dic:obj];
            [self addSubview:mask];
        }
    }];
    
}

@end
