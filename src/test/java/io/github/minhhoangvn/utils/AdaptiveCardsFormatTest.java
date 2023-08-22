package io.github.minhhoangvn.utils;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.ce.posttask.CeTask;
import org.sonar.api.ce.posttask.CeTask.Status;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask.ProjectAnalysis;
import org.sonar.api.ce.posttask.Project;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.ce.posttask.QualityGate.Condition;
import org.sonar.api.ce.posttask.QualityGate.EvaluationStatus;
import org.sonar.api.ce.posttask.QualityGate.Operator;
import org.sonar.api.ce.posttask.ScannerContext;

import java.util.List;
import java.util.Map;

public class AdaptiveCardsFormatTest {

    private final Condition stubNonRatingCondition =
            new Condition() {
                @Override
                public EvaluationStatus getStatus() {
                    return EvaluationStatus.OK;
                }

                @Override
                public String getMetricKey() {
                    return "stub_metric_key_name";
                }

                @Override
                public Operator getOperator() {
                    return Operator.GREATER_THAN;
                }

                @Override
                public String getErrorThreshold() {
                    return "1";
                }

                @Override
                public String getValue() {
                    return "1";
                }
            };

    private final Condition stubRatingCondition =
            new Condition() {
                @Override
                public EvaluationStatus getStatus() {
                    return EvaluationStatus.OK;
                }

                @Override
                public String getMetricKey() {
                    return "stub_rating_metric_key_name";
                }

                @Override
                public Operator getOperator() {
                    return Operator.GREATER_THAN;
                }

                @Override
                public String getErrorThreshold() {
                    return "1";
                }

                @Override
                public String getValue() {
                    return "1";
                }
            };

    private final Condition stubWithNoValueCondition =
            new Condition() {
                @Override
                public EvaluationStatus getStatus() {
                    return EvaluationStatus.NO_VALUE;
                }

                @Override
                public String getMetricKey() {
                    return "stub_rating_metric_key_name";
                }

                @Override
                public Operator getOperator() {
                    return Operator.GREATER_THAN;
                }

                @Override
                public String getErrorThreshold() {
                    return "1";
                }

                @Override
                public String getValue() {
                    return "1";
                }
            };

    /**
     * Method under test: {@link
     * AdaptiveCardsFormat#createMessageCardJSONPayload(PostProjectAnalysisTask.ProjectAnalysis,
     * String)}
     */
    @Test
    public void testCreateMessageCardJSONPayloadWithInvalidArgument() {
        assertThrows(
                NullPointerException.class,
                () ->
                        AdaptiveCardsFormat.createMessageCardJSONPayload(
                                null, "https://example.org/example"));
    }

    @Test
    public void testCreateMessageCardJSONPayloadWithValidArgumentForNonRatingCondition() {
        ProjectAnalysis mockProjectAnalysis = mock(ProjectAnalysis.class);
        Project mockProject = mock(Project.class);
        CeTask mockCeTask = mock(CeTask.class);
        ScannerContext mockScannerContext = mock(ScannerContext.class);
        QualityGate mockQualityGate = mock(QualityGate.class);

        when(mockProjectAnalysis.getProject()).thenReturn(mockProject);
        when(mockProjectAnalysis.getCeTask()).thenReturn(mockCeTask);
        when(mockProjectAnalysis.getQualityGate()).thenReturn(mockQualityGate);
        when(mockProjectAnalysis.getScannerContext()).thenReturn(mockScannerContext);
        when(mockProject.getName()).thenReturn("stub-project-name");
        when(mockCeTask.getStatus()).thenReturn(Status.SUCCESS);
        when(mockQualityGate.getStatus()).thenReturn(QualityGate.Status.OK);
        when(mockQualityGate.getConditions()).thenReturn(List.of(stubNonRatingCondition));
        when(mockScannerContext.getProperties())
                .thenReturn(
                        Map.of(
                                "sonar.host.url",
                                "https://toilatester.blog",
                                "sonar.notify.microsoft.team.enable",
                                "true",
                                "sonar.notify.microsoft.team.webhook.url",
                                "https://toilatester.blog/hook",
                                "sonar.notify.microsoft.team.webhook.avatar",
                                "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png",
                                "sonar.notify.webhook.fail.only.enable",
                                "true"));
        String actualJsonPayload =
                AdaptiveCardsFormat.createMessageCardJSONPayload(
                        mockProjectAnalysis, "https://example.org/example");
        Assert.assertEquals(
                "{\"summary\":\"SonarQube Quality Gate"
                    + " Result\",\"themeColor\":\"0072C6\",\"@type\":\"MessageCard\",\"potentialAction\":[{\"@type\":\"OpenUri\",\"name\":\"View"
                    + " Analysis\",\"targets\":[{\"os\":\"default\",\"uri\":\"https://example.org/example\"}]}],\"@context\":\"http://schema.org/extensions\",\"sections\":[{\"activitySubtitle\":\"Status:"
                    + " **SUCCESS**\",\"activityTitle\":\"[stub-project-name] SonarQube Analysis"
                    + " Result\",\"activityImage\":\"https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png\",\"markdown\":\"true\",\"facts\":[{\"name\":\"Quality"
                    + " Gate\",\"style\":\"Good\"},{\"name\":\"Stub Metric Key"
                    + " Name\",\"style\":\"Good\",\"value\":\"Status OK\\n"
                    + "Current value is 1\\n"
                    + "Threshold value 1\"}]}]}",
                actualJsonPayload);
    }

    @Test
    public void testCreateMessageCardJSONPayloadWithValidArgumentForRatingCondition() {
        ProjectAnalysis mockProjectAnalysis = mock(ProjectAnalysis.class);
        Project mockProject = mock(Project.class);
        CeTask mockCeTask = mock(CeTask.class);
        ScannerContext mockScannerContext = mock(ScannerContext.class);
        QualityGate mockQualityGate = mock(QualityGate.class);

        when(mockProjectAnalysis.getProject()).thenReturn(mockProject);
        when(mockProjectAnalysis.getCeTask()).thenReturn(mockCeTask);
        when(mockProjectAnalysis.getQualityGate()).thenReturn(mockQualityGate);
        when(mockProjectAnalysis.getScannerContext()).thenReturn(mockScannerContext);
        when(mockProject.getName()).thenReturn("stub-project-name");
        when(mockCeTask.getStatus()).thenReturn(Status.SUCCESS);
        when(mockQualityGate.getStatus()).thenReturn(QualityGate.Status.OK);
        when(mockQualityGate.getConditions()).thenReturn(List.of(stubRatingCondition));
        when(mockScannerContext.getProperties())
                .thenReturn(
                        Map.of(
                                "sonar.host.url",
                                "https://toilatester.blog",
                                "sonar.notify.microsoft.team.enable",
                                "true",
                                "sonar.notify.microsoft.team.webhook.url",
                                "https://toilatester.blog/hook",
                                "sonar.notify.microsoft.team.webhook.avatar",
                                "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png",
                                "sonar.notify.webhook.fail.only.enable",
                                "true"));
        String actualJsonPayload =
                AdaptiveCardsFormat.createMessageCardJSONPayload(
                        mockProjectAnalysis, "https://example.org/example");
        Assert.assertEquals(
                "{\"summary\":\"SonarQube Quality Gate"
                    + " Result\",\"themeColor\":\"0072C6\",\"@type\":\"MessageCard\",\"potentialAction\":[{\"@type\":\"OpenUri\",\"name\":\"View"
                    + " Analysis\",\"targets\":[{\"os\":\"default\",\"uri\":\"https://example.org/example\"}]}],\"@context\":\"http://schema.org/extensions\",\"sections\":[{\"activitySubtitle\":\"Status:"
                    + " **SUCCESS**\",\"activityTitle\":\"[stub-project-name] SonarQube Analysis"
                    + " Result\",\"activityImage\":\"https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png\",\"markdown\":\"true\",\"facts\":[{\"name\":\"Quality"
                    + " Gate\",\"style\":\"Good\"},{\"name\":\"Stub Rating Metric Key"
                    + " Name\",\"style\":\"Good\",\"value\":\"Status OK\\n"
                    + "Current value is A\\n"
                    + "Threshold value A\"}]}]}",
                actualJsonPayload);
    }

    @Test
    public void testCreateMessageCardJSONPayloadWithValidArgumentForNoValueCondition() {
        ProjectAnalysis mockProjectAnalysis = mock(ProjectAnalysis.class);
        Project mockProject = mock(Project.class);
        CeTask mockCeTask = mock(CeTask.class);
        ScannerContext mockScannerContext = mock(ScannerContext.class);
        QualityGate mockQualityGate = mock(QualityGate.class);

        when(mockProjectAnalysis.getProject()).thenReturn(mockProject);
        when(mockProjectAnalysis.getCeTask()).thenReturn(mockCeTask);
        when(mockProjectAnalysis.getQualityGate()).thenReturn(mockQualityGate);
        when(mockProjectAnalysis.getScannerContext()).thenReturn(mockScannerContext);
        when(mockProject.getName()).thenReturn("stub-project-name");
        when(mockCeTask.getStatus()).thenReturn(Status.SUCCESS);
        when(mockQualityGate.getStatus()).thenReturn(QualityGate.Status.OK);
        when(mockQualityGate.getConditions()).thenReturn(List.of(stubWithNoValueCondition));
        when(mockScannerContext.getProperties())
                .thenReturn(
                        Map.of(
                                "sonar.host.url",
                                "https://toilatester.blog",
                                "sonar.notify.microsoft.team.enable",
                                "true",
                                "sonar.notify.microsoft.team.webhook.url",
                                "https://toilatester.blog/hook",
                                "sonar.notify.microsoft.team.webhook.avatar",
                                "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png",
                                "sonar.notify.webhook.fail.only.enable",
                                "true"));
        String actualJsonPayload =
                AdaptiveCardsFormat.createMessageCardJSONPayload(
                        mockProjectAnalysis, "https://example.org/example");
        Assert.assertEquals(
                "{\"summary\":\"SonarQube Quality Gate"
                    + " Result\",\"themeColor\":\"0072C6\",\"@type\":\"MessageCard\",\"potentialAction\":[{\"@type\":\"OpenUri\",\"name\":\"View"
                    + " Analysis\",\"targets\":[{\"os\":\"default\",\"uri\":\"https://example.org/example\"}]}],\"@context\":\"http://schema.org/extensions\",\"sections\":[{\"activitySubtitle\":\"Status:"
                    + " **SUCCESS**\",\"activityTitle\":\"[stub-project-name] SonarQube Analysis"
                    + " Result\",\"activityImage\":\"https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png\",\"markdown\":\"true\",\"facts\":[{\"name\":\"Quality"
                    + " Gate\",\"style\":\"Good\"},{\"name\":\"Stub Rating Metric Key"
                    + " Name\",\"style\":\"Warning\",\"value\":\"Status NO_VALUE\\n"
                    + "Current value is 0\\n"
                    + "Threshold value A\"}]}]}",
                actualJsonPayload);
    }
}
