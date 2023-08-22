package io.github.minhhoangvn.extension;

import io.github.minhhoangvn.client.MSTeamsWebHookClient;
import io.github.minhhoangvn.utils.AdaptiveCardsFormat;
import io.github.minhhoangvn.utils.Constants;

import okhttp3.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;

import java.io.IOException;

public class MSTeamsPostProjectAnalysisTask implements PostProjectAnalysisTask {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MSTeamsPostProjectAnalysisTask.class);

    private final MSTeamsWebHookClient client;
    private ProjectAnalysis analysis;

    /** Constructor. */
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
        String jsonPayload =
                AdaptiveCardsFormat.createMessageCardJSONPayload(
                        analysis, this.getSonarqubeProjectUrl());

        // Return if analysis status is SUCCESS
        boolean skipSendNotifyForStatusPassed =
                jsonPayload.contains(Constants.ANALYSIS_STATUS_SUCCESS)
                        && this.isEnablePushResultToMSTeamsWhenScanFailed();
        if (skipSendNotifyForStatusPassed) {
            LOG.info("Analysis PASSED. Skip sending notification to Teams.");
            return;
        }

        LOG.info("Send Sonarqube result to Microsoft Teams payload: {}", jsonPayload);
        try (Response resp = client.sendNotify(webhookUrl, jsonPayload)) {
            int statusCode = resp.code();
            LOG.info("Send Sonarqube result to Microsoft Teams {}", statusCode);
            if (isHttpStatusInvalid(statusCode)) {
                LOG.error("Send Sonarqube result to Microsoft Teams {}", statusCode);
            }
        } catch (IOException e) {
            LOG.error("Error in sending Sonarqube result to Microsoft Team");
        }
    }

    private String getSonarqubeProjectUrl() {
        String projectKey = this.analysis.getProject().getKey();
        return String.format("%s/dashboard?id=%s", this.getSonarHostUrl(), projectKey);
    }

    private String getSonarHostUrl() {
        return this.analysis.getScannerContext().getProperties().get(Constants.SONAR_URL);
    }

    private String getWebhookUrl() {
        return this.analysis.getScannerContext().getProperties().get(Constants.WEBHOOK_URL);
    }

    private boolean isEnablePushResultToMSTeams() {
        return Boolean.parseBoolean(
                this.analysis.getScannerContext().getProperties().get(Constants.ENABLE_NOTIFY));
    }

    private boolean isEnablePushResultToMSTeamsWhenScanFailed() {
        return Boolean.parseBoolean(
                this.analysis
                        .getScannerContext()
                        .getProperties()
                        .get(Constants.WEBHOOK_SEND_ON_FAILED));
    }

    private boolean isHttpStatusInvalid(int statusCode) {
        return statusCode < 200 || statusCode > 202;
    }
}
