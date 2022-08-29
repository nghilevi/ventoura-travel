package com.Mindelo.Ventoura.Entity;

import lombok.Data;

@Data
public class ImageProfile extends Image{
	
	long id;
	long userId;
	boolean isPortal;
}
