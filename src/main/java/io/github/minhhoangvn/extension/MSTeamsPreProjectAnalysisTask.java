package io.github.minhhoangvn.extension;

import io.github.minhhoangvn.utils.Constants;

import org.apache.commons.validator.routines.UrlValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Phase;
import org.sonar.api.batch.Phase.Name;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;

@Phase(name = Name.DEFAULT)
public class MSTeamsPreProjectAnalysisTask implements Sensor {

    private final UrlValidator urlValidator;
    private final Configuration settings;

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MSTeamsPreProjectAnalysisTask.class);

    public MSTeamsPreProjectAnalysisTask(Configuration settings) {
        this.settings = settings;
        this.urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
    }

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name(getClass().getName());
    }

    @Override
    public void execute(@NotNull SensorContext sensorContext) {
        if (!isEnablePushResultToMSTeams(sensorContext)) {
            LOG.info("Notify result to Microsoft Team is disabled");
            return;
        }
        sensorContext.addContextProperty(Constants.SONAR_URL, this.getSonarBaseUrl(sensorContext));
        sensorContext.addContextProperty(Constants.WEBHOOK_URL, this.getWebhookUrl(sensorContext));
        sensorContext.addContextProperty(
                Constants.ENABLE_NOTIFY,
                Boolean.toString(this.isEnablePushResultToMSTeams(sensorContext)));
        sensorContext.addContextProperty(
                Constants.WEBHOOK_SEND_ON_FAILED,
                Boolean.toString(this.isEnablePushResultToMSTeamsWhenScanFailed(sensorContext)));
        sensorContext.addContextProperty(
                Constants.WEBHOOK_MESSAGE_AVATAR, this.getWebhookAvatar(sensorContext));
    }

    private String getSonarBaseUrl(SensorContext sensorContext) {
        String settingSonarBaseUrl =
                this.settings.get("sonar.core.serverBaseURL").orElseGet(() -> "");
        String runtimeSonarBaseUrl =
                sensorContext.config().get(Constants.SONAR_URL).orElseGet(() -> "");
        if (urlValidator.isValid(runtimeSonarBaseUrl)) {
            return runtimeSonarBaseUrl;
        }
        if (urlValidator.isValid(settingSonarBaseUrl)) {
            return settingSonarBaseUrl;
        }
        LOG.error(
                "Invalid Sonarqube base URL,the value of runtime argument is '{}' or the value in"
                        + " SonarQube setting is '{}'",
                runtimeSonarBaseUrl,
                settingSonarBaseUrl);
        throw new RuntimeException(
                "Please provide Sonarqube base url through 'General-> Server base URL' config, "
                        + "or provide through sonar.host.url in mvn command");
    }

    private String getWebhookUrl(SensorContext sensorContext) {
        String runtimeWebhookUrl =
                sensorContext.config().get(Constants.WEBHOOK_URL).orElseGet(() -> "");
        String settingWebhookUrl = this.settings.get(Constants.WEBHOOK_URL).orElseGet(() -> "");
        if (urlValidator.isValid(runtimeWebhookUrl)) {
            return runtimeWebhookUrl;
        }
        if (urlValidator.isValid(settingWebhookUrl)) {
            return settingWebhookUrl;
        }
        LOG.error(
                "Invalid Microsoft Webhook URL,the value of runtime argument is '{}' or the value"
                        + " in SonarQube setting is '{}'",
                runtimeWebhookUrl,
                settingWebhookUrl);
        throw new RuntimeException(
                "Invalid Microsoft Webhook URL,the value of runtime argument is '{}' or the value"
                        + " in SonarQube setting is '{}'");
    }

    private String getWebhookAvatar(SensorContext sensorContext) {
        String runtimeWebhookAvatarUrl =
                sensorContext.config().get(Constants.WEBHOOK_MESSAGE_AVATAR).orElseGet(() -> "");
        String webhookAvatar =
                this.settings.get(Constants.WEBHOOK_MESSAGE_AVATAR).orElseGet(() -> "");
        if (urlValidator.isValid(runtimeWebhookAvatarUrl)) {
            return runtimeWebhookAvatarUrl;
        }
        if (urlValidator.isValid(webhookAvatar)) {
            return webhookAvatar;
        }
        return Constants.DEFAULT_WEBHOOK_MESSAGE_AVATAR;
    }

    private boolean isEnablePushResultToMSTeams(SensorContext sensorContext) {
        var isEnablePushResultToMSTeamsRuntime =
                Boolean.parseBoolean(
                        sensorContext
                                .config()
                                .get(Constants.ENABLE_NOTIFY)
                                .orElseGet(() -> "false"));
        if (isEnablePushResultToMSTeamsRuntime) {
            return true;
        }
        return this.settings.getBoolean(Constants.ENABLE_NOTIFY).orElseGet(() -> false);
    }

    private boolean isEnablePushResultToMSTeamsWhenScanFailed(SensorContext sensorContext) {
        var isEnablePushResultToMSTeamsRuntime =
                Boolean.parseBoolean(
                        sensorContext
                                .config()
                                .get(Constants.WEBHOOK_SEND_ON_FAILED)
                                .orElseGet(() -> "false"));
        if (isEnablePushResultToMSTeamsRuntime) {
            return true;
        }
        return this.settings.getBoolean(Constants.WEBHOOK_SEND_ON_FAILED).orElseGet(() -> false);
    }
}
