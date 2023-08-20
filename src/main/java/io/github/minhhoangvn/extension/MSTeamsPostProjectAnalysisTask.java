package io.github.minhhoangvn.extension;

import io.github.minhhoangvn.client.MSTeamsWebHookClient;
import io.github.minhhoangvn.utils.AdaptiveCardsFormat;
import io.github.minhhoangvn.utils.Constants;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import okhttp3.Response;
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
      LOG.info("Notify result to Microsoft Team is disabled");
      return;
    }
    try {
      String webhookUrl = this.getWebhookUrl();
      String jsonPayload = AdaptiveCardsFormat.createMessageCardJSONPayload(analysis,
          this.getSonarqubeProjectUrl());
      try (Response resp = client.sendNotify(webhookUrl, jsonPayload)) {
        int statusCode = resp.code();
        LOG.info("Send Sonarqube result to Microsoft Teams {}", statusCode);
      } catch (IOException e) {
        return;
      }
    } catch (InvalidPropertiesFormatException e) {
      return;
    }
  }

  private String getSonarqubeProjectUrl() {
    String projectKey = this.analysis.getProject().getKey();
    return String.format("%s/dashboard?id=%s",
        this.getSonarHostUrl(),
        projectKey);
  }

  private String getSonarHostUrl() {
    return this.analysis.getScannerContext().getProperties()
        .get(Constants.SONAR_URL);
  }

  private String getWebhookUrl() throws InvalidPropertiesFormatException {
    return this.analysis.getScannerContext().getProperties()
        .get(Constants.WEBHOOK_URL);
  }

  private boolean isEnablePushResultToMSTeams() {
    return this.settings.getBoolean(Constants.ENABLE_NOTIFY)
        .orElseGet(() -> false);
  }
}
