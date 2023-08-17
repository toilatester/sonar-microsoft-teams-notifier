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
    System.out.printf("Minh Hoang ProjectAnalysis: " + analysis);
    LOG.info("Minh Hoang settings: " + this.settings);
    LOG.info("Minh Hoang getQualityGate: " + analysis.getQualityGate());
    LOG.info("Minh Hoang getAnalysis: " + analysis.getAnalysis());
    LOG.info("Minh Hoang getScannerContext: " + analysis.getScannerContext().getProperties());
    LOG.info("Minh Hoang getCeTask: " + analysis.getCeTask());
    LOG.info("Minh Hoang createMessageCardJSONPayload: " + AdaptiveCardsFormat.createMessageCardJSONPayload(analysis));
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
}
