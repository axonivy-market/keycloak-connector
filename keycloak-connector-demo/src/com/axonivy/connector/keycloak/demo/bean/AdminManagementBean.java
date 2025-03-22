package com.axonivy.connector.keycloak.demo.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ManagedBean
@ViewScoped
public class AdminManagementBean {
  private UploadedFile file;
  private StreamedContent theme;
  private static final String BASE_LOGIN_FTL_PATH = "/opt/keycloak/themes/base/login/login.ftl";

  public void upload() {
    if (file != null) {
      FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
      FacesContext.getCurrentInstance().addMessage(null, message);
    }
  }

  public List<String> readThemesFromJar(UploadedFile file) {
    List<String> themes = new ArrayList<>();

    try (InputStream inputStream = file.getInputStream()) {
      Path tempFile = java.nio.file.Files.createTempFile("keycloakThemes", ".jar");
      Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

      try (JarFile jarFile = new JarFile(tempFile.toFile())) {
        ZipEntry themesEntry = jarFile.getEntry("META-INF/keycloak-themes.json");

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

  public StreamedContent createCustomThemeAsZip(String themeName) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try (ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
        addModifiedLoginFtlToZip(zipOutputStream, themeName);
        addThemePropertiesToZip(zipOutputStream, themeName);
      }
      byte[] zipBytes = baos.toByteArray();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(zipBytes);

      return DefaultStreamedContent.builder().contentType("application/zip").name(themeName + "-theme.zip")
          .stream(() -> inputStream).build();
    } catch (IOException e) {
      throw new RuntimeException("Error creating theme ZIP: " + e.getMessage(), e);
    }
  }

  private void addModifiedLoginFtlToZip(ZipOutputStream zipOutputStream, String themeName) throws IOException {
    String content = Files.readString(Paths.get(BASE_LOGIN_FTL_PATH), StandardCharsets.UTF_8);
    String modifiedContent = content.replace("<a href=\"${url.registration}\">", "<a href=\"abc\">");
    ZipEntry entry = new ZipEntry(themeName + "/login/login.ftl");
    zipOutputStream.putNextEntry(entry);
    zipOutputStream.write(modifiedContent.getBytes(StandardCharsets.UTF_8));
    zipOutputStream.closeEntry();
  }

  private void addThemePropertiesToZip(ZipOutputStream zipOutputStream, String themeName) throws IOException {
    String propertiesContent = "parent=base\n" + "import=common/keycloak\n";

    // Add theme.properties to the ZIP at the theme root
    ZipEntry entry = new ZipEntry(themeName + "/theme.properties");
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
}
