package io.github.minhhoangvn.extension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;

public class MSTeamsPreProjectAnalysisTaskTest {

    @Test
    public void testDescribeWithCorrectClassName() {
        SensorDescriptor mockSensorDescriptor = mock(SensorDescriptor.class);
        Configuration mockConfiguration = mock(Configuration.class);
        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);
        preProjectAnalysisTask.describe(mockSensorDescriptor);

        verify(mockSensorDescriptor)
                .name("io.github.minhhoangvn.extension.MSTeamsPreProjectAnalysisTask");
    }
}
