package net.coatli.persistence;

import java.util.List;

import net.coatli.domain.Person;

public interface PersonsMapper {

  public List<Person> retrieveAll();

  public int create(Person person);

  public Person retrieveOne(String personId);

  public int update(Person person);

}
