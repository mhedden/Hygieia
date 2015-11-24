/*
 * Copyright 2015 Pivotal Software, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.capitalone.dashboard.datafactory.agm.sdk.model;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
* Holds access token information
*/
public class AccessToken {

   private String accessToken;
   private String tokenType;
   private int expiresIn;
   private String scope;

   public AccessToken(String accessToken, String tokenType, int expiresIn, String scope) {
       this.accessToken = accessToken;
       this.tokenType = tokenType;
       this.expiresIn = expiresIn;
       this.scope = scope;
   }

   public String getAccessToken() {
       return accessToken;
   }

   public String getTokenType() {
       return tokenType;
   }

   public int getExpiresIn() {
       return expiresIn;
   }

   public String getScope() {
       return scope;
   }

   public static AccessToken createFromJson(String json) throws ParseException {
       JSONParser parser = new JSONParser();
       JSONObject obj = (JSONObject) parser.parse(json);
       return new AccessToken((String)obj.get("access_token"), (String) obj.get("token_type"), Integer.parseInt(obj.get("expires_in").toString()), (String) obj.get("scope"));
   }
}