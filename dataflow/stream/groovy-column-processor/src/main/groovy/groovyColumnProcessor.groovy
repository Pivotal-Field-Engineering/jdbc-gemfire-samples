package main.groovy

//import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import java.sql.Timestamp

/**
 * Created by wlund on 9/12/16.
 */

static String toCamelCase( String text, boolean capitalized = false ) {
    text = text.replaceAll( "(_)([A-Za-z0-9])", { Object[] it -> it[2].toUpperCase() } )
    return capitalized ? capitalize(text) : text
}

// birth_date: new Date("1977-08-30 00:00:00.0").toTimestamp().format('yyyy-MM-dd HH:mm:ss:S'),
// last_rev_datetime: new Date("2014-09-13 00:00:00.0").toTimestamp().format('yyyy-MM-dd HH:mm:ss:S'), last_verify_date: new Date("2014-09-13 10:45:35.35").toTimestamp().format('yyyy-MM-dd HH:mm:ss:S'), update_datetime: new Date("2015-09-03 15:59:24.83").toTimestamp().format('yyyy-MM-dd HH:mm:ss:S'),

// def payload = '{"member_id":"4", "region_code":"NCAL", "guid":"null", "uid":"null", "mrn":"78472174", "first_name":"ALEXA", "last_name":"PARKER", "middle_name":"E", "display_name":"PARKER,ALEXA", "gender":"F", "birth_date":"1977-08-30 00:00:00.0", "death_flag":"null", "death_date":"null", "email_address":"prompttest102@yahoo.com", "zip_code":"11195     ", "invalid_mrn_code":"null", "mrn_replaced_by":"null", "last_rev_datetime":"2014-09-13 00:00:00.0", "last_verify_date":"2014-09-13 10:45:35.35", "update_datetime":"2015-09-03 15:59:24.83", "update_flag":"null", "suffix":"null", "blind_flag":"null", "deaf_flag":"null", "mute_flag":"null", "kpmcp_emp_flag":"null", "intrptr_req_code":"null", "mrn_prefix":"11"}'

// def payload = [member_id : 4, region_code: 'NCAL', uid: null, mrn: '78472174', first_name: 'ALEXA', last_name: 'PARKER', middle_name: 'E', display_name: 'PARKER, ALEXA', gender: 'F',  death_flag: null, death_date: null, email_address: 'prompttest102@yahoo.com', zip_code: '11195', invalid_mrn_code: null, mrn_replaced_by: null, update_flag: null, suffix: null, blind_flag: null, deaf_flag: null, mute_flag: null, kpmcp_emp_flag: null, intrptr_req_code: null, mrn_prefix: '11']
//def payload = [LMA_CODE: 'XYZ', 'NUID_PIC_1': 'a pic 1', ADDRESS_ID:1, STREET_NAME1:'4501 SAND CREEK ROAD',  CITY:'ANTIOCH                       ', STATE:'CA', ZIP_CODE:'94531     ', ZIP_CODE_SUFFIX:'8577',  COUNTRY:null, ADDITIONAL_INFO:null, LOCATION:null]
/*
String streetName2 = null

BigInteger bigInteger =  BigInteger.valueOf(1)
BigDecimal latitude = BigDecimal.valueOf(37.996725)
BigDecimal longitude = BigDecimal.valueOf(-121.807911)

payload.put("STREET_NAME2", streetName2)
payload.put("ADDRESS_ID", bigInteger)
payload.put("LATITUDE", latitude)
payload.put("LONGITUDE", longitude)

BigInteger guid = null
payload.put('guid', guid)

Timestamp t = Timestamp.valueOf("2007-01-03 06:25:31");

payload.put("birth_date", t);
payload.put("death_date", "null");
payload.put("last_rev_datetime", t);
payload.put("last_verify_date", t);
payload.put("update_datetime", t);


// def s = new JsonBuilder(payload).toString()
println payload.getClass()
println payload

def jsonstring = payload.toString()
*/

//println jsonstring
// def columns = new JsonSlurper().parseText(jsonstring)
def columns = payload
println columns

def javaBeanColumnNames = [:]
def dict = ['LMA', 'NUID']

for(Object m : columns ) {
    def column = m.toString();
    // println "column: $column"
    def columnName = ""
    def javaBeanColumnName
    List javaBeanColumnNameParts = []

    String[] keyAndValue = column.split("=")
    def value = payload.get(keyAndValue[0])

    if (keyAndValue[0] == keyAndValue[0].toLowerCase()) {
        def camelCaseColumn = toCamelCase(keyAndValue[0])
        javaBeanColumnNames.put(camelCaseColumn, value);

    } else {
        // take care of special case where first word is in the dictionary
        // assumed if we passed the toLowerCase() test above.

        String[] ruleDrivenColumnNames = keyAndValue[0].split('_');
        //  println "ruleDrivenColumnNames: $ruleDrivenColumnNames"
        def columnNamePart = ""
        for (int i = 0; i < ruleDrivenColumnNames.length; i++) {
            if (dict.contains(ruleDrivenColumnNames[i])) {
                // special processing for Acronyms used by TPMG
                javaBeanColumnNameParts.add(ruleDrivenColumnNames[i]);
            } else {
                //  the first word may have been all caps but not in the dictionary. Just convert to lowercase.
                if (i == 0) {
                    columnNamePart = ruleDrivenColumnNames[0].toLowerCase()
                    //             println "columnNamePart @ 0: $columnNamePart"
                    javaBeanColumnNameParts.add(columnNamePart.toLowerCase());

                } else {
                    columnNamePart = ruleDrivenColumnNames[i]
                    javaBeanColumnName = columnNamePart.toLowerCase().capitalize()
                    //            println("$javaBeanColumnName: $javaBeanColumnName")
                    javaBeanColumnNameParts.add(javaBeanColumnName);
                }


            }
        }
            // println javaBeanColumnNameParts.join()
        javaBeanColumnNames.put(javaBeanColumnNameParts.join(), value);

    }

}

println "javaBeanColumnNames: $javaBeanColumnNames"
return javaBeanColumnNames;
