package io.github.minhhoangvn.extension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.minhhoangvn.utils.Constants;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;

import java.util.HashMap;
import java.util.Optional;

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

    @Test
    public void testSetupAddingContextPropertyWithCorrectKey() {
        var actualContextProperty = new HashMap<>();
        SensorContext mockSensorContext = mock(SensorContext.class);
        Configuration mockConfiguration = mock(Configuration.class);
        Configuration mockConfigurationRuntime = mock(Configuration.class);

        when(mockSensorContext.config()).thenReturn(mockConfigurationRuntime);
        when(mockConfiguration.getBoolean(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of(true));
        when(mockConfiguration.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfiguration.get("sonar.core.serverBaseURL"))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy.com/hook"));
        when(mockConfiguration.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_MESSAGE_AVATAR))
                .thenReturn(Optional.of("https://dummy.com/hook/avatar"));

        when(mockConfigurationRuntime.get(Constants.WEBHOOK_MESSAGE_AVATAR))
                .thenReturn(Optional.of(""));

        Mockito.doAnswer(
                        i -> {
                            String key = i.getArgument(0);
                            String value = i.getArgument(1);
                            actualContextProperty.put(key, value);
                            return null;
                        })
                .when(mockSensorContext)
                .addContextProperty(any(), any());

        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);

        preProjectAnalysisTask.execute(mockSensorContext);

        verify(mockSensorContext, Mockito.times(5)).addContextProperty(any(), any());

        Assert.assertEquals(
                "https://dummy.com/hook/avatar",
                actualContextProperty.get(Constants.WEBHOOK_MESSAGE_AVATAR));
        Assert.assertEquals("https://dummy.com", actualContextProperty.get(Constants.SONAR_URL));
        Assert.assertEquals(
                "https://dummy.com/hook", actualContextProperty.get(Constants.WEBHOOK_URL));
        Assert.assertEquals("false", actualContextProperty.get(Constants.WEBHOOK_SEND_ON_FAILED));
    }

    @Test
    public void testSetupAddingContextPropertyCorrectKeyWithDisablePlugin() {
        SensorContext mockSensorContext = mock(SensorContext.class);
        Configuration mockConfiguration = mock(Configuration.class);

        when(mockSensorContext.config()).thenReturn(mockConfiguration);
        when(mockConfiguration.getBoolean(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of(false));
        when(mockConfiguration.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfiguration.get("sonar.core.serverBaseURL"))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy.com/hook"));
        when(mockConfiguration.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy.com"));

        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);

        preProjectAnalysisTask.execute(mockSensorContext);

        verify(mockSensorContext, Mockito.times(0)).addContextProperty(any(), any());
    }

    @Test
    public void testSetupAddingContextPropertyWithDynamicConfigDisablePlugin() {
        SensorContext mockSensorContext = mock(SensorContext.class);
        Configuration mockConfiguration = mock(Configuration.class);
        Configuration mockConfigurationRuntime = mock(Configuration.class);

        when(mockSensorContext.config()).thenReturn(mockConfigurationRuntime);
        when(mockConfiguration.getBoolean(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of(false));
        when(mockConfiguration.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfiguration.get("sonar.core.serverBaseURL"))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy.com/hook"));
        when(mockConfiguration.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy.com"));

        when(mockConfigurationRuntime.get(Constants.ENABLE_NOTIFY))
                .thenReturn(Optional.of("false"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy-runtime.com/hook"));
        when(mockConfigurationRuntime.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy-runtime.com"));

        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);

        preProjectAnalysisTask.execute(mockSensorContext);

        verify(mockSensorContext, Mockito.times(0)).addContextProperty(any(), any());
    }

    @Test
    public void testSetupAddingContextPropertyWithRuntimeDynamicConfigPlugin() {
        var actualContextProperty = new HashMap<>();

        SensorContext mockSensorContext = mock(SensorContext.class);
        Configuration mockConfiguration = mock(Configuration.class);
        Configuration mockConfigurationRuntime = mock(Configuration.class);

        when(mockSensorContext.config()).thenReturn(mockConfigurationRuntime);
        when(mockConfiguration.getBoolean(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of(true));
        when(mockConfiguration.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfiguration.get("sonar.core.serverBaseURL"))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy.com/hook"));
        when(mockConfiguration.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy.com"));

        when(mockConfigurationRuntime.get(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy-runtime.com/hook"));
        when(mockConfigurationRuntime.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy-runtime.com"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_MESSAGE_AVATAR))
                .thenReturn(Optional.of("https://dummy-runtime.com/hook/avatar"));

        Mockito.doAnswer(
                        i -> {
                            String key = i.getArgument(0);
                            String value = i.getArgument(1);
                            actualContextProperty.put(key, value);
                            return null;
                        })
                .when(mockSensorContext)
                .addContextProperty(any(), any());

        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);

        preProjectAnalysisTask.execute(mockSensorContext);

        verify(mockSensorContext, Mockito.times(5)).addContextProperty(any(), any());

        Assert.assertEquals(
                "https://dummy-runtime.com", actualContextProperty.get(Constants.SONAR_URL));
        Assert.assertEquals(
                "https://dummy-runtime.com/hook", actualContextProperty.get(Constants.WEBHOOK_URL));
        Assert.assertEquals(
                "https://dummy-runtime.com/hook/avatar",
                actualContextProperty.get(Constants.WEBHOOK_MESSAGE_AVATAR));
        Assert.assertEquals("true", actualContextProperty.get(Constants.WEBHOOK_SEND_ON_FAILED));
    }

    @Test
    public void testSetupAddingContextPropertyWithRuntimeDynamicConfigPluginWithEmptyUrl() {
        var actualContextProperty = new HashMap<>();

        SensorContext mockSensorContext = mock(SensorContext.class);
        Configuration mockConfiguration = mock(Configuration.class);
        Configuration mockConfigurationRuntime = mock(Configuration.class);

        when(mockSensorContext.config()).thenReturn(mockConfigurationRuntime);
        when(mockConfiguration.getBoolean(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of(true));
        when(mockConfiguration.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfiguration.get("sonar.core.serverBaseURL"))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy.com/hook"));
        when(mockConfiguration.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_MESSAGE_AVATAR)).thenReturn(Optional.of(""));

        when(mockConfigurationRuntime.get(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_URL)).thenReturn(Optional.of(""));
        when(mockConfigurationRuntime.get(Constants.SONAR_URL)).thenReturn(Optional.of(""));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_MESSAGE_AVATAR))
                .thenReturn(Optional.of(""));

        Mockito.doAnswer(
                        i -> {
                            String key = i.getArgument(0);
                            String value = i.getArgument(1);
                            actualContextProperty.put(key, value);
                            return null;
                        })
                .when(mockSensorContext)
                .addContextProperty(any(), any());

        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);

        preProjectAnalysisTask.execute(mockSensorContext);

        verify(mockSensorContext, Mockito.times(5)).addContextProperty(any(), any());

        Assert.assertEquals(
                "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png",
                actualContextProperty.get(Constants.WEBHOOK_MESSAGE_AVATAR));
        Assert.assertEquals("https://dummy.com", actualContextProperty.get(Constants.SONAR_URL));
        Assert.assertEquals(
                "https://dummy.com/hook", actualContextProperty.get(Constants.WEBHOOK_URL));
        Assert.assertEquals("true", actualContextProperty.get(Constants.WEBHOOK_SEND_ON_FAILED));
    }

    @Test(expected = RuntimeException.class)
    public void testSetupAddingContextPropertyWithInvalidSonarUrl() {

        SensorContext mockSensorContext = mock(SensorContext.class);
        Configuration mockConfiguration = mock(Configuration.class);
        Configuration mockConfigurationRuntime = mock(Configuration.class);

        when(mockSensorContext.config()).thenReturn(mockConfigurationRuntime);
        when(mockConfiguration.getBoolean(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of(true));
        when(mockConfiguration.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfiguration.get("sonar.core.serverBaseURL")).thenReturn(Optional.of(""));
        when(mockConfiguration.get(Constants.WEBHOOK_URL))
                .thenReturn(Optional.of("https://dummy.com/hook"));
        when(mockConfiguration.get(Constants.SONAR_URL)).thenReturn(Optional.of(""));
        when(mockConfiguration.get(Constants.WEBHOOK_MESSAGE_AVATAR)).thenReturn(Optional.of(""));

        when(mockConfigurationRuntime.get(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_URL)).thenReturn(Optional.of(""));
        when(mockConfigurationRuntime.get(Constants.SONAR_URL)).thenReturn(Optional.of(""));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_MESSAGE_AVATAR))
                .thenReturn(Optional.of(""));

        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);

        preProjectAnalysisTask.execute(mockSensorContext);
    }

    @Test(expected = RuntimeException.class)
    public void testSetupAddingContextPropertyWithInvalidWebhookUrl() {

        SensorContext mockSensorContext = mock(SensorContext.class);
        Configuration mockConfiguration = mock(Configuration.class);
        Configuration mockConfigurationRuntime = mock(Configuration.class);

        when(mockSensorContext.config()).thenReturn(mockConfigurationRuntime);
        when(mockConfiguration.getBoolean(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of(true));
        when(mockConfiguration.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("false"));
        when(mockConfiguration.get("sonar.core.serverBaseURL"))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_URL)).thenReturn(Optional.of(""));
        when(mockConfiguration.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfiguration.get(Constants.WEBHOOK_MESSAGE_AVATAR)).thenReturn(Optional.of(""));

        when(mockConfigurationRuntime.get(Constants.ENABLE_NOTIFY)).thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_SEND_ON_FAILED))
                .thenReturn(Optional.of("true"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_URL)).thenReturn(Optional.of(""));
        when(mockConfigurationRuntime.get(Constants.SONAR_URL))
                .thenReturn(Optional.of("https://dummy.com"));
        when(mockConfigurationRuntime.get(Constants.WEBHOOK_MESSAGE_AVATAR))
                .thenReturn(Optional.of(""));

        MSTeamsPreProjectAnalysisTask preProjectAnalysisTask =
                new MSTeamsPreProjectAnalysisTask(mockConfiguration);

        preProjectAnalysisTask.execute(mockSensorContext);
    }
}
