package com.kp.rabbitmq.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kp.org.parrs.model.AppointmentResource;
import com.kp.org.parrs.model.ResourceType;

public class AppointmentResourceFields {
	private static final Logger log = LoggerFactory.getLogger(AppointmentResourceFields.class);

	private static Map<String, FieldDetail> map = new HashMap<String, FieldDetail>();
	static{
		map.put("resourceTypes", new FieldDetail("RESOURCE_TYPE_CODE", "ResourceType"));
		map.put("subDepartmentCode", new FieldDetail("SUB_DEPT_CODE", "String"));
			
	}
	public static StringBuilder getAppointmentResourceFields(AppointmentResource ars) throws Exception{
		StringBuilder buf = new StringBuilder();
		Field [] fields=AppointmentResource.class.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			FieldDetail filedDetail=map.get(field.getName());
			if(filedDetail != null && field.get(ars)!= null){
				buf.append(filedDetail.sqlField);
				if(filedDetail.getDataType().equals("String") ){
					buf.append("=\"");
					buf.append((String) field.get(ars));
					buf.append('\"');
				}else if(filedDetail.getDataType().equals("ResourceType")){
					buf.append("=\"");
					@SuppressWarnings("unchecked")
					List<ResourceType> resourceTypes= (List<ResourceType>)field.get(ars);
					buf.append(resourceTypes.get(0).getType());	//not checking for size s its mandatory  field and should throw exception if its null.
					buf.append('\"');
				}
				buf.append(',');
			}
		}
		log.debug("getAppointmentResourceFields="+buf);
		return buf;
	}
}

