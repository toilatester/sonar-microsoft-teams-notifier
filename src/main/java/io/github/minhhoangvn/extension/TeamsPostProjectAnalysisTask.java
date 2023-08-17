package io.github.minhhoangvn.extension;

import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class TeamsPostProjectAnalysisTask implements PostProjectAnalysisTask, PostJob {

  /**
   * Logger.
   */
  private static final Logger LOG = Loggers.get(TeamsPostProjectAnalysisTask.class);
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
    LOG.info("Minh Hoang getQualityGate", analysis.getQualityGate());
    LOG.info("Minh Hoang getAnalysis", analysis.getAnalysis());
    LOG.info("Minh Hoang getScannerContext", analysis.getScannerContext());
    LOG.info("Minh Hoang getCeTask", analysis.getCeTask());
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
