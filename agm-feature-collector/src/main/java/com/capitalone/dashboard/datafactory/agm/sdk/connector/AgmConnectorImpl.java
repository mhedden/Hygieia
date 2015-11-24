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


import com.capitalone.dashboard.datafactory.agm.sdk.model.AccessToken;
import com.capitalone.dashboard.datafactory.agm.sdk.model.AgmRestRequest;
import com.capitalone.dashboard.datafactory.agm.sdk.model.AgmRestResponse;
import com.capitalone.dashboard.util.FeatureSettings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ParseException;

/**
 *
 * @author MHEDDEN
 */
public class AgmConnectorImpl implements AgmConnector{
    private final Log logger = LogFactory
			.getLog(AgmConnectorImpl.class);
    private final String AGM_OAUTH_PATH = "/agm/oauth/token";
    private final String AGM_OAUTH_VALUES = "?client_id=%s&client_secret=%s&grant_type=client_credentials";

    private int agmWorkspace = 1000; 
    private String agmBaseUrl = "<http(s)>://<server>:<port>";
    private String agmClientId = "<client id>";
    private String agmSecret = "<secret>";
    private final AccessToken accessToken;

    /**
     * Default empty constructor
     * Pulls all settings from properties file
     */
    public AgmConnectorImpl() {
        this(new FeatureSettings().getWorkspace(), new FeatureSettings().getAgmBaseUri(), new FeatureSettings().getAgmClientId(), new FeatureSettings().getAgmSecret());
    } 
    
    /**
     * Constructor containing injected agmBaseUrl, agmClientId, and agmSecret
     * 
     * @param agmBaseUrl base url for agm api containing hostname and port (no trailing slash)
     * @param agmClientId agm client id provided by agm administrator
     * @param agmSecret agm secret for the client id
     */
    public AgmConnectorImpl(String agmBaseUrl, String agmClientId, String agmSecret){
        this(1000, agmBaseUrl, agmClientId, agmSecret, null);
    }
    
    /** 
     * Constructor containing injected workspace id, agmBaseUrl, agmClientId, agmSecret
     * 
     * @param workspace workspace to query for all actions with this connector
     * @param agmBaseUrl base url for agm api containing hostname and port (no trailing slash)
     * @param agmClientId agm client id provided by agm administrator
     * @param agmSecret agm secret for the client id
     */
    public AgmConnectorImpl(int workspace, String agmBaseUrl, String agmClientId, String agmSecret){
        this(workspace, agmBaseUrl, agmClientId, agmSecret, null);        
    }
    
    public AgmConnectorImpl(int workspace, String agmBaseUrl, String agmClientId, String agmSecret, AccessToken accessToken){
        this.agmWorkspace = workspace;
        this.agmBaseUrl = agmBaseUrl;
        this.agmClientId = agmClientId;
        this.agmSecret = agmSecret;
        if( null != accessToken){
            this.accessToken = accessToken; 
        }else{
            this.accessToken = agmAuthentication();
        }
     
    }
    
    @Override
    public String executeQuery(String query){
        AgmRestRequest agmRestRequest = createAgmRestRequestFromQuery(query);
        if (null == agmRestRequest){
            logger.info("There was an error generating an AgmRestRequest from the query: " + query);
            return null;
        }
        AgmRestResponse agmRestResponse = sendRequest(agmRestRequest);
        
        if (agmRestResponse.getStatusCode() != 200){
            logger.error("AgmRestRequest '"
                    + agmRestRequest.getUrl().toString()
                    + "' returned with status code '"
                    + agmRestResponse.getStatusCode());
            return null;
        }
        return agmRestResponse.getData();
    }

    private AccessToken agmAuthentication() {
        String urlStr = this.agmBaseUrl + this.AGM_OAUTH_PATH + String.format(this.AGM_OAUTH_VALUES, agmClientId, agmSecret);
        URL url = null;
        try{
            url = new URL(urlStr);
        }catch (MalformedURLException e){
            logger.error("Failed to Authenticate due to MalformedURLException for url: " + urlStr + ". Exception: " + e);
            throw new RuntimeException("Failed to Authenticate with configured client_id and secret.");
        }
        AgmRestRequest agmRequest = new AgmRestRequest(url, "POST", null, null);
        try{
            AgmRestResponse agmResponse = sendRequest(agmRequest);
            return AccessToken.createFromJson(agmResponse.getData());            
        }catch (ParseException e){
            logger.error("ParseException while processing response from url: " + urlStr + ". Exception: " + e);
            throw new RuntimeException("Failed to Authenticate with configured client_id and secret.");
        }catch (RuntimeException e){
            logger.error("Authentication failed due to sendRequest() RuntimeException:", e);
            throw new RuntimeException(e);            
        }
    }
    
    private AgmRestResponse sendRequest(AgmRestRequest request){
        if(null == request){
            logger.error("Request passed into sendRequest is null!");
            throw new RuntimeException("Null Request passed to sendRequest");
        }
        int statusCode;
        StringBuilder responseDataBuffer = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader br = null;        
        try{
            URL url = request.getUrl();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(request.getMethod());
            conn.setRequestProperty("Accept", "application/json");
            if(null != request.getHeaders()){
                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()){
                    conn.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                } 
            }
            if(request.getMethod().equals("POST")){
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                String data = request.getData();
                if (null == data){
                    //no data to post
                    os.write("".getBytes());
                }else{
                    os.write(data.getBytes());
                }
                os.flush();
            }

            statusCode = conn.getResponseCode();
        
            if (statusCode < 200 || statusCode > 300){
                throw new RuntimeException("Failed : HTTP error code : " + statusCode);
            }
            isr = new InputStreamReader(conn.getInputStream());
            br = new BufferedReader(isr);
            String output;
        
            while ((output = br.readLine()) != null){
                responseDataBuffer.append(output);
            }
            conn.disconnect();
        } catch (IOException e){
            logger.error("Exception thrown while reading the response of " + request.getUrl() + " Exception: ", e);
            throw new RuntimeException("Failed to parse response");
        } finally{
            try{  
                if (null != br){
                    br.close();
                }
            } catch (Exception e){
                logger.error("Unexpected Exception while trying to close BufferedReader. Exception: " + e);
                throw new RuntimeException("Unexpected Exception while trying to close BufferedReader.");
            }
            try{    
                if (null != isr){
                    isr.close();
                }
            } catch (Exception e){
                logger.error("Unexpected Exception while trying to close InputStreamReader. Exception: " + e);
                throw new RuntimeException("Unexpected Exception while trying to close InputStreamReader.");
            }
        }
        return new AgmRestResponse(statusCode, responseDataBuffer.toString());
    }
    
    private AgmRestRequest createAgmRestRequestFromQuery(String query){
        Map<String, String> header = new HashMap<>();
        if(null != this.accessToken){
            header.put("Authorization", "bearer " + this.accessToken.getAccessToken());
        }
        StringBuilder urlStr = new StringBuilder(agmBaseUrl);
        urlStr.append("/agm/api/workspaces/");
        urlStr.append(this.agmWorkspace);
        urlStr.append("/");
        urlStr.append(query);
        URL url;
        try{
            url = new URL(urlStr.toString());
        } catch (MalformedURLException e){
            logger.error("There was an error creating a URL object from the string " + urlStr.toString() + ".\nException: ", e);
            return null;
        }
        return new AgmRestRequest(url, "GET", null, header);
    }
}
