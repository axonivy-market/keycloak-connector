package com.axonivy.connector.keycloak.feature.serialize;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@SuppressWarnings("deprecation")
public class OpenApiJsonFeature extends JacksonJsonProvider {
	@Override
	public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
		ObjectMapper mapper = super.locateMapper(type, mediaType);
		// match our generated jax-rs client beans: that contain JSR310 data types
		mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		// allow fields starting with an upper case character (e.g. in ODATA specs)!
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		// not sending this optional value seems to be lass prone to errors for some
		// remote services.
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
}
