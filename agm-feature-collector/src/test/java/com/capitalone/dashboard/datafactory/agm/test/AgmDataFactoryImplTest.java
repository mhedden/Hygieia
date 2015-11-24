package com.capitalone.dashboard.datafactory.agm.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capitalone.dashboard.datafactory.agm.AgmDataFactoryImpl;

/**
 * Tests all facets of the agmDataFactoryImpl class, which is responsible
 * for handling all transactions to the source system, AGM.
 *
 * @author KFK884
 * @author mhedden
 *
 */
public class AgmDataFactoryImplTest {
	private static final Logger logger = LoggerFactory
			.getLogger("AgmDataFactoryImplTest");
	protected static String queryName;
	protected static String query;
	protected static String yesterday;
	protected static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	protected static AgmDataFactoryImpl agmDataFactory;

	/**
	 * Default constructor.
	 */
	public AgmDataFactoryImplTest() {
	}

	/**
	 * Runs actions before test is initialized.
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		logger.info("Beginning tests for com.capitalone.dashboard.datafactory.agm.AgmDataFactoryImpl");

                //TODO: include your own test values
                int workspace = 1000;
                String agmBaseUrl = "http://qaagmweb01t:8080";
                String agmClientId = "api_integration_client_1_2";
                String agmSecret = "R53vCtXWmenJ6ms";
                
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -3);
		yesterday = dateFormat.format(cal.getTime());
		yesterday = yesterday.replace(" ", "T");

                //TODO: Include your own test query
		query = "backlog_items?query=%22release_id=1028%22";
		agmDataFactory = new AgmDataFactoryImpl(workspace, agmBaseUrl, agmClientId, agmSecret);
	}

	/**
	 * Runs actions after test is complete.
	 *
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		agmDataFactory = null;
		yesterday = null;
		query = null;
	}

	/**
	 * Performs these actions before each test.
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Performs these actions after each test completes.
	 *
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.capitalone.dashboard.datafactory.versionone.VersionOneDataFactoryImpl#buildPagingQuery(int)}
	 * .
	 */
	@Test
	public void testBuildPagingQuery() {
		agmDataFactory.setPageSize(1);
		agmDataFactory.buildPagingQuery(30);
		assertNotNull("The basic query was created",
				agmDataFactory.getPagingQuery());
		assertEquals("The page size was accurate", 1,
				agmDataFactory.getPageSize());
		assertEquals("The page index was accurate", 30,
				agmDataFactory.getPageIndex());
	}

	/**
	 * Test method for
	 * {@link com.capitalone.dashboard.datafactory.agm.AgmDataFactoryImpl#getPagingQueryResponse()}
	 * .
	 */
	//@Ignore
	@Test
	public void testGetPagingQueryResponse() {
		agmDataFactory.setPageSize(1);
		agmDataFactory.buildBasicQuery(query);
		agmDataFactory.buildPagingQuery(10);
		try {
			JSONArray rs = agmDataFactory.getPagingQueryResponse();

			/*
			 * Testing actual JSON for values
			 */
			//JSONArray dataMainArry;
			JSONObject dataMainObj;
			//dataMainArry = (JSONArray) rs.get(0);
			dataMainObj = (JSONObject) rs.get(0);

			// id			
			assertNotNull("No valid id was found in response", dataMainObj.get("id"));
                        logger.info("PageTest: ID returned from first object = {}", dataMainObj.get("id"));
		} catch (NullPointerException npe) {
			fail("There was a problem with an object used to connect to Agm during the test");
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			fail("The object returned from Agm had no JSONObjects in it during the test; try increasing the scope of your test case query and try again.");
		} catch (IndexOutOfBoundsException ioobe) {
			logger.info("JSON artifact may be empty - re-running test to prove this out...");

			JSONArray rs = agmDataFactory.getPagingQueryResponse();

			/*
			 * Testing actual JSON for values
			 */
			String strRs;
			strRs = rs.toString();

			assertEquals(
					"There was nothing returned from AGM that is consistent with a valid response.",
					"[[]]", strRs);
		} catch (Exception e) {
			fail("There was an unexpected problem while connecting to AGM during the test");
		}
	}

	/**
	 * Test method for
	 * {@link com.capitalone.dashboard.datafactory.agm.AgmDataFactoryImpl#AgmDataFactoryImpl()}
	 * .
	 */
	@Test
	public void testVersionOneDataFactoryImpl() {
		assertEquals("The compared contructed page size values did not match",
				2000, agmDataFactory.getPageSize());
	}


	/**
	 * Test method for
	 * {@link com.capitalone.dashboard.datafactory.agm.AgmDataFactoryImpl#buildBasicQuery(java.lang.String)}
	 * .
	 */
	@Test
	public void testBuildBasicQuery() {
		agmDataFactory.setPageSize(1);
                agmDataFactory.setPageIndex(0);
		agmDataFactory.buildBasicQuery(query);
		assertNotNull("The basic query was created",
				agmDataFactory.getBasicQuery());
		assertEquals("The page size was accurate", 1,
				agmDataFactory.getPageSize());
		assertEquals("The page index was accurate", 0,
				agmDataFactory.getPageIndex());
	}

	/**
	 * Test method for
	 * {@link com.capitalone.dashboard.datafactory.agm.AgmDataFactoryImpl#getQueryResponse(java.lang.String)}
	 * .
	 */
	//@Ignore
	@Test
	public void testGetQueryResponse() {
		agmDataFactory.setPageSize(1);
		agmDataFactory.buildBasicQuery(query);
		try {
			JSONArray rs = agmDataFactory.getQueryResponse();

			/*
			 * Testing actual JSON for values
			 */
			//JSONArray dataMainArry;
			JSONObject dataMainObj;
			//dataMainArry = (JSONArray) rs.get(0);
			dataMainObj = (JSONObject) rs.get(0);

			// check valid id from response
			assertNotNull("No valid id was found in response", dataMainObj.get("id"));
                        logger.info("QueryTest: ID returned from first object = {}", dataMainObj.get("id"));
		} catch (NullPointerException npe) {
			fail("There was a problem with an object used to connect to Agm during the test");
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			fail("The object returned from Agm had no JSONObjects in it during the test; try increasing the scope of your test case query and try again.");
		} catch (IndexOutOfBoundsException ioobe) {
			logger.info("JSON artifact may be empty - re-running test to prove this out...");

			JSONArray rs = agmDataFactory.getQueryResponse();

			/*
			 * Testing actual JSON for values
			 */
			String strRs;
			strRs = rs.toString();

			assertEquals(
					"There was nothing returned from Agm that is consistent with a valid response.",
					"[[]]", strRs);
		} catch (Exception e) {
                        logger.error("There was an unexpected problem while connecting to Agm during the test. Exception: ", e);
			fail("There was an unexpected problem while connecting to Agm during the test.");
		}
	}

}
