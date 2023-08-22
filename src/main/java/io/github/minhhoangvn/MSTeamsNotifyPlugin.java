package io.github.minhhoangvn;

import io.github.minhhoangvn.extension.MSTeamsPostProjectAnalysisTask;
import io.github.minhhoangvn.extension.MSTeamsPreProjectAnalysisTask;
import io.github.minhhoangvn.settings.MSTeamsNotifyProperties;

import org.sonar.api.Plugin;

public class MSTeamsNotifyPlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtension(MSTeamsPreProjectAnalysisTask.class);
        context.addExtension(MSTeamsPostProjectAnalysisTask.class);
        context.addExtensions(MSTeamsNotifyProperties.getProperties());
    }
}
