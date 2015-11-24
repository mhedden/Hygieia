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
package com.capitalone.dashboard.datafactory.agm.sdk.connector;

/**
 * The AgmConnector is used to interact with AGM
 * 
 * @author MHEDDEN
 */
public interface AgmConnector {
    /**
     * Returns a json formatted response from the AGM REST API using a REST request
     * generated with the inputted Query.
     * <p>
     * This will create and send a Get Request using the collectors configuration
     * in conjunction with the inputted query. If the response does not return the
     * expected HTTP status code then this String will be null.
     *  
     * @param query the query to send to the AGM Rest API
     * @return a json formatted response from the AGM Rest API
     */
    String executeQuery(String query);
    
}
