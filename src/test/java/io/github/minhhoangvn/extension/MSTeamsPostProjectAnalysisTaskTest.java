package io.github.minhhoangvn.extension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.http.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.ce.posttask.CeTask;
import org.sonar.api.ce.posttask.CeTask.Status;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask.Context;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask.ProjectAnalysis;
import org.sonar.api.ce.posttask.Project;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.ce.posttask.QualityGate.Condition;
import org.sonar.api.ce.posttask.QualityGate.EvaluationStatus;
import org.sonar.api.ce.posttask.QualityGate.Operator;
import org.sonar.api.ce.posttask.ScannerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MSTeamsPostProjectAnalysisTaskTest {

    private RequestBodyCaptureListener requestBodyCaptureListener =
            new RequestBodyCaptureListener();
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

    private WireMockServer wireMockServer;

    @Before
    public void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8080));
        wireMockServer.start();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testConstructor() {
        assertEquals(
                "MSTeamsPostProjectAnalysisTask",
                (new MSTeamsPostProjectAnalysisTask()).getDescription());
    }

    @Test
    public void testFinishedWithSendNotify() {
        wireMockServer.addMockServiceRequestListener(requestBodyCaptureListener);
        Context mockContext = Mockito.mock(Context.class);
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
        when(mockCeTask.getStatus()).thenReturn(Status.FAILED);
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
                                "http://localhost:8080/mock",
                                "sonar.notify.microsoft.team.webhook.avatar",
                                "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png",
                                "sonar.notify.webhook.fail.only.enable",
                                "true"));

        Mockito.when(mockContext.getProjectAnalysis()).thenReturn(mockProjectAnalysis);

        stubFor(
                post(urlEqualTo("/mock"))
                        .withHeader(
                                "Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
                        .withRequestBody(
                                WireMock.equalToJson(
                                        "{\"summary\":\"SonarQube Quality Gate"
                                            + " Result\",\"themeColor\":\"0072C6\",\"@type\":\"MessageCard\",\"potentialAction\":[{\"@type\":\"OpenUri\",\"name\":\"View"
                                            + " Analysis\",\"targets\":[{\"os\":\"default\",\"uri\":\"https://toilatester.blog/dashboard?id=null\"}]}],\"@context\":\"http://schema.org/extensions\",\"sections\":[{\"activitySubtitle\":\"Status:"
                                            + " **FAILED**\",\"activityTitle\":\"[stub-project-name]"
                                            + " SonarQube Analysis"
                                            + " Result\",\"activityImage\":\"https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png\",\"markdown\":\"true\",\"facts\":[{\"name\":\"Quality"
                                            + " Gate\",\"style\":\"Good\"},{\"name\":\"Stub Metric"
                                            + " Key Name\",\"style\":\"Good\",\"value\":\"Status"
                                            + " OK\\n"
                                            + "Current value is 1\\n"
                                            + "Threshold value 1\"}]}]}"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("{\"message\": \"Mock response\"}")));

        MSTeamsPostProjectAnalysisTask msTeamsPostProjectAnalysisTask =
                new MSTeamsPostProjectAnalysisTask();
        msTeamsPostProjectAnalysisTask.finished(mockContext);

        Assert.assertEquals(
                "{\"summary\":\"SonarQube Quality Gate"
                    + " Result\",\"themeColor\":\"0072C6\",\"@type\":\"MessageCard\",\"potentialAction\":[{\"@type\":\"OpenUri\",\"name\":\"View"
                    + " Analysis\",\"targets\":[{\"os\":\"default\",\"uri\":\"https://toilatester.blog/dashboard?id=null\"}]}],\"@context\":\"http://schema.org/extensions\",\"sections\":[{\"activitySubtitle\":\"Status:"
                    + " **FAILED**\",\"activityTitle\":\"[stub-project-name] SonarQube Analysis"
                    + " Result\",\"activityImage\":\"https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png\",\"markdown\":\"true\",\"facts\":[{\"name\":\"Quality"
                    + " Gate\",\"style\":\"Good\"},{\"name\":\"Stub Metric Key"
                    + " Name\",\"style\":\"Good\",\"value\":\"Status OK\\n"
                    + "Current value is 1\\n"
                    + "Threshold value 1\"}]}]}",
                requestBodyCaptureListener.getCapturedBodies().get(0));
    }

    @Test
    public void testFinishedWithNotSendNotifyWhenPassed() {
        wireMockServer.addMockServiceRequestListener(requestBodyCaptureListener);
        Context mockContext = Mockito.mock(Context.class);
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
                                "http://localhost:8080/mock",
                                "sonar.notify.microsoft.team.webhook.avatar",
                                "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png",
                                "sonar.notify.webhook.fail.only.enable",
                                "true"));

        Mockito.when(mockContext.getProjectAnalysis()).thenReturn(mockProjectAnalysis);

        stubFor(
                post(urlEqualTo("/mock"))
                        .withHeader(
                                "Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
                        .withRequestBody(
                                WireMock.equalToJson(
                                        "{\"summary\":\"SonarQube Quality Gate"
                                            + " Result\",\"themeColor\":\"0072C6\",\"@type\":\"MessageCard\",\"potentialAction\":[{\"@type\":\"OpenUri\",\"name\":\"View"
                                            + " Analysis\",\"targets\":[{\"os\":\"default\",\"uri\":\"https://toilatester.blog/dashboard?id=null\"}]}],\"@context\":\"http://schema.org/extensions\",\"sections\":[{\"activitySubtitle\":\"Status:"
                                            + " **FAILED**\",\"activityTitle\":\"[stub-project-name]"
                                            + " SonarQube Analysis"
                                            + " Result\",\"activityImage\":\"https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png\",\"markdown\":\"true\",\"facts\":[{\"name\":\"Quality"
                                            + " Gate\",\"style\":\"Good\"},{\"name\":\"Stub Metric"
                                            + " Key Name\",\"style\":\"Good\",\"value\":\"Status"
                                            + " OK\\n"
                                            + "Current value is 1\\n"
                                            + "Threshold value 1\"}]}]}"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("{\"message\": \"Mock response\"}")));

        MSTeamsPostProjectAnalysisTask msTeamsPostProjectAnalysisTask =
                new MSTeamsPostProjectAnalysisTask();
        msTeamsPostProjectAnalysisTask.finished(mockContext);

        Assert.assertEquals(0, requestBodyCaptureListener.getCapturedBodies().size());
    }

    static class RequestBodyCaptureListener implements RequestListener {

        private final List<String> capturedBodies = new ArrayList<>();

        public List<String> getCapturedBodies() {
            return capturedBodies;
        }

        @Override
        public void requestReceived(Request request, Response response) {
            String requestBody = request.getBodyAsString();
            capturedBodies.add(requestBody);
        }
    }
}
