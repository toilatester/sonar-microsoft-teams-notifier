package io.github.minhhoangvn.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

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
  }
}
