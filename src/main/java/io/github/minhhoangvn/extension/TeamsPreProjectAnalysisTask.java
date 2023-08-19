package io.github.minhhoangvn.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Phase;
import org.sonar.api.batch.Phase.Name;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

@Phase(name = Name.DEFAULT)
public class TeamsPreProjectAnalysisTask implements Sensor {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TeamsPreProjectAnalysisTask.class);

  @Override
  public void describe(SensorDescriptor sensorDescriptor) {
    sensorDescriptor.name(getClass().getName());
    LOG.info("Something to do pre scan: " + sensorDescriptor);
  }

  @Override
  public void execute(SensorContext sensorContext) {
    LOG.info("Something to do pre scan sensorContext: " + sensorContext);
    LOG.info("Something to do pre scan sonar.core.serverBaseURL: " + sensorContext.config()
        .get("sonar.core.serverBaseURL").orElseGet(() -> "N/A"));
    LOG.info(
        "Something to do pre scan sonar.host.url: " + sensorContext.config().get("sonar.host.url")
            .orElseGet(() -> "N/A"));
    LOG.info("Something to do pre scan sonar.microsoft.team.notify.webhook.url: "
        + sensorContext.config().get("sonar.microsoft.team.notify.webhook.url")
        .orElseGet(() -> "N/A"));
    LOG.info("Something to do pre scan sonar.microsoft.team.notify.webhook.title: "
        + sensorContext.config().get("sonar.microsoft.team.notify.webhook.title")
        .orElseGet(() -> "N/A"));
    sensorContext.addContextProperty("sonar.host.url",
        sensorContext.config().get("sonar.host.url").get());
    sensorContext.addContextProperty("sonar.microsoft.team.notify.webhook.url",
        sensorContext.config().get("sonar.microsoft.team.notify.webhook.url").get());
    sensorContext.addContextProperty("sonar.microsoft.team.notify.webhook.title",
        sensorContext.config().get("sonar.microsoft.team.notify.webhook.title").get());
  }
}
