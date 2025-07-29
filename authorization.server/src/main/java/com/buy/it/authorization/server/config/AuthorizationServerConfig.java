package com.buy.it.authorization.server.config;

import com.buy.it.authorization.server.entity.OAuthKey;
import com.buy.it.authorization.server.repository.OAuth2ClientRepository;
import com.buy.it.authorization.server.repository.OAuthKeyRepository;
import com.buy.it.authorization.server.repository.UserRepository;
import com.buy.it.authorization.server.service.DatabaseRegisteredClientRepository;
import com.buy.it.authorization.server.util.KeyUtil;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class AuthorizationServerConfig {

    private final OAuthKeyRepository oAuthKeyRepository;

    /**
     * This is the main configuration class for the OAuth2 Authorization Server.
     * It sets up the security filter chains, user details service, and token customizers.
     * It also configures the JWK source for JWT decoding.
     *
     * This configuration uses Spring Security's OAuth2 Authorization Server support to create an authorization server
     * that can handle OAuth2 flows such as authorization code, client credentials, and refresh tokens.
     *
     * It includes endpoints for user authentication, client registration, and token issuance.
     * It also provides a way to customize the JWT tokens issued by the server.
     *
     * The configuration is divided into two main security filter chains:
     * 1. `authorizationServerSecurityFilterChain`: This chain handles the OAuth2 authorization endpoints and requires authentication for all requests.
     * 2. `defaultSecurityFilterChain`: This chain secures the application's other endpoints, allowing public access to registration endpoints while requiring authentication for all other requests.
     * * The `userDetailsService` bean retrieves user details from the database, allowing for dynamic user management.
     * * The `authorizationService` bean is responsible for managing OAuth2 authorizations, either in memory or using a JDBC implementation.
     * * The `registeredClientRepository` bean manages registered clients, allowing for dynamic client registration and management.
     * * The `tokenCustomizer` bean customizes the JWT tokens issued by the server, adding roles and client ID claims.
     * * The `jwkSource` bean provides the RSA keys used for signing and verifying JWT tokens, either from an in-memory source or a database.
     * * The `jwtDecoder` bean is configured to decode JWT tokens using the public key from the JWK source.
     * This configuration also includes settings for the authorization server, such as the issuer URL and token customization.
     * It uses BCrypt for password encoding and provides a way to generate RSA keys for signing JWT tokens.
     * This configuration is essential for setting up a secure OAuth2 authorization server that can handle user authentication, client registration, and token issuance.
     * * @see <a href="https://docs.spring.io/spring-security/reference/servlet/oauth2/authorization-server/index.html">Spring Security OAuth2 Authorization Server Documentation</a>
     */

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {

        /**
         *
         *  This creates a configure that registers all OAuth2 authorization endpoints (like /oauth2/token, /oauth2/authorize, etc.).
         */
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();


        // The line http.securityMatcher(...) is crucial—it ensures that this entire set of security rules only applies to requests for the standard OAuth/OIDC endpoints, leaving other parts of your application unaffected
        // This is important because it allows you to have a dedicated security configuration for your OAuth2 endpoints without interfering with the rest of your application.

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher()) // Ensures that the security rules only apply to OAuth2 endpoints and not other application endpoints.
                .with(authorizationServerConfigurer, (authorizationServer) -> //Registers OAuth2 authorization endpoints.
                        authorizationServer
                                .oidc(Customizer.withDefaults()) //Enables OpenID Connect 1.0 (adds /userinfo, /jwks.json, etc.).
                )
                .authorizeHttpRequests((authorize) ->  //Requires authentication for all incoming requests to the Auth Server.
                        authorize
                                .anyRequest().authenticated()
                )
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),  // If a user is not authenticated, redirect them to /login instead of returning an HTTP 401 error.
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML) // This applies only for web browsers (MediaType.TEXT_HTML), API clients (Postman, curl) will still receive a 401 Unauthorized response.
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (for now)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register","/api/auth/register-client").permitAll() // Allow registration without authentication
                        .anyRequest().authenticated() // Secure other endpoints
                )
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());;

        return http.build();
    }

    /**
     *
     * This code defines a UserDetailsService bean, a core component in Spring Security responsible for loading a user's data during the login process.
     * It shows two different strategies for doing this: one (commented out) that loads all users into memory, and another (active) that fetches users from a database on demand.
     *
     *
     *
     * ## 1. The Database-Driven Approach (Active Code)
     * This is the standard and recommended approach for most applications.
     *
     * How it Works: It provides a lambda expression username -> { ... } that gets executed every time a user tries to log in.
     *
     * Database Query: Inside the lambda, userRepository.findByUsername(username) queries the database for a single user matching the provided username.
     *
     * Result: If the user is found, their details (username, password hash, and roles) are wrapped in a UserDetails object and returned to Spring Security for password validation.
     * If not found, it throws a UsernameNotFoundException.
     *
     * This method is efficient and scalable because it only loads the data for the specific user who is authenticating, and it always gets the most up-to-date information from the database.
     *
     *
     *
     *
     *
     * ## 2. The In-Memory Approach (Commented-Out Code)
     * This approach is simpler but has significant drawbacks for most real-world applications.
     *
     * How it Works: It loads all users from the database into a list in memory when the application first starts.
     *
     * In-Memory Lookups: The InMemoryUserDetailsManager then handles login requests by looking for the user in this pre-loaded list.
     *
     * Drawbacks: As the comment notes, this is not dynamic. If a new user registers or a user's role changes, the application won't know about it until it's restarted.
     * It can also consume a lot of memory if you have a large number of users and is not suitable for applications where users can register or change roles frequently.
     */


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {



        /*

        List<com.buy.it.authorization.server.entity.User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                                                                   .toList();

        List<UserDetails> userDetails = new ArrayList<>();
        for (com.buy.it.authorization.server.entity.User user : users) {
            userDetails.add(User.withUsername(user.getUsername())
                    .password(user.getPasswordHash())
                    .roles(String.valueOf(user.getRole()))
                    .build());
        }

        return new InMemoryUserDetailsManager(userDetails);

        */

        return username -> {
            // Fetch the user only if they belong to the given client_id
            com.buy.it.authorization.server.entity.User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return User.withUsername(user.getUsername())
                    .password(user.getPasswordHash())
                    .roles(String.valueOf(user.getRole()))
                    .build();
        };
    }

    /*

       OAuth2AuthorizationService : By default we OAuth2 Support In-memory and Jdbc, but we can also create our own implementation
         InMemoryOAuth2AuthorizationService : This is how you can store Authorization information InMemory,
         JdbcOAuth2AuthorizationService : Store auth data in DB. For this we will need table in db. Refer to oAuth-auth-service.sql under sql folder

       OAuth2AuthorizationService is a Spring Security interface that stores and retrieves authorization details for OAuth2 flows.
            It helps manage authorization codes, access tokens, refresh tokens, and authentication details for users and clients in an OAuth2 system.

         The JdbcOAuth2AuthorizationService is a concrete implementation of OAuth2AuthorizationService that uses a JDBC database to store authorization data.
            It allows for persistent storage of OAuth2 authorizations, making it suitable for production environments where you need to retain authorization data across server restarts.
        * This bean is responsible for managing OAuth2 authorizations, such as storing and retrieving authorization codes, access tokens, and refresh tokens.
        * It is typically used in conjunction with a database to persist authorization data.

        When you use JdbcOAuth2AuthorizationService, you need to ensure that the necessary database tables are created and table structure is set up according to the Spring Security OAuth2 specifications.
        Table name and structure can be found in the `oAuth-auth-service.sql` file under the SQL folder of the project. It must be same as the one used by Spring Security OAuth2.

        That is how when this method receive jdcTemplate and RegisteredClientRepository, it will create a JdbcOAuth2AuthorizationService instance that uses the provided JdbcTemplate for database operations
        and the RegisteredClientRepository for client management.

     */

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, clientRepository);
    }

    /*@Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }*/



    @Bean
    @Primary
    public RegisteredClientRepository registeredClientRepository(OAuth2ClientRepository oAuth2ClientRepository) {
        return new DatabaseRegisteredClientRepository(oAuth2ClientRepository);
    }

   /* @Bean


     This is how you can store Client information InMemory, drawback is that it will fetched every time server restart and do not support dynamic Client registration validation

    public RegisteredClientRepository registeredClientRepository(OAuth2ClientRepository oAuth2ClientRepository) {

        List<OAuth2Client> clients = StreamSupport.stream(oAuth2ClientRepository.findAll().spliterator(), false)
                                             .toList();

        List<RegisteredClient> registeredClients = new ArrayList<>();
        for (OAuth2Client client : clients) {
            RegisteredClient registeredClient = RegisteredClient.withId(client.getId().toString())
                    .clientId(client.getClientId())
                    .clientName(client.getClientId())
                    .clientSecret(client.getClientSecret())
                    .clientSecretExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri(client.getRedirectUris().stream().findFirst().get())
                    .postLogoutRedirectUri("http://127.0.0.1:8082/")
                    .scope(OidcScopes.OPENID)
                    .scope(OidcScopes.PROFILE)
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                    .build();
            registeredClients.add(registeredClient);
        }

        return new InMemoryRegisteredClientRepository(registeredClients);
    }*/

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                addRolesClaim(context);
            }
            if (context.get(RegisteredClient.class) != null) {
                addClientIdClaim(context);
            }
        };
    }

    private void addRolesClaim(JwtEncodingContext context) {
        Authentication authentication = context.getPrincipal();
        var authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        context.getClaims().claim("roles", authorities);
    }

    private void addClientIdClaim(JwtEncodingContext context) {
        context.getClaims().claim("client_id",
                Objects.requireNonNull(context.get(RegisteredClient.class)).getId());
    }


   /*

       Way 1 of creating Private and public keys


   @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }*/

    /*

    Way 2 of creating Private and public keys

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID("static-kid-value") // Keep a stable key ID
                .algorithm(JWSAlgorithm.RS256)
                .build();
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));

        private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    }*/

    /**
     *
     * ## Different Ways To Provide Keys
     * The method shown—loading keys from a database—is a good, persistent approach. However, there are several other common ways to manage your signing keys, each with its own trade-offs.
     *
     * ### 1. Generate Keys In-Memory
     * You can generate a new key pair every time the application starts. This is the simplest method and is often used for demos or development.
     * How: Use Java's KeyPairGenerator to create a new RSAKey on startup.
     * Drawback: The keys change on every restart. This means previously issued tokens can't be validated after a reboot, and Resource Servers must constantly refetch the new public key.
     *
     * ### 2. Load Keys from a File
     * You can store your keys in files (e.g., key.pem) and load them from the classpath or filesystem. This is more persistent than the in-memory approach.
     * How: Place key files in your project's resources or on the server's filesystem and use an InputStream to read them.
     * Drawback: Requires secure file management. The key files must be protected and deployed securely with your application.
     *
     * ### 3. Use a Secrets Manager (e.g., HashiCorp Vault)
     * This is the most secure and production-ready approach. Keys are managed by a dedicated secrets management tool.
     * How: Your application authenticates with a service like HashiCorp Vault or AWS KMS at startup to fetch the signing keys.
     * Drawback: Adds an external dependency and more configuration complexity to your application.
     *
     * @return
     * @throws Exception
     */

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        OAuthKey oauthKey = oAuthKeyRepository.findLatestKey();
        if (oauthKey == null) {
            throw new IllegalStateException("No RSA keys found in the database!");
        }

        RSAPrivateKey privateKey = KeyUtil.convertToPrivateKey(oauthKey.getPrivateKey());
        RSAPublicKey publicKey = KeyUtil.convertToPublicKey(oauthKey.getPublicKey());

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(oauthKey.getKeyId())
                .algorithm(JWSAlgorithm.RS256)
                .build();

        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    /*
     *  The JWKSource<SecurityContext> is already configured to fetch the RSA key from the database.
     *  OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource) initializes a JWT decoder using the public key from JWK.
     * This JwtDecoder verifies and decodes incoming JWTs when clients send access tokens to protected resources.
     *
     * In Case this auth server, it will help to project resources other than OAuth endpoint
     *
     * @param jwkSource
     * @return
     */

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /*
       Enabling default endpoint of OAuth2.
       Generally we do not customize ti unless auth serve is behind proxy or Load balancer

       public AuthorizationServerSettings authorizationServerSettings() {
            return AuthorizationServerSettings.builder()
                .issuer("https://auth.example.com")  // External-facing URL
                .build();
       }

     */

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


   /*

     Use this encoder in production because though Bcrypt is a powerful it expect plaintext when matching
     and we can not send client secret in plain text format

   private static final String SECRET_KEY = "some-random-secret"; // Server-side secret, should be in plain text

   @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder(SECRET_KEY, 10000, 256);
    }

        SECRET_KEY → A private key used for hashing (server-side).
        10000 iterations → Higher means stronger security.
        256-bit hash → Secure enough for password hashing.

    */


}
