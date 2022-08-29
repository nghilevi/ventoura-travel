//
// Person.m
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

#import "Person.h"

@implementation Person

#pragma mark - Object Lifecycle
//
//- (instancetype)initWithName:(NSString *)name
//                       image:(UIImage *)image
//                         age:(NSString*)age
//       numberOfSharedFriends:(NSUInteger)numberOfSharedFriends
//     numberOfSharedInterests:(NSUInteger)numberOfSharedInterests
//              numberOfPhotos:(NSUInteger)numberOfPhotos {
//    self = [super init];
//    if (self) {
//        _name = name;
//        _image = image;
//        _age = age;
//        _numberOfSharedFriends = numberOfSharedFriends;
//        _numberOfSharedInterests = numberOfSharedInterests;
//        _numberOfPhotos = numberOfPhotos;
//    }
//    return self;
//}

- (instancetype)initForPackageWithFirstName:(NSString *)firstName
                                      image:(UIImage *)image
                                       city:(NSString *)city
                                        age:(NSString*)age
                                 ventouraId:(NSString*)ventouraId
                                   userRole:(NSString *)userRole
                                    country:(NSString *)country
                                   tourType:(NSString *)tourType
                     useravgReviewScoreRole:(NSString *)useravgReviewScoreRole
                              textBiography:(NSString *) textBiography
                                     images:(NSMutableArray*)images

{
    self = [super init];
    if (self) {
        _firstName = firstName;
        _image = image;
        _city = city;
        _age = age;
        _ventouraId = ventouraId;
        _userRole = userRole;
        _country = country;
        _tourType = tourType;
        _useravgReviewScoreRole = useravgReviewScoreRole;
        _textBiography = textBiography;
        _images = images;
    }
    return self;
}

- (instancetype)initForGuideProfileWithFirstName:(NSString *)firstName
                                           image:(UIImage *)image
                                            city:(NSString *)city
                                       tourPrice:(NSString *)tourPrice
                                      ventouraId:(NSString *)ventouraId
                                   paymentMethod:(NSString *)paymentMethod
                                      tourLength:(NSString *) tourLength
                                     attractions:(NSMutableArray *)attractions
                                    localSecrets:(NSMutableArray *)localSecrets
                                   textBiography:(NSString *)textBiography
                                     dateOfBirth:(NSString *)dateOfBirth
                                             age:(NSString*)age
                                         country:(NSString*)country
                                        tourType:(NSString *)tourType
                              useravgReviewScoreRole:(NSString*)useravgReviewScoreRole
                                          images:(NSMutableArray*)images{
    self = [super init];
    if (self) {
        _firstName = firstName;
        _image = image;
        _city = city;
        _tourPrice = tourPrice;
        _ventouraId = ventouraId;
        _tourLength = tourLength;
        _paymentMethod = paymentMethod;
        _attractions = attractions;
        _localSecrets =localSecrets;
        _textBiography = textBiography;
        _dateOfBirth = dateOfBirth;
        _age = age;
        _country = country;
        _tourType = tourType;
        _useravgReviewScoreRole = useravgReviewScoreRole;
        _images = images;
        
    }
    return self;
}

-(instancetype) initForTravellerProfileWithFirstName:(NSString *)firstName city:(NSString *)city ventouraId:(NSString *)ventouraId gender:(NSString *)gender dateOfBirth:(NSString *)dateOfBirth country:(NSString *)country textBiography:(NSString *)textBiography age:(NSString *)age images:(NSMutableArray *)images{
    self = [super init];
    if (self) {
        _firstName = firstName;
        _city = city;
        _ventouraId = ventouraId;
        _country = country;
        _textBiography =textBiography;
        _dateOfBirth = dateOfBirth;
        _gender = gender;
        _age = age;
        _images=images;
    }
    return self;


}

-(instancetype) initForBuddyListWithFirstName:(NSString *)firstName
                                        image:(UIImage *)image
                                         city:(NSString *)city
                                          age:(NSString *)age
                                   ventouraId:(NSString *)ventouraId
                                     userRole:(NSString *)userRole
                                      country:(NSString *)country
                                     tourType:(NSString *)tourType
                       useravgReviewScoreRole:(NSString *)useravgReviewScoreRole
                                textBiography:(NSString *)textBiography
                                       images:(NSMutableArray *)images
                              lastMessageTime:(NSString *)lastMessageTime
                                    matchTime:(NSString *)matchTime
                                  unreadCount:(NSInteger)unreadCount
                                  lastMessage:(NSString *)lastMessage isNewMatch:(BOOL)isNewMatch{
    self = [super init];
    if (self) {
        _firstName = firstName;
        _image = image;
        _city = city;
        _age = age;
        _ventouraId = ventouraId;
        _userRole = userRole;
        _country = country;
        _tourType = tourType;
        _useravgReviewScoreRole = useravgReviewScoreRole;
        _textBiography = textBiography;
        _images = images;
        _lastMessageTime = lastMessageTime;
        _matchTime = matchTime;
        _unreadCount = unreadCount;
        _lastMessage = lastMessage;
        _isNewMatch = isNewMatch;
        
    }
    return self;
}
@end
