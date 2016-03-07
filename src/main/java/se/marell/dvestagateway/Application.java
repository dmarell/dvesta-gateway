/*
 * Created by Daniel Marell 14-02-22 13:51
 */
package se.marell.dvestagateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import se.marell.dvesta.system.BuildInfo;
import se.marell.dvesta.system.RunEnvironment;

@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        String runEnv = System.getProperty("se.marell.dvestagateway.environment");
        if (runEnv == null) {
            runEnv = "local";
        }
        app.setAdditionalProfiles(runEnv);
        app.addListeners(new ApplicationListener<ApplicationEnvironmentPreparedEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
                logger.info("Application Version: " + BuildInfo.getAppVersion());
                logger.info("Runtime Environment: " + RunEnvironment.getCurrentEnvironment(event.getEnvironment()));
            }
        });
        app.run(args);
    }
}
