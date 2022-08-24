package com.Mindelo.VentouraServer.JSONEntity;

import lombok.Data;


@Data
public class JSONKeyValueMessage<K,M> {
	K key;
	M value;
}