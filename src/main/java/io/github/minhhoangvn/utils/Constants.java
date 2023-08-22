package io.github.minhhoangvn.utils;

public class Constants {

    public static final String SONAR_URL = "sonar.host.url";
    public static final String CATEGORY = "Microsoft Team Notify Settings";
    public static final String ENABLE_NOTIFY = "sonar.notify.microsoft.team.enable";
    public static final String WEBHOOK_URL = "sonar.notify.microsoft.team.webhook.url";
    public static final String WEBHOOK_MESSAGE_AVATAR =
            "sonar.notify.microsoft.team.webhook.avatar";
    public static final String DEFAULT_WEBHOOK_MESSAGE_AVATAR =
            "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png";
    public static final String WEBHOOK_SEND_ON_FAILED = "sonar.notify.webhook.fail.only.enable";
    public static final String ANALYSIS_STATUS_SUCCESS = "SUCCESS";

    private Constants() {}
}
