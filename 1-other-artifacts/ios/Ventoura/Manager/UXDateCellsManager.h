//
//  UXDateCellsManager.h
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

#import <Foundation/Foundation.h>

/**
 *  Provides methods to manage the logic of 'Start Date', 'End Date' and 'All day' 
 *  UITableViewCells, as found in the 'Add Event' section of many calendar apps.
 */
@interface UXDateCellsManager : NSObject

/**
 *  If it's currently visible, the index path of the date picker cell, otherwise nil.
 */
@property (strong, nonatomic) NSIndexPath *indexPathOfVisibleDatePicker;

/**
 *  The date displayed in the 'start date' cell.
 */
@property (strong, nonatomic) NSDate *startDate;

/**
 *  The date displayed in the 'end date' cell.
 */
@property (strong, nonatomic) NSDate *endDate;

/**
 *  If the displayed dates represent an event measured in entire days, this is true.
 */
@property (nonatomic) BOOL allDay;

/**
 *  The table view containing the date-choosing cells.
 */
@property (strong, nonatomic) UITableView *tableView;

/**
 *  The index path of the start date cell.
 */
@property (strong, nonatomic) NSIndexPath *indexPathForStartDateCell;

/**
 *  The index path of the end date cell when the picker is not visible.
 */
@property (strong, nonatomic) NSIndexPath *indexPathForEndDateCell;

/**
 *  The index path of the all-day cell.
 */
@property (strong, nonatomic) NSIndexPath *indexPathForAllDayCell;


/**
 *  Initializes a new instance of CABDateCellsManager with all parameters specified.
 *
 */
- (id)initWithTableView:(UITableView *)tableView
              startDate:(NSDate *)startDate
                endDate:(NSDate *)endDate
               isAllDay:(BOOL)allDay
 indexPathForAllDayCell:(NSIndexPath *)indexPathForAllDayCell
indexPathForStartDateCell:(NSIndexPath *)indexPathForStartDateCell
indexPathForEndDateCell:(NSIndexPath *)indexPathForEndDateCell;
- (id)initWithTableView:(UITableView *)tableView
              startDate:(NSDate *)startDate
                endDate:(NSDate *)endDate
               isAllDay:(BOOL)allDay
indexPathForStartDateCell:(NSIndexPath *)indexPathForStartDateCell
indexPathForEndDateCell:(NSIndexPath *)indexPathForEndDateCell;

/**
 *  Whether the index path specified is managed by this instance.
 */
- (BOOL)isManagedDateCell:(NSIndexPath *)indexPath;

/**
 *  The tableView should call this in its identically named delegate method for the cells managed by this instance.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 *  The tableView should call this in its identically named delegate method for the cells managed by this instance.
 */
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 *  The tableView should call this in its identically named delegate method for the cells managed by this instance.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 *  Updates the relevant cells when the date picker value is changed.
 */
- (void)dateChangedInPicker:(UIDatePicker *)datePicker;

/**
 *  Updates the relevant cells when the on button switch is changed.
 *
 *  @param onSwitch The UISwitch that was changed.
 */
- (void)onSwitchStateDidChange:(UISwitch *)onSwitch;

/**
 *  The actual index path of the end date cell, allowing for the fact that a date picker cell may have offset its usual position.
 */
- (NSIndexPath *)indexPathForEndDateCellActual;

/**
 *  The number of visible date pickers managed by this instance. Since there can currently only be one visible date picker at a time, this method will only return either 0 or 1.
 */
- (NSInteger)numberOfVisibleDatePickers;

/**
 *  The height of a cell containing a date picker.
 */
- (CGFloat)heightOfDatePickerCell;
@end
