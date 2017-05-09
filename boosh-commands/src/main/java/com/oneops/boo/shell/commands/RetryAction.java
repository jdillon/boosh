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

import javax.annotation.Nonnull;

import com.oneops.boo.ClientConfig;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;

import static com.google.common.base.Preconditions.checkState;

/**
 * Retry deployments.
 */
@Command(name="boo/retry", description = "Retry deployments")
public class RetryAction
  extends BooActionSupport
{
  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    ClientConfig config = createConfig();
    BuildAllPlatforms flow = createFlow(config);

    checkState(flow.isAssemblyExist(), "Missing assembly: %s", config.getYaml().getAssembly().getName());

    flow.retryDeployment();

    return null;
  }
}
