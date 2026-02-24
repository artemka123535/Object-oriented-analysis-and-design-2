using System;
using System.Collections.Generic;


public abstract class Message
{
    public DateTime Timestamp { get; protected set; }
    public string Status { get; set; }

    protected Message()
    {
        Timestamp = DateTime.Now;
        Status = "Sent";
    }
    
    public abstract void Display();
}

public class TextMessage : Message
{
    public override void Display()
    {
        Console.WriteLine("Отобразилось текстовое сообщение");
    }
}

public class VideoMessage : Message
{
    public override void Display()
    {
        Console.WriteLine("Отобразилось видео");
    }
}

public class VoiceMessage : Message
{
    public override void Display()
    {
        Console.WriteLine("Отобразилось голосовое сообщение");
    }
}

public class ImageMessage : Message
{
    public override void Display()
    {
        Console.WriteLine("Отобразилась картинка");
    }
}

public abstract class MessageCreator
{
    private List<Message> messages = new List<Message>();

    protected abstract Message CreateMessage();
    public void NewMessage()
    {
        Message msg = CreateMessage();
        messages.Add(msg);
        msg.Display();
    }
}

public class VideoMessageCreator : MessageCreator 
{
    protected override VideoMessage CreateMessage()
    {
        return new VideoMessage();
    }
}

public class VoiceMessageCreator : MessageCreator 
{
    protected override VoiceMessage CreateMessage()
    {
        return new VoiceMessage();
    }
}

public class ImageMessageCreator : MessageCreator 
{
    protected override ImageMessage CreateMessage()
    {
        return new ImageMessage();
    }
}

public class TextMessageCreator : MessageCreator 
{
    protected override TextMessage CreateMessage()
    {
        return new TextMessage();
    }
}

class Program
{
    static void Main()
    {
        Console.Write("Введите тип сообщения (text/video/voice/image): ");
        string type = Console.ReadLine()?.Trim().ToLower();

        MessageCreator messageCreator = type switch
        {
            "text"  => new TextMessageCreator(),
            "video" => new VideoMessageCreator(),
            "voice" => new VoiceMessageCreator(),
            "image" => new ImageMessageCreator(),
        };
        messageCreator.NewMessage();
    }
}