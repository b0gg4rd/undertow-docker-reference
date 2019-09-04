package net.coatli.util;

import net.coatli.domain.Person;

public final class PersonsRequestBody {

  private static final String CURP_REGEX = "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$";

  public static boolean invalidPersonCreate(final Person person) {

    if (person.getCurp() == null || person.getCurp().trim().isEmpty() || !person.getCurp().matches(CURP_REGEX)) {
      return true;
    }

    return false;

  }

  public static boolean invalidPersonUpdate(final Person person) {

    if (person.getCurp() == null || person.getCurp().trim().isEmpty() || !person.getCurp().matches(CURP_REGEX)) {
      return true;
    }

    if (person.getNames().trim().isEmpty() && !person.getNames().matches("^[A-Za-zñÑ ]+$")) {
      return true;
    }

    return false;

  }

}
