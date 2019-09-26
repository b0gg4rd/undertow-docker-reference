package net.coatli.persistence.type;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class BirthdayTypeHandler extends BaseTypeHandler<String>{

  private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

  @Override
  public void setNonNullParameter(final PreparedStatement ps,
                                  final int i,
                                  final String parameter,
                                  final JdbcType jdbcType) throws SQLException {

    try {
      ps.setDate(i, parameter == null || parameter.isBlank() ? null : new Date(FORMATTER.parse(parameter).getTime()));
    } catch (final ParseException exc) {
      throw new RuntimeException(exc);
    }
  }

  @Override
  public String getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
    return null;
  }

  @Override
  public String getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return null;
  }

  @Override
  public String getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
    return null;
  }

}
