package ac.dnd.server.global.config.properties

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["ac.dnd.server.global.config.properties"])
class PropertiesSourceConfig