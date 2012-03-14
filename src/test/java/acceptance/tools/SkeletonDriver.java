package acceptance.tools;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;


public class SkeletonDriver {
    public static final int APP_PORT = 8080;
    public static final int TIME_OUT_IN_SECONDS = 3;
    private final WebDriver webDriver;

    public static SkeletonDriver start(String url) {
        return new SkeletonDriver(startWebDriver(url));
    }

    public static SkeletonDriver start() {
        return start("http://localhost:" + APP_PORT);
    }

    public void close() {
        webDriver.close();
    }

    public SkeletonDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    private static WebDriver startWebDriver(String url) {
        WebDriver driver = new HtmlUnitDriver(true);
//        WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("/home/q2/dev/tools/firefox-3.6.27/firefox-bin")), new FirefoxProfile());
        driver.get(url);

        return driver;
    }

    public void assertThatTitle(final Matcher<String> matcher) {
        assertThatEventually(By.className("title"), matcher);
    }

    private void assertThatEventually(By elementLocator, Matcher<String> matcher) {
        FallibleCondition condition = new FallibleCondition(elementLocator, matcher);
        try {
            new WebDriverWait(webDriver, TIME_OUT_IN_SECONDS).until(condition);
        } catch (org.openqa.selenium.TimeoutException e) {
            condition.reportFailure(e);
        }
    }

    private static class FallibleCondition implements ExpectedCondition<Object> {
        private final Matcher<String> matcher;
        private String actual;
        private boolean hasFailed = false;
        private final By elementLocator;

        public FallibleCondition(By elementLocator, Matcher<String> matcher) {
            this.matcher = matcher;
            this.elementLocator = elementLocator;
        }

        @Override
        public Object apply(WebDriver webDriver) {
            actual = webDriver.findElement(elementLocator).getText();
            if (matcher.matches(actual)) {
                return true;
            } else {
                hasFailed = true;
                return false;
            }
        }

        public void reportFailure(Throwable exceptionToBeChained) {
            Description description = new StringDescription();
            description.appendText("Timed out")
                    .appendText("\nExpected: ").appendDescriptionOf(matcher);
            if (hasFailed) {
                description.appendText("\n     but: ");
                matcher.describeMismatch(actual, description);
            }

            throw new RuntimeException(description.toString(), exceptionToBeChained);
        }
    }
}