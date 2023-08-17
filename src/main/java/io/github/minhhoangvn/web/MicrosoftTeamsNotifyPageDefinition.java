package io.github.minhhoangvn.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

public class MicrosoftTeamsNotifyPageDefinition implements PageDefinition {

  @Override
  public void define(Context context) {
    context
        .addPage(Page.builder("msteamsnotifier/global_page")
            .setName("Global Page using Vanilla JS")
            .build());
  }
}
