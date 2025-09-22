package ua.cn.stu.main;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ua.cn.stu.domain.Product;
import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseExplorer extends JFrame {

    //private static Connection connection = null;
    private static DefaultListModel<Product> productListModel;
    private static DriverManagerDataSource dataSource;

    private JPanel contentPane;
    private JList<Product> dataList;
    private JTextField txtFldName;
    private JTextField txtFldDescription;
    private JButton addProduct;
    private JScrollPane dataScrollPane;
    private JLabel lblName;
    private JLabel lblDescription;

    public DatabaseExplorer() {
        setContentPane(contentPane);
        setTitle("Database Explorer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        addProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductJDBCTemplate(dataSource, txtFldName.getText(), txtFldDescription.getText());
                List<Product> productList = getAllProductsJDBCTemplate(dataSource);
                productListModel.removeAllElements();
                for (Product product : productList) {
                    productListModel.addElement(product);
                }
                txtFldName.setText("");
                txtFldDescription.setText("");
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new DatabaseExplorer();

            Dotenv dotenv = Dotenv.load();

            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            dataSource = connectToDatabaseJDBCTemplate(url, user, password);

            List<Product> productList = getAllProductsJDBCTemplate(dataSource);
            productListModel.removeAllElements();
            for (Product product : productList) {
                productListModel.addElement(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection connectToDatabase(String url, String name, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, name, password);
    }

    private static List<Product> getAllProducts(Connection connection) throws SQLException {
        List<Product> productList = new ArrayList<Product>();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from product");
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            Product product = new Product();
            product.setId(result.getLong(1));
            product.setName(result.getString(2));
            product.setDescription(result.getString(3));
            productList.add(product);
        }
        preparedStatement.close();
        return productList;
    }

    private static void addProduct(Connection connection, String productName, String productDescription) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into product(product_name, product_description) values (?, ?)"
        );
        preparedStatement.setString(1, productName);
        preparedStatement.setString(2, productDescription);
        preparedStatement.execute();
        preparedStatement.close();
    }

    private static DriverManagerDataSource connectToDatabaseJDBCTemplate (String url, String name, String password) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(name);
        dataSource.setPassword(password);
        return dataSource;
    }

    private static List<Product> getAllProductsJDBCTemplate(DriverManagerDataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("select * from product", new ProductMapper());
    }

    private static void addProductJDBCTemplate(DriverManagerDataSource dataSource, String productName, String productDescription) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("insert into product(product_name, product_description) values (?, ?)", productName, productDescription);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setPreferredSize(new Dimension(600, 300));
        dataScrollPane = new JScrollPane();
        contentPane.add(dataScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        dataList = new JList<Product>();
        productListModel = new DefaultListModel<Product>();
        dataList.setModel(productListModel);
        dataScrollPane.setViewportView(dataList);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 1, new Insets(0, 10, 0, 10), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblName = new JLabel();
        lblName.setText("Product Name");
        panel1.add(lblName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldName = new JTextField();
        panel1.add(txtFldName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblDescription = new JLabel();
        lblDescription.setText("Product Description");
        panel1.add(lblDescription, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldDescription = new JTextField();
        panel1.add(txtFldDescription, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addProduct = new JButton();
        addProduct.setText("Add Product");
        panel1.add(addProduct, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
