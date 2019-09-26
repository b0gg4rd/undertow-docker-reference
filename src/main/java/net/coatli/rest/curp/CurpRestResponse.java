package net.coatli.rest.curp;

import static com.jsoniter.output.JsonStream.serialize;

public class CurpRestResponse {

  private String codigoMensaje;
  private String mensaje;
  private String nombre;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String sexo;
  private String estadoNacimiento;
  private String fechaNacimiento;

  public CurpRestResponse() {
  }

  public String getCodigoMensaje() {
    return codigoMensaje;
  }

  public CurpRestResponse setCodigoMensaje(final String codigoMensaje) {
    this.codigoMensaje = codigoMensaje;

    return this;
  }

  public String getMensaje() {
    return mensaje;
  }

  public CurpRestResponse setMensaje(final String mensaje) {
    this.mensaje = mensaje;

    return this;
  }

  public String getNombre() {
    return nombre;
  }

  public CurpRestResponse setNombre(final String nombre) {
    this.nombre = nombre;

    return this;
  }

  public String getApellidoPaterno() {
    return apellidoPaterno;
  }

  public CurpRestResponse setApellidoPaterno(final String apellidoPaterno) {
    this.apellidoPaterno = apellidoPaterno;

    return this;
  }

  public String getApellidoMaterno() {
    return apellidoMaterno;
  }

  public CurpRestResponse setApellidoMaterno(final String apellidoMaterno) {
    this.apellidoMaterno = apellidoMaterno;

    return this;
  }

  public String getSexo() {
    return sexo;
  }

  public CurpRestResponse setSexo(final String sexo) {
    this.sexo = sexo;

    return this;
  }

  public String getEstadoNacimiento() {
    return estadoNacimiento;
  }

  public CurpRestResponse setEstadoNacimiento(final String estadoNacimiento) {
    this.estadoNacimiento = estadoNacimiento;

    return this;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public CurpRestResponse setFechaNacimiento(final String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;

    return this;
  }

  @Override
  public String toString() {
    return serialize(this);
  }

}
