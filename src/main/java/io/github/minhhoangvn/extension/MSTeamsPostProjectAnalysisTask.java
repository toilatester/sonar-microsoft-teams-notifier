package io.github.minhhoangvn.extension;

import io.github.minhhoangvn.client.MSTeamsWebHookClient;
import io.github.minhhoangvn.utils.AdaptiveCardsFormat;
import io.github.minhhoangvn.utils.Constants;
import java.io.IOException;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;

public class MSTeamsPostProjectAnalysisTask implements PostProjectAnalysisTask, PostJob {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MSTeamsPostProjectAnalysisTask.class);
  private final MSTeamsWebHookClient client;
  private ProjectAnalysis analysis;

  private boolean sendResultToMSTeamFailed = false;

  /**
   * Constructor.
   */
  public MSTeamsPostProjectAnalysisTask() {
    this.client = new MSTeamsWebHookClient();
  }

  @Override
  public void finished(final Context context) {
    this.analysis = context.getProjectAnalysis();
    if (!isEnablePushResultToMSTeams()) {
      LOG.info("Notify result to Microsoft Team is disabled");
      return;
    }
    String webhookUrl = this.getWebhookUrl();
    String jsonPayload = AdaptiveCardsFormat.createMessageCardJSONPayload(analysis,
        this.getSonarqubeProjectUrl());
    LOG.info("Send Sonarqube result to Microsoft Teams payload: {}", jsonPayload);
    try (Response resp = client.sendNotify(webhookUrl, jsonPayload)) {
      int statusCode = resp.code();
      LOG.info("Send Sonarqube result to Microsoft Teams {}", statusCode);
      if (isHttpStatusInvalid(statusCode)) {
        sendResultToMSTeamFailed = true;
      }
    } catch (IOException e) {
      LOG.error("Error in sending Sonarqube result to Microsoft Team");
      sendResultToMSTeamFailed = true;
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

  private String getWebhookUrl() {
    return this.analysis.getScannerContext().getProperties()
        .get(Constants.WEBHOOK_URL);
  }

  private boolean isEnablePushResultToMSTeams() {
    return Boolean.parseBoolean(this.analysis.getScannerContext().getProperties()
        .get(Constants.ENABLE_NOTIFY));
  }

  @Override
  public void describe(PostJobDescriptor descriptor) {
    descriptor.name(this.toString());
  }

  @Override
  public void execute(@NotNull PostJobContext context) {
    if (sendResultToMSTeamFailed) {
      LOG.error("Error in sending Sonarqube result to Microsoft Team");
    }
  }

  private boolean isHttpStatusInvalid(int statusCode) {
    return statusCode < 200 || statusCode > 202;
  }
}
