/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package org.ops4j.pax.web.itest.undertow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.web.itest.base.client.HttpTestClientFactory;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.OptionUtils.combine;


/**
 * @author Achim Nierbeck
 */
@RunWith(PaxExam.class)
public class WebFragmentIntegrationTest extends ITestBase {

	private static final Logger LOG = LoggerFactory.getLogger(WebFragmentIntegrationTest.class);

	@Configuration
	public static Option[] configure() {
		return combine(configureUndertow(),
				mavenBundle().groupId("org.ops4j.pax.web.samples.web-fragment").artifactId("war").versionAsInProject(),
				mavenBundle().groupId("org.ops4j.pax.web.samples.web-fragment").artifactId("fragment").versionAsInProject()
				);
	}


	@Before
	public void setUp() throws BundleException, InterruptedException {
		LOG.info("Setting up test");
		
		initWebListener();
		waitForWebListener();
	}


	@Test
	public void testWC() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withResponseAssertion("Response must contain '<h1>Hello World</h1>'",
						resp -> resp.contains("<h1>Hello World</h1>"))
				.doGETandExecuteTest("http://127.0.0.1:8181/war/wc");

//		testClient.testWebPath("http://127.0.0.1:8181/war/wc", "<h1>Hello World</h1>");
			
	}

	@Test
	public void testFilterInit() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withResponseAssertion("Response must contain 'Have bundle context in filter: true'",
						resp -> resp.contains("Have bundle context in filter: true"))
				.doGETandExecuteTest("http://127.0.0.1:8181/war/wc");

//		testClient.testWebPath("http://127.0.0.1:8181/war/wc", "Have bundle context in filter: true");
	}
	
	@Test
	public void testWebContainerExample() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withResponseAssertion("Response must contain '<h1>Hello World</h1>'",
						resp -> resp.contains("<h1>Hello World</h1>"))
				.doGETandExecuteTest("http://127.0.0.1:8181/war/wc/example");
		// test image-serving
		HttpTestClientFactory.createDefaultTestClient()
				.doGETandExecuteTest("http://127.0.0.1:8181/war/images/logo.png");

//		testClient.testWebPath("http://127.0.0.1:8181/war/wc/example", "<h1>Hello World</h1>");
//		testClient.testWebPath("http://127.0.0.1:8181/war/images/logo.png", "", 200, false);
		
	}
	
	@Test
	public void testWebContainerSN() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withResponseAssertion("Response must contain '<h1>Hello World</h1>'",
						resp -> resp.contains("<h1>Hello World</h1>"))
				.doGETandExecuteTest("http://127.0.0.1:8181/war/wc/sn");

//		testClient.testWebPath("http://127.0.0.1:8181/war/wc/sn", "<h1>Hello World</h1>");

	}
	
	@Test
	public void testSlash() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withReturnCode(403)
				.withResponseAssertion("Response must contain '<h1>Error Page</h1>'",
						resp -> resp.contains("<h1>Error Page</h1>"))
				.doGETandExecuteTest("http://127.0.0.1:8181/war/");

//		testClient.testWebPath("http://127.0.0.1:8181/war/", "<h1>Error Page</h1>", 403, false);
	}
	
	
	@Test
	public void testSubJSP() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withResponseAssertion("Response must contain '<h2>Hello World!</h2>'",
						resp -> resp.contains("<h2>Hello World!</h2>"))
				.doGETandExecuteTest("http://127.0.0.1:8181/war/wc/subjsp");

//		testClient.testWebPath("http://127.0.0.1:8181/war/wc/subjsp", "<h2>Hello World!</h2>");
	}
	
	@Test
	public void testErrorJSPCall() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withReturnCode(404)
				.withResponseAssertion("Response must contain '<h1>Error Page</h1>'",
						resp -> resp.contains("<h1>Error Page</h1>"))
				.doGETandExecuteTest("http://127.0.0.1:8181/test");

//		testClient.testWebPath("http://127.0.0.1:8181/war/wc/error.jsp", "<h1>Error Page</h1>", 404, false);
	}
	
	@Test
	public void testWrongServlet() throws Exception {
		HttpTestClientFactory.createDefaultTestClient()
				.withReturnCode(404)
				.withResponseAssertion("Response must contain '<h1>Error Page</h1>'",
						resp -> resp.contains("<h1>Error Page</h1>"))
				.doGETandExecuteTest("http://127.0.0.1:8181/war/wrong/");

//		testClient.testWebPath("http://127.0.0.1:8181/war/wrong/", "<h1>Error Page</h1>", 404, false);
	}

}