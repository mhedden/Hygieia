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
package com.capitalone.dashboard.client.project;

import com.capitalone.dashboard.datafactory.agm.AgmDataFactoryImpl;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.ProjectRepository;
import com.capitalone.dashboard.util.FeatureSettings;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author MHEDDEN
 */
public class ProjectDataClientImplTest {
    private static final Logger logger = LoggerFactory
			.getLogger(ProjectDataClientImplTest.class);
    private static ProjectDataClientImpl instance = null;
    @Autowired
    private static ProjectRepository projectRepository;
    private static final FeatureSettings featureSettings = new FeatureSettings();
    @Autowired
    private static FeatureCollectorRepository featureCollectorRepository;
    private static AgmDataFactoryImpl agmApi = null;    
    
    public ProjectDataClientImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        //TODO: include your own test env values
        int workspace = 1000;
        String agmBaseUrl = "http://qaagmweb01t:8080";
        String agmClientId = "api_integration_client_1_2";
        String agmSecret = "R53vCtXWmenJ6ms"; 
        
        agmApi = new AgmDataFactoryImpl(workspace, agmBaseUrl, agmClientId, agmSecret);
        instance = new ProjectDataClientImpl(featureSettings, projectRepository, featureCollectorRepository, agmApi);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of updateProjectInformation method, of class ProjectDataClientImpl.
     */
    @Test
    public void testUpdateProjectInformation() {
        //TODO
        logger.info("Starting updateProjectInformation Unit test");
        instance.updateProjectInformation();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        logger.info("Completed updateProjectInformation Unit test");
    }
    
}
