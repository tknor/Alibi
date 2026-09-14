package cz.tomas.alibi;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Starts a fresh PostgreSQL container through Testcontainers.
 * Spring Boot connects to the container using its dynamically allocated port.
 * Liquibase applies the schema.
 *
 * Docker must be running for integration tests.
 */
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
abstract class AbstractIntegrationTest {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.10-alpine");
}
