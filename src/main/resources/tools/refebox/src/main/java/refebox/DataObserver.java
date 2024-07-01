package refebox;

import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Iterator;

import java.io.IOException;

public class DataObserver {
   /////////////////////// fields ///////////////////////////////
   String filePath;
   JSONArray states;
   Iterator<Object> iteratorState;

   /////////////////////// constructors /////////////////////////
   public DataObserver() {
      filePath = null;
   }

   public DataObserver(String initialFilePath) throws IOException {
      this.filePath = initialFilePath;
      // read the sting content
      String logData = new String(Files.readAllBytes(Paths.get(filePath)));

      System.out.println(logData.length());
      states = new JSONArray(logData);
      iteratorState = states.iterator();
   }

   /////////////////////// methods //////////////////////////////
   public void SetFilePath(String newFilePath) {
      filePath = newFilePath;
   }

   public JSONObject GetData() {
      try {

         JSONObject dataState  = new JSONObject();
         // read the sting content
         if(iteratorState.hasNext())
         {
            dataState = (JSONObject) iteratorState.next();  
         }

         return dataState;

      } catch (Exception e) {
         System.out.println(e);
         // the return JSONOBject is null when the read file path is not valid.
         return null;
      }
   }
}
