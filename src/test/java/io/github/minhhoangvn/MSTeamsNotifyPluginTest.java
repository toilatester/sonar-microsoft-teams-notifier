package io.github.minhhoangvn;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import io.github.minhhoangvn.settings.MSTeamsNotifyProperties;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarRuntime;
import org.sonar.api.config.PropertyDefinition;

public class MSTeamsNotifyPluginTest {

    /** Method under test: {@link MSTeamsNotifyPlugin#define(Plugin.Context)} */
    @Test
    public void testDefinePluginWithCorrectExtensions() {
        MSTeamsNotifyPlugin msTeamsNotifyPlugin = new MSTeamsNotifyPlugin();
        Plugin.Context context = new Plugin.Context(mock(SonarRuntime.class));
        msTeamsNotifyPlugin.define(context);
        assertEquals(6, context.getExtensions().size());
        for (int i = 0; i < MSTeamsNotifyProperties.getProperties().size(); i++) {
            Assert.assertSame(
                    MSTeamsNotifyProperties.getProperties().get(i),
                    context.getExtensions().get(i + 2));
        }
    }

    @Test
    public void testDefinePluginWithCorrectEnableNotifySetting() {
        MSTeamsNotifyPlugin msTeamsNotifyPlugin = new MSTeamsNotifyPlugin();
        Plugin.Context context = new Plugin.Context(mock(SonarRuntime.class));
        msTeamsNotifyPlugin.define(context);
        PropertyDefinition actualPropertyDefinition =
                (PropertyDefinition) context.getExtensions().get(2);
        PropertyDefinition expectedPropertyDefinition =
                MSTeamsNotifyProperties.getEnableNotifyProperty();
        Assert.assertEquals(0, actualPropertyDefinition.index());
        Assert.assertEquals("Microsoft Team Notify Settings", actualPropertyDefinition.category());
        Assert.assertEquals("Enable Plugin", actualPropertyDefinition.name());
        Assert.assertEquals(
                "Enable push Sonarqube result to Microsoft Teams",
                actualPropertyDefinition.description());
        Assert.assertEquals("false", actualPropertyDefinition.defaultValue());
        Assert.assertEquals(PropertyType.BOOLEAN, actualPropertyDefinition.type());
        Assert.assertEquals("sonar.notify.microsoft.team.enable", actualPropertyDefinition.key());
    }

    @Test
    public void testDefinePluginWithCorrectWebhookUrlPropertySetting() {
        MSTeamsNotifyPlugin msTeamsNotifyPlugin = new MSTeamsNotifyPlugin();
        Plugin.Context context = new Plugin.Context(mock(SonarRuntime.class));
        msTeamsNotifyPlugin.define(context);
        PropertyDefinition actualPropertyDefinition =
                (PropertyDefinition) context.getExtensions().get(3);
        PropertyDefinition expectedPropertyDefinition =
                MSTeamsNotifyProperties.getEnableNotifyProperty();
        Assert.assertEquals(1, actualPropertyDefinition.index());
        Assert.assertEquals("Microsoft Team Notify Settings", actualPropertyDefinition.category());
        Assert.assertEquals("Webhook URL", actualPropertyDefinition.name());
        Assert.assertEquals(
                "Input your Webhook URL for sending SonarQube quality gate result",
                actualPropertyDefinition.description());
        Assert.assertEquals("", actualPropertyDefinition.defaultValue());
        Assert.assertEquals(PropertyType.TEXT, actualPropertyDefinition.type());
        Assert.assertEquals(
                "sonar.notify.microsoft.team.webhook.url", actualPropertyDefinition.key());
    }

    @Test
    public void testDefinePluginWithCorrectWebhookMessageAvatarPropertySetting() {
        MSTeamsNotifyPlugin msTeamsNotifyPlugin = new MSTeamsNotifyPlugin();
        Plugin.Context context = new Plugin.Context(mock(SonarRuntime.class));
        msTeamsNotifyPlugin.define(context);
        PropertyDefinition actualPropertyDefinition =
                (PropertyDefinition) context.getExtensions().get(4);
        PropertyDefinition expectedPropertyDefinition =
                MSTeamsNotifyProperties.getEnableNotifyProperty();
        Assert.assertEquals(2, actualPropertyDefinition.index());
        Assert.assertEquals("Microsoft Team Notify Settings", actualPropertyDefinition.category());
        Assert.assertEquals("Webhook Message Avatar", actualPropertyDefinition.name());
        Assert.assertEquals(
                "Input your Webhook avatar URL", actualPropertyDefinition.description());
        Assert.assertEquals(
                "https://raw.githubusercontent.com/toilatester/logo/main/toilatester.png",
                actualPropertyDefinition.defaultValue());
        Assert.assertEquals(PropertyType.STRING, actualPropertyDefinition.type());
        Assert.assertEquals(
                "sonar.notify.microsoft.team.webhook.avatar", actualPropertyDefinition.key());
    }

    @Test
    public void testDefinePluginWithCorrectWebhookSendOnFailedPropertySetting() {
        MSTeamsNotifyPlugin msTeamsNotifyPlugin = new MSTeamsNotifyPlugin();
        Plugin.Context context = new Plugin.Context(mock(SonarRuntime.class));
        msTeamsNotifyPlugin.define(context);
        PropertyDefinition actualPropertyDefinition =
                (PropertyDefinition) context.getExtensions().get(5);
        PropertyDefinition expectedPropertyDefinition =
                MSTeamsNotifyProperties.getEnableNotifyProperty();
        Assert.assertEquals(3, actualPropertyDefinition.index());
        Assert.assertEquals("Microsoft Team Notify Settings", actualPropertyDefinition.category());
        Assert.assertEquals("Webhook Send On Failed", actualPropertyDefinition.name());
        Assert.assertEquals(
                "Only send webhook when analysis failed", actualPropertyDefinition.description());
        Assert.assertEquals("true", actualPropertyDefinition.defaultValue());
        Assert.assertEquals(PropertyType.BOOLEAN, actualPropertyDefinition.type());
        Assert.assertEquals(
                "sonar.notify.webhook.fail.only.enable", actualPropertyDefinition.key());
    }
}
