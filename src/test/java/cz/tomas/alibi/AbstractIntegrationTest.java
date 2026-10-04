package cz.tomas.alibi;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Starts one PostgreSQL container for the integration-test JVM.
 * Spring Boot connects to the container using its dynamically allocated port.
 * Liquibase applies the schema.
 *
 * Docker must be running for integration tests.
 */
@ActiveProfiles("test")
@SpringBootTest
public abstract class AbstractIntegrationTest {

	@MockitoBean
    JwtDecoder jwtDecoder;

	@ServiceConnection
	static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.10-alpine");

	static {
		postgres.start();
	}
}
