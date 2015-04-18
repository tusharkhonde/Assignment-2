package assgn2;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class KafkaProducer extends Thread{
	
	
    public  void MessageProducer() throws UnknownHostException, ParseException{
        Properties properties = new Properties();
        properties.put("metadata.broker.list","54.149.84.25:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        ProducerConfig producerConfig = new ProducerConfig(properties);
        kafka.javaapi.producer.Producer<String,String> producer = new kafka.javaapi.producer.Producer<String, String>(producerConfig);
        
        String topic = "cmpe273-topic";
        int i = 0;
     
        MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB("tushardb");
        DBCollection mod = db.getCollection("moderator");
        DBCollection poll = db.getCollection("poll");
        
        Date date = new Date();
        DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String current_date= date_format.format(date);
	    Date n_date = date_format.parse(current_date);

	    DBCursor myCursor=poll.find();
		BasicDBObject dbObject = (BasicDBObject) myCursor.next();
		DBObject tObject = new BasicDBObject();
		String old_date = dbObject.getString("expired_at").toString();
		
		if( n_date.toString().compareTo(old_date) <=0 )
			 i = 1;
		
		if(i==1){
		
		while(myCursor.hasNext()){
		
	    //System.out.println(myCursor.next());
		tObject = myCursor.next();
		BasicDBList list = (BasicDBList) tObject.get("result");
        int firstOption = (Integer) list.get(0);
        int secondOption = (Integer) list.get(1);
        
        BasicDBList list1 = (BasicDBList) tObject.get("choice");
        String firstChoice= (String) list1.get(0);
        String secondChoice = (String) list1.get(1);
        
        String myModId = dbObject.get("mod_id").toString();
		//System.out.println(myModId);
        DBCursor myModCursor = mod.find(new BasicDBObject("id", myModId));
		DBObject dbModObject = myModCursor.next();
		String email = dbModObject.get("email").toString();
		
		String msg = email + ":009982986:Poll Result [" + firstChoice + "=" + firstOption + "," + secondChoice  + "=" + secondOption + "]";
        System.out.println(msg);
        
        KeyedMessage<String, String> message =new KeyedMessage<String, String>(topic,msg);
	    producer.send(message);
        //producer.close();
         
		 }
		}
		
//		else if(i==0){
//			System.out.println("<<<<<=>>>>>");
//		}
      client.close();  
    }

}