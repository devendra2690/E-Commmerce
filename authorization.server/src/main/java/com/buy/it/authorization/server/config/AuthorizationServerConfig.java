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

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {

        // This creates a configurer that registers all OAuth2 authorization endpoints (like /oauth2/token, /oauth2/authorize, etc.).
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher()) //Ensures that the security rules only apply to OAuth2 endpoints and not other application endpoints.
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


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {



        /*

        This is how you can store User information InMemory, drawback is that it will fetched every time server restart and do not support dynamic user registration validation

        List<com.buy.it.authorization.server.entity.User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                                                                   .toList();

        List<UserDetails> userDetails = new ArrayList<>();
        for (com.buy.it.authorization.server.entity.User user : users) {
            userDetails.add(User.withUsername(user.getUsername())
                    .password(user.getPasswordHash())
                    .roles(String.valueOf(user.getRole()))
                    .build());
        }

        return new InMemoryUserDetailsManager(userDetails);*/

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

       OAuth2AuthorizationService : By default we OAuth2 Support Inmemrory and Jdbc, but we can also create our own implementation
         InMemoryOAuth2AuthorizationService : This is how you can store Authorization information InMemory,
         JdbcOAuth2AuthorizationService : Store auth data in DB. For this we will need table in db. Refer to oAuth-auth-service.sql under sql folder

           OAuth2AuthorizationService is a Spring Security interface that stores and retrieves authorization details for OAuth2 flows.
            It helps manage authorization codes, access tokens, refresh tokens, and authentication details for users and clients in an OAuth2 system.



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
