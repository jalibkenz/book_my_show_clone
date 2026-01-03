package in.kenz.bookmyshow.notification.email.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig {

    private final SpringTemplateEngine templateEngine;

    public ThymeleafConfig(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @PostConstruct
    public void addStringTemplateResolver() {
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        resolver.setOrder(1); // higher priority than file-based resolver

        templateEngine.addTemplateResolver(resolver);
    }
}