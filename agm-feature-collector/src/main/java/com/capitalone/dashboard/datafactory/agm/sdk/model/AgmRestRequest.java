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

import java.util.Map;
import java.net.URL;

/**
 *
 * @author MHEDDEN
 */
public class AgmRestRequest {
    
    private URL url;
    private String method;
    private String data;
    private Map<String, String> headers;

    public AgmRestRequest(URL url, String method, String data) {
        this(url, method, data, null);
    }

    public AgmRestRequest(URL url, String method, String data, Map<String, String> headers) {
        this.url = url;
        this.method = method;
        this.data = data;
        this.headers = headers;
    }
    
    public AgmRestRequest(URL url, String method) {
        this(url, method, null, null);
    }
    
    public AgmRestRequest(URL url){
        this (url, "GET", null, null);
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    
}
