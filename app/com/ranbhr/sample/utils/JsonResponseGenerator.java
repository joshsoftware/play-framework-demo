package com.ranbhr.sample.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class JsonResponseGenerator {
	
	public static ObjectNode createResponse(Object response, boolean isSuccessful) {
        ObjectNode result = Json.newObject();
        result.put("isSuccessful", isSuccessful);
        result.set("data", (isSuccessful ? Json.toJson(response): null));
        result.put("error", isSuccessful ? null : (String)response);
        return result;
    }

}
