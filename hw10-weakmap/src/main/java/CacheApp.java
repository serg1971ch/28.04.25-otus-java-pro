import javax.sql.DataSource;

import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.repository.ClientDataTemplateJdbc;
import ru.otus.crm.service.CacheServiceImpl;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CacheApp {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger logger = LoggerFactory.getLogger(CacheApp.class);
    private static final int NUM_CLIENTS = 10000;
    private static final int NUM_TESTS = 1000;

    public static void main(String[] args) {
        // Инициализация DataSource
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();



        ClientDataTemplateJdbc clientTemplate = new ClientDataTemplateJdbc(dbExecutor);
        MyCache<Long, Client> cache = new MyCache<>();
        CacheServiceImpl cacheService = new CacheServiceImpl(cache);
        DbServiceClientImpl dbService = new DbServiceClientImpl(transactionRunner, clientTemplate);

        // Заполнение базы данных тестовыми данными
        populateDatabase(dbService);
//        createdClientDB(dbExecutor, transactionRunner);

        // Тестирование производительности
        testPerformance(cacheService, dbService);

    }

    private static void populateDatabase(DbServiceClientImpl dbService) {
        logger.info("Заполнение базы данных тестовыми данными...");
        Random random = new Random();
        for (int i = 0; i < NUM_CLIENTS; i++) {
            Client client = new Client("Name_" + random.nextInt(100));
            dbService.saveClient(client);
        }
        logger.info("База данных заполнена");
    }

    private static void testPerformance(CacheServiceImpl cacheService, DbServiceClientImpl dbService) {
        logger.info("Начало тестирования производительности...");

        // Заполнение кэша
        List<Client> allClients = dbService.findAll();
        cacheService.saveAll(allClients);

        // Тестирование скорости получения данных из кэша
        testCacheSpeed(cacheService);

        // Тестирование скорости получения данных из БД
        testDbSpeed(dbService);
    }

    private static void testCacheSpeed(CacheServiceImpl cacheService) {
        long startTime = System.nanoTime();
        for (int i = 0; i < NUM_TESTS; i++) {
            long id = (long) (Math.random() * NUM_CLIENTS);
            cacheService.getClient(id);
        }
        long endTime = System.nanoTime();
        logger.info("Среднее время получения из кэша: {} наносекунд",
                (endTime - startTime) / NUM_TESTS);
    }

    private static void testDbSpeed(DbServiceClientImpl dbService) {
        long startTime = System.nanoTime();
        for (int i = 0; i < NUM_TESTS; i++) {
            long id = (long) (Math.random() * NUM_CLIENTS);
            dbService.getClient(id);
        }
        long endTime = System.nanoTime();
        logger.info("Среднее время получения из БД: {} наносекунд",
                (endTime - startTime) / NUM_TESTS);
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("classpath:C:\\Users\\Honor\\Desktop\\28.04.25-otus\\hw10-weakmap\\src\\main\\resources\\db\\migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
