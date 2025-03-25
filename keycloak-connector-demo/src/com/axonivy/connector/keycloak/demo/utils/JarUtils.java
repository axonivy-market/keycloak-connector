package com.axonivy.connector.keycloak.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JarUtils {
  private static final String AVAILABLE_THEME_PATH = "META-INF/keycloak-themes.json";
  private static final String LOGIN_FTL_PATH = "theme/%s/login/login.ftl";
  private static final String THEME_PROPERTIES_CONTENT = "parent=%s\nimport=common/keycloak\n";
  private static final String TEMP_FILE_NAME = "custom-theme.zip";
  private static final String ZIP_FILE_TYPE = "application/zip";

  private static final String DEFAULT_REGISTER_URL_KEY = "${url.registrationUrl}";
  private static final String CUSTOM_THEME_LOGIN_TEMPLATE_PATH = "custom-theme/login/login.ftl";
  private static final String CUSTOM_THEME_PROPERTY_TEMPLATE_PATH = "custom-theme/login/theme.properties";

  public static List<String> readThemeNameFromJar(JarFile jarFile) throws IOException {
    ZipEntry themesEntry = jarFile.getEntry(AVAILABLE_THEME_PATH);
    List<String> themes = new ArrayList<>();
    if (themesEntry != null) {
      InputStream themesInputStream = jarFile.getInputStream(themesEntry);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = mapper.readTree(themesInputStream);
      JsonNode themesNode = rootNode.path("themes");
      if (themesNode.isArray()) {
        for (JsonNode themeNode : themesNode) {
          JsonNode nameNode = themeNode.path("name");
          if (nameNode.isTextual()) {
            themes.add(nameNode.asText());
          }
        }
      }
    }
    return themes;
  }

  public static StreamedContent processJarFile(JarFile jarFile, String referencesTheme, String registrationUrl) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
      String loginTemplatePath = String.format(LOGIN_FTL_PATH, referencesTheme);
      JarEntry loginTemplateEntry = jarFile.stream()
          .filter(entry -> StringUtils.equals(entry.getName(), loginTemplatePath)).findAny()
          .orElseThrow(() -> new IOException("Template file not found in JAR: " + loginTemplatePath));

      // Create entry for custom theme
      try (InputStream inputStream = jarFile.getInputStream(loginTemplateEntry)) {
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        content = content.replace(DEFAULT_REGISTER_URL_KEY, registrationUrl);
        writeEntryToZipStream(zipOutputStream, content.getBytes(StandardCharsets.UTF_8),
            CUSTOM_THEME_LOGIN_TEMPLATE_PATH);
      }
      
      // Create entry for custom theme property
      String propertyContent = String.format(THEME_PROPERTIES_CONTENT, referencesTheme);
      writeEntryToZipStream(zipOutputStream, propertyContent.getBytes(StandardCharsets.UTF_8),
          CUSTOM_THEME_PROPERTY_TEMPLATE_PATH);
    } catch (IOException e) {
      throw new RuntimeException("Error processing JAR file", e);
    }

    InputStream zipInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    return DefaultStreamedContent.builder().name(TEMP_FILE_NAME).contentType(ZIP_FILE_TYPE).stream(() -> zipInputStream)
        .build();
  }

  private static void writeEntryToZipStream(ZipOutputStream zipOutputStream, byte[] content, String entryPath)
      throws IOException {
    ZipEntry zipEntry = new ZipEntry(entryPath);
    zipOutputStream.putNextEntry(zipEntry);
    zipOutputStream.write(content);
    zipOutputStream.closeEntry();
  }
}
