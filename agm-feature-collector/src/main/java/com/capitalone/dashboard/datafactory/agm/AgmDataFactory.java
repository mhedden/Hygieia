package com.capitalone.dashboard.datafactory.agm;

import org.json.simple.JSONArray;

import com.capitalone.dashboard.datafactory.DataFactory;

/**
 * Interface for AGM data connection. An implemented class should be able to create a formatted request,
 * and retrieve a response in JSON syntax from that request to AGM.
 *
 * @author KFK884
 *
 */
public interface AgmDataFactory extends DataFactory{
	String buildBasicQuery(String query);

	String buildPagingQuery(int inPageIndex);

	JSONArray getPagingQueryResponse();

	JSONArray getQueryResponse();

	String getBasicQuery();

	String getPagingQuery();

	int getPageIndex();

}
