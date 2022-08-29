//
// Person.h
//
// Copyright (c) 2014 to present, Brian Gesiak @modocache
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//

#import <Foundation/Foundation.h>
@interface Person : NSObject

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *lastName;
@property (nonatomic, copy) NSString *firstName;
@property (nonatomic, strong) UIImage *image;
@property (nonatomic, copy) NSString *age;
@property (nonatomic, copy) NSString *ventouraId;
@property (copy, nonatomic) NSString *gender;
@property (copy, nonatomic) NSString *textBiography;
@property (copy, nonatomic) NSString *country;
@property (copy, nonatomic) NSString *city;
@property (copy, nonatomic) NSString *dateOfBirth;
@property (copy, nonatomic) NSString *userRole;
@property (copy, nonatomic) NSString *useravgReviewScoreRole;
@property (copy, nonatomic) NSString *tourLength;
@property (copy, nonatomic) NSString *tourPrice;
@property (copy, nonatomic) NSString *tourType;
@property (copy, nonatomic) NSString *paymentMethod;
@property (copy, nonatomic) NSMutableArray *attractions;
@property (copy, nonatomic) NSMutableArray *localSecrets;
@property (copy, nonatomic) NSMutableArray *images;
//properties for matchList person
@property (copy, nonatomic) NSString *lastMessageTime;
@property (copy, nonatomic) NSString *lastMessage;
@property (copy, nonatomic) NSString *matchTime;
@property (assign, nonatomic) BOOL *isNewMatch;

@property (assign, nonatomic) NSInteger unreadCount;
@property (nonatomic, copy) NSDate *dateForSorting;





//TODO dont need the stuff below delete it later.
//@property (nonatomic, assign) NSUInteger numberOfSharedFriends;
//@property (nonatomic, assign) NSUInteger numberOfSharedInterests;
//@property (nonatomic, assign) NSUInteger numberOfPhotos;

//- (instancetype)initWithName:(NSString *)name
//                       image:(UIImage *)image
//                         age:(NSString*)age
//       numberOfSharedFriends:(NSUInteger)numberOfSharedFriends
//     numberOfSharedInterests:(NSUInteger)numberOfSharedInterests
//              numberOfPhotos:(NSUInteger)numberOfPhotos;

- (instancetype)initForPackageWithFirstName:(NSString *)firstName
                                      image:(UIImage *)image
                                       city:(NSString *)city
                                        age:(NSString *)age
                                 ventouraId:(NSString *)ventouraId
                                   userRole:(NSString *) userRole
                                    country:(NSString *) country
                                   tourType:(NSString*)tourType
                     useravgReviewScoreRole:(NSString*)useravgReviewScoreRole
                              textBiography:(NSString *) textBiography
                            images:(NSMutableArray*)images;



- (instancetype)initForBuddyListWithFirstName:(NSString *)firstName
                                        image:(UIImage *)image
                                         city:(NSString *)city
                                          age:(NSString *)age
                                   ventouraId:(NSString *)ventouraId
                                     userRole:(NSString *) userRole
                                      country:(NSString *) country
                                     tourType:(NSString*)tourType
                       useravgReviewScoreRole:(NSString*)useravgReviewScoreRole
                                textBiography:(NSString *) textBiography
                                       images:(NSMutableArray*)images
                              lastMessageTime:(NSString*)lastMessageTime
                                    matchTime:(NSString*)matchTime
                                   unreadCount:(NSInteger)unreadCount
                                  lastMessage:(NSString*)lastMessage
                                   isNewMatch:(BOOL)isNewMatch;



- (instancetype)initForGuideProfileWithFirstName:(NSString *)firstName
                                           image:(UIImage *)image
                                            city:(NSString *)city
                                       tourPrice:(NSString *)tourPrice
                                      ventouraId:(NSString *)ventouraId
                                   paymentMethod:(NSString *)paymentMethod
                                      tourLength:(NSString *) tourLength
                                     attractions:(NSMutableArray *) attractions
                                     localSecrets:(NSMutableArray *) localSecrets
                                   textBiography:(NSString *) textBiography
                                     dateOfBirth:(NSString *) dateOfBirth
                                             age:(NSString*)age
                                         country:(NSString*)country
                                        tourType:(NSString*)tourType
                              useravgReviewScoreRole:(NSString*)useravgReviewScoreRole
                                          images:(NSMutableArray*)images;



- (instancetype)initForTravellerProfileWithFirstName:(NSString *)firstName
                                                city:(NSString *)city
                                          ventouraId:(NSString *)ventouraId
                                              gender:(NSString *)gender
                                         dateOfBirth:(NSString *)dateOfBirth
                                                country:(NSString *)country
                                       textBiography:(NSString *) textBiography
                                                 age:(NSString*) age
                                              images:(NSMutableArray*)images;

@end
