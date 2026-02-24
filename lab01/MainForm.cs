using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace FactoryMethodMessenger
{
    public class MainForm : Form
    {
        private User currentUser = new User("Я");
        private User contact = new User("Алиса");
        private Chat chat;

        private Dictionary<string, MessageCreator> factories = new Dictionary<string, MessageCreator>();

        private ListBox lstMessages;
        private ComboBox cmbMessageType;
        private Panel pnlInput;
        private Button btnSend;

        // Элементы для разных типов
        private TextBox txtText;
        private NumericUpDown nudDuration;
        private TextBox txtPath;
        private NumericUpDown nudWidth;
        private NumericUpDown nudHeight;

        public MainForm()
        {
            InitializeComponent();
            InitializeFactories();
            InitializeChat();
        }

        private void InitializeFactories()
        {
            factories["Текст"] = new TextMessageCreator();
            factories["Голос"] = new VoiceMessageCreator();
            factories["Изображение"] = new ImageMessageCreator();
            factories["Видео"] = new VideoMessageCreator();
        }

        private void InitializeChat()
        {
            chat = new Chat(currentUser, contact);
        }

        private void InitializeComponent()
        {
            this.Text = "Мессенджер (Factory Method)";
            this.Size = new System.Drawing.Size(800, 500);
            this.StartPosition = FormStartPosition.CenterScreen;

            // SplitContainer для разделения контактов и чата
            SplitContainer splitContainer = new SplitContainer
            {
                Dock = DockStyle.Fill,
                SplitterWidth = 2,
                Panel1MinSize = 200,
                Panel2MinSize = 400
            };

            // Левая панель - контакты
            ListBox lstContacts = new ListBox
            {
                Dock = DockStyle.Fill,
                Font = new System.Drawing.Font("Segoe UI", 12)
            };
            lstContacts.Items.Add(contact);
            lstContacts.DisplayMember = "Name";
            splitContainer.Panel1.Controls.Add(lstContacts);
            splitContainer.Panel1.Padding = new Padding(5);
            splitContainer.Panel1.BackColor = System.Drawing.Color.LightGray;

            // Правая панель - сообщения и ввод
            TableLayoutPanel rightPanel = new TableLayoutPanel
            {
                Dock = DockStyle.Fill,
                RowCount = 2,
                ColumnCount = 1
            };
            rightPanel.RowStyles.Add(new RowStyle(SizeType.Percent, 70));
            rightPanel.RowStyles.Add(new RowStyle(SizeType.Percent, 30));

            // Список сообщений
            lstMessages = new ListBox
            {
                Dock = DockStyle.Fill,
                Font = new System.Drawing.Font("Segoe UI", 11)
            };
            // Отображаем свойство DisplayText
            lstMessages.DisplayMember = "DisplayText";
            rightPanel.Controls.Add(lstMessages, 0, 0);

            // Нижняя панель (ввод)
            Panel bottomPanel = new Panel { Dock = DockStyle.Fill };
            SetupBottomPanel(bottomPanel);
            rightPanel.Controls.Add(bottomPanel, 0, 1);

            splitContainer.Panel2.Controls.Add(rightPanel);

            this.Controls.Add(splitContainer);
        }

        private void SetupBottomPanel(Panel panel)
        {
            panel.Height = 150;
            panel.BackColor = System.Drawing.Color.WhiteSmoke;

            // Комбобокс выбора типа
            cmbMessageType = new ComboBox
            {
                Location = new System.Drawing.Point(10, 10),
                Width = 150,
                DropDownStyle = ComboBoxStyle.DropDownList
            };
            cmbMessageType.Items.AddRange(new string[] { "Текст", "Голос", "Изображение", "Видео" });
            cmbMessageType.SelectedIndex = 0;
            cmbMessageType.SelectedIndexChanged += (s, e) => UpdateInputPanel();
            panel.Controls.Add(cmbMessageType);

            // Панель для динамических полей ввода
            pnlInput = new Panel
            {
                Location = new System.Drawing.Point(10, 40),
                Width = panel.Width - 90,
                Height = 60,
                AutoScroll = true
            };
            panel.Controls.Add(pnlInput);

            // Кнопка отправки
            btnSend = new Button
            {
                Text = "Отправить",
                Location = new System.Drawing.Point(panel.Width - 80, 10),
                Width = 70,
                Height = 30
            };
            btnSend.Click += BtnSend_Click;
            panel.Controls.Add(btnSend);

            // Первоначальное заполнение панели ввода
            UpdateInputPanel();
        }

        private void UpdateInputPanel()
        {
            pnlInput.Controls.Clear();
            string selectedType = cmbMessageType.SelectedItem.ToString();

            switch (selectedType)
            {
                case "Текст":
                    txtText = new TextBox { Width = 200, Location = new System.Drawing.Point(0, 0) };
                    pnlInput.Controls.Add(new Label { Text = "Текст:", Location = new System.Drawing.Point(0, 3), AutoSize = true });
                    pnlInput.Controls.Add(txtText);
                    break;

                case "Голос":
                    nudDuration = new NumericUpDown { Minimum = 1, Maximum = 300, Value = 10, Location = new System.Drawing.Point(100, 0), Width = 60 };
                    pnlInput.Controls.Add(new Label { Text = "Длительность (сек):", Location = new System.Drawing.Point(0, 3), AutoSize = true });
                    pnlInput.Controls.Add(nudDuration);
                    break;

                case "Изображение":
                    txtPath = new TextBox { Width = 150, Location = new System.Drawing.Point(70, 0) };
                    nudWidth = new NumericUpDown { Minimum = 1, Maximum = 5000, Value = 800, Location = new System.Drawing.Point(70, 30), Width = 60 };
                    nudHeight = new NumericUpDown { Minimum = 1, Maximum = 5000, Value = 600, Location = new System.Drawing.Point(200, 30), Width = 60 };
                    pnlInput.Controls.Add(new Label { Text = "Путь:", Location = new System.Drawing.Point(0, 3), AutoSize = true });
                    pnlInput.Controls.Add(txtPath);
                    pnlInput.Controls.Add(new Label { Text = "Ширина:", Location = new System.Drawing.Point(0, 33), AutoSize = true });
                    pnlInput.Controls.Add(nudWidth);
                    pnlInput.Controls.Add(new Label { Text = "Высота:", Location = new System.Drawing.Point(130, 33), AutoSize = true });
                    pnlInput.Controls.Add(nudHeight);
                    break;

                case "Видео":
                    txtPath = new TextBox { Width = 150, Location = new System.Drawing.Point(70, 0) };
                    nudDuration = new NumericUpDown { Minimum = 1, Maximum = 3600, Value = 60, Location = new System.Drawing.Point(70, 30), Width = 60 };
                    pnlInput.Controls.Add(new Label { Text = "Путь:", Location = new System.Drawing.Point(0, 3), AutoSize = true });
                    pnlInput.Controls.Add(txtPath);
                    pnlInput.Controls.Add(new Label { Text = "Длительность (сек):", Location = new System.Drawing.Point(0, 33), AutoSize = true });
                    pnlInput.Controls.Add(nudDuration);
                    break;
            }
        }

        private void BtnSend_Click(object sender, EventArgs e)
        {
            string selectedType = cmbMessageType.SelectedItem.ToString();
            if (!factories.ContainsKey(selectedType)) return;

            MessageCreator creator = factories[selectedType];
            Message message = null;

            try
            {
                switch (selectedType)
                {
                    case "Текст":
                        if (string.IsNullOrWhiteSpace(txtText.Text)) return;
                        message = creator.NewMessage(currentUser, txtText.Text);
                        break;
                    case "Голос":
                        message = creator.NewMessage(currentUser, (int)nudDuration.Value);
                        break;
                    case "Изображение":
                        if (string.IsNullOrWhiteSpace(txtPath.Text)) return;
                        message = creator.NewMessage(currentUser, txtPath.Text, (int)nudWidth.Value, (int)nudHeight.Value);
                        break;
                    case "Видео":
                        if (string.IsNullOrWhiteSpace(txtPath.Text)) return;
                        message = creator.NewMessage(currentUser, txtPath.Text, (int)nudDuration.Value);
                        break;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка: {ex.Message}");
                return;
            }

            if (message != null)
            {
                chat.AddMessage(message);
                lstMessages.Items.Add(message);
                // Очистка полей
                txtText?.Clear();
                if (txtPath != null) txtPath.Clear();
                // Прокрутка вниз
                lstMessages.TopIndex = lstMessages.Items.Count - 1;
            }
        }

        [STAThread]
        public static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm());
        }
    }
}