package io.github.minhhoangvn.extension;

import io.github.minhhoangvn.client.MSTeamsWebHookClient;
import io.github.minhhoangvn.utils.AdaptiveCardsFormat;
import io.github.minhhoangvn.utils.Constants;
import java.util.InvalidPropertiesFormatException;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.config.Configuration;

public class MSTeamsPostProjectAnalysisTask implements PostProjectAnalysisTask {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MSTeamsPostProjectAnalysisTask.class);
  /**
   * SonarQube settings.
   */
  private final Configuration settings;
  private final MSTeamsWebHookClient client;
  private ProjectAnalysis analysis;

  /**
   * Constructor.
   *
   * @param settings The SonarQube Configuration settings.
   */
  public MSTeamsPostProjectAnalysisTask(Configuration settings) {
    this.settings = settings;
    this.client = new MSTeamsWebHookClient();
  }

  @Override
  public void finished(final Context context) {
    this.analysis = context.getProjectAnalysis();
    if (!isEnablePushResultToMSTeams()) {
      return;
    }
    try {
      String webhookUrl = this.getWebhookUrl();
      LOG.info("Minh Hoang createMessageCardJSONPayload: "
          + AdaptiveCardsFormat.createMessageCardJSONPayload(analysis,
          this.getSonarqubeProjectUrl()));
    } catch (InvalidPropertiesFormatException e) {
      return;
    }
  }

  private String getSonarqubeProjectUrl() {
    String sonarqubeHomeUrl = this.analysis.getScannerContext().getProperties()
        .get(Constants.SONAR_URL);
    String projectKey = this.analysis.getProject().getKey();
    return String.format("%s/dashboard?id=%s",
        sonarqubeHomeUrl,
        projectKey);
  }

  private String getSonarHostUrl() {
    return this.analysis.getScannerContext().getProperties()
        .get(Constants.SONAR_URL);
  }

  private String getWebhookUrl() throws InvalidPropertiesFormatException {
    var runtimeWebhookUrl = this.analysis.getScannerContext().getProperties()
        .get(Constants.WEBHOOK_URL);
    var settingWebhookUrl = this.settings.get(Constants.WEBHOOK_URL).orElseGet(() -> "");
    if (UrlValidator.getInstance().isValid(runtimeWebhookUrl)) {
      return runtimeWebhookUrl;
    }
    if (UrlValidator.getInstance().isValid(settingWebhookUrl)) {
      return settingWebhookUrl;
    }
    LOG.error(
        "Invalid Microsoft Webhook URL,the value of runtime argument is '{}' or the value in SonarQube setting is '{}'",
        runtimeWebhookUrl, settingWebhookUrl);
    throw new InvalidPropertiesFormatException(
        "Invalid Microsoft Webhook URL,the value of runtime argument is '{}' or the value in SonarQube setting is '{}'");
  }

  private boolean isEnablePushResultToMSTeams() {
    return this.settings.getBoolean(Constants.ENABLE_NOTIFY)
        .orElseGet(() -> false);
  }
}
