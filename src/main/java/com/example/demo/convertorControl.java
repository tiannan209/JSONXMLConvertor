package com.example.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

@RestController
public class convertorControl {

    @RequestMapping(value = "/convertor", method = RequestMethod.GET)
    public String getXml(){
        //JSONArray jsonArray = extractSourceText("jsonExample.txt", "properties");
        //for(int i = 0; i < jsonArray.size(); i++){
        //    System.out.println(jsonArray.get(i).toString());
        //}
        //extractSourceText("jsonExample.txt", "properties");
        return getXmlSource(readTxt("jsonExample.txt"));
    }

    private String getXmlSource(String jsonStr){
        String xmlSource = null;

        try{
            //JSONArray jsonArray = JSONArray.fromObject(jsonStr);
            //System.out.println(jsonArray.size());
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            System.out.println(jsonArray.size());

            Iterator<JSONArray> iterator = jsonArray.iterator();
            JSONObject configuration = null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<configuration>\n");
            while(iterator.hasNext()){
                JSONObject temp = JSONObject.fromObject(iterator.next());
                System.out.println(temp.get("href"));
                System.out.println(temp.get("Config"));
                System.out.println(temp.get("properties"));
                configuration = JSONObject.fromObject(temp.get("properties"));
                System.out.println(configuration.size());
                Iterator<JSONObject.Entry<String, String>> configItr = configuration.entrySet().iterator();
                while(configItr.hasNext()){
                    JSONObject.Entry<String, String> property = configItr.next();
                    System.out.println("<name>" + property.getKey().trim() + "</name>");
                    System.out.println("<value>" + property.getValue().trim() + "</value>");
                    stringBuilder.append("   <property>\n");
                    stringBuilder.append("      <name>" + property.getKey().trim() + "</name>\n");
                    stringBuilder.append("      <value>" + property.getValue().trim() + "</value>\n");
                    stringBuilder.append("   </property>\n");
                }
            }
            stringBuilder.append("</configuration>");
            xmlSource = stringBuilder.toString();

            //jsonObject.getJSONArray("properties");
//            JsonFactory factory =  new JsonFactory();
//            ObjectMapper mapper = new ObjectMapper(factory);
//            JsonNode rootNode = mapper.readTree(jsonStr);
//
//            Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
//            while (fieldsIterator.hasNext()) {
//
//                Map.Entry<String,JsonNode> field = fieldsIterator.next();
//                System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
//            }
            //XMLSerializer xmlSerializer = new XMLSerializer();
            //xmlSource = xmlSerializer.write(configuration);


            /* print Json array test*/
        }catch(Exception ex){
            System.err.println("Error to generate xml format string: " + ex.getMessage());
        }

        return xmlSource;
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

    private void extractSourceText(String doc, String targetStr){
        BufferedReader bufferedReader;

        try{
            bufferedReader = new BufferedReader(new FileReader(doc));

            String strLine;
            while((strLine = bufferedReader.readLine()) != null){
                JSONObject jsonObject = JSONObject.fromObject(strLine);
                System.out.println(jsonObject.toString());
            }

        }catch(Exception ex){
            System.err.println("Fail to extract JSON array: " + ex.getMessage());
        }

        return;
    }
}
