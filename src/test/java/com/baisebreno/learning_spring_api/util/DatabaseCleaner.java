package com.baisebreno.learning_spring_api.util;
// Based on: https://brightinventions.pl/blog/clear-database-in-spring-boot-tests/

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility component to clean (truncate) all tables in the configured
 * test database between test runs.
 * <p>
 * It:
 * <ul>
 *   <li>Fetches all table names via {@link DatabaseMetaData}.</li>
 *   <li>Disables foreign key checks, truncates every table, then re-enables them.</li>
 *   <li>Skips the Flyway schema history table (<code>flyway_schema_history</code>).</li>
 *   <li>Guards against accidental use on non-test databases by requiring the
 *       JDBC catalog name to end with <code>"test"</code>.</li>
 * </ul>
 *
 * <h3>Intended use</h3>
 * Call {@link #clearTables()} in a test lifecycle hook (e.g., JUnit 5
 * {@code @BeforeEach}) to reset DB state to empty while keeping schema/migrations intact.
 *
 * <h3>Notes & Assumptions</h3>
 * <ul>
 *   <li>This implementation uses MySQL/MariaDB-specific SQL for foreign key toggling
 *       (<code>SET FOREIGN_KEY_CHECKS</code>) and <code>TRUNCATE TABLE</code>.</li>
 *   <li>It relies on {@link DataSource#getConnection()} and the connection's
 *       catalog name to validate it’s a test database.</li>
 *   <li>Not thread-safe by design; intended for single-threaded test execution.</li>
 * </ul>
 */
@Component
public class DatabaseCleaner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSource dataSource;

    /** The current JDBC connection; set during {@link #clearTables()}. */
    private Connection connection;

    /**
     * Clears (truncates) all non-Flyway tables in the current test database.
     * <p>
     * Steps:
     * <ol>
     *   <li>Open a connection from the {@link DataSource}.</li>
     *   <li>Validate the catalog name ends with <code>"test"</code>.</li>
     *   <li>Collect all table names (excluding <code>flyway_schema_history</code>).</li>
     *   <li>Disable foreign keys, truncate each table, re-enable foreign keys.</li>
     * </ol>
     *
     * @throws RuntimeException if the database is not a test database or a SQL error occurs.
     */
    public void clearTables() {
        try (Connection connection = dataSource.getConnection()) {
            this.connection = connection;

            checkTestDatabase();
            tryToClearTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.connection = null;
        }
    }

    /**
     * Ensures that the current JDBC catalog name ends with "test".
     * This is a safety net to avoid truncating production or dev databases.
     *
     * @throws SQLException if catalog retrieval fails
     * @throws RuntimeException if the catalog is null or does not end with "test"
     */
    private void checkTestDatabase() throws SQLException {
        String catalog = connection.getCatalog();

        if (catalog == null || !catalog.endsWith("test")) {
            throw new RuntimeException(
                    "Cannot clear database tables because '" + catalog + "' is not a test database (suffix 'test' not found).");
        }
    }

    /**
     * Retrieves table names and performs the truncation batch.
     *
     * @throws SQLException if discovery or truncation fails
     */
    private void tryToClearTables() throws SQLException {
        List<String> tableNames = getTableNames();
        clear(tableNames);
    }

    /**
     * Reads all physical TABLE names from the current schema (catalog),
     * excluding Flyway's schema history table.
     *
     * @return list of table names to truncate
     * @throws SQLException if metadata lookup fails
     */
    private List<String> getTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(connection.getCatalog(), null, null, new String[] { "TABLE" });

        while (rs.next()) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }

        // Keep migrations history intact
        tableNames.remove("flyway_schema_history");

        return tableNames;
    }

    /**
     * Executes the generated TRUNCATE statements in a batch with FK checks off.
     *
     * @param tableNames tables to truncate
     * @throws SQLException if execution fails
     */
    private void clear(List<String> tableNames) throws SQLException {
        Statement statement = buildSqlStatement(tableNames);

        logger.debug("Executing SQL");
        statement.executeBatch();
    }

    /**
     * Builds a {@link Statement} batch that:
     * <ol>
     *   <li>Turns off foreign key checks.</li>
     *   <li>TRUNCATEs every table.</li>
     *   <li>Turns foreign key checks back on.</li>
     * </ol>
     *
     * @param tableNames tables to truncate
     * @return prepared statement with a full batch
     * @throws SQLException if statement creation fails
     */
    private Statement buildSqlStatement(List<String> tableNames) throws SQLException {
        Statement statement = connection.createStatement();

        statement.addBatch(sql("SET FOREIGN_KEY_CHECKS = 0"));
        addTruncateSatements(tableNames, statement);
        statement.addBatch(sql("SET FOREIGN_KEY_CHECKS = 1"));

        return statement;
    }

    /**
     * Appends one TRUNCATE statement per table to the provided batch.
     * <p>
     * Uses raw table names from metadata; assumes they are safe/valid identifiers.
     *
     * @param tableNames list of table names
     * @param statement  statement to which batch entries are added
     */
    private void addTruncateSatements(List<String> tableNames, Statement statement) {
        tableNames.forEach(tableName -> {
            try {
                statement.addBatch(sql("TRUNCATE TABLE " + tableName));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Logs and returns the SQL string; used to centralize debug logging.
     *
     * @param sql the SQL to log
     * @return the same SQL
     */
    private String sql(String sql) {
        logger.debug("Adding SQL: {}", sql);
        return sql;
    }

}
