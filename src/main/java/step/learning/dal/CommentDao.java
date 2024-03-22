package step.learning.dal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entity.Comment;
import step.learning.entity.News;
import step.learning.services.db.DbService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CommentDao {

    private final DbService dbService;
    private final Logger logger;

    @Inject
    public CommentDao(DbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }

    public boolean addComment (Comment comment) {
        String sql = "INSERT INTO Comments (id, user_id, news_id, `text`) "
        +"VALUES( UUID(), ?, ?, ?)";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString(1, comment.getUserId().toString());
            prep.setString(2, comment.getNewsId().toString());
            prep.setString(3, comment.getText());
            prep.executeUpdate();
            return true;
        }
        catch(SQLException ex) {
            logger.log(Level. SEVERE, ex.getMessage()+" "+ sql);
        }
        return false;
    }

    public List<Comment> getNewsComment(News news){
        return getNewsComment(news.getId().toString());
    }

    private List<Comment> getNewsComment(String newsId) {
        List<Comment> ret = new ArrayList<>();
        String sql = "SELECT * FROM Comments c JOIN users u ON u.id = c.user_id WHERE news_id=?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)){
            prep.setString(1,newsId);
            ResultSet resultSet = prep.executeQuery();
            while (resultSet.next()){
                ret.add(new Comment(resultSet));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + "--" + sql);
        }
        return ret;
    }
}
