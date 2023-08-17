package io.github.minhhoangvn.utils;

import java.util.Collection;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask.ProjectAnalysis;
import org.sonar.api.ce.posttask.QualityGate.Condition;

public class AdaptiveCardsFormat {

  public static String createMessageCardJSONPayload(ProjectAnalysis analysis) {
    return createMessageCard(analysis, "").toString();
  }

  private static JSONObject createMessageCard(ProjectAnalysis analysis, String projectUrl) {
    JSONObject messageCard = new JSONObject();
    messageCard.put("@type", "MessageCard");
    messageCard.put("@context", "http://schema.org/extensions");
    messageCard.put("themeColor", "0072C6");
    messageCard.put("summary", "SonarQube Analysis Result");

    JSONArray sections = new JSONArray();
    sections.put(createSection(analysis));
    messageCard.put("sections", sections);

    JSONArray potentialActions = new JSONArray();
    potentialActions.put(createOpenUriAction(projectUrl));
    messageCard.put("potentialAction", potentialActions);

    return messageCard;
  }

  private static JSONObject createSection(
      ProjectAnalysis analysis) {
    String projectName = analysis.getProject().getName();
    String taskStatus = analysis.getCeTask().getStatus().name();
    JSONObject section = new JSONObject();
    section.put("activityTitle", "[" + projectName + "] SonarQube Analysis Result");
    section.put("activitySubtitle", "Status: **" + taskStatus + "**");
    section.put("activityImage",
        "https://codefresh.io/docs/images/testing/sonarqube/sonarqube-logo.png");
    section.put("facts", createFacts(analysis));
    section.put("markdown", "true");
    return section;
  }

  private static JSONArray createFacts(ProjectAnalysis analysis) {
    JSONArray facts = new JSONArray();
    facts.put(createQualityGateFact(analysis));

    Collection<Condition> conditions = Objects.requireNonNull(analysis.getQualityGate()).getConditions();

    conditions.parallelStream().forEach(condition -> {
      JSONObject qualityGateConditionFact = new JSONObject();
      String conditionName = condition.getMetricKey();
      String conditionStatus = condition.getStatus().name();
      String conditionStatusValue = condition.getStatus().name().equals("NO_VALUE") ? "N/A" : condition.getValue();
      String conditionOperator = condition.getOperator().name();
      String conditionErrorThreshold = condition.getErrorThreshold();
      qualityGateConditionFact.put("name", condition.getMetricKey());
      qualityGateConditionFact.put("value",
          String.format("%s - %s: Current value is %s %s threshold value %s",
              conditionName,
              conditionStatus,
              conditionStatusValue,
              conditionOperator,
              conditionErrorThreshold));
      facts.put(qualityGateConditionFact);
    });

    return facts;
  }

  private static JSONObject createQualityGateFact(ProjectAnalysis analysis) {
    String qualityGateStatus = Objects.requireNonNull(analysis.getQualityGate()).getStatus().name();
    String qualityGateName = analysis.getQualityGate().getName();
    JSONObject qualityGateFact = new JSONObject();
    qualityGateFact.put("name", "Quality Gate");
    qualityGateFact.put("value", qualityGateName);
    qualityGateFact.put("style", getQualityGateStyle(qualityGateStatus));
    return qualityGateFact;
  }

  private static String getQualityGateStyle(String qualityGateStatus) {
    return qualityGateStatus.equals("OK") ? "Good" : "Warning";
  }

  private static JSONObject createOpenUriAction(String projectUrl) {
    JSONObject openUriAction = new JSONObject();
    JSONArray targets = new JSONArray();
    targets.put(createUriTarget(projectUrl));
    openUriAction.put("@type", "OpenUri");
    openUriAction.put("name", "View Analysis");
    openUriAction.put("targets", targets);
    return openUriAction;
  }

  private static JSONObject createUriTarget(String projectUrl) {
    JSONObject uriTarget = new JSONObject();
    uriTarget.put("os", "default");
    uriTarget.put("uri", projectUrl);
    return uriTarget;
  }
}
