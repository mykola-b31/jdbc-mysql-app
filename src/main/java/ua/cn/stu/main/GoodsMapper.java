package ua.cn.stu.main;

import org.springframework.jdbc.core.RowMapper;
import ua.cn.stu.domain.Goods;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GoodsMapper implements RowMapper<Goods> {
    @Override
    public Goods mapRow(ResultSet rs, int rowNum) throws SQLException {
        Goods goods = new Goods();
        goods.setGoodsId(rs.getLong("goods_id"));
        goods.setGoodsName(rs.getString("goods_name"));
        goods.setGoodsPrice(rs.getBigDecimal("goods_price"));
        goods.setSupplierId(rs.getLong("supplier_id"));
        goods.setGoodsQuantity(rs.getInt("goods_quantity"));
        try {
            goods.setSupplierName(rs.getString("supplier_name"));
        } catch (SQLException e) {
            goods.setSupplierName("Unknown");
        }
        return goods;
    }
}
