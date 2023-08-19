package io.github.minhhoangvn;

import io.github.minhhoangvn.extension.TeamsPostProjectAnalysisTask;
import io.github.minhhoangvn.extension.TeamsPreProjectAnalysisTask;
import io.github.minhhoangvn.settings.MicrosoftTeamsNotifyProperties;
import io.github.minhhoangvn.web.MicrosoftTeamsNotifyPageDefinition;
import org.sonar.api.Plugin;

public class MicrosoftTeamsNotifyPlugin implements Plugin {

  @Override
  public void define(Context context) {
    context.addExtension(TeamsPreProjectAnalysisTask.class);
    context.addExtension(TeamsPostProjectAnalysisTask.class);
    context
        .addExtensions(MicrosoftTeamsNotifyProperties.getProperties());

    context.addExtension(MicrosoftTeamsNotifyPageDefinition.class);
  }

}