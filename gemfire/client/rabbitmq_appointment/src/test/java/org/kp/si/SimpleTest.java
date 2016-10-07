/**
 * 
 */
package org.kp.si;

//import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Felipe Gutierrez <fgutierrezcruz@gopivotal.com>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/integration/kaiser-integration-context.xml")
public class SimpleTest {

	@Test
	public void test() throws InterruptedException {
		Thread.sleep(10000);
	}

}
