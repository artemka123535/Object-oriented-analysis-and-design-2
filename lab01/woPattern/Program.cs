using System;
using System.Collections.Generic;

public class Message
{
    private DateTime timestamp;
    private string status;
    private string type;
    public Message(string type)
    {
        this.type = type;
    }
    
    public void display()
    {
        if (type == "text")
        {
            Console.WriteLine("Отобразилось текстовое сообщение");
        }

        if (type == "video")
        {
            Console.WriteLine("Отобразилось видео");
        }

        if (type == "image")
        {
            Console.WriteLine("Отобразилась картинка");
        }

        if (type == "voice")
        {
            Console.WriteLine("Отобразилось голосовое сообщение");
        }

    }
}

public class MessageCreator
{
    private List<Message> messages = new List<Message>();

    protected Message CreateMessage()
    {
        Console.Write("Введите тип сообщения (text/video/voice/image): ");
        string type = Console.ReadLine()?.Trim().ToLower();
        return new Message(type);
    }
    public void NewMessage()
    {
        Message msg = CreateMessage();
        messages.Add(msg);
        msg.display();
    }
}

class Program
{
    static void Main()
    {
        MessageCreator messageCreator = new MessageCreator();
        messageCreator.NewMessage();
    }
}