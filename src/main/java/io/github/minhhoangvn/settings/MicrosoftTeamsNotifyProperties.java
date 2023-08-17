package io.github.minhhoangvn.settings;

import java.util.ArrayList;
import java.util.List;
import org.sonar.api.config.PropertyDefinition;

public class MicrosoftTeamsNotifyProperties {

  public static final String WEBHOOK_URL = "sonar.microsoft.team.notify.webhook.url";
  public static final String CATEGORY = "Microsoft Team Notify Settings";

  public static final String WEBHOOK_TITLE = "sonar.microsoft.team.notify.webhook.title";

  private MicrosoftTeamsNotifyProperties() {
    // only statics
  }

  public static List<PropertyDefinition> getProperties() {
    List<PropertyDefinition> properties = new ArrayList<>();
    properties.add(
        PropertyDefinition.builder(WEBHOOK_URL)
            .name("Microsoft Teams Webhook URL")
            .description("Input your Webhook URL for sending SonarQube Result")
            .defaultValue("")
            .category(CATEGORY)
            .build());

    properties.add(
        PropertyDefinition.builder(WEBHOOK_TITLE)
            .name("Microsoft Teams Webhook URL")
            .description("Input your notify title")
            .defaultValue("")
            .category(CATEGORY)
            .build());

    return properties;
  }


}
