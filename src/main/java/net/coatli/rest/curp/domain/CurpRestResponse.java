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
public class CurpRestResponse {

  private String codigoMensaje;
  private String mensaje;
  private String nombre;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String sexo;
  private String estadoNacimiento;
  private String fechaNacimiento;

  @Override
  public String toString() {
    return serialize(this);
  }

}
