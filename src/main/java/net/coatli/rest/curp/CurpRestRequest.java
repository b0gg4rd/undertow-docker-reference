package net.coatli.rest.curp;

import com.jsoniter.output.JsonStream;

public class CurpRestRequest {

  private String curp;
  private String documento;

  public CurpRestRequest() {
  }

  public String getCurp() {
    return curp;
  }

  public CurpRestRequest setCurp(final String curp) {
    this.curp = curp;

    return this;
  }

  public String getDocumento() {
    return documento;
  }

  public CurpRestRequest setDocumento(final String documento) {
    this.documento = documento;

    return this;
  }

  @Override
  public String toString() {
    return JsonStream.serialize(this);
  }

}
