package gobblin.runtime.locks;

import java.io.IOException;
import java.util.Properties;

import org.apache.curator.test.TestingServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import gobblin.configuration.ConfigurationKeys;

/**
 * Unit test for {@link ZookeeperBasedJobLock}.
 *
 * @author Joel Baranick
 */
@Test(groups = {"gobblin.runtime"})
public class ZookeeperBasedJobLockTest extends JobLockTest {
  private TestingServer testingServer;

  @BeforeClass
  public void setUp() throws Exception {
    testingServer = new TestingServer(-1);
  }

  @Override
  protected JobLock getJobLock() throws JobLockException, IOException {
    Properties properties = new Properties();
    properties.setProperty(ZookeeperBasedJobLock.CONNECTION_STRING, testingServer.getConnectString());
    properties.setProperty(ConfigurationKeys.JOB_NAME_KEY, "ZookeeperBasedJobLockTest-" + System.currentTimeMillis());
    properties.setProperty(ZookeeperBasedJobLock.MAX_RETRY_COUNT, "1");
    properties.setProperty(ZookeeperBasedJobLock.LOCKS_ACQUIRE_TIMEOUT_MILLISECONDS, "1000");
    properties.setProperty(ZookeeperBasedJobLock.RETRY_BACKOFF_SECONDS, "1");
    properties.setProperty(ZookeeperBasedJobLock.SESSION_TIMEOUT_SECONDS, "180");
    properties.setProperty(ZookeeperBasedJobLock.CONNECTION_TIMEOUT_SECONDS, "30");
    ZookeeperBasedJobLock lock = new ZookeeperBasedJobLock(properties);
    lock.setEventListener(new JobLockEventListener());
    return lock;
  }

  @Override
  @AfterClass
  public void tearDown() throws IOException {
    if (testingServer != null) {
      testingServer.close();
    }
  }
}
