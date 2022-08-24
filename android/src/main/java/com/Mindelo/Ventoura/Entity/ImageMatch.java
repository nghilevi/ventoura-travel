package com.Mindelo.Ventoura.Entity;

import com.Mindelo.Ventoura.Enum.UserRole;

import lombok.Data;


@Data
public class ImageMatch extends Image{
	
	long id;
	long userId;
	UserRole userRole;
}
