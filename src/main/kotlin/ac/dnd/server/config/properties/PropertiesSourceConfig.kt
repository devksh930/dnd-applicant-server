package ac.dnd.server.config.properties

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["ac.dnd.server.config.properties"])
class PropertiesSourceConfig