#include <iostream>
#include <memory>
#include <string>

using namespace std;

class ISender {
public:
    virtual ~ISender() = default;
    virtual void sendMessage(const string& text) const = 0;
};

class EmailSender : public ISender {
public:
    void sendMessage(const string& text) const override {
        cout << "Email: " << text << endl;
    }
};

class SmsSender : public ISender {
public:
    void sendMessage(const string& text) const override {
        cout << "SMS: " << text << endl;
    }
};

class TelegramSender : public ISender {
public:
    void sendMessage(const string& text) const override {
        cout << "Telegram: " << text << endl;
    }
};

class Notification {
protected:
    shared_ptr<ISender> sender_;

public:
    Notification(shared_ptr<ISender> sender) : sender_(move(sender)) {}

    virtual ~Notification() = default;
    virtual void send(const string& message) const = 0;
};

class SimpleNotification : public Notification {
public:
    using Notification::Notification;
    void send(const string& message) const override {
        sender_->sendMessage(message);
    }
};

class UrgentNotification : public Notification {
public:
    using Notification::Notification;
    void send(const string& message) const override {
        sender_->sendMessage("[СРОЧНО] " + message);
    }
};

class SpamNotification : public Notification {
public:
    using Notification::Notification;
    void send(const string& message) const override {
        sender_->sendMessage("[СПАМ] " + message);
    }
};

int main() {
    SimpleNotification simpleEmail(make_shared<EmailSender>());
    simpleEmail.send("Здравствуйте, Радмир Ренатович");

    UrgentNotification urgentSms(make_shared<SmsSender>());
    urgentSms.send("ВОЗЬМИ ТЕЛЕФОН ДЕТКА");

    SpamNotification spamTelegram(make_shared<TelegramSender>());
    spamTelegram.send("УРА! Вы выиграли миллион рублей, отпраьте нам 1000 и мы скинем вам его!");
}