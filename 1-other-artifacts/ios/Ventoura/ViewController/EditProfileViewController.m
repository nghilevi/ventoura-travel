//
//  EditProfileViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 20/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.



#import "EditProfileViewController.h"
#import "Cell.h"

@interface EditProfileViewController ()<ProfileManagerDelegate,MBProgressHUDDelegate>{
//    NSMutableArray *userImagesArr;
//    NSMutableArray *userAttractionArr;
    NSMutableArray *attractionsArrDeleted;
    NSMutableArray *imageArrDisplay;
    NSMutableArray *imageArrDeleted;
    UICollectionView *_collectionView;
    NSInteger lastSelectedImageIndex;
    ProfileManager *_pmanager;
    MBProgressHUD *HUD;


}
@property (nonatomic , strong) Person *testPerson;
@property (nonatomic, strong) City *city;
@property (nonatomic, strong) City *country;
@property (nonatomic, strong) DBManager *dbManager;

@end
BOOL userIsGuide;
NSUInteger uploadFlagCounter;
NSUInteger uploadFlag;

@implementation EditProfileViewController






- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad{
    lastSelectedImageIndex =-1;
    self.city =[[City alloc]initWithId:@"0" cityName:@"N/A" countryId:@"0" countryName:@"N/A" tableIndexPath:0];

    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    if ([ventouraUtility isUserGuide]) {
        if(_person.city && [_person.city integerValue] > 0){
            NSString *query =[NSString stringWithFormat:@"select cityName, countryId from City WHERE id =%@",_person.city];
            NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
            if (results.count>0) {
                _person.country = results[0][1];
                self.city =[[City alloc]initWithId:_person.city cityName:results[0][0] countryId:_person.country countryName:nil tableIndexPath:0];
            }
        }
    }else{
        if (_person.country && [_person.country integerValue]>0) {
            NSString *query =[NSString stringWithFormat:@"select countryName from Country WHERE id =%@",_person.country];
            NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
            if(results.count>0){
                NSString * countryName = results[0][0];
                self.country = [[City alloc]initWithId:nil cityName:nil countryId:_person.country countryName:countryName tableIndexPath:0];
            }
            
        }
    
    }
    
    //testing should make a sperate person object coming from previous viewcontroller
    [super viewDidLoad];
    [self.navigationController.navigationBar  setBarTintColor:[UIColor whiteColor]];
    self.navigationController.navigationBar.translucent = YES;
    attractionsArrDeleted = [[NSMutableArray alloc] init];
//    secretsArrDeleted = [[NSMutableArray alloc] init];

    userIsGuide =[ventouraUtility isUserGuide];
    imageArrDeleted = [[NSMutableArray alloc] init];
    imageArrDisplay = [[NSMutableArray alloc] init];
    UIImage *mask = [UIImage imageNamed:@"circlemask.png"];
    //initialise array
    
    for (int i = 0; i<[self.imagesPath count]; i++) {
        NSString* tmpStringId  = self.imagesPath[i];
        
        NSString *path = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imageId:tmpStringId];
        UserImage *userImage = [[UserImage alloc]initWithImageId:self.imagesPath[i] ownerId:nil isInMeory:NO img:[UIImage imageWithContentsOfFile:[NSString stringWithFormat:@"%@",path]] imgOriginal:nil path:self.imagesPath[i]];
        
        UIImage *maskedImage = [self maskImage:[ventouraUtility imageCropSquareCentre:userImage.img] withMask:mask];
        userImage.img = maskedImage;
        [imageArrDisplay addObject:userImage];
        
    }
    //do the  a check here for guide/ local. local will have 5 picture
    NSInteger pictureLimit = 4;
    if ([ventouraUtility isUserGuide]) {
        pictureLimit = 5;
    }
    while (imageArrDisplay.count < pictureLimit) {
        //            UIImageView *backgroundView =[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"addnew.png"]];
        UserImage *userImage = [[UserImage alloc]initWithImageId:@"-1" ownerId:nil isInMeory:NO img:[UIImage imageNamed:@"addbutton.png"] imgOriginal:nil path:nil];
        [imageArrDisplay addObject:userImage];
    }
    
    NSInteger cViewHeight = 380;
    if (userIsGuide){
        cViewHeight = 380;
    }
    DraggableCollectionViewFlowLayout *layout=[[DraggableCollectionViewFlowLayout alloc] init];
    CGRect frame = CGRectMake(5, 0, self.view.frame.size.width-10, cViewHeight);
    _collectionView=[[UICollectionView alloc] initWithFrame:frame collectionViewLayout:layout];
    _collectionView.draggable = YES;
    UIBarButtonItem *saveButton = [[UIBarButtonItem alloc] initWithTitle:@"Save" style:UIBarButtonItemStylePlain target:self action:@selector(saveProfile)];
    UIBarButtonItem *cancelButton = [[UIBarButtonItem alloc] initWithTitle:@"Cancel" style:UIBarButtonItemStylePlain target:self action:@selector(cancelProfile)];
    
    
    
    

    self.navigationItem.rightBarButtonItem = saveButton;
    self.navigationItem.leftBarButtonItem = cancelButton;
    

//    self.navigationItem.leftBarButtonItem = saveButton1
    _pmanager = [[ProfileManager alloc] init];
    _pmanager.communicator =[[ProfileCommunicator alloc] init];
    _pmanager.communicator.delegate = _pmanager;
    _pmanager.delegate = self;
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor clearColor];
    self.navigationController.navigationBar.translucent = NO;

    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    _collectionView.backgroundColor = [UIColor clearColor];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }
    self.tView.backgroundColor = [UIColor clearColor];
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void)editAboutMeViewController:(AboutMeEditViewController *)controller didFinishEditAboutMe:(NSString *)aboutMe{
    NSLog(@"new aboout me:%@", aboutMe);
    self.person.textBiography = aboutMe;
}

-(void)editTourDetailViewController:(EditTourDetailViewController *)controller didFinishEditTourDetail:(NSString *)value type:(NSInteger)type{
    if (type ==1) {
        self.person.tourPrice = value;
    }else{
        self.person.tourLength = value;
    }
}

-(void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.tView reloadData];
    [_collectionView reloadData];

//    [self.tView reloadData];
//    [_collectionView reloadData];
}

-(void) viewDidAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}

#pragma mark - Table view data source


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
//this can be changed to return number of rows
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (userIsGuide) {
        return 7;
    }
    return 3;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

    if ([ventouraUtility isUserGuide]) {
        return [self tableViewForLocal:tableView cellForRowAtIndexPath:indexPath];
    }
    return [self tableViewForTraveller:tableView cellForRowAtIndexPath:indexPath];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([ventouraUtility isUserGuide]) {
        [self tableViewForLocal:tableView didSelectRowAtIndexPath:indexPath];
    }else{
        [self tableViewForTraveller:tableView didSelectRowAtIndexPath:indexPath];
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if ([ventouraUtility isUserGuide]) {
        return [self tableViewForLocal:tableView heightForRowAtIndexPath:indexPath];
    }
    return [self tableViewForTraveller:tableView heightForRowAtIndexPath:indexPath];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return imageArrDisplay.count;
}

// The cell that is returned must be retrieved from a call to -dequeueReusableCellWithReuseIdentifier:forIndexPath:
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    UICollectionViewCell *cell=[collectionView dequeueReusableCellWithReuseIdentifier:@"cellIdentifier" forIndexPath:indexPath];
    
//    if(imageArrDisplay.count == indexPath.row){
//            UIImageView *backgroundView =[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"addnew.png"]];
//            backgroundView.layer.cornerRadius = 100 / 2;
//            backgroundView.clipsToBounds = YES;
//            backgroundView.layer.borderWidth = 3.0f;
//            backgroundView.layer.borderColor = [ventouraUtility ventouraBlue].CGColor;
//            cell.backgroundView = backgroundView;
//
//    }else{
//        
        UserImage *userImage= imageArrDisplay[indexPath.row];
        UIImageView *backgroundView = [[UIImageView alloc] initWithImage:[ventouraUtility imageCropSquareCentre:userImage.img]];

        backgroundView.clipsToBounds = YES;
        cell.backgroundView = backgroundView;
//    }
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
//    return CGSizeMake(100, 100);
    
    if (indexPath.row==0) {
        return CGSizeMake(250, 250);
        
    }
    if ([ventouraUtility isUserGuide]) {
        return CGSizeMake(70, 70);

    }
    return CGSizeMake(95, 95);
}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    UserImage *userImage= imageArrDisplay[indexPath.row];

     if ([userImage.imageId intValue] == -1) {
        [self showUploadActionSheetWithImageIndex:indexPath.row];
    }else{
        [self showNormalActionSheetWithImageIndex:indexPath.row];
    }
}
- (void)showNormalActionSheetWithImageIndex:(NSInteger)index {
    
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"What do you want to do with this Photo?"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:@"Delete it"
                                                    otherButtonTitles:@"Make Profile", nil];
    lastSelectedImageIndex =index;
    // showNormalActionSheet:
    actionSheet.tag = 100;
    [actionSheet showInView:self.view];
}

-(void)showUploadActionSheetWithImageIndex:(NSInteger)index{
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Take A Photo", @"Upload From Album", nil];
    
    lastSelectedImageIndex =index;
    actionSheet.tag = 200;
    [actionSheet showInView:self.view];
}
-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
     if (actionSheet.tag == 100) {
         //Actual Image Sheet
        if (buttonIndex ==0) {
            //delete image!
            UserImage* secondImage = imageArrDisplay[1];
            if ([secondImage.imageId intValue]==-1) {
                UIAlertView *message = [[UIAlertView alloc] initWithTitle: @"Error"
                                                                  message:@"You must have at least one photo."
                                                                 delegate:nil
                                                        cancelButtonTitle:@"OK"
                                                        otherButtonTitles:nil];
                
                [message show];
            }else{
            UserImage *userImage = imageArrDisplay[lastSelectedImageIndex];
            if (userImage.isInMeory == NO) {
                [imageArrDeleted addObject:userImage];
            }
                [imageArrDisplay removeObjectAtIndex: lastSelectedImageIndex];
                UserImage *addImage = [[UserImage alloc]initWithImageId:@"-1" ownerId:nil isInMeory:NO img:[UIImage imageNamed:@"AddNewPressed.png"] imgOriginal:nil path:nil];
                [imageArrDisplay addObject:addImage];
                [_collectionView reloadData];
            }
          
            
        }
        else if (buttonIndex ==1){
            //make profile File Button
            NSMutableArray *tmpArray = [[NSMutableArray alloc] initWithArray: imageArrDisplay];
            id object = [imageArrDisplay objectAtIndex:lastSelectedImageIndex] ;
            [imageArrDisplay removeObjectAtIndex:lastSelectedImageIndex];
            [imageArrDisplay insertObject:object atIndex:0];
            [_collectionView performBatchUpdates:^{
                for (NSInteger i = 0; i < imageArrDisplay.count; i++) {
                    NSIndexPath *fromIndexPath = [NSIndexPath indexPathForRow:i inSection:0];
                    NSInteger j = [imageArrDisplay indexOfObject:tmpArray[i]];
                    NSIndexPath *toIndexPath = [NSIndexPath indexPathForRow:j inSection:0];
                    [_collectionView moveItemAtIndexPath:fromIndexPath toIndexPath:toIndexPath];
                }
            } completion:^(BOOL finished) {
//                [_collectionView reloadData];
            }];
        
        }
        lastSelectedImageIndex =-1;
     }
    if (actionSheet.tag == 200) {
        //Add Photo sheet
        if (buttonIndex ==0) {
            NSLog(@"take a photo");
            [self takePhoto];

        }else if (buttonIndex ==1){
            NSLog(@"upload a photo");
            [self selectPhoto];
        }
//        lastSelectedImageIndex =-1;
    }
}
-(void)selectPhoto{
    UIImagePickerController *picker = [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.allowsEditing = YES;
    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    [self presentViewController:picker animated:YES completion:NULL];
}

-(void)takePhoto{
    UIImagePickerController *picker = [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.allowsEditing = YES;
    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
    [self presentViewController:picker animated:YES completion:NULL];
}
#pragma mark - Image Picker Controller delegate methods

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    UIImage *chosenImage = info[UIImagePickerControllerEditedImage];
    UIImage *mask = [UIImage imageNamed:@"circlemask.png"];
    UserImage *userImage = [[UserImage alloc]initWithImageId:nil ownerId:nil isInMeory:YES img:[self maskImage:[ventouraUtility imageCropSquareCentre:chosenImage] withMask:mask] imgOriginal:chosenImage path:nil];
    [imageArrDisplay removeObjectAtIndex:lastSelectedImageIndex];
    
    [imageArrDisplay insertObject: userImage atIndex:[self getFirstIndexNumberOfFreeSlot]];
    [picker dismissViewControllerAnimated:YES completion:NULL];
    
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    
    [picker dismissViewControllerAnimated:YES completion:NULL];
    
}

//Error Handling, If upload Fails. Method should pass back an index number, and changed the uploaded image to be not in memory:)
-(void)didReceiveImageId:(NSString *)imageId isPortal:(BOOL)isPortal{
    NSLog(@"Image upload Complete");
    //save image into the database here?
    
    [ventouraDatabaseUtility saveTravellerProfileImageDataToDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] imgId:imageId isUserGuide:[ventouraUtility isUserGuide] isPortal:isPortal];
    
    uploadFlagCounter++;
    if (isPortal) {
        [_pmanager setPortalImageId:imageId withUserId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
    }
    NSLog(@"process %ld / %ld", uploadFlagCounter,uploadFlag);
    if (uploadFlagCounter==uploadFlag) {
        [self closeEditProfileView];
    }
    //image upload complete, if it is a profile image, just upload it again
}

-(void) receivedSetPortalImage{
    NSLog(@"Set Portal Image Done");
    uploadFlagCounter++;
    NSLog(@"process %ld / %ld", uploadFlagCounter,uploadFlag);
    if (uploadFlagCounter==uploadFlag) {
        [self closeEditProfileView];
    }

}

-(void) closeEditProfileView{
    //delete all images in the directory. with Cache, we can delete individual ones. after deleted success!
    
//    NSFileManager *fm = [NSFileManager defaultManager];
//    NSString* directory = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
//    NSError *error = nil;
//    for (NSString *file in [fm contentsOfDirectoryAtPath:directory error:&error]) {
//        NSLog(@"deleting File %@",[NSString stringWithFormat:@"%@/%@", directory, file]);
//        BOOL success = [fm removeItemAtPath:[directory stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@/",file]] error:&error];
//        if (!success || error) {
//            // it failed.
//            NSLog(@"deleting failed");
//            NSLog(@"Error %@; %@", error, [error localizedDescription]);
//
//        }
//    }
    
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            [self.navigationController popViewControllerAnimated:YES];
        });
    });
}
-(NSInteger) getFirstIndexNumberOfFreeSlot{
    for(int i = 0; i < imageArrDisplay.count; i++){
        UserImage *userImage= imageArrDisplay[i];
        if ([userImage.imageId intValue] ==-1) {
            return i;
        }

    }
    return imageArrDisplay.count;
}

-(void)saveProfile{
//    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    
    
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    //    HUD.labelText = @"Loading";
    [HUD show:YES];
    for(int i=0; i<imageArrDisplay.count; i++){
        UserImage* tmp = imageArrDisplay[i];
        if (tmp.isInMeory==YES) {
            uploadFlag++;
        }
    }
    uploadFlag++;
    NSLog(@"upload Flag is :%ld", uploadFlag);
    [self updateProfile];

    
}

-(void)cancelProfile{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Do you want to cancel?"
                                                    message:@"All changes made to the profile will be discarded"
                                                   delegate:self
                                          cancelButtonTitle:@"No"
                                          otherButtonTitles:@"Yes",nil];
    [alert show];
}


-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex ==1) {
        NSLog(@"buttons hit");
        [self.navigationController popViewControllerAnimated:YES];
    }
    
}


-(void) updateAttractions{
    [_pmanager updateGuideAttractions:_person.attractions];
}



-(void) updateProfile{
    
    if(!_person.tourType){
        _person.tourType =@"";
    }
    if (self.city.cityId ==nil) {
        self.city.cityId = @"";
    }
    if(self.city.countryId==nil){
        self.city.countryId =@"";
    }
    
    if (self.country.countryId == nil) {
        self.country.countryId = @"";
    }
    if(self.person.tourLength==nil)
    {
        self.person.tourLength=@"";
    }
    
    if ([ventouraUtility isUserGuide]) {
        NSLog(@"update Guide");
        
        [_pmanager updateGuideProfile:[ventouraUtility returnMyUserId] textBiography:self.person.textBiography tourLength:self.person.tourLength tourPrice:self.person.tourPrice city:self.city.cityId country:self.city.countryId tourType:_person.tourType];
        
    }else{
        NSLog(@"update Traveller");

        [_pmanager updateTravellerProfile:[ventouraUtility returnMyUserId] textBiography:self.person.textBiography country:self.country.countryId];

    }
}

-(void) deleteImages{
    if (imageArrDeleted.count==0) {
        [self uploadImages];
    }else{
        [_pmanager deleteUserImages:imageArrDeleted isUserGuide:[ventouraUtility isUserGuide]];
    }
    
}

-(void) receivedDeleteUserImages{
    NSLog(@"image delete action Complete");
    //delete images from database
    for (int i =0; i<imageArrDeleted.count; i++) {
        
        UserImage *userImage = imageArrDeleted[i];

        NSLog(@"IMG ID for Deletetion, %@",userImage.imageId);
        //remove from db
        [ventouraDatabaseUtility deleteImageData:self.dbManager ownerId:[ventouraUtility returnMyUserId] imageId:userImage.imageId userId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
        
        //remove actual image
        [ventouraUtility deleteLocalImageBy:userImage.imageId ventouraId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
    }
    
    [self uploadImages];
    
}

-(void) uploadImages{
    //post stuff here.
    NSLog(@"Start Image Up Load, All Image Count %ld",[imageArrDisplay count]);
    for (int i = 0; i<[imageArrDisplay count]; i++) {
        UserImage *tmpImage = imageArrDisplay[i];

        
        if (tmpImage.isInMeory) {
            NSLog(@"Upload %d",i);
            if (i==0) {
                [_pmanager createNewImage:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] image:tmpImage.img isPortal:YES];
            }else{
                [_pmanager createNewImage:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] image:tmpImage.img isPortal:NO];
            }
        }else{
            if (i==0) {
                [_pmanager setPortalImageId:tmpImage.imageId withUserId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
            }
        }
    }
}

-(void)fetchingProfileFailedWithError:(NSError *)error{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        // Now the image will have been loaded and decoded and is ready to rock for the main thread
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];
        });
    });
}

-(void) didReceiveGuideProfileImages:(NSArray *)imageArray{}
-(void) didReceiveTravellerProfileImages:(NSArray *)imageArray{}
-(void) didReceiveGuideProfile:(Person *)personProfile{

}
-(void) didReceiveTravellerProfile:(Person *)personProfile{}
-(void) didReceivePersonProfile:(Person *)personProfile{} //TODO WHATSTHIS??

-(void) receivedUpdateTravellerProfile{
    NSLog(@"Traveller Profile Updated");
    [ventouraDatabaseUtility saveTravellerProfileDataFromDatabase:self.dbManager userId:[ventouraUtility returnMyUserIdWithType] name:self.person.firstName city:self.person.city country:self.person.country age:self.person.age textBio:self.person.textBiography];
    [self deleteImages];
}
-(void) receivedUpdateGuideProfile{
    NSLog(@"Guide Profile Updated");
    [self updateAttractions];
}
-(void) receivedGuideUpdateAttractions{
    NSLog(@"Attractions UPDATED");
    [_pmanager deleteGuideAttractions:attractionsArrDeleted];
}



-(void) receivedGuideDeleteAttractions{
    NSLog(@"ATTRACTIONS Deleted");
    [self deleteImages];

}




- (void)willPresentActionSheet:(UIActionSheet *)actionSheet
{
    for (UIView *subview in actionSheet.subviews) {
        if ([subview isKindOfClass:[UIButton class]]) {
            UIButton *button = (UIButton *)subview;
            if ([button.currentTitle isEqualToString:@"Cancel"]) {
                [button setTitleColor:[ventouraUtility ventouraTitleColour] forState:UIControlStateNormal];
            }else{
                [button setTitleColor:[ventouraUtility ventouraPink] forState:UIControlStateNormal];
            }
            button.titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
        }
    }
}



- (UIImage*) maskImage:(UIImage *) image withMask:(UIImage *) mask
{
    CGImageRef imageReference = image.CGImage;
    CGImageRef maskReference = mask.CGImage;
    
    CGImageRef imageMask = CGImageMaskCreate(CGImageGetWidth(maskReference),
                                             CGImageGetHeight(maskReference),
                                             CGImageGetBitsPerComponent(maskReference),
                                             CGImageGetBitsPerPixel(maskReference),
                                             CGImageGetBytesPerRow(maskReference),
                                             CGImageGetDataProvider(maskReference),
                                             NULL, // Decode is null
                                             YES // Should interpolate
                                             );
    
    CGImageRef maskedReference = CGImageCreateWithMask(imageReference, imageMask);
    CGImageRelease(imageMask);
    
    UIImage *maskedImage = [UIImage imageWithCGImage:maskedReference];
    CGImageRelease(maskedReference);
    
    return maskedImage;
}

- (BOOL)collectionView:(LSCollectionViewHelper *)collectionView canMoveItemAtIndexPath:(NSIndexPath *)indexPath
{
    UserImage *userImage= imageArrDisplay[indexPath.item];
    if ([userImage.imageId intValue]==-1) {
        return NO;
    }
    return YES;
}

- (BOOL)collectionView:(UICollectionView *)collectionView canMoveItemAtIndexPath:(NSIndexPath *)indexPath toIndexPath:(NSIndexPath *)toIndexPath
{
    UserImage *userImage= imageArrDisplay[toIndexPath.item];
    if ([userImage.imageId intValue]==-1) {
        return NO;
    }
    return YES;
}

- (void)collectionView:(LSCollectionViewHelper *)collectionView moveItemAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
    NSNumber *index = [imageArrDisplay objectAtIndex:fromIndexPath.item];
    [imageArrDisplay removeObjectAtIndex:fromIndexPath.item];
    [imageArrDisplay insertObject:index atIndex:toIndexPath.item];
    //this should keep everything in order
}


- (void)addItemViewController:(CitySelectionViewController *)controller didFinishCitySelection:(City *)city
{
    NSLog(@"From Selection %@,%@s",city.cityName,city.countryId);
    self.city = city;
}
-(void)addItemViewController:(CountrySelectionViewController *)controller didFinishCountrySelection:(City *)country{
    self.country=country;
}
-(void)addItemViewController:(TourTypeSelectionViewController *)controller didFinishTourTypeSelection:(NSString *)tourType{
    self.person.tourType = tourType;
}

- (UITableViewCell *)tableViewForLocal:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{


    static NSString *CellIdentifier = @"Cell";
    switch ( indexPath.row )
    {
        case 0:
            CellIdentifier = @"profile";
            break;
        case 1:
            CellIdentifier = @"twoLabels";
            break;
            
        case 2:
            CellIdentifier = @"userAboutMe";
            break;
        case 3:
            CellIdentifier = @"twoLabels";
            break;
        case 4:
            CellIdentifier = @"twoLabels";
            break;
        case 5:
            CellIdentifier = @"twoLabels";
            break;
        case 6:
            CellIdentifier = @"twoLabels";
            break;
            
    }
    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    switch ( indexPath.row )
    {
        case 0:{
            
            
            [_collectionView setDataSource:self];
            [_collectionView setDelegate:self];
            [_collectionView registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"cellIdentifier"];
            [_collectionView setBackgroundColor:[UIColor clearColor]];
            [cell addSubview:_collectionView];
            break;
        }
        case 1:{
            
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:180];
            nameLabel.text = [NSString stringWithFormat:@"City"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:181];
            aboutMe.text = [NSString stringWithFormat:@"%@", self.city.cityName];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
            
            break;
        }
        case 2:{
            
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:125];
            nameLabel.text = [NSString stringWithFormat:@"About Me"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:126];
            aboutMe.text = [NSString stringWithFormat:@"%@", self.person.textBiography];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, 280, aboutMe.frame.size.height);

            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            break;
        }
        case 3:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:180];
            nameLabel.text = [NSString stringWithFormat:@"Tour Price"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:181];
            float adjustedValue =[self.person.tourPrice integerValue]*1.2;
            aboutMe.text = [NSString stringWithFormat:@"£%.f", adjustedValue];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, 280, aboutMe.frame.size.height);

            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
            break;
        }
        case 4:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:180];
            nameLabel.text = [NSString stringWithFormat:@"Tour Length"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:181];
            aboutMe.text = [NSString stringWithFormat:@"%@ Hours",self.person.tourLength];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, 280, aboutMe.frame.size.height);

            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            break;
        }
        case 5:{
            
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:180];
            nameLabel.text = [NSString stringWithFormat:@"Attractions"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:181];
            NSString *attractionsLabel=@"";
            if ([self.person.attractions count]>0) {
                Attraction *tmp = self.person.attractions[0];
                attractionsLabel = tmp.attractionName;
                
                for (int i=1; i<_person.attractions.count; i++) {
                    tmp = self.person.attractions[i];
                    attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
                }
            }
            
            
            aboutMe.text = [NSString stringWithFormat:@"%@", attractionsLabel];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize  = [self frameForText:attractionsLabel sizeWithFont:[UIFont fontWithName:@"Roboto-Regular" size:16] constrainedToSize:constraintSize lineBreakMode:aboutMe.lineBreakMode ];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, labelSize.width, labelSize.height);
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
            break;
        }

        case 6:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:180];
            nameLabel.text = [NSString stringWithFormat:@"Tour Type"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:181];
            aboutMe.text = [NSString stringWithFormat:@""];
            
            if (_person.tourType && [_person.tourType integerValue]>=0) {
                NSString *query =[NSString stringWithFormat:@"select tourName from tourType WHERE tourId =%@",_person.tourType];
                NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
                if (results.count>0) {
                    NSString * tourType =results[0][0];
                    aboutMe.text = [NSString stringWithFormat:@"%@",tourType];
                }
            }
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            break;
        }
 
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.backgroundColor = [UIColor clearColor];
    return cell;


}



- (UITableViewCell *)tableViewForTraveller:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    static NSString *CellIdentifier = @"Cell";
    switch ( indexPath.row )
    {
        case 0:
            CellIdentifier = @"profile";
            break;
        case 1:
            CellIdentifier = @"twoLabels";
            break;
            
        case 2:
            CellIdentifier = @"twoLabels";
            break;
    }
    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    switch ( indexPath.row )
    {
        case 0:{
            
            
            [_collectionView setDataSource:self];
            [_collectionView setDelegate:self];
            [_collectionView registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"cellIdentifier"];
            [_collectionView setBackgroundColor:[UIColor clearColor]];
            [cell addSubview:_collectionView];
            break;
        }
        case 1:{
            
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:180];
            nameLabel.text = [NSString stringWithFormat:@"Country"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:181];
            aboutMe.text = [NSString stringWithFormat:@"%@", self.country.countryName];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
            
            break;
        }
        case 2:{
            
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:180];
            nameLabel.text = [NSString stringWithFormat:@"About Me"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:181];
            aboutMe.text = [NSString stringWithFormat:@"%@", self.person.textBiography];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, 280, aboutMe.frame.size.height);
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            break;
        }
    }
    cell.backgroundColor = [UIColor clearColor];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    return cell;
    
    
}


- (CGFloat)tableViewForLocal:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat cellHeight = 50;
    switch ( indexPath.row ){
        case 0:{
            //User Profile Height
           
            cellHeight = 380;
            break;
        }
        case 1:
            cellHeight = 60;
            break;
        case 2:{
            //About Me Height
            UIFont *cellFont = [UIFont fontWithName:@"Roboto-Regular" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [_person.textBiography sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height + 50;
            break;
        }
        case 3:
            cellHeight = 60;
            break;
        case 4:{
            cellHeight = 60;
            break;
        }
        case 5:{
            NSString *attractionsLabel =@"";
            if([_person.attractions count] >0){
                Attraction *tmp = _person.attractions[0];
                attractionsLabel = tmp.attractionName;
                for (int i=1; i<_person.attractions.count; i++) {
                    tmp = _person.attractions[i];
                    attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
                }
                
            }
            
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            UILabel * tmplabel =[[UILabel alloc] init];
         
            CGSize labelSize  = [self frameForText:attractionsLabel sizeWithFont:[UIFont fontWithName:@"Roboto-Regular" size:16] constrainedToSize:constraintSize lineBreakMode:tmplabel.lineBreakMode ];
            
            return labelSize.height + 40;
            break;
            
        }
//        case 6:{
//            
//            NSString *attractionsLabel =@"";
//            if ([ _person.localSecrets count] >0) {
//                Attraction *tmp = _person.localSecrets[0];
//                attractionsLabel = tmp.attractionName;
//                
//                for (int i=1; i<_person.localSecrets.count; i++) {
//                    tmp = _person.localSecrets[i];
//                    attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
//                }
//            }
//            
//            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
//            UILabel * tmplabel =[[UILabel alloc] init];
//            CGSize labelSize  = [self frameForText:attractionsLabel sizeWithFont:[UIFont fontWithName:@"Roboto-Regular" size:16] constrainedToSize:constraintSize lineBreakMode:tmplabel.lineBreakMode ];
//            return labelSize.height + 40;
//            break;
//            
//        }
        case 6:{
            cellHeight = 60;
            break;
        }
            
    }
    
    return cellHeight;
}
- (CGFloat)tableViewForTraveller:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat cellHeight = 50;
    switch ( indexPath.row ){
        case 0:{
            cellHeight = 380;
            break;
        }
        case 1:
            cellHeight = 60;
            break;
        case 2:{
            //About Me Height
//            UIFont *cellFont = [UIFont fontWithName:@"Roboto" size:16];
//            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
//            UILabel * tmplabel =[[UILabel alloc] init];
//            tmplabel.numberOfLines = 0;
//            [tmplabel sizeToFit];
            
//            CGSize labelSize  = [self frameForText:_person.textBiography sizeWithFont:[UIFont fontWithName:@"Roboto-Regular" size:16] constrainedToSize:constraintSize lineBreakMode:tmplabel.lineBreakMode ];
//            NSLog(@"About Me Cell Height: %f with text %@", labelSize.height, _person.textBiography);
//            return labelSize.height + 40;
            UIFont *cellFont = [UIFont fontWithName:@"Roboto-Regular" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [_person.textBiography sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height+40;

            break;
        }
        case 6:{
            
            NSString *attractionsLabel =@"";
            if ([ _person.localSecrets count] >0) {
                Attraction *tmp = _person.localSecrets[0];
                attractionsLabel = tmp.attractionName;
                
                for (int i=1; i<_person.localSecrets.count; i++) {
                    tmp = _person.localSecrets[i];
                    attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
                }
            }
            
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            UILabel * tmplabel =[[UILabel alloc] init];
            CGSize labelSize  = [self frameForText:attractionsLabel sizeWithFont:[UIFont fontWithName:@"Roboto" size:16] constrainedToSize:constraintSize lineBreakMode:tmplabel.lineBreakMode ];
            return labelSize.height + 40;
            break;
            
        }
            
    }
    
    return cellHeight;
}


- (void)tableViewForLocal:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
        switch ( indexPath.row )
        {
            case 1:{
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                     bundle:nil];
                CitySelectionViewController *viewController =
                [storyboard instantiateViewControllerWithIdentifier:@"CitySelectionViewController"];
                viewController.delegate = self;
                viewController.city = self.city;
                [[self navigationController] pushViewController:viewController animated:YES ];
                break;
            }
            case 2:{
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                     bundle:nil];
                AboutMeEditViewController *viewController =
                [storyboard instantiateViewControllerWithIdentifier:@"AboutMeEditViewController"];
                NSLog(@"%@", self.testPerson.textBiography);
                viewController.delegate = self;
                viewController.aboutMeText = self.person.textBiography;
                [[self navigationController] pushViewController:viewController animated:YES ];
                
                break;
            }
            case 3:{
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                     bundle:nil];
                SetPriceViewController *viewController =
                [storyboard instantiateViewControllerWithIdentifier:@"SetPriceViewController"];
                viewController.priceValue = [self.person.tourPrice integerValue];
                viewController.paymentType = [self.person.paymentMethod integerValue];
                viewController.delegate =self;
                [[self navigationController] pushViewController:viewController animated:YES ];
                
                break;
            }
            case 4:{
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                     bundle:nil];
                EditTourDetailViewController *viewController =
                [storyboard instantiateViewControllerWithIdentifier:@"EditTourDetailViewController"];
                viewController.textValue = self.person.tourLength;
                viewController.valueType = 2;
                viewController.delegate =self;
                [[self navigationController] pushViewController:viewController animated:YES ];
                
                break;
            }
                
            case 5:{
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                     bundle:nil];
                EditTagViewController *viewController =
                [storyboard instantiateViewControllerWithIdentifier:@"EditTagViewController"];
                viewController.deletedArray = attractionsArrDeleted;
                viewController.person = self.person;
                viewController.valueType = 1;
                [[self navigationController] pushViewController:viewController animated:YES ];
                
                break;
            }
                
//            case 6:{
//                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
//                                                                     bundle:nil];
//                EditTagViewController *viewController =
//                [storyboard instantiateViewControllerWithIdentifier:@"EditTagViewController"];
//                //            viewController.tags = self.person.localSecrets;
//                viewController.deletedArray = secretsArrDeleted;
//                viewController.valueType = 2;
//                viewController.person = self.person;
//                [[self navigationController] pushViewController:viewController animated:YES ];
//                
//                break;
//            }
            case 6:{
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                     bundle:nil];
                TourTypeSelectionViewController *viewController =
                [storyboard instantiateViewControllerWithIdentifier:@"TourTypeSelectionViewController"];
                viewController.delegate = self;
                viewController.tourType = self.person.tourType;
                [[self navigationController] pushViewController:viewController animated:YES ];
                break;
            }
        }
}
- (void)tableViewForTraveller:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    switch (indexPath.row) {
        case 1:{
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                 bundle:nil];
            CountrySelectionViewController *viewController =
            [storyboard instantiateViewControllerWithIdentifier:@"CountrySelectionViewController"];
            viewController.delegate = self;
            viewController.country = self.country;
            [[self navigationController] pushViewController:viewController animated:YES ];
            break;
        }
        case 2:{
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                 bundle:nil];
            AboutMeEditViewController *viewController =
            [storyboard instantiateViewControllerWithIdentifier:@"AboutMeEditViewController"];
            NSLog(@"%@", self.testPerson.textBiography);
            viewController.delegate = self;
            viewController.aboutMeText = self.person.textBiography;
            [[self navigationController] pushViewController:viewController animated:YES ];
            
            break;
        }
        case 3:{
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                 bundle:nil];
            EditTourDetailViewController *viewController =
            [storyboard instantiateViewControllerWithIdentifier:@"EditTourDetailViewController"];
            viewController.textValue = self.person.tourPrice;
            viewController.valueType = 1;
            viewController.delegate =self;
            [[self navigationController] pushViewController:viewController animated:YES ];
            
            break;
        }
    }

}


-(CGSize)frameForText:(NSString*)text sizeWithFont:(UIFont*)font constrainedToSize:(CGSize)size lineBreakMode:(NSLineBreakMode)lineBreakMode  {
    
    NSMutableParagraphStyle * paragraphStyle = [[NSMutableParagraphStyle defaultParagraphStyle] mutableCopy];
    paragraphStyle.lineBreakMode = lineBreakMode;
    
    NSDictionary * attributes = @{NSFontAttributeName:font,
                                  NSParagraphStyleAttributeName:paragraphStyle
                                  };
    
    
    CGRect textRect = [text boundingRectWithSize:size
                                         options:NSStringDrawingUsesLineFragmentOrigin
                                      attributes:attributes
                                         context:nil];
    
    //Contains both width & height ... Needed: The height
    return textRect.size;
}

-(void)didReceiveImageId:(NSString *)imageId{}

-(void)editPriceViewController:(SetPriceViewController *)controller didFinishEditTourDetail:(NSInteger)value{
    self.person.tourPrice = [NSString stringWithFormat:@"%ld", value];

}
@end
