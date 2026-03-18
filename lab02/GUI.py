import tkinter as tk
from tkinter import ttk, messagebox
from abc import ABC, abstractmethod

class EmailSimpleNotification:
    def send(self, message):
        return f"Email: {message}"

class EmailUrgentNotification:
    def send(self, message):
        return f"Email [СРОЧНО]: {message}"

class EmailSpamNotification:
    def send(self, message):
        return f"Email [СПАМ]: {message}"

class SmsSimpleNotification:
    def send(self, message):
        return f"SMS: {message}"

class SmsUrgentNotification:
    def send(self, message):
        return f"SMS [СРОЧНО]: {message}"

class SmsSpamNotification:
    def send(self, message):
        return f"SMS [СПАМ]: {message}"

class TelegramSimpleNotification:
    def send(self, message):
        return f"Telegram: {message}"

class TelegramUrgentNotification:
    def send(self, message):
        return f"Telegram [СРОЧНО]: {message}"

class TelegramSpamNotification:
    def send(self, message):
        return f"Telegram [СПАМ]: {message}"

class MessageSender(ABC):
    @abstractmethod
    def send_message(self, text):
        pass

# Concrete Implementors
class EmailSender(MessageSender):
    def send_message(self, text):
        return f"Email: {text}"

class SmsSender(MessageSender):
    def send_message(self, text):
        return f"SMS: {text}"

class TelegramSender(MessageSender):
    def send_message(self, text):
        return f"Telegram: {text}"

class Notification(ABC):
    def __init__(self, sender: MessageSender):
        self._sender = sender

    @abstractmethod
    def send(self, message):
        pass

class SimpleNotification(Notification):
    def send(self, message):
        return self._sender.send_message(message)

class UrgentNotification(Notification):
    def send(self, message):
        return self._sender.send_message(f"[СРОЧНО] {message}")

class SpamNotification(Notification):
    def send(self, message):
        return self._sender.send_message(f"[СПАМ] {message}")


class NotificationApp:
    def __init__(self, root):
        self.root = root
        self.root.title("ТОПОВАЯ GUI")
        self.root.geometry("600x500")
        self.root.resizable(False, False)

        self.tab_control = ttk.Notebook(root)

        self.tab_naive = ttk.Frame(self.tab_control)
        self.tab_control.add(self.tab_naive, text="Без паттерна")
        self.setup_naive_tab()

        self.tab_bridge = ttk.Frame(self.tab_control)
        self.tab_control.add(self.tab_bridge, text="С паттерном")
        self.setup_bridge_tab()

        self.tab_control.pack(expand=1, fill="both")

        self.status = ttk.Label(root, text="Готов к работе", relief=tk.SUNKEN, anchor=tk.W)
        self.status.pack(side=tk.BOTTOM, fill=tk.X)

    def setup_naive_tab(self):
        ttk.Label(self.tab_naive, text="Сообщение:").grid(row=0, column=0, sticky=tk.W, padx=10, pady=5)
        self.naive_message = ttk.Entry(self.tab_naive, width=40)
        self.naive_message.grid(row=0, column=1, columnspan=2, sticky=tk.W, padx=10, pady=5)

        ttk.Label(self.tab_naive, text="Тип уведомления:").grid(row=1, column=0, sticky=tk.W, padx=10, pady=5)
        self.naive_type = ttk.Combobox(self.tab_naive, values=["Обычное", "Срочное", "Спам"], state="readonly")
        self.naive_type.grid(row=1, column=1, sticky=tk.W, padx=10, pady=5)
        self.naive_type.current(0)

        ttk.Label(self.tab_naive, text="Канал доставки:").grid(row=2, column=0, sticky=tk.W, padx=10, pady=5)
        self.naive_channel = ttk.Combobox(self.tab_naive, values=["Email", "SMS", "Telegram"], state="readonly")
        self.naive_channel.grid(row=2, column=1, sticky=tk.W, padx=10, pady=5)
        self.naive_channel.current(0)

        self.naive_btn = ttk.Button(self.tab_naive, text="Отправить", command=self.send_naive)
        self.naive_btn.grid(row=3, column=0, columnspan=2, pady=10)

        ttk.Label(self.tab_naive, text="История отправок:").grid(row=4, column=0, sticky=tk.W, padx=10, pady=5)
        self.naive_listbox = tk.Listbox(self.tab_naive, height=10, width=70)
        self.naive_listbox.grid(row=5, column=0, columnspan=3, padx=10, pady=5)

    def setup_bridge_tab(self):
        ttk.Label(self.tab_bridge, text="Сообщение:").grid(row=0, column=0, sticky=tk.W, padx=10, pady=5)
        self.bridge_message = ttk.Entry(self.tab_bridge, width=40)
        self.bridge_message.grid(row=0, column=1, columnspan=2, sticky=tk.W, padx=10, pady=5)

        ttk.Label(self.tab_bridge, text="Тип уведомления:").grid(row=1, column=0, sticky=tk.W, padx=10, pady=5)
        self.bridge_type = ttk.Combobox(self.tab_bridge, values=["Обычное", "Срочное", "Спам"], state="readonly")
        self.bridge_type.grid(row=1, column=1, sticky=tk.W, padx=10, pady=5)
        self.bridge_type.current(0)

        ttk.Label(self.tab_bridge, text="Канал доставки:").grid(row=2, column=0, sticky=tk.W, padx=10, pady=5)
        self.bridge_channel = ttk.Combobox(self.tab_bridge, values=["Email", "SMS", "Telegram"], state="readonly")
        self.bridge_channel.grid(row=2, column=1, sticky=tk.W, padx=10, pady=5)
        self.bridge_channel.current(0)

        self.bridge_btn = ttk.Button(self.tab_bridge, text="Отправить", command=self.send_bridge)
        self.bridge_btn.grid(row=3, column=0, columnspan=2, pady=10)

        ttk.Label(self.tab_bridge, text="История отправок:").grid(row=4, column=0, sticky=tk.W, padx=10, pady=5)
        self.bridge_listbox = tk.Listbox(self.tab_bridge, height=10, width=70)
        self.bridge_listbox.grid(row=5, column=0, columnspan=3, padx=10, pady=5)

    def send_naive(self):
        msg = self.naive_message.get().strip()
        if not msg:
            messagebox.showwarning("Пустое сообщение", "Введите текст сообщения")
            return

        notif_type = self.naive_type.get()
        channel = self.naive_channel.get()

        try:
            if channel == "Email":
                if notif_type == "Обычное":
                    obj = EmailSimpleNotification()
                elif notif_type == "Срочное":
                    obj = EmailUrgentNotification()
                else:
                    obj = EmailSpamNotification()
            elif channel == "SMS":
                if notif_type == "Обычное":
                    obj = SmsSimpleNotification()
                elif notif_type == "Срочное":
                    obj = SmsUrgentNotification()
                else:
                    obj = SmsSpamNotification()
            else: 
                if notif_type == "Обычное":
                    obj = TelegramSimpleNotification()
                elif notif_type == "Срочное":
                    obj = TelegramUrgentNotification()
                else:
                    obj = TelegramSpamNotification()
        except NameError:
            messagebox.showerror("Ошибка", "Класс для данной комбинации не найден (это проблема наивного подхода)")
            return

        result = obj.send(msg)
        self.naive_listbox.insert(tk.END, result)
        self.naive_message.delete(0, tk.END)
        self.status.config(text=f"Отправлено без паттерна: {result}")

    def send_bridge(self):
        msg = self.bridge_message.get().strip()
        if not msg:
            messagebox.showwarning("Пустое сообщение", "Введите текст сообщения")
            return

        notif_type = self.bridge_type.get()
        channel = self.bridge_channel.get()

        if channel == "Email":
            sender = EmailSender()
        elif channel == "SMS":
            sender = SmsSender()
        else:
            sender = TelegramSender()

        if notif_type == "Обычное":
            notif = SimpleNotification(sender)
        elif notif_type == "Срочное":
            notif = UrgentNotification(sender)
        else:
            notif = SpamNotification(sender)

        result = notif.send(msg)
        self.bridge_listbox.insert(tk.END, result)
        self.bridge_message.delete(0, tk.END)
        self.status.config(text=f"Отправлено с паттерном: {result}")

if __name__ == "__main__":
    root = tk.Tk()
    app = NotificationApp(root)
    root.mainloop()