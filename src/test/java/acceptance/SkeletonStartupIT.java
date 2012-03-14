package acceptance;

import acceptance.tools.SkeletonDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class SkeletonStartupIT {
    private SkeletonDriver driver;

    @Test
    public void showsTitle() {
        driver.assertThatTitle(is("Skeleton"));
    }

    @Before
    public void setUp() throws Exception {
        driver = SkeletonDriver.start();
    }

    @After
    public void tearDown() throws Exception {
        driver.close();
    }
}