package io.github.minhhoangvn.extension;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class TeamsPreProjectAnalysisTask implements Sensor {

  /**
   * Logger.
   */
  private static final Logger LOG = Loggers.get(TeamsPreProjectAnalysisTask.class);

  @Override
  public void describe(SensorDescriptor sensorDescriptor) {
    sensorDescriptor.name(getClass().getName());
    LOG.info("Something to do pre scan: " + sensorDescriptor);
  }

  @Override
  public void execute(SensorContext sensorContext) {
    LOG.info("Something to do pre scan sensorContext: " + sensorContext);
  }
}
