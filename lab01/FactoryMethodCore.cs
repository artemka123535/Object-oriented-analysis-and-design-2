using System;

namespace FactoryMethodMessenger
{
    // ---------- Перечисления ----------
    public enum MessageStatus
    {
        Sent,
        Delivered,
        Read
    }

    // ---------- Модели ----------
    public class User
    {
        public string Name { get; set; }
        public User(string name) => Name = name;
        public override string ToString() => Name;
    }

    public abstract class Message
    {
        public User Sender { get; }
        public DateTime Timestamp { get; }
        public MessageStatus Status { get; set; }

        protected Message(User sender)
        {
            Sender = sender;
            Timestamp = DateTime.Now;
            Status = MessageStatus.Sent;
        }

        public abstract string GetPreviewText();
        public abstract string GetIconEmoji();

        // Для отображения в ListBox
        public virtual string DisplayText => $"{GetIconEmoji()} {Sender.Name} [{Timestamp:HH:mm}] {GetPreviewText()}";
    }

    public class TextMessage : Message
    {
        public string Text { get; }

        public TextMessage(User sender, string text) : base(sender)
        {
            Text = text;
        }

        public override string GetPreviewText() =>
            Text.Length > 30 ? Text.Substring(0, 27) + "..." : Text;

        public override string GetIconEmoji() => "📝";
    }

    public class VoiceMessage : Message
    {
        public int DurationSeconds { get; }

        public VoiceMessage(User sender, int durationSeconds) : base(sender)
        {
            DurationSeconds = durationSeconds;
        }

        public override string GetPreviewText() =>
            $"🎤 Голосовое сообщение {DurationSeconds} сек";

        public override string GetIconEmoji() => "🎤";
    }

    public class ImageMessage : Message
    {
        public string FilePath { get; }
        public int Width { get; }
        public int Height { get; }

        public ImageMessage(User sender, string filePath, int width, int height) : base(sender)
        {
            FilePath = filePath;
            Width = width;
            Height = height;
        }

        public override string GetPreviewText() =>
            $"📷 Изображение {Width}x{Height}";

        public override string GetIconEmoji() => "📷";
    }

    public class VideoMessage : Message
    {
        public string FilePath { get; }
        public int DurationSeconds { get; }

        public VideoMessage(User sender, string filePath, int durationSeconds) : base(sender)
        {
            FilePath = filePath;
            DurationSeconds = durationSeconds;
        }

        public override string GetPreviewText() =>
            $"🎥 Видео {DurationSeconds} сек";

        public override string GetIconEmoji() => "🎥";
    }

    public class Chat
    {
        public User CurrentUser { get; }
        public User Contact { get; }
        public System.Collections.Generic.List<Message> Messages { get; } = new System.Collections.Generic.List<Message>();

        public Chat(User currentUser, User contact)
        {
            CurrentUser = currentUser;
            Contact = contact;
        }

        public void AddMessage(Message message) => Messages.Add(message);
    }

    // ---------- Фабрики ----------
    public abstract class MessageCreator
    {
        protected abstract Message CreateMessage(User sender, params object[] data);

        public Message NewMessage(User sender, params object[] data)
        {
            var message = CreateMessage(sender, data);
            // Общая логика (логирование и т.п.)
            System.Diagnostics.Debug.WriteLine($"Создано сообщение: {message.GetPreviewText()}");
            return message;
        }
    }

    public class TextMessageCreator : MessageCreator
    {
        protected override Message CreateMessage(User sender, object[] data)
        {
            string text = data[0] as string;
            return new TextMessage(sender, text);
        }
    }

    public class VoiceMessageCreator : MessageCreator
    {
        protected override Message CreateMessage(User sender, object[] data)
        {
            int duration = (int)data[0];
            return new VoiceMessage(sender, duration);
        }
    }

    public class ImageMessageCreator : MessageCreator
    {
        protected override Message CreateMessage(User sender, object[] data)
        {
            string path = (string)data[0];
            int width = (int)data[1];
            int height = (int)data[2];
            return new ImageMessage(sender, path, width, height);
        }
    }

    public class VideoMessageCreator : MessageCreator
    {
        protected override Message CreateMessage(User sender, object[] data)
        {
            string path = (string)data[0];
            int duration = (int)data[1];
            return new VideoMessage(sender, path, duration);
        }
    }
}