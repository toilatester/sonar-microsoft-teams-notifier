package io.github.minhhoangvn.extension;

import io.github.minhhoangvn.utils.AdaptiveCardsFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.config.Configuration;

public class TeamsPostProjectAnalysisTask implements PostProjectAnalysisTask, PostJob {

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
    System.out.printf("Minh Hoang ProjectAnalysis: " + analysis);
    LOG.info("Minh Hoang settings: " + this.settings);
    LOG.info("Minh Hoang settings: " + this.settings.get("sonar.core.serverBaseURL")
        .orElseGet(() -> "N/A"));
    LOG.info("Minh Hoang getQualityGate: " + analysis.getQualityGate());
    LOG.info("Minh Hoang getAnalysis: " + analysis.getAnalysis());
    LOG.info("Minh Hoang getScannerContext: " + analysis.getScannerContext().getProperties());
    analysis.getScannerContext().getProperties().forEach((k, v) ->
        LOG.info("Minh Hoang getScannerContext: " + k.toString() + " : " + v.toString()));
    LOG.info("Minh Hoang getCeTask: " + analysis.getCeTask());
    LOG.info("Minh Hoang createMessageCardJSONPayload: "
        + AdaptiveCardsFormat.createMessageCardJSONPayload(analysis,
        this.getSonarqubeProjectUrl()));
  }

  @Override
  public void describe(PostJobDescriptor descriptor) {
    descriptor.name("After scan");
    LOG.info("Something to do after scan: " + descriptor);
  }

  @Override
  public void execute(PostJobContext context) {
    LOG.info("Minh Hoang Something to do after the analysis report has been submitted: " + context);
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
