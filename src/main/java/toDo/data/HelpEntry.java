package toDo.data;

public class HelpEntry 
{
	private String topic, text;
	public HelpEntry(String topic, String text)
	{
		this.topic = topic;
		this.text = text;
		
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String subTopic) {
		this.topic = subTopic;
	}
	public String getText() {
		return text;
	}
	public void setText(String subTopicText) {
		this.text = subTopicText;
	}
}
