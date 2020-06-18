package com.ranbhr.sample.controllers.apis;

import com.ranbhr.sample.dtos.SystemUserDTO;

import play.libs.typedmap.TypedKey;

public class Attrs {
	public static final TypedKey<SystemUserDTO> USER = TypedKey.<SystemUserDTO>create("user");

}
