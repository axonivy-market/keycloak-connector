package com.axonivy.connector.keycloak.helper;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordGeneratorHelper {
  private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
  private static final String NUMBER = "0123456789";
  private static final String SPECIAL = "!@#$%^&*()_-+=<>?/[]{}|";

  private static final String ALL_CHARS = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL;
  private static final SecureRandom random = new SecureRandom();

  public static String generatePassword() {
    List<Character> pwdChars = IntStream.range(0, 12)
        .mapToObj(i -> ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length()))).collect(Collectors.toList());
    Collections.shuffle(pwdChars, random);
    return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
  }
}
