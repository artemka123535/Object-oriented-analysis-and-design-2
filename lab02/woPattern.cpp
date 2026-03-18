#include <iostream>
#include <string>

using namespace std;

class EmailSimpleNotification {
public:
    void send(const string& message) const {
        cout << "Email: " << message << endl;
    }
};

class EmailUrgentNotification {
public:
    void send(const string& message) const {
        cout << "Email [СРОЧНО]: " << message << endl;
    }
};

class EmailSpamNotification {
public:
    void send(const string& message) const {
        cout << "Email [СПАМ]: " << message << endl;
    }
};

class SmsSimpleNotification {
public:
    void send(const string& message) const {
        cout << "SMS: " << message << endl;
    }
};

class SmsUrgentNotification {
public:
    void send(const string& message) const {
        cout << "SMS [СРОЧНО]: " << message << endl;
    }
};

class SmsSpamNotification {
public:
    void send(const string& message) const {
        cout << "SMS [СПАМ]: " << message << endl;
    }
};

class TelegramSimpleNotification {
public:
    void send(const string& message) const {
        cout << "Telegram: " << message << endl;
    }
};

class TelegramUrgentNotification {
public:
    void send(const string& message) const {
        cout << "Telegram [СРОЧНО]: " << message << endl;
    }
};

class TelegramSpamNotification {
public:
    void send(const string& message) const {
        cout << "Telegram [СПАМ]: " << message << endl;
    }
};

int main() {
    EmailSimpleNotification notif1;
    notif1.send("Здравствуйте, Радмир Ренатович");

    SmsUrgentNotification notif2;
    notif2.send("ВОЗЬМИ ТЕЛЕФОН ДЕТКА");

    TelegramSpamNotification notif3;
    notif3.send("УРА! Вы выиграли миллион рублей, отпраьте нам 1000 и мы скинем вам его!");
}