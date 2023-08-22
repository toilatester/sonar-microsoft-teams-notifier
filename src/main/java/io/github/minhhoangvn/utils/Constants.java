package io.github.minhhoangvn.utils;

public class Constants {

  public static final String SONAR_URL = "sonar.host.url";
  public static final String CATEGORY = "Microsoft Team Notify Settings";
  public static final String ENABLE_NOTIFY = "sonar.microsoft.team.notify.enable";
  public static final String WEBHOOK_URL = "sonar.microsoft.team.notify.webhook.url";
  public static final String WEBHOOK_MESSAGE_AVATAR = "sonar.microsoft.team.notify.webhook.avatar";
  public static final String DEFAULT_WEBHOOK_MESSAGE_AVATAR = "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png";
  public static final String WEBHOOK_SEND_ON_FAIL = "sonar.webhook.fail.only.enable";
  public static final String ANALYSIS_STATUS_SUCCESS = "SUCCESS";

  private Constants() {
  }
}
