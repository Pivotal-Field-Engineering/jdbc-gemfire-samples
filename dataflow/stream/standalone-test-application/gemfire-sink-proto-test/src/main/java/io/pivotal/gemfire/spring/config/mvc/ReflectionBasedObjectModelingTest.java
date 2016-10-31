package io.pivotal.gemfire.spring.config.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.json.JSONObject;

@RestController
public class ReflectionBasedObjectModelingTest {
	private static final Logger LOG = LoggerFactory.getLogger(ReflectionBasedObjectModelingTest.class);

	@Autowired
	ClientCache cache;
	
	@Autowired
	DozerBeanMapper dmb;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/test")
	public @ResponseBody void runTest() throws Exception {
		BigInteger bigInt = BigInteger.valueOf(11111);
		Timestamp t = Timestamp.valueOf("2007-01-03 06:25:31.23");
		String jsonString = "{\"regionCode\":\"NCAL\", \"uid\":\"null\", \"mrn\":\"784554564\", "
				+ "\"firstName\":\"ALEXA\", \"lastName\":\"PARKER\", \"middleName\":\"E\", "
				+ "\"displayName\":\"PARKER,ALEXA\", \"gender\":\"F\"," + "\"deathFlag\":\"null\", "
				+ "\"emailAddress\":\"prompttest102@yahoo.com\", \"zipCode\":\"11195    "
				+ " \", \"invalidMrnCode\":\"null\", \"mrnReplacedBy\":\"null\", " + "  "
				+ " \"updateFlag\":\"null\", \"suffix\":\"null\", "
				+ "\"blindFlag\":\"null\", \"deafFlag\":\"null\", \"muteFlag\":\"null\", \"kpmcpEmpFlag\":\"null\", "
				+ " \"mrnPrefix\":\"26\"}";
		JSONObject json = new JSONObject(jsonString);

		LOG.info("JSON={}", json);
		ObjectMapper mapper = new ObjectMapper();
		HashMap payloadAsMap = mapper.readValue(json.toString(), HashMap.class);
		payloadAsMap.put("guid", bigInt);
		payloadAsMap.put("birthDate", t);
		payloadAsMap.put("deathDate", null);
		payloadAsMap.put("intrptrReqCode", null);
		payloadAsMap.put("lastRevDatetime", t);
		payloadAsMap.put("lastVerifyDate", t);
		payloadAsMap.put("updateDatetime", t);

		
		LOG.info("Testing...");
		LOG.info("RegionName : {}", "Author");
		try {
			Set keyset = payloadAsMap.keySet();
			LOG.info("Fields from Map = {}", keyset.toString());

			Class K = Class.forName("io.pivotal.gemfire.common.key.AuthorKey");
			Object k = K.newInstance();
			LOG.debug("KeyClass : {}", K.getName());

			Class V = Class.forName("io.pivotal.kaiser.gemfire.common.model.Author");
			Object v = V.newInstance();
			LOG.debug("ValueClass : {}", V.getName());

			Mapper mp = dmb;
			LOG.info("Starting Dozer Mapping of Class [{}]", K.getName());
			k = mp.map(payloadAsMap, K);
			LOG.info("Starting Dozer Mapping of Class [{}]", V.getName());
			v = mp.map(payloadAsMap, V);
			
			Region region = cache.getRegion("Author");
			LOG.info("Region to Load : {}", region.getFullPath().toString());
			LOG.info("Data to store : [{},{}] : KeyClass={}   ValueClass={}", k.toString(), v.toString(), k.getClass(),
					v.getClass());

			region.put(k, v);

		} catch (Exception e) {
			LOG.error("Exception in {} : Exception {} : Caused by: {}", "GemfireSinkProtoConfiguration.class",
					e.getMessage(), e.getCause());

		}

	}

	public static List<Field> getPrivateFields(Class<?> theClass) {
		List<Field> privateFields = new ArrayList<Field>();

		Field[] fields = theClass.getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();
			LOG.debug("Got private field {}", fieldName);
			if (Modifier.isPrivate(field.getModifiers())) {
				privateFields.add(field);
			}
		}
		return privateFields;
	}
}
