package io.github.minhhoangvn.settings;

import io.github.minhhoangvn.utils.Constants;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class MSTeamsNotifyProperties {

    private static final PropertyDefinition ENABLE_NOTIFY =
            PropertyDefinition.builder(Constants.ENABLE_NOTIFY)
                    .name("Enable Plugin")
                    .description("Enable push Sonarqube result to Microsoft Teams")
                    .defaultValue("false")
                    .type(PropertyType.BOOLEAN)
                    .category(Constants.CATEGORY)
                    .index(1)
                    .build();

    private static final PropertyDefinition WEBHOOK_URL =
            PropertyDefinition.builder(Constants.WEBHOOK_URL)
                    .name("Webhook URL")
                    .description("Input your Webhook URL for sending SonarQube quality gate result")
                    .defaultValue("")
                    .category(Constants.CATEGORY)
                    .type(PropertyType.TEXT)
                    .index(2)
                    .build();

    private static final PropertyDefinition WEBHOOK_MESSAGE_AVATAR =
            PropertyDefinition.builder(Constants.WEBHOOK_MESSAGE_AVATAR)
                    .name("Webhook Message Avatar")
                    .description("Input your Webhook Avatar URL")
                    .defaultValue(
                            "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png")
                    .category(Constants.CATEGORY)
                    .type(PropertyType.STRING)
                    .index(3)
                    .build();

    private static final PropertyDefinition WEBHOOK_SEND_ON_FAILED =
            PropertyDefinition.builder(Constants.WEBHOOK_SEND_ON_FAILED)
                    .name("Webhook Send On Failed")
                    .description("Only send webhook when analysis failed")
                    .type(PropertyType.BOOLEAN)
                    .category(Constants.CATEGORY)
                    .defaultValue("true")
                    .index(4)
                    .build();

    private MSTeamsNotifyProperties() {
        // only statics
    }

    public static List<PropertyDefinition> getProperties() {

        List<PropertyDefinition> properties = new ArrayList<>();
        properties.add(ENABLE_NOTIFY);
        properties.add(WEBHOOK_URL);
        properties.add(WEBHOOK_MESSAGE_AVATAR);
        properties.add(WEBHOOK_SEND_ON_FAILED);
        return properties;
    }
}
