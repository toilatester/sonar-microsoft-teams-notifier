package io.github.minhhoangvn.settings;

import io.github.minhhoangvn.utils.Constants;

import lombok.Getter;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class MSTeamsNotifyProperties {

    @Getter
    private static final PropertyDefinition enableNotifyProperty =
            PropertyDefinition.builder(Constants.ENABLE_NOTIFY)
                    .name("Enable Plugin")
                    .description("Enable push Sonarqube result to Microsoft Teams")
                    .defaultValue("false")
                    .type(PropertyType.BOOLEAN)
                    .category(Constants.CATEGORY)
                    .index(0)
                    .build();

    @Getter
    private static final PropertyDefinition webhookUrlProperty =
            PropertyDefinition.builder(Constants.WEBHOOK_URL)
                    .name("Webhook URL")
                    .description("Input your Webhook URL for sending SonarQube quality gate result")
                    .defaultValue("")
                    .category(Constants.CATEGORY)
                    .type(PropertyType.TEXT)
                    .index(1)
                    .build();

    @Getter
    private static final PropertyDefinition webhookMessageAvatarProperty =
            PropertyDefinition.builder(Constants.WEBHOOK_MESSAGE_AVATAR)
                    .name("Webhook Message Avatar")
                    .description("Input your Webhook avatar URL")
                    .defaultValue(
                            "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png")
                    .category(Constants.CATEGORY)
                    .type(PropertyType.STRING)
                    .index(2)
                    .build();

    @Getter
    private static final PropertyDefinition webhookSendOnFailedProperty =
            PropertyDefinition.builder(Constants.WEBHOOK_SEND_ON_FAILED)
                    .name("Webhook Send On Failed")
                    .description("Only send webhook when analysis failed")
                    .type(PropertyType.BOOLEAN)
                    .category(Constants.CATEGORY)
                    .defaultValue("true")
                    .index(3)
                    .build();

    private MSTeamsNotifyProperties() {
        // only statics
    }

    public static List<PropertyDefinition> getProperties() {

        List<PropertyDefinition> properties = new ArrayList<>();
        properties.add(enableNotifyProperty);
        properties.add(webhookUrlProperty);
        properties.add(webhookMessageAvatarProperty);
        properties.add(webhookSendOnFailedProperty);
        return properties;
    }
}
