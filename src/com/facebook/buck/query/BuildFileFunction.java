/*
 * Copyright 2015-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.buck.query;

import com.facebook.buck.query.QueryEnvironment.Argument;
import com.facebook.buck.query.QueryEnvironment.ArgumentType;
import com.facebook.buck.query.QueryEnvironment.QueryFunction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListeningExecutorService;

import java.util.Set;

/**
 * A "buildfile" query expression, which computes the build files that define the given targets.
 *
 * <pre>expr ::= BUILDFILE '(' expr ')'</pre>
 */
public class BuildFileFunction implements QueryFunction {

  private static final ImmutableList<ArgumentType> ARGUMENT_TYPES =
      ImmutableList.of(ArgumentType.EXPRESSION);

  public BuildFileFunction() {
  }

  @Override
  public String getName() {
    return "buildfile";
  }

  @Override
  public int getMandatoryArguments() {
    return 1;
  }

  @Override
  public ImmutableList<ArgumentType> getArgumentTypes() {
    return ARGUMENT_TYPES;
  }

  @Override
  public <T> Set<T> eval(
      QueryEnvironment<T> env,
      ImmutableList<Argument> args,
      ListeningExecutorService executor) throws QueryException, InterruptedException {
    Set<T> argumentSet = args.get(0).getExpression().eval(env, executor);
    return Sets.newHashSet(env.getBuildFiles(argumentSet));
  }

}
