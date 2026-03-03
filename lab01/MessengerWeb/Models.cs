using System;

namespace MessengerWeb.Models
{
    public abstract class Message
    {
        public string Content { get; set; } // Имя файла или текст
        public DateTime Timestamp { get; } = DateTime.Now;
        public string? FileUrl { get; set; } // Путь к файлу на сервере (/uploads/...)

        public abstract string GetStyle();
        public abstract string GetIcon();
    }

    public class TextMessage : Message
    {
        public override string GetStyle() => "alert-primary";
        public override string GetIcon() => "💬";
    }

    public class ImageMessage : Message { public override string GetStyle() => "alert-warning"; public override string GetIcon() => "🖼️"; }
    public class VoiceMessage : Message { public override string GetStyle() => "alert-success"; public override string GetIcon() => "🎤"; }
    public class VideoMessage : Message { public override string GetStyle() => "alert-danger";  public override string GetIcon() => "📹"; }

    // --- CREATORS ---
    public abstract class MessageCreator
    {
        protected abstract Message CreateMessage();
        public Message HandleMessage(string content)
        {
            var msg = CreateMessage();
            msg.Content = content;
            return msg;
        }
    }

    public class TextCreator : MessageCreator { protected override Message CreateMessage() => new TextMessage(); }
    public class ImageCreator : MessageCreator { protected override Message CreateMessage() => new ImageMessage(); }
    public class VoiceCreator : MessageCreator { protected override Message CreateMessage() => new VoiceMessage(); }
    public class VideoCreator : MessageCreator { protected override Message CreateMessage() => new VideoMessage(); }
}