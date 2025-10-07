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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.wcm.handler.media.Media;
import io.wcm.handler.media.MediaHandler;
import io.wcm.handler.media.Rendition;
import io.wcm.wcm.commons.contenttype.ContentType;

/**
 * Implements a simple REST interface that allows resolving DAM asset paths to URLs.
 * For image assets resolving to specific dimensions is supported.
 */
class AssetRequestServlet extends SlingSafeMethodsServlet {
  private static final long serialVersionUID = 1L;

  private final DamPathHandler damPathHandler;
  private static final JsonMapper JSON_MAPPER = new JsonMapper();

  private static final Logger log = LoggerFactory.getLogger(AssetRequestServlet.class);

  AssetRequestServlet(DamPathHandler damPathHandler) {
    this.damPathHandler = damPathHandler;
  }

  @SuppressWarnings("null")
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    String assetPath = request.getResource().getPath();

    // check if asset path is valid
    if (!damPathHandler.isAllowedAssetPath(assetPath)) {
      log.debug("Asset path not allowed {}", assetPath);
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // get media handler
    MediaHandler mediaHandler = request.getResource().adaptTo(MediaHandler.class);
    if (mediaHandler == null) {
      log.debug("Unable to get media handler for {}", assetPath);
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // build list of asset service requests with optional input parameters
    List<AssetRequest> requests = AssetRequestParser.getAssetRequests(assetPath, request);

    // resolve asset service requests
    List<Media> mediaList = resolveMedia(requests, mediaHandler);
    if (mediaList.isEmpty()) {
      log.debug("No matching assets/renditions found for {}; requests: {}", assetPath, requests);
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // output result json
    ArrayNode resultJson = toResultJson(mediaList);
    response.setContentType(ContentType.JSON);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(JSON_MAPPER.writeValueAsString(resultJson));
  }

  private List<Media> resolveMedia(List<AssetRequest> requests, MediaHandler mediaHandler) {
    List<Media> result = new ArrayList<>();
    for (AssetRequest request : requests) {
      Media media = request.resolve(mediaHandler);
      if (media.isValid()) {
        result.add(media);
      }
    }
    return result;
  }

  private ArrayNode toResultJson(List<Media> mediaList) {
    ArrayNode array = JSON_MAPPER.createArrayNode();
    for (Media media : mediaList) {
      Rendition rendition = media.getRendition();
      ObjectNode mediaObject = JSON_MAPPER.createObjectNode();
      mediaObject.put("assetPath", media.getAsset().getPath());
      mediaObject.put("url", media.getUrl());
      if (rendition.getWidth() > 0 && rendition.getHeight() > 0) {
        mediaObject.put("width", rendition.getWidth());
        mediaObject.put("height", rendition.getHeight());
      }
      if (rendition.getFileSize() > 0) {
        mediaObject.put("fileSize", rendition.getFileSize());
      }
      String fileExtension = rendition.getFileExtension();
      if (fileExtension != null) {
        mediaObject.put("fileExtension", fileExtension);
      }
      String mimeType = rendition.getMimeType();
      if (mimeType != null) {
        mediaObject.put("mimeType", mimeType);
      }
      array.add(mediaObject);
    }
    return array;
  }

}
