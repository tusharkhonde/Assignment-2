package assgn2;

public class newPoll {

	public newPoll() {
		// TODO Auto-generated constructor stub
	}
   private String id;
   private String question;
   private String started_at;
   private String expired_at;
  
   private String[] choice;
  
   public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getQuestion() {
	return question;
}
public void setQuestion(String question) {
	this.question = question;
}
public String getStarted_at() {
	return started_at;
}
public void setStarted_at(String started_at) {
	this.started_at = started_at;
}
public String getExpired_at() {
	return expired_at;
}
public void setExpired_at(String expired_at) {
	this.expired_at = expired_at;
}
public String[] getChoice() {
	return choice;
}
public void setChoice(String[] choice) {
	this.choice = choice;
}


public newPoll(String id, String question, String started_at, String expired_at,
		String[] choice) {
	super();
	this.id = id;
	this.question = question;
	this.started_at = started_at;
	this.expired_at = expired_at;
	this.choice = choice;
}


	
}
