package net.coatli.domain;

import static com.jsoniter.output.JsonStream.serialize;

import java.util.Date;

public class Person {

  private String  id;
  private String  curp;
  private String names;
  private String firstSurname;
  private String secondSurname;
  private String gender;
  private Date   birthday;

  public Person() {
  }

  public String getId() {
    return id;
  }

  public Person setId(final String id) {
    this.id = id;

    return this;
  }

  public String getCurp() {
    return curp;
  }

  public Person setCurp(final String curp) {
    this.curp = curp;

    return this;
  }

  public String getNames() {
    return names;
  }

  public Person setNames(final String names) {
    this.names = names;

    return this;
  }

  public String getFirstSurname() {
    return firstSurname;
  }

  public Person setFirstSurname(final String firstSurname) {
    this.firstSurname = firstSurname;

    return this;
  }

  public String getSecondSurname() {
    return secondSurname;
  }

  public Person setSecondSurname(final String secondSurname) {
    this.secondSurname = secondSurname;

    return this;
  }

  public String getGender() {
    return gender;
  }

  public Person setGender(final String gender) {
    this.gender = gender;

    return this;
  }

  public Date getBirthday() {
    return birthday;
  }

  public Person setBirthday(final Date birthday) {
    this.birthday = birthday;

    return this;
  }

  @Override
  public String toString() {
    return serialize(this);
  }

}
