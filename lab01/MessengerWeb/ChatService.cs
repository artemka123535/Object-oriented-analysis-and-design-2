using MessengerWeb.Models;
using System.Collections.Generic;

public class ChatService
{
    // Список хранится здесь, а не на странице
    public List<Message> Messages { get; } = new List<Message>();

    public void AddMessage(Message msg)
    {
        Messages.Add(msg);
    }
}