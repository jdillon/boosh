/*
 * Copyright 2017-present Walmart, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneops.boo.shell.commands;

import com.oneops.api.OOInstance;
import com.oneops.boo.ClientConfig;
import com.oneops.boo.utils.BooUtils;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.oneops.boo.yaml.BooBean;
import com.planet57.gshell.command.CommandActionSupport;
import com.planet57.gshell.util.cli2.Option;

import javax.annotation.Nullable;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Support for Boo actions.
 */
public abstract class BooActionSupport
  extends CommandActionSupport
{
  @Option(name="f", longName = "file", required = true, description = "Use template", token = "FILE")
  protected File template;

  @Option(name="p", longName = "profile", description = "Use profile", token = "PROFILE")
  protected String profile = ClientConfig.ONEOPS_DEFAULT_PROFILE;

  @Nullable
  @Option(name="a", longName = "assembly", description = "Override assembly name", token="NAME")
  protected String assembly;

  // FIXME: comment doesn't seem general, but its required to construct a BuildAllPlatforms which is why its here

  @Nullable
  @Option(name="m", longName = "message", description = "Customize comment for deployment", token = "MESSAGE")
  protected String comment;

  /**
   * Construct a new configuration.
   */
  protected ClientConfig createConfig() throws Exception {
    ClientConfig config = new ClientConfig(template, profile);
    new BooUtils().verifyTemplate(config);

    if (assembly != null) {
      config.getYaml().getAssembly().setName(assembly);
    }

    return config;
  }

  /**
   * Construct a new flow with configuration.
   */
  protected BuildAllPlatforms createFlow(final ClientConfig config) throws Exception {
    checkNotNull(config);

    OOInstance oo = new OOInstance();
    BooBean boo = config.getYaml().getBoo();
    oo.setAuthtoken(boo.getApikey());
    oo.setOrgname(boo.getOrg());
    oo.setEndpoint(boo.getHost());
    oo.setGzipEnabled(boo.isGzipEnabled());

    return new BuildAllPlatforms(oo, config, comment);
  }
}
