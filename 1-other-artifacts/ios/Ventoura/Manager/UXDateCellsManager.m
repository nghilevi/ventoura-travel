//
//  UXDateCellsManager.m
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

#import "UXDateCellsManager.h"
#import "UXDatePickerCell.h"
#import "UXSwitchCell.h"
#import "NSCalendar+UX.h"

static NSString * const UXDatePickerCellIdentifier = @"UXDatePickerCell";
static NSString * const UXSwitchCellIdentifier = @"UXSwitchCell";
static NSString * const RightDetailCellIdentifier = @"RightDetailCell";

@interface UXDateCellsManager ()

// Date formatters
@property (strong, nonatomic) NSDateFormatter *dateFormatter;
@property (strong, nonatomic) NSDateFormatter *timeFormatter;
@property (strong, nonatomic) NSDateFormatter *allDayDateFormatter;

@end


@implementation UXDateCellsManager

#pragma mark - Initializers
- (id)init
{
    return [self initWithTableView:nil
                         startDate:[NSDate date]
                           endDate:[NSDate date]
                          isAllDay:NO
            indexPathForAllDayCell:nil
         indexPathForStartDateCell:nil
           indexPathForEndDateCell:nil];
}

- (id)initWithTableView:(UITableView *)tableView
              startDate:(NSDate *)startDate
                endDate:(NSDate *)endDate
               isAllDay:(BOOL)allDay
 indexPathForAllDayCell:(NSIndexPath *)indexPathForAllDayCell
indexPathForStartDateCell:(NSIndexPath *)indexPathForStartDateCell
indexPathForEndDateCell:(NSIndexPath *)indexPathForEndDateCell
{
    self = [super init];
    if (self) {
        // Set properties
        self.tableView = tableView;
        self.startDate = startDate;
        self.endDate = endDate;
        self.allDay = allDay;
        
        // Set up date formatters
        self.dateFormatter = [[NSDateFormatter alloc] init];
        self.dateFormatter.dateFormat = [NSDateFormatter dateFormatFromTemplate:@"dd MMM yyyy"
                                                                        options:0
                                                                         locale:[NSLocale currentLocale]];
        self.timeFormatter = [[NSDateFormatter alloc] init];
        self.timeFormatter.dateFormat = [NSDateFormatter dateFormatFromTemplate:@"HH:mm"
                                                                        options:0
                                                                         locale:[NSLocale currentLocale]];
        self.allDayDateFormatter = [[NSDateFormatter alloc] init];
        self.allDayDateFormatter.dateFormat = [NSDateFormatter dateFormatFromTemplate:@"EEE dd MMM yyyy"
                                                                              options:0
                                                                               locale:[NSLocale currentLocale]];
        
        // Set indexPaths
        self.indexPathForAllDayCell = indexPathForAllDayCell;
        self.indexPathForStartDateCell = indexPathForStartDateCell;
        self.indexPathForEndDateCell = indexPathForEndDateCell;
    }
    
    return self;
}

- (id)initWithTableView:(UITableView *)tableView
              startDate:(NSDate *)startDate
                endDate:(NSDate *)endDate
               isAllDay:(BOOL)allDay
indexPathForStartDateCell:(NSIndexPath *)indexPathForStartDateCell
indexPathForEndDateCell:(NSIndexPath *)indexPathForEndDateCell
{
    self = [super init];
    if (self) {
        // Set properties
        self.tableView = tableView;
        self.startDate = startDate;
        self.endDate = endDate;
        self.allDay = allDay;
        
        // Set up date formatters
        self.dateFormatter = [[NSDateFormatter alloc] init];
        self.dateFormatter.dateFormat = [NSDateFormatter dateFormatFromTemplate:@"dd MMM yyyy"
                                                                        options:0
                                                                         locale:[NSLocale currentLocale]];
        self.timeFormatter = [[NSDateFormatter alloc] init];
        self.timeFormatter.dateFormat = [NSDateFormatter dateFormatFromTemplate:@"HH:mm"
                                                                        options:0
                                                                         locale:[NSLocale currentLocale]];
        self.allDayDateFormatter = [[NSDateFormatter alloc] init];
        self.allDayDateFormatter.dateFormat = [NSDateFormatter dateFormatFromTemplate:@"EEE dd MMM yyyy"
                                                                              options:0
                                                                               locale:[NSLocale currentLocale]];
        
        // Set indexPaths
        self.indexPathForStartDateCell = indexPathForStartDateCell;
        self.indexPathForEndDateCell = indexPathForEndDateCell;
    }
    
    return self;
}


#pragma mark - Setup Cells

- (void)setupStartDateCell:(UITableViewCell **)cell
{
    (*cell).textLabel.text = @"Arrival";
    (*cell).backgroundColor = [UIColor clearColor];
    (*cell).textLabel.textColor = [UIColor whiteColor];
    (*cell).detailTextLabel.text = [self localizedDateTimeStringFromDate:self.startDate];
    (*cell).detailTextLabel.textColor = [UIColor whiteColor];
}

- (void)setupEndDateCell:(UITableViewCell **)cell
{
    (*cell).textLabel.text = @"Departure";
    (*cell).detailTextLabel.text = [self localizedEndDateStringFromEndDate:self.endDate
                                                             withStartDate:self.startDate];
}

- (void)setupAllDayCell:(UXSwitchCell **)cell
{
    (*cell).textLabel.text = @"All-day";
    (*cell).onSwitch.on = self.allDay;
    // Turn off selection
    [*cell setSelectionStyle:UITableViewCellSelectionStyleNone];
}

- (void)setupDatePickerCell:(UXDatePickerCell **)cell withIndexPath:(NSIndexPath *)indexPath
{
    // Work out mode of date picker based on whether the event is 'all-day'
    (*cell).datePicker.datePickerMode = self.allDay ? UIDatePickerModeDate : UIDatePickerModeDateAndTime;
    // Set date of datePicker
    // If date picker is for startDate:
    if ([self.indexPathForStartDateCell isEqual:[NSIndexPath indexPathForRow:indexPath.row - 1 inSection:indexPath.section]]) {
        [(*cell).datePicker setDate:self.startDate animated:NO];
        
        // Can pick any date (a minimum may have been set by the end date cell - see below)
        (*cell).datePicker.minimumDate = nil;
    }
    // If date picker is for endDate:
    else {
        [(*cell).datePicker setDate:self.endDate animated:NO];
        
        // Don't allow user to pick a date before startDate
        (*cell).datePicker.minimumDate = self.startDate;
    }
}


#pragma mark - Date helper methods
- (NSIndexPath *)indexPathForEndDateCellActual
{
    NSIndexPath *indexPathForEndDate = self.indexPathForEndDateCell;
    
    NSIndexPath *datePickerIndex = self.indexPathOfVisibleDatePicker;
    
    // Check if we need to add 1 to the row to account for the visible date picker
    if (datePickerIndex != nil && datePickerIndex.section == indexPathForEndDate.section && datePickerIndex.row <= indexPathForEndDate.row) {
        indexPathForEndDate = [NSIndexPath indexPathForRow:indexPathForEndDate.row + 1
                                                 inSection:indexPathForEndDate.section];
    }
    
    return indexPathForEndDate;
}

- (void)refreshDateStrings
{
    // Get cells containing date strings
    UITableViewCell *startDateCell = [self.tableView cellForRowAtIndexPath:self.indexPathForStartDateCell];
    UITableViewCell *endDateCell = [self.tableView cellForRowAtIndexPath:self.indexPathForEndDateCellActual];
    
    // Update them depending on the allDay value
    if (self.allDay) {
        startDateCell.detailTextLabel.text = [self localizedDateStringForAllDayEventFromDate:self.startDate];
        endDateCell.detailTextLabel.text = [self localizedDateStringForAllDayEventFromDate:self.endDate];
    }
    else {
        startDateCell.detailTextLabel.text = [self localizedDateTimeStringFromDate:self.startDate];
        endDateCell.detailTextLabel.text = [self localizedEndDateStringFromEndDate:self.endDate withStartDate:self.startDate];
    }
}

- (NSString *)localizedDateTimeStringFromDate:(NSDate *)date
{
    return [NSString stringWithFormat:@"%@\t%@",
            [self.dateFormatter stringFromDate:date],
            [self.timeFormatter stringFromDate:date]];
}

- (NSString *)localizedEndDateStringFromEndDate:(NSDate *)date withStartDate:(NSDate *)startDate
{
    // Only show time bit if date bit is the same
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    if ([calendar date:date isInSameDayAsDate:startDate]) {
        return [self.timeFormatter stringFromDate:date];
    }
    // Show full date if on a different day
    else {
        return [self localizedDateTimeStringFromDate:date];
    }
}

- (NSString *)localizedDateStringForAllDayEventFromDate:(NSDate *)date
{
    return [self.allDayDateFormatter stringFromDate:date];
}

- (void)setDateOnVisibleDatePicker:(NSDate *)date
{
    // Get the index path of the currently visible date picker
    NSIndexPath *indexPath = self.indexPathOfVisibleDatePicker;
    
    // Exit if no date picker is currently visible
    if (indexPath == nil) return;
    
    // Get the cell containing the date picker
    UXDatePickerCell *cell = (UXDatePickerCell *)[self.tableView cellForRowAtIndexPath:indexPath];
    
    // Set the date
    [cell.datePicker setDate:date animated:NO];
}

- (void)toggleDatePickerForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableView *tableView = self.tableView;
    
    // Get indexPath of (potential) datePicker cell below this cell
    NSIndexPath *indexPathOfVisibleDatePicker = [NSIndexPath indexPathForRow:indexPath.row + 1
                                                                   inSection:indexPath.section];
    
    [tableView beginUpdates];
    
    // Case: No currently visible picker
    if (self.indexPathOfVisibleDatePicker == nil) {
        
        // Remember the indexPath of the picker cell
        self.indexPathOfVisibleDatePicker = indexPathOfVisibleDatePicker;
        // Insert the picker cell
        [tableView insertRowsAtIndexPaths:@[indexPathOfVisibleDatePicker]
                         withRowAnimation:UITableViewRowAnimationMiddle];
        // Change date colour on cell
            
        UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        cell.detailTextLabel.textColor = [self selectedDateColor];
        
    }
    
    // Case: Picker is currently visible for the selected cell
    else if ([self.indexPathOfVisibleDatePicker isEqual:indexPathOfVisibleDatePicker]) {
        
        // Set picker cell property to nil
        self.indexPathOfVisibleDatePicker = nil;
        
        // Delete the picker cell
        [tableView deleteRowsAtIndexPaths:@[indexPathOfVisibleDatePicker]
                         withRowAnimation:UITableViewRowAnimationMiddle];
        
        // Change date colour on cell
        UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        cell.detailTextLabel.textColor = [UIColor blackColor];
    }
    
    // Case: Picker is currently visible for a different cell
    else {
        
        // Do nothing (caller should remove visible picker first)
    }
    
    [tableView endUpdates];
}

- (UIColor *)selectedDateColor
{
    return [ventouraUtility ventouraBlue];
}

- (NSInteger)numberOfVisibleDatePickers
{
    return self.indexPathOfVisibleDatePicker == nil ? 0 : 1;
}

- (CGFloat)heightOfDatePickerCell
{
    UXDatePickerCell *datePickerCell = [self.tableView dequeueReusableCellWithIdentifier:UXDatePickerCellIdentifier];
    CGFloat height = datePickerCell.datePicker.bounds.size.height;
    return (height == 0) ? 216 : height;
}


#pragma mark - Selecting rows

- (BOOL)isManagedDateCell:(NSIndexPath *)indexPath
{
    if ([indexPath isEqual:self.indexPathForAllDayCell] ||
        [indexPath isEqual:self.indexPathForStartDateCell] ||
        [indexPath isEqual:self.indexPathForEndDateCellActual] ||
        [indexPath isEqual:self.indexPathOfVisibleDatePicker]) {
        return true;
    }
    else
    {
        return false;
    }
}

- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([indexPath isEqual:self.indexPathForAllDayCell]) {
        return nil;
    }
    else
    {
        return indexPath;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSIndexPath *indexPathForStartDateCell = self.indexPathForStartDateCell;
    NSIndexPath *indexPathForEndDateCell = self.indexPathForEndDateCellActual;
    // For indexPaths which show a date picker cell when pressed
    if ([indexPath isEqual:indexPathForStartDateCell] ||
        [indexPath isEqual:indexPathForEndDateCell]) {
        
        // If picker already visible for a different cell, remember where it was and remove it
        NSIndexPath *pickerIndex = self.indexPathOfVisibleDatePicker;
        if (pickerIndex != nil && (pickerIndex.row - 1 != indexPath.row || pickerIndex.section != indexPath.section)) {
            pickerIndex = self.indexPathOfVisibleDatePicker;
            NSIndexPath *cellShowingPicker = [NSIndexPath indexPathForRow:pickerIndex.row - 1
                                                                inSection:pickerIndex.section];
            [self toggleDatePickerForRowAtIndexPath:cellShowingPicker];
            
            // Get the new indexPath for this cell (accounting for the offset in
            // row number where the just-deleted picker was before this one)
            if (pickerIndex.section == indexPath.section && pickerIndex.row <= indexPath.row) {
                indexPath = [NSIndexPath indexPathForRow:indexPath.row - 1
                                               inSection:indexPath.section];
            }
        }
        
        // Toggle the date picker
        [self toggleDatePickerForRowAtIndexPath:indexPath];
        
        if ([indexPath isEqual:indexPathForStartDateCell]) {
            [self setDateOnVisibleDatePicker:self.startDate];
        }
        else if ([indexPath isEqual:indexPathForEndDateCell]) {
            [self setDateOnVisibleDatePicker:self.endDate];
        }
        
        // Deselect the row
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *identifier;
    
    // Work out identifier to use based on indexPath
    if ([indexPath isEqual:self.indexPathOfVisibleDatePicker]) {
        identifier = UXDatePickerCellIdentifier;
    }
    else if ([indexPath isEqual:self.indexPathForAllDayCell]) {
        identifier = UXSwitchCellIdentifier;
    }
    else {
        identifier = RightDetailCellIdentifier;
    }
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    // If cell couldn't be dequeued, create a new one
    if (!cell) {
        if (identifier == UXDatePickerCellIdentifier) {
            cell = [[UXDatePickerCell alloc] initWithStyle:UITableViewCellStyleDefault
                                          reuseIdentifier:identifier];
        }
        else if (identifier == UXSwitchCellIdentifier) {
            cell = [[UXSwitchCell alloc] initWithStyle:UITableViewCellStyleDefault
                                       reuseIdentifier:identifier];
        }
        else {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1
                                          reuseIdentifier:identifier];
        }
    }
    
    // Change the color and font size of the detailTextLabel
    cell.detailTextLabel.textColor = [UIColor blackColor];
    cell.detailTextLabel.font = [UIFont systemFontOfSize:16];
    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    // All-day cell
    if ([indexPath isEqual:self.indexPathForAllDayCell]) {
        cell.textLabel.text = @"All-day";
        ((UXSwitchCell *)cell).onSwitch.on = self.allDay;
        // Turn off selection
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        // Add action
        [[(UXSwitchCell *)cell onSwitch] addTarget:self action:@selector(onSwitchStateDidChange:) forControlEvents:UIControlEventValueChanged];
    }
    // Start Date cell
    else if ([indexPath isEqual:self.indexPathForStartDateCell]) {
        cell.textLabel.text = @"Arrival";
        cell.textLabel.textColor = [UIColor whiteColor];
        cell.detailTextLabel.text = [self localizedDateTimeStringFromDate:self.startDate];
        cell.detailTextLabel.textColor  = [UIColor whiteColor];
        cell.backgroundColor = [UIColor clearColor];
        
    }
    // End Date cell
    else if ([indexPath isEqual:self.indexPathForEndDateCellActual]) {
        cell.textLabel.text = @"Depature";
        cell.detailTextLabel.text = [self localizedEndDateStringFromEndDate:self.endDate
                                                              withStartDate:self.startDate];
    }
    // Visible Date Picker cell
    else if ([indexPath isEqual:self.indexPathOfVisibleDatePicker]) {
        // Work out mode of date picker based on whether the event is 'all-day'
        ((UXDatePickerCell *) cell).datePicker.datePickerMode = self.allDay ? UIDatePickerModeDate : UIDatePickerModeDateAndTime;
        // Set date of datePicker
        // If date picker is for startDate:
        if ([self.indexPathForStartDateCell isEqual:[NSIndexPath indexPathForRow:indexPath.row - 1 inSection:indexPath.section]]) {
            [((UXDatePickerCell *) cell).datePicker setDate:self.startDate animated:NO];
            
            // Can pick any date (a minimum may have been set by the end date cell - see below)
            ((UXDatePickerCell *) cell).datePicker.minimumDate = nil;
        }
        // If date picker is for endDate:
        else {
            [((UXDatePickerCell *) cell).datePicker setDate:self.endDate animated:NO];
            
            // Don't allow user to pick a date before startDate
            ((UXDatePickerCell *) cell).datePicker.minimumDate = self.startDate;
        }
        // Add action
        [[(UXDatePickerCell *)cell datePicker] addTarget:self action:@selector(dateChangedInPicker:) forControlEvents:UIControlEventValueChanged];
    }
    
    return cell;
}


#pragma mark - IBActions
- (void)dateChangedInPicker:(UIDatePicker *)datePicker
{
    // Get indexPath for cell before date picker
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:self.indexPathOfVisibleDatePicker.row - 1
                                                inSection:self.indexPathOfVisibleDatePicker.section];
    
    // Update property and update date in cell
    // If startDate changes, update end date too
    if ([indexPath isEqual:self.indexPathForStartDateCell]) {
        
        // Calculate duration of event
        NSTimeInterval duration = [self.endDate timeIntervalSinceDate:self.startDate];
        
        // Update properties
        self.startDate = datePicker.date;
        self.endDate = [datePicker.date dateByAddingTimeInterval:duration];
        
    }
    // endDate changes
    else {
        // Update property
        self.endDate = datePicker.date;
    }
    
    // Update text
    [self refreshDateStrings];
}

- (void)onSwitchStateDidChange:(UISwitch *)onButton
{
    UITableView *tableView = self.tableView;
    
    // Update all-day property
    self.allDay = onButton.on;
    
    // Reload datePicker if it's visible to display appropriate mode
    NSIndexPath *pickerPath = self.indexPathOfVisibleDatePicker;
    if (pickerPath != nil) {
        [tableView reloadRowsAtIndexPaths:@[pickerPath] withRowAnimation:UITableViewRowAnimationNone];
    }
    
    // Change date labels
    [self refreshDateStrings];
    
    
}

@end
