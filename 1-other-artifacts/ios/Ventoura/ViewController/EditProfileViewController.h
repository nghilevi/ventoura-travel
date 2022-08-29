//
//  EditProfileViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 20/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Person.h"
#import "UserImage.h"

#import "AboutMeEditViewController.h"
#import "EditTourDetailViewController.h"
#import "EditTagViewController.h"
#import "ProfileManager.h"
#import "ProfileManagerDelegate.h"
#import "ProfileCommunicator.h"
#import "Attraction.h"
#import "MBProgressHUD.h"
#import "UICollectionView+Draggable.h"
#import "DraggableCollectionViewFlowLayout.h"
#import "CitySelectionViewController.h"
#import "CountrySelectionViewController.h"
#import "TourTypeSelectionViewController.h"
#import "SetPriceViewController.h"
#import "ventouraDatabaseUtility.h"
@interface EditProfileViewController : UIViewController<AboutMeEditViewControllerDelegate, EditTourDetailViewControllerDelegate, UIImagePickerControllerDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout, UIActionSheetDelegate,UINavigationControllerDelegate,UICollectionViewDataSource_Draggable, UICollectionViewDelegate,CitySelectionViewControllerDelegate,CountrySelectionViewControllerDelegate,TourTypeSelectionViewControllerDelegate,SetPriceViewControllerDelegate>
@property (nonatomic, readwrite, assign) Person *person;
@property (nonatomic, readwrite, assign) City *tourType;

@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic,retain) IBOutlet UICollectionView *cView;
@property (nonatomic,strong) NSMutableArray *imagesPath;

@end
