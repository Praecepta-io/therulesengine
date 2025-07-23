package io.praecepta.data.processor.impl;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.data.processor.PraeceptaAbstractDataProcessor;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.ConnectionCallback;

public class PraeceptaDBDataProcessor<OBJ_RULE_STORE extends PraeceptaRequestStore> extends PraeceptaAbstractDataProcessor<OBJ_RULE_STORE,PraeceptaDBInjestorConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(PraeceptaDBDataProcessor.class);
    private  JdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    private String insertQuery;

    public void initializeProcessor(PraeceptaDBInjestorConfig dbConfig) {
        initializeDBConnection(dbConfig);
    }

    private DataSource initializeDBConnection(PraeceptaDBInjestorConfig dbConfig) {

        Map<String, String> dbConfigPropertis = dbConfig.getDataConfigurationWithValues();

        if (!PraeceptaObjectHelper.isObjectEmpty(dbConfigPropertis)) {

            try {
                dataSource = initializeDataSource(dbConfigPropertis);

                insertQuery = dbConfigPropertis.get(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName());

            } catch (Exception e) {
                LOG.error("error while initializing DataSource", e);
                throw new RuntimeException(e);
            }
        } else {
            LOG.info("DB data processor config properties found empty/null");
        }
        return dataSource;
    }

    protected DataSource initializeDataSource(Map<String, String> dbConfigPropertis) throws Exception {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_URL.getElementName()));
        config.setUsername(dbConfigPropertis.get(PraeceptaDBDataConfigType.USERNAME.getElementName()));
        config.setPassword(dbConfigPropertis.get(PraeceptaDBDataConfigType.PASSWORD.getElementName()));
        config.setDriverClassName(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_DRIVER.getElementName()));

        // Optional tuning
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setPoolName("OracleHikariCP");

        DataSource dataSource = new HikariDataSource(config);

        if (!PraeceptaObjectHelper.isObjectNull(dataSource)) {
            jdbcTemplate = initializeJdbcTemplate(dataSource);
        }
        return dataSource;
    }

    public JdbcTemplate initializeJdbcTemplate(DataSource dataSource) {
        LOG.info(" initializing the Jdbc Template");
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Override
    public void processData(Collection<OBJ_RULE_STORE> dataToProcess) {
        jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
            String query = insertQuery;
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                connection.setAutoCommit(false);
                for (OBJ_RULE_STORE ruleStore : dataToProcess) {
                    Map<String, Object> ruleResponseMap = (Map<String, Object>) ((PraeceptaRequestStore) ruleStore).fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);
                    pstmt.setString(1,(String)ruleResponseMap.get("CIFID"));
                    pstmt.setString(2,(String)ruleResponseMap.get("ACID"));
                    pstmt.setString(3,(String)ruleResponseMap.get("FIRST_NAME"));
                    pstmt.setString(4,(String)ruleResponseMap.get("EMAIL"));
                    pstmt.setString(5,(String)ruleResponseMap.get("MMF"));
                    pstmt.setString(6,(String)ruleResponseMap.get("RSA"));
                    pstmt.addBatch();
                }
                int[] resultCounts = pstmt.executeBatch();
                connection.commit();
                LOG.info("Inserted %d record(s).\n", resultCounts.length);
            } catch (SQLException e) {
                LOG.error("Batch insert failed: " , e);
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    LOG.error("Rollback failed- ", rollbackEx);
                }
            }
            return null;
        });
    }
}
