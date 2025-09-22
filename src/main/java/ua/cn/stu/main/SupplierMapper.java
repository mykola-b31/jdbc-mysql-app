package ua.cn.stu.main;

import org.springframework.jdbc.core.RowMapper;
import ua.cn.stu.domain.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierMapper implements RowMapper<Supplier> {

    @Override
    public Supplier mapRow(ResultSet rs, int rowNum) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(rs.getLong("supplier_id"));
        supplier.setSupplierName(rs.getString("supplier_name"));
        supplier.setSupplierContact(rs.getString("supplier_contact"));
        supplier.setSupplierAddress(rs.getString("supplier_address"));
        return supplier;
    }
}
