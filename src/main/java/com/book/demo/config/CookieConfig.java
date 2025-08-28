@Configuration
public class CookieConfig {

    @Bean
    public DefaultCookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None");       // allow cross-site cookies
        serializer.setUseSecureCookie(true);  // required for HTTPS
        return serializer;
    }
}
