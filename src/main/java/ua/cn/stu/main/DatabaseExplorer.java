package ua.cn.stu.main;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ua.cn.stu.domain.Goods;
import io.github.cdimascio.dotenv.Dotenv;
import ua.cn.stu.domain.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class DatabaseExplorer extends JFrame {

    private static DefaultListModel<Supplier> supplierListModel;
    private static DefaultListModel<Goods> goodsListModel;

    private static DriverManagerDataSource dataSource;

    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JList<Supplier> supplierList;
    private JList<Goods> goodsList;

    private JTextField txtFldSupplierName;
    private JTextField txtFldSupplierContact;
    private JTextField txtFldSupplierAddress;
    private JButton addSupplierButton;

    private JTextField txtFldGoodsName;
    private JTextField txtFldGoodsPrice;
    private JTextField txtFldGoodsQuantity;
    private JComboBox<Supplier> supplierComboBox;
    private JButton addGoodsButton;

    public DatabaseExplorer() {
        setContentPane(contentPane);
        setTitle("Database Explorer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        refreshData();
        setVisible(true);
        addSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        addGoodsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGoods();
            }
        });
    }

    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();

            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            dataSource = connectToDatabaseJDBCTemplate(url, user, password);

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new DatabaseExplorer();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Database connection error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static DriverManagerDataSource connectToDatabaseJDBCTemplate(String url, String name, String password) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(name);
        dataSource.setPassword(password);
        return dataSource;
    }

    private static List<Supplier> getAllSuppliers() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("select * from supplier", new SupplierMapper());
    }

    private static void addSupplierToDB(String name, String contact, String address) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("insert into supplier(supplier_name, supplier_contact, supplier_address) values (?, ?, ?)",
                name, contact, address);
    }

    private static List<Goods> getAllGoods() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String query = "select g.*, s.supplier_name from goods g " +
                        "left join supplier s on g.supplier_id = s.supplier_id";
        return jdbcTemplate.query(query, new GoodsMapper());
    }

    private static void addGoodsToDB(String name, BigDecimal price, Long supplierId, Integer quantity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("insert into goods(goods_name, goods_price, supplier_id, goods_quantity) values (?, ?, ?, ?)",
                name, price, supplierId, quantity);
    }

    private void addSupplier() {
        try {
            String name = txtFldSupplierName.getText();
            String contact = txtFldSupplierContact.getText();
            String address = txtFldSupplierAddress.getText();

            if (name.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(contentPane,
                        "Fill in required fields",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addSupplierToDB(name, contact, address);

            txtFldSupplierName.setText("");
            txtFldSupplierContact.setText("");
            txtFldSupplierAddress.setText("");

            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentPane,
                    "Error adding supplier: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addGoods() {
        try {
            String name = txtFldGoodsName.getText();
            String priceStr = txtFldGoodsPrice.getText();
            String quantityStr = txtFldGoodsQuantity.getText();
            Supplier selectedSupplier = (Supplier) supplierComboBox.getSelectedItem();
            if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || selectedSupplier == null) {
                JOptionPane.showMessageDialog(contentPane,
                        "Fill in all fields",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal price = new BigDecimal(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            if (price.compareTo(BigDecimal.ZERO) <= 0 || quantity < 0) {
                JOptionPane.showMessageDialog(contentPane,
                        "Price must be greater then 0, quantity must be at least 0",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addGoodsToDB(name, price, selectedSupplier.getSupplierId(), quantity);

            txtFldGoodsName.setText("");
            txtFldGoodsPrice.setText("");
            txtFldGoodsQuantity.setText("");
            supplierComboBox.setSelectedIndex(0);

            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentPane,
                    "Error adding goods: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        List<Supplier> suppliers = getAllSuppliers();
        supplierListModel.removeAllElements();
        for (Supplier supplier : suppliers) {
            supplierListModel.addElement(supplier);
        }

        if (supplierComboBox != null) {
            supplierComboBox.removeAllItems();
            for (Supplier supplier : suppliers) {
                supplierComboBox.addItem(supplier);
            }
        }

        List<Goods> listGoods = getAllGoods();
        goodsListModel.removeAllElements();
        for (Goods goods : listGoods) {
            goodsListModel.addElement(goods);
        }
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
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setPreferredSize(new Dimension(600, 300));
        tabbedPane1 = new JTabbedPane();
        contentPane.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Supplier", panel1);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        supplierList = new JList<Supplier>();
        supplierListModel = new DefaultListModel<Supplier>();
        supplierList.setModel(supplierListModel);
        scrollPane1.setViewportView(supplierList);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 7), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Supplier Name");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldSupplierName = new JTextField();
        panel2.add(txtFldSupplierName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Supplier Contact");
        panel2.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldSupplierContact = new JTextField();
        txtFldSupplierContact.setText("");
        panel2.add(txtFldSupplierContact, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Supplier Address");
        panel2.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldSupplierAddress = new JTextField();
        panel2.add(txtFldSupplierAddress, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addSupplierButton = new JButton();
        addSupplierButton.setText("Add Supplier");
        panel2.add(addSupplierButton, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Goods", panel3);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        goodsList = new JList<Goods>();
        goodsListModel = new DefaultListModel<Goods>();
        goodsList.setModel(goodsListModel);
        scrollPane2.setViewportView(goodsList);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(9, 1, new Insets(0, 0, 0, 7), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Goods Name");
        panel4.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldGoodsName = new JTextField();
        panel4.add(txtFldGoodsName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Goods Price");
        panel4.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldGoodsPrice = new JTextField();
        panel4.add(txtFldGoodsPrice, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Goods Quantity");
        panel4.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFldGoodsQuantity = new JTextField();
        panel4.add(txtFldGoodsQuantity, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Goods Supplier ID");
        panel4.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        supplierComboBox = new JComboBox<Supplier>();
        panel4.add(supplierComboBox, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addGoodsButton = new JButton();
        addGoodsButton.setText("Add Goods");
        panel4.add(addGoodsButton, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
