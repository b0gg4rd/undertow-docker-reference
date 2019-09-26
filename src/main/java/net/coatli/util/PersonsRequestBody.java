package net.coatli.util;

import net.coatli.domain.Person;

public final class PersonsRequestBody {

  private static final String CURP_REGEX     = "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$";
  private static final String NAMES_REGEX    = "^[A-Za-zñÑ ]+$";
  private static final String BIRTHDAY_REGEX = "^(((0[1-9]|1[0-9]|2[0-8])[\\\\/](0[1-9]|1[012]))|((29|30|31)[\\\\/](0[13578]|1[02]))|((29|30)[\\\\/](0[4,6,9]|11)))[\\\\/](19|[2-9][0-9])\\\\d\\\\d$)|(^29[\\\\/]02[\\\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$";

  public static boolean invalidPersonCreate(final Person person) {

    if (person.getCurp() == null || person.getCurp().isBlank() || !person.getCurp().matches(CURP_REGEX)) {
      return true;
    }

    return false;

  }

  public static boolean invalidPersonUpdate(final Person person) {

    if (person.getCurp() == null || person.getCurp().isBlank() || !person.getCurp().matches(CURP_REGEX)) {
      return true;
    }

    if (person.getNames() != null && !person.getNames().matches(NAMES_REGEX)) {
      return true;
    }

    if (person.getFirstSurname() != null && !person.getFirstSurname().matches(NAMES_REGEX)) {
      return true;
    }

    if (person.getSecondSurname() != null && !person.getSecondSurname().matches(NAMES_REGEX)) {
      return true;
    }

    if (person.getBirthday() != null && !person.getBirthday().matches(BIRTHDAY_REGEX)) {
      return true;
    }

    return false;

  }

}
