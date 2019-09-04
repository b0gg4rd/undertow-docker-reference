package net.coatli.persistence.typehandler;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

/**
 *
 */
@MappedJdbcTypes(JdbcType.DATE)
public class LongDateTypeHandler extends BaseTypeHandler<Long> {

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNonNullParameter(final PreparedStatement ps,
                                  final int i,
                                  final Long parameter,
                                  final JdbcType jdbcType) throws SQLException {

    ps.setDate(i, new Date(parameter));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getNullableResult(final ResultSet rs, final String columnName) throws SQLException {

    return rs.getDate(columnName).getTime();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {

    return rs.getDate(columnIndex).getTime();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {

    return cs.getDate(columnIndex).getTime();

  }

}
