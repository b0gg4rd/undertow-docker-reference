package net.coatli.rest.curp.domain;

import static com.jsoniter.output.JsonStream.serialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain  = true,
           fluent = false)
public class CurpRestRequest {

  private String curp;
  private String documento;

  @Override
  public String toString() {
    return serialize(this);
  }

}
