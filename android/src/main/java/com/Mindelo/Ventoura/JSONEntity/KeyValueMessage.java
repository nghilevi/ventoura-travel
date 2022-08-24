package com.Mindelo.Ventoura.JSONEntity;

import lombok.Data;

@Data
public class KeyValueMessage<K,M> {
	K key;
	M value;
}