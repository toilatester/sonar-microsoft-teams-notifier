package io.github.minhhoangvn.settings;

import io.github.minhhoangvn.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

public class MSTeamsNotifyProperties {


  private MSTeamsNotifyProperties() {
    // only statics
  }

  public static List<PropertyDefinition> getProperties() {
    List<PropertyDefinition> properties = new ArrayList<>();
    properties.add(
        PropertyDefinition.builder(Constants.ENABLE_NOTIFY)
            .name("Microsoft Teams Webhook URL")
            .description("Enable push Sonarqube result to Microsoft Teams")
            .defaultValue("false")
            .type(PropertyType.BOOLEAN)
            .category(Constants.CATEGORY)
            .build());

    properties.add(
        PropertyDefinition.builder(Constants.WEBHOOK_URL)
            .name("Microsoft Teams Webhook URL")
            .description("Input your Webhook URL for sending SonarQube result")
            .defaultValue("")
            .category(Constants.CATEGORY)
            .type(PropertyType.TEXT)
            .build());

    return properties;
  }


}
