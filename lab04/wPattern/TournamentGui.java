import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TournamentGui extends JFrame {
    private PlayerRepository repository;
    private PlayerConverter converter;
    private JTable playerTable;
    private DefaultTableModel playerModel;
    private JTable matchTable;
    private DefaultTableModel matchModel;

    public TournamentGui(PlayerRepository repo, PlayerConverter conv) {
        this.repository = repo;
        this.converter = conv;

        setTitle("Управление турниром TSU");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Игроки", createPlayerPanel());
        tabs.addTab("Матчи", createMatchPanel());
        add(tabs);

        updatePlayerTable(null);
    }

    private void showEditPlayerDialog() {
        int row = playerTable.getSelectedRow();
        if (row == -1) return;
        long id = (long) playerTable.getValueAt(row, 0);
        String currentNick = (String) playerTable.getValueAt(row, 1);
        String currentTeam = (String) playerTable.getValueAt(row, 2);

        JTextField nickField = new JTextField(currentNick);
        JTextField teamField = new JTextField(currentTeam);
        Object[] message = {"Никнейм:", nickField, "Команда:", teamField};

        int option = JOptionPane.showConfirmDialog(null, message, "Редактировать", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            repository.updatePlayer(id, nickField.getText(), teamField.getText());
            updatePlayerTable(null);
        }
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Поиск");
        JButton addBtn = new JButton("Добавить игрока");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        toolbar.add(new JLabel("Никнейм:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        playerModel = new DefaultTableModel(new String[]{"ID", "Ник", "Команда", "Общий K/D"}, 0);
        playerTable = new JTable(playerModel);
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(playerTable), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> updatePlayerTable(searchField.getText()));
        addBtn.addActionListener(e -> showAddPlayerDialog());
        deleteBtn.addActionListener(e -> deleteSelectedPlayer());
        editBtn.addActionListener(e -> showEditPlayerDialog());

        return panel;
    }

    private JPanel createMatchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        matchModel = new DefaultTableModel(new String[]{"Дата", "Убийства", "Смерти", "K/D в матче"}, 0);
        matchTable = new JTable(matchModel);
        
        JButton viewMatchesBtn = new JButton("Посмотреть матчи выбранного игрока");
        JButton addMatchBtn = new JButton("Добавить матч игроку");
        
        JPanel toolbar = new JPanel();
        toolbar.add(viewMatchesBtn);
        toolbar.add(addMatchBtn);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(matchTable), BorderLayout.CENTER);

        viewMatchesBtn.addActionListener(e -> updateMatchTable());
        addMatchBtn.addActionListener(e -> showAddMatchDialog());

        return panel;
    }

    private void updatePlayerTable(String filter) {
        playerModel.setRowCount(0);
        List<PlayerEntity> players = (filter == null || filter.isEmpty()) 
            ? repository.getAllPlayers() 
            : repository.searchPlayers(filter);

        for (PlayerEntity p : players) {
            List<MatchEntity> matches = repository.getMatchesByPlayer(p.getId());
            PlayerDto dto = converter.convertToDto(p, matches);
            playerModel.addRow(new Object[]{p.getId(), dto.getNickname(), dto.getTeam(), dto.getOverallKd()});
        }
    }

    private void showAddPlayerDialog() {
        JTextField nick = new JTextField();
        JTextField team = new JTextField();
        Object[] message = {"Никнейм:", nick, "Команда:", team};

        int option = JOptionPane.showConfirmDialog(null, message, "Новый игрок", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            repository.savePlayer(nick.getText(), team.getText());
            updatePlayerTable(null);
        }
    }

    private void deleteSelectedPlayer() {
        int row = playerTable.getSelectedRow();
        if (row != -1) {
            long id = (long) playerTable.getValueAt(row, 0);
            repository.deletePlayer(id);
            updatePlayerTable(null);
        }
    }

    private void updateMatchTable() {
        int row = playerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите игрока во вкладке 'Игроки'");
            return;
        }
        long playerId = (long) playerTable.getValueAt(row, 0);
        matchModel.setRowCount(0);
        List<MatchEntity> matches = repository.getMatchesByPlayer(playerId);
        for (MatchEntity m : matches) {
            MatchDto dto = converter.convertToMatchDto(m);
            matchModel.addRow(new Object[]{dto.getDate(), dto.getKills(), dto.getDeaths(), dto.getMatchKd()});
        }
    }

    private void showAddMatchDialog() {
        int row = playerTable.getSelectedRow();
        if (row == -1) return;
        long playerId = (long) playerTable.getValueAt(row, 0);

        JTextField k = new JTextField();
        JTextField d = new JTextField();
        JTextField date = new JTextField("2026-04-27");
        Object[] message = {"Убийства:", k, "Смерти:", d, "Дата:", date};

        if (JOptionPane.showConfirmDialog(null, message, "Добавить матч", JOptionPane.OK_OPTION) == JOptionPane.OK_OPTION) {
            repository.saveMatch(playerId, Integer.parseInt(k.getText()), Integer.parseInt(d.getText()), date.getText());
            updatePlayerTable(null);
            updateMatchTable();
        }
    }

    public static void start(PlayerRepository repo, PlayerConverter conv) {
        EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            new TournamentGui(repo, conv).setVisible(true);
        });
    }
}