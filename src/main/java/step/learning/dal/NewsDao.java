package step.learning.dal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entity.News;
import step.learning.entity.User;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class NewsDao {

    private final DbService dbService;
    private final Logger logger;

    @Inject
    public NewsDao(DbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }

    public boolean installTable() {
        String sql = "CREATE TABLE IF NOT EXISTS News(" + "id         CHAR(36)     PRIMARY KEY DEFAULT( UUID() )," + "title      VARCHAR(256) NOT NULL," + "spoiler    VARCHAR(512) NOT NULL," + "text       TEXT         NOT NULL," + "image_url  VARCHAR(256) NOT NULL," + "created_dt DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP," + "deleted_dt DATETIME     NULL" + ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4";
        try (Statement statement = dbService.getConnection().createStatement()) {

            statement.executeUpdate(sql);
            ResultSet rs = statement.executeQuery("SHOW COLUMNS FROM News LIKE 'author_id'");
            if (!rs.next()) {
                statement.executeUpdate("ALTER TABLE News ADD COLUMN author_id CHAR(36) NOT NULL ");
            }

            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " -- " + sql);
        }
        return false;
    }

    public boolean restoreNews(String id) {
        String sql = "UPDATE News SET deleted_dt = NULL WHERE id=?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " -- " + sql);
        }
        return false;
    }

    public boolean deleteNews(String id) {
        String sql = "UPDATE News SET deleted_dt = CURRENT_TIMESTAMP WHERE id=?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " -- " + sql);
        }
        return false;
    }

    public News getById(String id) {
        String sql = "SELECT * FROM News WHERE id=?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, id);
            ResultSet res = prep.executeQuery();
            return res.next() ? News.FromResultSet(res) : null;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " -- " + sql);
        }
        return null;
    }

    public List<News> getAll() {
        return getAll(false);
    }

    public List<News> getAll(boolean withDeleted) {
        List<News> ret = new ArrayList<>();
        String sql = "SELECT * FROM News";
        if (!withDeleted) {
            sql += " WHERE deleted_dt IS NULL";
        }

        try (Statement statement = dbService.getConnection().createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            while (res.next()) {
                ret.add(News.FromResultSet(res));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " -- " + sql);
        }
        return ret;
    }

    public boolean addNews(News news) {
        // Валідація даних
        if (news.getTitle().length() < 10) {
            logger.log(Level.SEVERE, "Назва повинна містити не менше 10 символів");
            return false;
        }
        if (news.getSpoiler().split(" ").length < 10) {
            logger.log(Level.SEVERE, "Анонс повинен містити не менше 10 слів");
            return false;
        }
        if (news.getCreateDt() == null) {
            //            logger.log(Level.SEVERE, "Дата не може бути null");
            //            return false;
            news.setCreateDt(new Date());
        }
        if (news.getText().length() < 300) {
            logger.log(Level.SEVERE, "Контент повинен містити не менше 300 символів");
            return false;
        }
        if (!news.getImageUrl().matches(".*\\.(jpg|png|gif|bmp)$")) {
            logger.log(Level.SEVERE, "Файл-картинка повинен мати графічний тип (розширення файлу)");
            return false;
        }

        String sql = "INSERT INTO News(id, title, spoiler, text, image_url, created_dt, author_id)" + "VALUES( UUID(), ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, news.getTitle());
            prep.setString(2, news.getSpoiler());
            prep.setString(3, news.getText());
            prep.setString(4, news.getImageUrl());
            prep.setTimestamp(5, new Timestamp(news.getCreateDt().getTime()));
            prep.setString(6, news.getAuthor_id().toString());
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + "--" + sql);
        }
        return false;
    }

}
