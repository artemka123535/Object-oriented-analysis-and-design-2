abstract public class Message()
{
    private DateTime timestamp;
    private string status;
    private string type;
    
    abstract public void display()
    {
        if (type == "text")
        {
            
        }

    }
}

abstract public class MessageCreator()
{
    private Message[] messages;

    abstract protected Message CreateMessage()
    {
        
    }
    public void NewMessage()
    {
        
    }
}

