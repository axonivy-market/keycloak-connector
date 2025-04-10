package com.axonivy.connector.keycloak.demo.bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.axonivy.connector.keycloak.demo.utils.JarUtils;

@ViewScoped
@ManagedBean
public class AdminManagementBean {
  private static final String TEMP_FILE_NAME = "keycloakThemes";
  private static final String JAR_EXTENTION = "keycloakThemes";
  private UploadedFile file;
  private StreamedContent theme;
  private String referenceTheme;
  private List<String> themes;
  private String registerUrl;
  private JarFile jarFile;

  public void getModifiedTheme() {
    theme = JarUtils.processJarFile(jarFile, referenceTheme, registerUrl);
  }

  public List<String> readJarFile(FileUploadEvent event) {
    this.file = event.getFile();
    if (Objects.isNull(file)) {
      return new ArrayList<>();
    }
    try (InputStream inputStream = file.getInputStream()) {
      Path tempFile = Files.createTempFile(TEMP_FILE_NAME, JAR_EXTENTION);
      Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
      jarFile = new JarFile(tempFile.toFile());
      themes = JarUtils.readThemeNameFromJar(jarFile);
      Files.deleteIfExists(tempFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return themes;
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

  public JarFile getJarFile() {
    return jarFile;
  }

  public void setJarFile(JarFile jarFile) {
    this.jarFile = jarFile;
  }
}
