
ALTER TABLE TravellerGallery ADD CONSTRAINT FK_TravellerGallery_Traveller_ID_Cascade
   FOREIGN KEY (travellerId) REFERENCES Traveller(id) ON DELETE CASCADE;

ALTER TABLE TravellerLastUpdatedLog
   ADD CONSTRAINT FK_TravellerLastUpdatedLog_Traveller_ID_Cascade
   FOREIGN KEY (travellerId) REFERENCES Traveller(id) ON DELETE CASCADE;
   

ALTER TABLE GuideGallery
   ADD CONSTRAINT FK_GuideGallery_Guide_ID_Cascade
   FOREIGN KEY (guideId) REFERENCES Guide(id) ON DELETE CASCADE;
   

ALTER TABLE Booking
   ADD CONSTRAINT FK_Booking_Guide_ID_Cascade
   FOREIGN KEY (guideId) REFERENCES Guide(id) ON DELETE CASCADE;

ALTER TABLE Booking ADD CONSTRAINT FK_Booking_Traveller_ID_Cascade FOREIGN KEY (travellerId) REFERENCES Traveller(id) ON DELETE CASCADE;
   

ALTER TABLE GuideLastUpdatedLog
   ADD CONSTRAINT FK_GuideLastUpdatedLog_Guide_ID_Cascade
   FOREIGN KEY (guideId) REFERENCES Guide(id) ON DELETE CASCADE;
   
   

ALTER TABLE GuideReview
   ADD CONSTRAINT FK_GuideReview_Guide_ID_Cascade
   FOREIGN KEY (guideId) REFERENCES Guide(id) ON DELETE CASCADE;  
   

ALTER TABLE GuideAttraction
   ADD CONSTRAINT FK_GuideAttraction_Guide_ID_Cascade
   FOREIGN KEY (guideId) REFERENCES Guide(id) ON DELETE CASCADE; 
   

ALTER TABLE GuideHiddenTreasure
   ADD CONSTRAINT FK_GuideHiddenTreasure_Guide_ID_Cascade
   FOREIGN KEY (guideId) REFERENCES Guide(id) ON DELETE CASCADE; 
 

ALTER TABLE TravellerSchedule
   ADD CONSTRAINT FK_TravellerSchedule_Traveller_ID_Cascade
   FOREIGN KEY (travellerId) REFERENCES Traveller(id) ON DELETE CASCADE;
   

ALTER TABLE TGMatch
   ADD CONSTRAINT FK_TGMatch_Traveller_ID_Cascade
   FOREIGN KEY (travellerId) REFERENCES Traveller(id) ON DELETE CASCADE;

   

ALTER TABLE TTMatch
   ADD CONSTRAINT FK_TTMatch_Blue_Traveller_ID_Cascade
   FOREIGN KEY (blueTravellerId) REFERENCES Traveller(id) ON DELETE CASCADE;
   