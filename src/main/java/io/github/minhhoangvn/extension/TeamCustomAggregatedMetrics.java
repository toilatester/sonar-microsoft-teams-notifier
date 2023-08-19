/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2020 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.github.minhhoangvn.extension;

import static java.util.Arrays.asList;

import java.util.List;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class TeamCustomAggregatedMetrics implements Metrics {

  public static final Metric<Integer> CUSTOM_AGGREGATED_METRIC = new Metric.Builder(
      "custom_aggregated_metric", "Custom Aggregated Metric", Metric.ValueType.INT)
      .setDescription("Number of characters of file names")
      .setDirection(Metric.DIRECTION_BETTER)
      .setQualitative(false)
      .setDomain(CoreMetrics.DOMAIN_GENERAL)
      .create();

  @Override
  public List<Metric> getMetrics() {
    return asList(CUSTOM_AGGREGATED_METRIC);
  }
}
