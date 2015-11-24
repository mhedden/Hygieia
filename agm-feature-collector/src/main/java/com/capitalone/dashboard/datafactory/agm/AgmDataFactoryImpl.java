/*************************DA-BOARD-LICENSE-START*********************************
 * Copyright 2014 CapitalOne, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************DA-BOARD-LICENSE-END*********************************/

package com.capitalone.dashboard.datafactory.agm;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.datafactory.agm.sdk.connector.AgmConnector;
import com.capitalone.dashboard.datafactory.agm.sdk.connector.AgmConnectorImpl;
import org.json.simple.JSONObject;

@Component
public class AgmDataFactoryImpl implements AgmDataFactory {
	private static final Log LOGGER = LogFactory.getLog(AgmDataFactoryImpl.class);
	@SuppressWarnings("PMD.AvoidUsingHardCodedIP") // not an IP
	private static final String AGENT_VER = "01.00.00.01";
	private static final String AGENT_NAME = "Hygieia Dashboard - AGM Feature Collector";

	protected int pageSize;
	protected int pageIndex;
	protected JSONArray jsonOutputArray;
	protected String basicQuery;
	protected String pagingQuery;
        protected AgmConnector agmService;

	/**
	 * Default constructor, which sets page size to 2000 and page index to 0.
	 */
	public AgmDataFactoryImpl() {
            LOGGER.info("Initializing version " + AGENT_VER + " of " + AGENT_NAME + " using empty constructor.");
            this.agmService = agmAuthentication();
            this.pageSize = 2000;
            this.pageIndex = 0;
	}
        
        public AgmDataFactoryImpl(int workspace, String agmBaseUrl, String agmClientId, String agmSecret) {
            LOGGER.info("Initializing version " + AGENT_VER + " of " + AGENT_NAME + " using AgmDataFactoryImpl(int workspace, String agmBaseUrl, String agmClientId, String agmSecret)");
            this.agmService = agmAuthentication(workspace, agmBaseUrl, agmClientId, agmSecret);
            this.pageSize = 2000;
            this.pageIndex = 0;
        }
        
        public AgmDataFactoryImpl(String agmBaseUrl, String agmClientId, String agmSecret) {
            this(-1, agmBaseUrl, agmClientId, agmSecret);
        }

	/**
	 * Used for establishing connection to Agm based on authentication
	 *
	 * @param auth
	 *            A key-value pairing of authentication values
	 * @return A V1Connector connection instance
	 */
	private AgmConnector agmAuthentication() {
		AgmConnector agmConnector = null;
                try {
                    agmConnector = new AgmConnectorImpl();
                }catch (Exception e){
                    LOGGER.fatal("Agm Authentication failed. Collector stopping. Exception: " + e);
                    System.exit(1);
                }
		return agmConnector;
	}
        
        private AgmConnector agmAuthentication(int workspace, String agmBaseUrl, String agmClientId, String agmSecret) {
		AgmConnector agmConnector = null;
                try {
                    if (workspace == -1){
                        agmConnector = new AgmConnectorImpl(1000, agmBaseUrl, agmClientId, agmSecret);                        
                    }else{
                        agmConnector = new AgmConnectorImpl(workspace, agmBaseUrl, agmClientId, agmSecret); 
                    }
                }catch (Exception e){
                    LOGGER.fatal("Agm Authentication failed. Collector stopping. Exception: " + e);
                    System.exit(1);
                }
		return agmConnector;
	}

	/**
	 * Sets the local query value on demand based on a given basic query.
	 *
	 * @param query
	 *            A query in YAML syntax as a String
	 * @return The saved YAML-syntax basic query
	 */
        @Override
	public String buildBasicQuery(String query) {
		this.setBasicQuery(query);
		return this.getBasicQuery();
	}

	/**
	 * Creates a query on demand based on a given basic query and a specified
	 * page index value. It is recommended to use this method in a loop to
	 * ensure all pages are covered.
	 *
	 * @param inPageIndex
	 *            A given query's current page index, from 0-oo
	 * @return A JSON-formatted response
	 */
        @Override
	public String buildPagingQuery(int inPageIndex) {
		this.setPageIndex(inPageIndex);
		String pageFilter = "&offset=" + pageIndex + "&limit=" +pageSize;
		this.setPagingQuery(this.getBasicQuery() + pageFilter);
		return this.getPagingQuery();
	}

	/**
	 * Runs the AgmConnection library tools against a given
	 * YAML-formatted query. This requires a pre-formatted paged query to run,
	 * and will not perform the paging for you - there are other helper methods
	 * for this.
	 *
	 * @return A formatted JSONArray response
	 */
        @Override
	public JSONArray getPagingQueryResponse() {
		synchronized (this.agmService) {
			this.setJsonOutputArray(this.agmService.executeQuery(this
					.getPagingQuery()));
		}

		return this.getJsonOutputArray();
	}

	/**
	 * Runs the AgmConnection library tools against a given
	 * YAML-formatted query. This requires a pre-formatted basic query
	 * (single-use).
	 *
	 * @return A formatted JSONArray response
	 */
        @Override
	public JSONArray getQueryResponse() {
		synchronized (this.agmService) {
			this.setJsonOutputArray(this.agmService.executeQuery(this
					.getBasicQuery()));
		}

		return this.getJsonOutputArray();
	}

	/**
	 * Mutator method for page index.
	 *
	 * @param pageIndex
	 *            Page index of query
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * Mutator method for page size.
	 *
	 * @param pageSize
	 *            defines page size to set number of results from a query
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Accessor method for page index.
	 *
	 * @return Page index of query
	 */
        @Override
	public int getPageIndex() {
		return this.pageIndex;
	}

	/**
	 * Accessor method for JSON response output array.
	 *
	 * @return JSON response array from Agm
	 */
	private JSONArray getJsonOutputArray() {
		return jsonOutputArray;
	}

	/**
	 * Mutator method for JSON response output array.
	 *
	 * @return JSON response array from Agm
	 */
	private void setJsonOutputArray(String stringResult) {
		JSONParser parser = new JSONParser();
		Object nativeRs = null;
		try {
			nativeRs = parser.parse(stringResult);
		} catch (ParseException e) {
			LOGGER.error("There was a problem parsing the JSONArray response value from the source system:\n"
					+ e.getMessage()
					+ " | "
					+ e.getCause().toString()
					+ " | "
					+ Arrays.toString(e.getStackTrace()));
		}
                if (null == nativeRs){
                    LOGGER.error("There was a problem parsing the following String into a JSONObject: " + stringResult);
                    setJsonOutputArray((JSONArray)null);
                    return;
                }
		JSONArray canonicalRs = (JSONArray) ((JSONObject) nativeRs).get("data");
                if (null == canonicalRs){
                    LOGGER.error("There was a problem parsing the 'data' JSONArray from the AGM Response: " + stringResult);
                }
		setJsonOutputArray(canonicalRs);
	}

        private void setJsonOutputArray(JSONArray jsonArray){
            this.jsonOutputArray = jsonArray;
        }

	/**
	 * Accessor method for basic query formatted object.
	 *
	 * @return Basic Agm YAML query
	 */
        @Override
	public String getBasicQuery() {
		return basicQuery;
	}

	/**
	 * Mutator method for basic query formatted object.
	 *
	 * @param Basic
	 *            Agm YAML query
	 */
	private void setBasicQuery(String basicQuery) {
		this.basicQuery = basicQuery;
	}

	/**
	 * Accessor method for retrieving paged query.
	 *
	 * @return The paged YAML query
	 */
        @Override
	public String getPagingQuery() {
		return pagingQuery;
	}

	/**
	 * Mutator method for setting paged query
	 *
	 * @param pagingQuery
	 *            The paged YAML query
	 */
	private void setPagingQuery(String pagingQuery) {
		this.pagingQuery = pagingQuery;
	}

	/**
	 * Used for testing: Accessor Method to get currently set page size
         * 
         * @return The currently set page size
	 */
	public int getPageSize() {
		return this.pageSize;
	}
}
