package com.axonivy.connector.keycloak.demo.bean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ViewScoped
@ManagedBean
public class AdminManagementBean {
  private UploadedFile file;
  private StreamedContent theme;
  private String referenceTheme;
  private static final String LOGIN_FTL_PATH = "theme/%s/login/login.ftl";
  private static final String DEFAULT_REGISTER_URL_KEY = "${url.registration}";
  private static final String CUSTOM_THEME_LOGIN_TEMPLATE_PATH = "custom-theme/login/login.ftl";
  private static final String AVAILABLE_THEME_PATH = "META-INF/keycloak-themes.json";
  private static final String CUSTOM_THEME_PROPERTY_TEMPLATE_PATH = "custom-theme/login/theme.properties";
  private static final String THEME_PROPERTIES_CONTENT = "parent=%s\nimport=common/keycloak\n";

  private List<String> themes;
  private String registerUrl;

  public void upload() {
    createCustomThemeAsZip();
  }



  public List<String> readJarFile(FileUploadEvent event) {
    this.file = event.getFile();
    themes = new ArrayList<>();
    if (Objects.isNull(file)) {
      return themes;
    }

    try (InputStream inputStream = file.getInputStream()) {
      Path tempFile = Files.createTempFile("keycloakThemes", ".jar");
      Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

      try (JarFile jarFile = new JarFile(tempFile.toFile())) {
        ZipEntry themesEntry = jarFile.getEntry(AVAILABLE_THEME_PATH);

        if (themesEntry != null) {
          try (InputStream themesInputStream = jarFile.getInputStream(themesEntry)) {
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
        }
      }
      Files.deleteIfExists(tempFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return themes;
  }

  public void updateThemes() {

  }

  public StreamedContent createCustomThemeAsZip() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try (ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
        addModifiedLoginFtlToZip(zipOutputStream);
        addThemePropertiesToZip(zipOutputStream);
      }
      byte[] zipBytes = baos.toByteArray();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(zipBytes);

      return DefaultStreamedContent.builder().contentType("application/zip").name("custom-theme.zip")
          .stream(() -> inputStream).build();
    } catch (IOException e) {
      throw new RuntimeException("Error creating theme ZIP: " + e.getMessage(), e);
    }
  }

  private String getFileContentFromJar(String filePath) {
    if (file == null || filePath == null || filePath.isEmpty()) {
      return null;
    }

    try {
      Path tempFile = Files.createTempFile("uploadedJar", ".jar");
      Files.write(tempFile, file.getContent());

      try (JarFile jarFile = new JarFile(tempFile.toFile())) {
        JarEntry entry = jarFile.getJarEntry(filePath);
        if (entry == null) {
          Files.deleteIfExists(tempFile);
          return null;
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8))) {
          StringBuilder content = new StringBuilder();
          String line;
          while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
          }
          Files.deleteIfExists(tempFile);
          return content.toString();
        }
      } finally {
        Files.deleteIfExists(tempFile);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;

    }
  }

  private void addModifiedLoginFtlToZip(ZipOutputStream zipOutputStream) throws IOException {
    String content = getFileContentFromJar(String.format(LOGIN_FTL_PATH, referenceTheme));
    String modifiedContent = content.replace(DEFAULT_REGISTER_URL_KEY, registerUrl);
    ZipEntry entry = new ZipEntry(CUSTOM_THEME_LOGIN_TEMPLATE_PATH);
    zipOutputStream.putNextEntry(entry);
    zipOutputStream.write(modifiedContent.getBytes(StandardCharsets.UTF_8));
    zipOutputStream.closeEntry();
  }

  private void addThemePropertiesToZip(ZipOutputStream zipOutputStream) throws IOException {
    String propertiesContent = String.format(THEME_PROPERTIES_CONTENT, referenceTheme);
    ZipEntry entry = new ZipEntry(CUSTOM_THEME_PROPERTY_TEMPLATE_PATH);
    zipOutputStream.putNextEntry(entry);
    zipOutputStream.write(propertiesContent.getBytes(StandardCharsets.UTF_8));
    zipOutputStream.closeEntry();
  }

  public UploadedFile getFile() {
    return file;
  }

  public void setFile(UploadedFile file) {
    this.file = file;
  }

  public StreamedContent getTheme() {
    return theme;
  }

  public void setTheme(StreamedContent theme) {
    this.theme = theme;
  }

  public void setReferenceTheme(String referenceTheme) {
    this.referenceTheme = referenceTheme;
  }

  public String getReferenceTheme() {
    return referenceTheme;
  }

  public List<String> getThemes() {
    return themes;
  }

  public void setThemes(List<String> themes) {
    this.themes = themes;
  }

  public String getRegisterUrl() {
    return registerUrl;
  }

  public void setRegisterUrl(String registerUrl) {
    this.registerUrl = registerUrl;
  }
}
