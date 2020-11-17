package com.bxoon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsService kiteUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private TokenStore redisTokenStore;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenStore tokenStore;

//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    @Autowired
//    private TokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /**
         * 普通 jwt 模式
         */
//         endpoints.tokenStore(jwtTokenStore)
//                .accessTokenConverter(jwtAccessTokenConverter)
//                .userDetailsService(kiteUserDetailsService)
//                /**
//                 * 支持 password 模式
//                 */
//                .authenticationManager(authenticationManager);

//        /**
//         * jwt 增强模式
//         */
//        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> enhancerList = new ArrayList<>();
//        enhancerList.add(jwtTokenEnhancer);
//        enhancerList.add(jwtAccessTokenConverter);
//        enhancerChain.setTokenEnhancers(enhancerList);
//        endpoints.tokenStore(tokenStore)
//                .userDetailsService(kiteUserDetailsService)
//                /**
//                 * 支持 password 模式
//                 */
//                .authenticationManager(authenticationManager)
//                .tokenEnhancer(enhancerChain)
//                .accessTokenConverter(jwtAccessTokenConverter);

        /**
         * redis token 方式
         */
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .userDetailsService(kiteUserDetailsService);

    }

    /**
     * 指定客户端信息的来源，可以为DataSource,也可以为Memory
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
// 	客户端的信息来源于数据库中，就是前面让新建的那张oauth_client_details表
//  clients.jdbc(dataSource);

// 客户端信息来源于内存中，生产环境一般不会采用这种模式。一般都是用数据库存放
        clients.inMemory()
                .withClient("order-client")
                .secret(passwordEncoder.encode("order-secret-8888"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password", "implicit", "client_credentials")
                .accessTokenValiditySeconds(3600)
                .scopes("all").redirectUris("http://localhost:6102/client-authcode/login")
                .and()
                .withClient("user-client")
                .secret(passwordEncoder.encode("user-secret-8888"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password", "implicit", "client_credentials")
                .accessTokenValiditySeconds(3600)
                .scopes("all").redirectUris("http://localhost:6102/client-authcode/login");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许token通过from表单的形式提交
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("isAuthenticated()");
        security.tokenKeyAccess("isAuthenticated()");
    }
}