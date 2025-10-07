/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2015 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.dam.assetservice.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.wcm.wcm.commons.caching.CacheHeader;
import io.wcm.wcm.commons.contenttype.ContentType;

/**
 * Returns generated data version if called on the root of an allowed asset path in DAM.
 */
class DataVersionServlet extends SlingSafeMethodsServlet {
  private static final long serialVersionUID = 1L;

  private final DamPathHandler damPathHandler;
  private static final JsonMapper JSON_MAPPER = new JsonMapper();

  private static final Logger log = LoggerFactory.getLogger(DataVersionServlet.class);

  DataVersionServlet(DamPathHandler damPathHandler) {
    this.damPathHandler = damPathHandler;
  }

  @Override
  protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws ServletException, IOException {
    String path = request.getResource().getPath();

    // check path is a valid DAM root folder path for asset service
    if (!damPathHandler.isAllowedDataVersionPath(path)) {
      log.debug("Path not allowed to get data version {}", path);
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // return data version as JSON
    ObjectNode jsonResponse = JSON_MAPPER.createObjectNode();
    jsonResponse.put("dataVersion", damPathHandler.getDataVersion(path));

    response.setContentType(ContentType.JSON);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(JSON_MAPPER.writeValueAsString(jsonResponse));
    CacheHeader.setNonCachingHeaders(response);
  }

}
