import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TournamentGui extends JFrame {

    private PlayerRepository repository;
    private JTable table;
    private DefaultTableModel tableModel;

    public TournamentGui(PlayerRepository repo) {
        this.repository = repo;

        setTitle("Киберспортивный Турнир (Без паттерна)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Таблица лидеров", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        String[] columns = {"Никнейм", "Коэффициент K/D"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton btnAdd = new JButton("Добавить игрока");
        JButton btnClear = new JButton("Очистить базу");
        JButton btnRefresh = new JButton("Обновить данные");

        btnAdd.addActionListener(e -> addSamplePlayer());
        btnClear.addActionListener(e -> clearData());
        btnRefresh.addActionListener(e -> updateTable());

        buttonPanel.add(btnAdd); buttonPanel.add(btnClear); buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);

        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        
        List<PlayerEntity> players = repository.getAllPlayers();

        for (PlayerEntity p : players) {
            Object[] row = { p.getNickname(), p.getKdForDisplay() };
            tableModel.addRow(row);
        }
    }

    private void addSamplePlayer() {
        repository.save(new PlayerEntity(null, "NoPattern_Noobik", (int)(Math.random()*10), (int)(Math.random()*50)+1));
        updateTable();
    }

    private void clearData() {
        repository.delete();
        updateTable();
    }

    public static void start(PlayerRepository repo) {
        EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            new TournamentGui(repo).setVisible(true);
        });
    }
}