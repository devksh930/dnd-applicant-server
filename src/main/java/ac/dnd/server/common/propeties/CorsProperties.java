package ac.dnd.server.common.propeties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "cors")
@Getter
public class CorsProperties {
	private List<String> allowedOriginsPatterns = new ArrayList<>();
	private List<String> allowedHeaders = new ArrayList<>();
	private List<String> exposedHeaders = new ArrayList<>();

	public void setAllowedOriginsPatterns(final List<String> allowedOriginsPatterns) {
		this.allowedOriginsPatterns = allowedOriginsPatterns;
	}

	public void setAllowedHeaders(final List<String> allowedHeaders) {
		this.allowedHeaders = allowedHeaders;
	}

	public void setExposedHeaders(final List<String> exposedHeaders) {
		this.exposedHeaders = exposedHeaders;
	}
}
