/*
	This is the Geb configuration file.

	See: http://www.gebish.org/manual/current/#configuration
*/


import geb.report.ScreenshotReporter
import org.openqa.selenium.htmlunit.HtmlUnitDriver

waiting {
    timeout = 2
}
//
environments {

    // run via “./gradlew htmlUnitTest”
    // See: http://code.google.com/p/selenium/wiki/ChromeDriver
    htmlUnit {
        driver = { new HtmlUnitDriver(true) }
    }

}


// -Dgeb.build.reportsDir=geb-repo
if (!System.getProperty("geb.build.reportsDir")) {
    reportsDir = "build/reports/geb"
}


reporter = new ScreenshotReporter() {
    @Override
    protected escapeFileName(String name) {
        // name.replaceAll("[^\\w\\s-]", "_")
        name.replaceAll('[\\\\/:\\*?\\"<>\\|]', '_')
    }
}

// To run the tests with all browsers just run “./gradlew test”

baseUrl = "http://localhost:9091/tmcs-reservation"
