package pack.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.CurRateDao;
import pack.transfer.api.DBManager;
import pack.transfer.model.CurRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurRateDaoImpl implements CurRateDao {
    private static final Logger log = LogManager.getLogger();
    private final DBManager db;

    public CurRateDaoImpl(DBManager db) {
        this.db = db;
    }

    @Override
    public void create(int id, String c2c, String rate) {
        try (Connection cn = db.getConnection()) {
            PreparedStatement stmt = cn.prepareStatement(db.getSql("insertCurRate"));
            stmt.setLong(1, id);
            stmt.setString(2, c2c);
            stmt.setString(3, rate);
            stmt.execute();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurRate> allCurRates() {
        List<CurRate> result = new ArrayList<>();
        try (Connection cn = db.getConnection()) {
            ResultSet rs = cn.prepareStatement(db.getSql("allCurRates")).executeQuery();
            while (rs.next()) {
                result.add(new CurRate(rs.getLong("id"), rs.getString("c2c"), rs.getString("rate")));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
