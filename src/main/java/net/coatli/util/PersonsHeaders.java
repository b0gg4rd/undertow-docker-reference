package net.coatli.util;

import io.undertow.util.HttpString;

public final class PersonsHeaders {

  public static final String TEXT_PLAIN      = "text/plain";
  public static final String TEXT_PLAIN_UTF8 = "text/plain; charset=utf-8";

  public static final String APPLICATION_JSON      = "application/json";
  public static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

  public static final String TRACE_HEADER = "X-Trace-Id";

  public static final String     X_PERSON_ID            = "X-Person-Id";
  public static final HttpString X_PERSON_ID_HTTPSTRING = new HttpString(X_PERSON_ID);

}
