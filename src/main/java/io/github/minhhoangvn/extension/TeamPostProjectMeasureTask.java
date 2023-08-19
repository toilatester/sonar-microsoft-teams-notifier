package io.github.minhhoangvn.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Phase;
import org.sonar.api.batch.Phase.Name;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static io.github.minhhoangvn.extension.TeamCustomAggregatedMetrics.CUSTOM_AGGREGATED_METRIC;

@Phase(name = Name.POST)
public class TeamPostProjectMeasureTask implements MeasureComputer {

  private static final Logger LOG = LoggerFactory.getLogger(TeamPostProjectMeasureTask.class);

  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext) {
    return defContext.newDefinitionBuilder()
        .setInputMetrics(
            "bugs",
            "vulnerabilities",
            "code_smells",
            "line_coverage",
            "coverage",
            "ncloc",
            "complexity",
            "cognitive_complexity")
        .setOutputMetrics(CUSTOM_AGGREGATED_METRIC.getKey())
        .build();
  }

  @Override
  public void compute(MeasureComputerContext context) {
    Measure bugs = context.getMeasure("bugs");
    Measure vulnerabilities = context.getMeasure("vulnerabilities");
    Measure codeSmells = context.getMeasure("code_smells");
    Measure lineCoverage = context.getMeasure("line_coverage");
    Measure newCodeCoverage = context.getMeasure("coverage");
    Measure allLoc = context.getMeasure("ncloc");
    Measure complexity = context.getMeasure("complexity");
    Measure cognitiveComplexity = context.getMeasure("cognitive_complexity");
    LOG.info("Metrics bugs: " + bugs.getIntValue());
    LOG.info("Metrics vulnerabilities: " + vulnerabilities.getIntValue());
    LOG.info("Metrics codeSmells: " + codeSmells.getIntValue());
    if (lineCoverage != null) {
      LOG.info("Metrics lineCoverage: " + lineCoverage.getDoubleValue());
    }
    if (newCodeCoverage != null) {
      LOG.info("Metrics newCodeCoverage: " + newCodeCoverage.getDoubleValue());
    }
    LOG.info("Metrics allLoc: " + allLoc.getIntValue());
    if (complexity != null) {
      LOG.info("Metrics complexity: " + complexity.getIntValue());
    }
    if (cognitiveComplexity != null) {
      LOG.info("Metrics cognitiveComplexity: " + cognitiveComplexity.getIntValue());
    }
    context.addMeasure(CUSTOM_AGGREGATED_METRIC.getKey(), 1);
  }
}
