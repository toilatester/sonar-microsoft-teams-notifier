package io.github.minhhoangvn;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import io.github.minhhoangvn.settings.MSTeamsNotifyProperties;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarRuntime;

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
}
