import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ToDoApp extends JFrame {
    private JTextField taskField;
    private JButton addButton, deleteButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;

    private static final String JDBC_URL = "jdbc:mysql://localhost/todo_list";
    private static final String USERNAME = "db_user_name";
    private static final String PASSWORD = "db_password";

    public ToDoApp() {
        setTitle("To-Do List");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        taskField = new JTextField(20);
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        // Layout
        JPanel inputPanel = new JPanel();
        inputPanel.add(taskField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Event listeners
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());

        // Populate task list
        loadTasks();
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO tasks (task) VALUES (?)")) {
                statement.setString(1, task);
                statement.executeUpdate();
                listModel.addElement(task);
                taskField.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String task = taskList.getSelectedValue();
            listModel.remove(selectedIndex);
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM tasks WHERE task = ?")) {
                statement.setString(1, task);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTasks() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT task FROM tasks")) {
            while (resultSet.next()) {
                listModel.addElement(resultSet.getString("task"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ToDoApp app = new ToDoApp();
            app.setVisible(true);
        });
    }
}

