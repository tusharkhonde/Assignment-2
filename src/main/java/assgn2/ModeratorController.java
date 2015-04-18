package assgn2;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import assgn2.KafkaProducer;

@Controller
@RestController
public class ModeratorController {

	  @Scheduled(fixedRate = 500000)
       public void Moderator() throws UnknownHostException, ParseException{
	   KafkaProducer kfp = new KafkaProducer();
       kfp.MessageProducer();
   }
	/* Moderator */
	
/* Create a new Moderator */
	
	@RequestMapping(value="/moderators", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
   public List<DBObject> createModerator(@Valid @RequestBody Moderator moderator) throws UnknownHostException{
       
				
		Date date = new Date();
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String created_at= date_format.format(date);
		
		String moderator_id=null; 	
	    List<DBObject> myList =null;
           try{		
	      
        	MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
   			MongoClient client = new MongoClient(uri);
   			DB db = client.getDB("tushardb");
	        DBCollection mod = db.getCollection("moderator");
	        
//	        BasicDBObject query = new BasicDBObject();
//	        BasicDBObject fields = new BasicDBObject("id",1).append("_id", 0);
//	        String dbObj = mod.findOne(query,fields).get("id").toString(); 
	      
	        DBCursor myCursor=mod.find().sort(new BasicDBObject("id",-1)).limit(1);
	        BasicDBObject dbObject = (BasicDBObject)myCursor.next();
	        String modid = dbObject.getString("id");
	      
	        long mod_id = Long.valueOf(modid, 16).longValue(); 
			mod_id=mod_id+1;
			moderator_id = Long.toHexString(mod_id);
		   
		    BasicDBObject doc = new BasicDBObject("id", moderator_id).
		            append("name", moderator.getName()).
		            append("email", moderator.getEmail()).
		            append("password", moderator.getPassword()).
		            append("created_at", created_at);
		        
		    mod.insert(doc);
		    
		    BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("id", moderator_id);
			BasicDBObject hide_IdQuery = new BasicDBObject();
			hide_IdQuery.put("_id", 0);
	        
		    DBCursor viewCursor=mod.find(whereQuery,hide_IdQuery); 
	        myList =viewCursor.toArray();
			myCursor.close();	
			
           }catch(Exception e){
      	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     	  } 
		
         return myList;
       	}
    
	/* View a Moderator */

	@RequestMapping(value="/moderators/{moderator_id}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	 public List<DBObject>  viewModerator  (@PathVariable("moderator_id") String moderator_id){
		
		
		List<DBObject> myList = null;
		
		try{		
			
		    MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
	        DBCollection mod = db.getCollection("moderator");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("id", moderator_id);
			BasicDBObject hide_IdQuery = new BasicDBObject();
			hide_IdQuery.put("_id", 0);
	        
		    DBCursor viewCursor=mod.find(whereQuery,hide_IdQuery); 
	        myList =viewCursor.toArray();
			viewCursor.close();	
         
		}catch(Exception e){
      	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     	  }
	     
	   return myList;  
	}
	

	/* Update Moderator */

	@RequestMapping(value="/moderators/{moderator_id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	 public List<DBObject> updateModerator(@Valid @RequestBody Moderator moderator, @Valid @PathVariable("moderator_id") String moderator_id){
		
		List<DBObject> myList = new ArrayList<DBObject>();
		try{		
			MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
			DBCollection mod = db.getCollection("moderator");
	        
	        DBCursor myCursor=mod.find(new BasicDBObject("id",moderator_id)); 
	        DBObject oneDetails = myCursor.next();  
			oneDetails.put("email", moderator.getEmail());
			oneDetails.put("password", moderator.getPassword());
			
			mod.update(new BasicDBObject("id",moderator_id), oneDetails);
			myCursor.close();	
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("id", moderator_id);
			BasicDBObject hide_IdQuery = new BasicDBObject();
			hide_IdQuery.put("_id", 0);
	        
		    DBCursor viewCursor=mod.find(whereQuery,hide_IdQuery); 
	        myList =viewCursor.toArray();
			viewCursor.close();	
         
			
		}catch(Exception e){
      	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     	  }
	   return myList;
	}
	
	/*************************************************************************************/
	/* Poll */

     /* Create Poll */
	
	@RequestMapping(value="/moderators/{moderator_id}/polls", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public List<DBObject> createPoll(@Valid @RequestBody Poll poll, @Valid @PathVariable("moderator_id") String moderator_id) 
 {
        List<DBObject> myList = null; 
        String pollid_no=null;
        
		try{		
			MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
	        DBCollection pol = db.getCollection("poll");
	        
			BasicDBObject query = new BasicDBObject();
	        BasicDBObject fields = new BasicDBObject("id",1);
	        //String dbObj = pol.findOne(query,fields).get("id").toString(); 
	        //System.out.println(dbObj);
			
	        DBCursor myCursor=pol.find(query,fields).sort(new BasicDBObject("_id",-1)).limit(1);
	        BasicDBObject dbObject = (BasicDBObject)myCursor.next();
	        String pollid = dbObject.getString("id").toString();
	        
	        long poll_id = Long.valueOf(pollid, 16).longValue(); 
			poll_id=poll_id+1;
			pollid_no = Long.toHexString(poll_id);
			
			
			BasicDBObject moddoc = new BasicDBObject("mod_id", moderator_id).
					append("id", pollid_no).
					append("question", poll.getQuestion()).
		            append("started_at", poll.getStarted_at()).
		            append("expired_at", poll.getExpired_at()).
		            append("choice", poll.getChoice()).
			        append("result",poll.getResults());
			pol.insert(moddoc);
	       
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("id", moderator_id);
			whereQuery.put("id", pollid_no);
			BasicDBObject hide_IdQuery = new BasicDBObject();
			hide_IdQuery.put("_id", 0);
	        hide_IdQuery.put("mod_id", 0);
	        hide_IdQuery.put("result", 0);
		    DBCursor viewCursor=pol.find(whereQuery,hide_IdQuery); 
	        myList =viewCursor.toArray();
	        viewCursor.close();		     	      		 

		}catch(Exception e){
      	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     	}
		
		return myList;
	}
	
	

	/* View Poll without results*/
	
	@RequestMapping(value="/polls/{poll_id}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody  
	public List<DBObject> viewPoll(@PathVariable("poll_id") String poll_id){
		
		List<DBObject> myList = null;
		try{		
			MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
	        DBCollection pol = db.getCollection("poll");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("id", poll_id);
			BasicDBObject hide_IdQuery = new BasicDBObject();
			hide_IdQuery.put("_id", 0);
	        hide_IdQuery.put("mod_id", 0);
	        hide_IdQuery.put("result", 0);
		    DBCursor viewCursor=pol.find(whereQuery,hide_IdQuery); 
	        myList =viewCursor.toArray();
	        
	    }catch(Exception e){
     	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    	}
		
		return myList;
	
	}
	
	
	/* View Poll with Results */
	
	@RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody  
	public List<DBObject> viewPollresults(@PathVariable("moderator_id") String moderator_id,@PathVariable("poll_id") String poll_id){
		List<DBObject> myList = null;
		try{		
			MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
	        DBCollection pol = db.getCollection("poll");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
	        whereQuery.put("id", moderator_id);
	        whereQuery.put("id", poll_id);
			BasicDBObject hide_IdQuery = new BasicDBObject();
			hide_IdQuery.put("_id", 0);
	        hide_IdQuery.put("mod_id", 0);
	        DBCursor viewCursor=pol.find(whereQuery,hide_IdQuery); 
	        myList =viewCursor.toArray();
	        
	    }catch(Exception e){
     	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    	}
		
		return myList;
		
			
	}
	
	
	/* List all Polls created by a moderator */
		
	@RequestMapping(value="/moderators/{moderator_id}/polls", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody  
	public List<DBObject> newPoll (@PathVariable("moderator_id") String moderator_id){
		          	 	      		
		List<DBObject> myList = null;
		try{		
			MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
	        DBCollection pol = db.getCollection("poll");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
	        whereQuery.put("mod_id", moderator_id);
	        BasicDBObject hide_IdQuery = new BasicDBObject();
			hide_IdQuery.put("_id", 0);
	        hide_IdQuery.put("mod_id", 0);
	        DBCursor viewCursor=pol.find(whereQuery,hide_IdQuery); 
	        myList =viewCursor.toArray();
	        
	    }catch(Exception e){
     	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    	}
		
		return myList;	
		
}

   /* Delete Polls created by a Moderator*/
	
	
	@RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	   public void deletePoll(@PathVariable("moderator_id") String moderator_id,@PathVariable("poll_id") String poll_id){

		try{		
			MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
	        DBCollection pol = db.getCollection("poll");
	        
	        
	        BasicDBObject whereQuery = new BasicDBObject();
	        whereQuery.put("id", poll_id);
	        pol.remove(whereQuery); 
	      
	        
	    }catch(Exception e){
     	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    	}

	}
	
	
   /*  Vote for a Poll */
	
	
	@RequestMapping(value="/polls/{poll_id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	   public void votePoll(@Valid @PathVariable("poll_id") String poll_id, @Valid @RequestParam(value="choice") int choice_index){
         
		try{		
			MongoClientURI uri =new  MongoClientURI("mongodb://tushardbuser:tushardbpass@ds045021.mongolab.com:45021/tushardb");
			MongoClient client = new MongoClient(uri);
			DB db = client.getDB("tushardb");
	        DBCollection pol = db.getCollection("poll");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
	        whereQuery.put("id", poll_id);
	        DBCursor viewCursor=pol.find(whereQuery); 
	        BasicDBList list = (BasicDBList) viewCursor.next().get("result");
	        int firstOption = (int) list.get(0);
	        int secondOption = (int) list.get(1);
	        
	        List<Integer> li = new ArrayList<Integer>(); 
	        
	        if(choice_index==0){
	        	  firstOption++;
	    	      li.add(0,firstOption);
	    	      li.add(1, secondOption);
	        }
	        else if(choice_index==1){
	        	  secondOption++;
	        	  li.add(0, firstOption);
	        	  li.add(1,secondOption);
	        }
	      	
	      		DBCursor myCursor=pol.find(new BasicDBObject("id",poll_id)); 
	        DBObject oneDetails = myCursor.next();  
			oneDetails.put("result", li);
			pol.update(new BasicDBObject("id",poll_id), oneDetails);
			myCursor.close();		     	      		 
			
	        
	    }catch(Exception e){
     	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    	}
		
		
	}  
	
}	


