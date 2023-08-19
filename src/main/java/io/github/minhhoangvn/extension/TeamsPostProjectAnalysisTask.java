package io.github.minhhoangvn.extension;

import io.github.minhhoangvn.utils.AdaptiveCardsFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.config.Configuration;

public class TeamsPostProjectAnalysisTask implements PostProjectAnalysisTask {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TeamsPostProjectAnalysisTask.class);
  /**
   * SonarQube settings.
   */
  private final Configuration settings;

  private ProjectAnalysis analysis;

  /**
   * Constructor.
   *
   * @param settings The SonarQube Configuration settings.
   */
  public TeamsPostProjectAnalysisTask(Configuration settings) {
    this.settings = settings;
  }

  @Override
  public void finished(final ProjectAnalysis analysis) {
    this.analysis = analysis;
    LOG.info("Minh Hoang createMessageCardJSONPayload: "
        + AdaptiveCardsFormat.createMessageCardJSONPayload(analysis,
        this.getSonarqubeProjectUrl()));
  }

  private String getSonarqubeProjectUrl() {
    String sonarqubeHomeUrl = this.analysis.getScannerContext().getProperties()
        .get("sonar.host.url");
    String projectKey = this.analysis.getProject().getKey();
    return String.format("%s/dashboard?id=%s",
        sonarqubeHomeUrl,
        projectKey);
  }
}
