package com.br.mom.ms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlConnection {

    private Connection conn;
    private static final Logger logger = LoggerFactory.getLogger(MySqlConnection.class);

    public MySqlConnection(String url, String user, String passwd) throws SQLException, ClassNotFoundException {
        String driver = "com.mysql.jdbc.Driver";
        Class.forName(driver);
        this.conn = DriverManager.getConnection(url, user, passwd);
    }

    public void destory() throws SQLException {
        conn.close();
    }

    public int queryCount(String sql, Object[] values) {
        if (sql == null) {
            throw new IllegalArgumentException("sql语句不能为空！！");
        }
        Connection con = conn;
        if (con == null) {
            return 0;
        }
        try {
            return query_transaction_count(con, sql, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return 0;
        } finally {
            close(con, false);
        }
    }

    public List<Map<String, Object>> query(String sql, Object[] values) {

        if (sql == null) {
            throw new IllegalArgumentException("sql语句不能为空！！");
        }
        Connection con = conn;
        if (con == null) {
            return null;
        }
        try {
            return query_transaction(con, sql, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            close(con, false);
        }
    }

    private int query_transaction_count(Connection con, String sql, Object[] values)
            throws SQLException {

        PreparedStatement stat = null;
        try {
            stat = con.prepareStatement(sql);
            if (stat == null) {
                return 0;
            }
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    stat.setObject(i, values[i - 1]);
                }
            }
            ResultSet set = stat.executeQuery();
            if (set == null) {
                return 0;
            }
            if (set.next()) {
                return set.getInt(1);
            }
            return 0;
        } finally {
            colse(stat);
        }
    }

    private List<Map<String, Object>> query_transaction(Connection con, String sql, Object[] values)
            throws SQLException {

        PreparedStatement stat = null;
        try {
            stat = con.prepareStatement(sql);
            if (stat == null) {
                return null;
            }
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    stat.setObject(i, values[i - 1]);
                }
            }
            ResultSet set = stat.executeQuery();
            if (set == null) {
                return null;
            }
            String[] names = getColNames(set);
            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
            while (set.next()) {
                Map<String, Object> row = new HashMap<String, Object>(names.length);
                for (int i = 0; i < names.length; i++) {
                    row.put(names[i], set.getObject(names[i]));
                }
                rows.add(row);
            }
            return rows;
        } finally {
            colse(stat);
        }
    }

    private String[] getColNames(ResultSet set) throws SQLException {
        ResultSetMetaData metaData = set.getMetaData();
        int fieldCt = metaData.getColumnCount();
        String[] names = new String[fieldCt];
        for (int i = 0; i < fieldCt; i++) {
            names[i] = metaData.getColumnName(i + 1);
        }
        return names;
    }

    public int[] saveOrUpdateBatch(String sql, List<Object[]> values) {

        if (sql == null || values == null || values.isEmpty()) {
            throw new IllegalArgumentException(
                    "method : saveOrUpdateBatch ERROR. please use saveOrUpdate");
        }
        Connection con = conn;
        if (con == null) {
            return new int[0];
        }
        try {
            return execute_transaction(con, sql, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return new int[0];
        } finally {
            close(con, true);
        }
    }

    private int[] execute_transaction(Connection con, String sql, List<Object[]> values)
            throws SQLException {

        PreparedStatement stat = null;
        try {
            stat = con.prepareStatement(sql);
            if (stat == null) {
                return new int[0];
            }
            for (Object[] arr : values) {
                if (arr == null || arr.length == 0) {
                    continue;
                }
                for (int j = 1; j <= arr.length; j++) {
                    stat.setObject(j, arr[j - 1]);
                }
                stat.addBatch();
            }
            int[] result = stat.executeBatch();
            con.commit();
            return result;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            colse(stat);
        }
    }

    public int saveOrUpdate(String sql, Object[] values) {

        if (sql == null) {
            throw new IllegalArgumentException("sql语句不能为空");
        }
        Connection con = conn;
        if (con == null) {
            return 0;
        }
        try {
            return execute_transaction(con, sql, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return 0;
        } finally {
            close(con, true);
        }
    }

    private int execute_transaction(Connection con, String sql, Object[] values) throws SQLException {

        PreparedStatement stat = null;
        try {
            stat = con.prepareStatement(sql);
            if (stat == null) {
                return 0;
            }
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    stat.setObject(i, values[i - 1]);
                }
            }
            int result = stat.executeUpdate();
            if (result > 0) {
                con.commit();
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            con.rollback();
            throw e;
        } finally {
            colse(stat);
        }
    }

    private void colse(PreparedStatement stat) {
        if (stat == null) {
            return;
        }
        try {
            stat.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    private void close(Connection con, boolean atom) {
        if (con == null) {
            return;
        }
        try {
            if (atom) {
                con.setAutoCommit(true);
            }
            con.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.conn;
    }
}
