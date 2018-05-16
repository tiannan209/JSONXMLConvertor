package com.example.demo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

@RestController
public class convertorControl {

    @RequestMapping(value = "/convertor", method = RequestMethod.GET)
    public String getXml(){
        String jsonStr = readTxt("jsonExample.txt");
        //saveXML(xmlSource);
        return getXmlStr(jsonStr);
    }

    private String getXmlStr(String jsonStr){
        String xmlStr = null;

        try{
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            System.out.println(jsonArray.size());

            Iterator iterator = jsonArray.iterator();
            /* configuration object is used to store property list */
            JSONObject configuration = null;
            //StringBuilder stringBuilder = new StringBuilder();
            //stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            //stringBuilder.append("<configuration>\n")
            while(iterator.hasNext()){
                JSONObject temp = JSONObject.fromObject(iterator.next());
                System.out.println(temp.get("href"));
                System.out.println(temp.get("Config"));
                System.out.println(temp.get("properties"));
                configuration = JSONObject.fromObject(temp.get("properties"));
            }
            xmlStr = getXmlStr(configuration);
            saveXML(xmlStr);
        }catch(Exception ex){
            System.err.println("Error to generate xml format string: " + ex.getMessage());
        }

        return xmlStr;
    }

    private String getXmlStr(JSONObject propertiesJson){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        stringBuilder.append("<configuration>\n");
        Iterator<JSONObject.Entry<String, String>> configItr = propertiesJson.entrySet().iterator();
        while(configItr.hasNext()){
            JSONObject.Entry<String, String> property = configItr.next();
            System.out.println("<name>" + property.getKey().trim() + "</name>");
            System.out.println("<value>" + property.getValue().trim() + "</value>");
            stringBuilder.append("   <property>\n");
            stringBuilder.append("      <name>").append(property.getKey().trim()).append("</name>\n");
            stringBuilder.append("      <value>").append(property.getValue().trim()).append("</value>\n");
            stringBuilder.append("   </property>\n");
        }
        stringBuilder.append("</configuration>");
        return stringBuilder.toString();
    }

    private void saveXML(String xmlSource) {
        try{
            FileWriter fw = new FileWriter("F:/configurations.xml");
            fw.write(xmlSource);
            fw.close();
        }catch(IOException ex){
            System.err.println("Fail to create a XML file: " + ex.getMessage());
        }
    }

    private String readTxt(String doc){
        BufferedReader bufferedReader;
        //StringReader stringReader = null;
        String content = null;

        try{
            bufferedReader = new BufferedReader(new FileReader(doc));
            StringBuilder stringBuilder = new StringBuilder();

            String strLine;
            while((strLine = bufferedReader.readLine()) != null){
                stringBuilder.append(strLine);
            }

            content = stringBuilder.toString();
            //stringReader = new StringReader(stringBuilder.toString());

        }catch(Exception ex){
            System.err.println("Fail to read content from text file: " + ex.getMessage());
        }
        //System.out.println(content);

        return content;
    }
}
