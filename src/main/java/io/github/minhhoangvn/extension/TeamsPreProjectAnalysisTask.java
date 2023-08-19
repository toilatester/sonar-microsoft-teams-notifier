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
  }

  @Override
  public void execute(SensorContext sensorContext) {
    sensorContext.addContextProperty("sonar.host.url",
        sensorContext.config().get("sonar.host.url").get());
    sensorContext.addContextProperty("sonar.microsoft.team.notify.webhook.url",
        sensorContext.config().get("sonar.microsoft.team.notify.webhook.url").get());
    sensorContext.addContextProperty("sonar.microsoft.team.notify.webhook.title",
        sensorContext.config().get("sonar.microsoft.team.notify.webhook.title").get());
  }
}
