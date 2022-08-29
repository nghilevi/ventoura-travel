//
//  UXSwitchCell.m
//
//  Copyright (c) 2014 Craig Brown
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
//

#import "UXSwitchCell.h"

@implementation UXSwitchCell

#pragma mark - Initialization
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        [self commonInit];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        // Initialization code
        [self commonInit];
    }
    return self;
}

- (void)commonInit
{
    // Initialize switch
    self.onSwitch = [[UISwitch alloc] init];
    
}


#pragma mark - Adding the date picker
- (void)layoutSubviews
{
    [super layoutSubviews];
    
    UILabel *textLabel = self.textLabel;
    UISwitch *onSwitch = self.onSwitch;
    
    // Stop constraints being created based on autoresizing mask
    [textLabel setTranslatesAutoresizingMaskIntoConstraints:NO];
    [onSwitch setTranslatesAutoresizingMaskIntoConstraints:NO];
    
    
    // Set font of textLabel
    textLabel.font = [UIFont systemFontOfSize:17];
    
    // Add switch to content view
    [self.contentView addSubview:onSwitch];
    
    
}

+ (BOOL)requiresConstraintBasedLayout
{
    return YES;
}

- (void)updateConstraints
{
    // Only add constraints if onSwitch has been added to superview and if content view has no constraints yet
    if (self.onSwitch.superview && [[self.contentView constraints] count] == 0) {
        
        // Set up dictionary of views
        UILabel *textLabel = self.textLabel;
        UISwitch *onSwitch = self.onSwitch;
        NSDictionary *views = NSDictionaryOfVariableBindings(textLabel, onSwitch);
        
        // Create horizontal constraints
        NSArray *constraintsH =
        [NSLayoutConstraint constraintsWithVisualFormat:@"H:|-15-[textLabel]-(>=8)-[onSwitch]-15-|"
                                                options:0
                                                metrics:nil
                                                  views:views];
        
        // Create vertical constraints
        NSLayoutConstraint *constraintVSwitch =
        [NSLayoutConstraint constraintWithItem:onSwitch
                                     attribute:NSLayoutAttributeCenterY
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:onSwitch.superview
                                     attribute:NSLayoutAttributeCenterY
                                    multiplier:1.0
                                      constant:0.0];
        
        NSLayoutConstraint *constraintVLabel =
        [NSLayoutConstraint constraintWithItem:textLabel
                                     attribute:NSLayoutAttributeCenterY
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:textLabel.superview
                                     attribute:NSLayoutAttributeCenterY
                                    multiplier:1.0
                                      constant:0.0];
        
        
        
        [self.contentView addConstraints:constraintsH];
        [self.contentView addConstraint:constraintVSwitch];
        [self.contentView addConstraint:constraintVLabel];
    }
    
    [super updateConstraints];
}

@end
