package ac.dnd.server.common.propeties

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["ac.dnd.server.common.propeties"])
class PropertiesSourceConfig