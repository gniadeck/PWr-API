package dev.wms.pwrapi.dao.forum;

import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.utils.forum.rowMappers.ReviewRowMapper;
import dev.wms.pwrapi.utils.forum.rowMappers.ReviewWithTeacherRowMapper;
import dev.wms.pwrapi.utils.forum.rowMappers.TeacherRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class ForumDAOImpl implements ForumDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private TeacherRowMapper teacherRowMapper;
    private ReviewRowMapper reviewRowMapper;
    private ReviewWithTeacherRowMapper reviewWithTeacherRowMapper;

    @Autowired
    public ForumDAOImpl(DataSource dataSource, TeacherRowMapper teacherRowMapper,
                        ReviewRowMapper reviewRowMapper, ReviewWithTeacherRowMapper reviewWithTeacherRowMapper){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.teacherRowMapper = teacherRowMapper;
        this.reviewRowMapper = reviewRowMapper;
        this.reviewWithTeacherRowMapper = reviewWithTeacherRowMapper;
    }

    @Override
    public int getNumberOfTeachers(){
        String query = "SELECT COUNT(*) FROM teacher";
        return jdbcTemplate.queryForObject(query, Map.of(), Integer.class);
    }

    @Override
    public int getNumberOfReviews(){
        String query = "SELECT COUNT(*) FROM review";
        return jdbcTemplate.queryForObject(query, Map.of(), Integer.class);
    }

    @Override
    public String getLastRefreshDate() {
        String query = "SELECT refresh_date FROM refresh_data ORDER BY refresh_date DESC LIMIT 1";
        return jdbcTemplate.queryForObject(query, Map.of(), String.class);
    }

    @Override
    public List<Teacher> fetchAllTeachers(){
        String query = "SELECT * FROM teacher";
        return jdbcTemplate.query(query, teacherRowMapper);
    }

    @Override
    public List<Teacher> fetchTeachersLimited(int limit){
        String query = "SELECT * FROM teacher LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("limit", limit), teacherRowMapper);
    }

    @Override
    public List<Review> fetchAllReviews(){
        String query = "SELECT * FROM review";
        return jdbcTemplate.query(query, reviewRowMapper);
    }

    @Override
    public List<Review> fetchReviewsLimited(int limit){
        String query = "SELECT * FROM review LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("limit", limit), reviewRowMapper);
    }

    @Override
    public List<Review> fetchTeacherReviewsById(int teacherId){
        String query = "SELECT * FROM review WHERE teacher_id = :tId";
        return jdbcTemplate.query(query, Map.of("tId", teacherId), reviewRowMapper);
    }

    @Override
    public List<Review> fetchTeacherReviewsByFullName(String firstName, String lastName){
        String query = "SELECT * FROM review WHERE teacher_id = (SELECT teacher_id FROM teacher WHERE full_name LIKE :fn AND full_name LIKE :ln)";
        return jdbcTemplate.query(query, Map.of("fn","%" + firstName + "%", "ln", "%" + lastName + "%"), reviewRowMapper);
    }

    @Override
    public List<Review> fetchTeacherReviewsByIdLimited(int teacherId, int limit){
        String query = "SELECT * FROM review WHERE teacher_id = :tId LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("tId", teacherId, "limit", limit), reviewRowMapper);
    }

    @Override
    public List<Review> fetchTeacherReviewsByFullNameLimited(String firstName, String lastName, int limit){
        String query = "SELECT * FROM review WHERE teacher_id = (SELECT teacher_id FROM teacher WHERE full_name LIKE :fn AND full_name LIKE :ln) LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("fn", "%" + firstName + "%", "ln", "%" + lastName + "%", "limit", limit), reviewRowMapper);
    }

    @Override
    public List<Review> fetchRecentTeacherReviewsByFullNameLimited(String firstName, String lastName, int limit){
        String query = "SELECT * FROM review WHERE teacher_id = (SELECT teacher_id FROM teacher WHERE full_name LIKE :fn AND full_name LIKE :ln) " +
                "ORDER BY post_date DESC LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("fn", "%" + firstName + "%", "ln", "%" + lastName + "%", "limit", limit), reviewRowMapper);
    }

    @Override
    public List<Review> fetchOldestTeacherReviewsByFullNameLimited(String firstName, String lastName, int limit){
        String query = "SELECT * FROM review WHERE teacher_id = (SELECT teacher_id FROM teacher WHERE full_name LIKE :fn AND full_name LIKE :ln) " +
                "ORDER BY post_date ASC LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("fn", "%" + firstName + "%", "ln","%" + lastName + "%", "limit", limit), reviewRowMapper);
    }

    @Override
    public List<Review> fetchRecentTeacherReviewsByIdLimited(int teacherId, int limit){
        String query = "SELECT * FROM review WHERE teacher_id = :tId ORDER BY post_date DESC LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("tId", teacherId,"limit", limit), reviewRowMapper);
    }

    @Override
    public List<Review> fetchOldestTeacherReviewsByIdLimited(int teacherId, int limit){
        String query = "SELECT * FROM review WHERE teacher_id = :tId ORDER BY post_date ASC LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("tId", teacherId, "limit", limit), reviewRowMapper);
    }

    @Override
    public Teacher findTeacherById(int teacherId){
        String query = "SELECT * FROM teacher WHERE teacher_id = :id";
        return jdbcTemplate.queryForObject(query, Map.of("id", teacherId), teacherRowMapper);
    }

    @Override
    public Teacher findTeacherByName(String firstName, String lastName){
        String query = "SELECT * FROM teacher WHERE full_name LIKE :fn AND full_name LIKE :ln";
        return jdbcTemplate.queryForObject(query, Map.of("fn", "%" + firstName + "%", "ln", "%" + lastName + "%"), teacherRowMapper);
    }

    @Override
    public Review findReviewById(int reviewId) {
        String query = "SELECT * FROM review JOIN teacher ON review.teacher_id = teacher.teacher_id WHERE review.review_id = :id";
        return jdbcTemplate.queryForObject(query, Map.of("id", reviewId), reviewWithTeacherRowMapper);
    }

    @Override
    public List<Teacher> fetchBestRatedTeachersLimited(int limit){
        String query = "SELECT * FROM teacher ORDER BY average_rating DESC LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("limit", limit), teacherRowMapper);
    }

    @Override
    public List<Teacher> fetchWorstOrBestTeachersByCategoryWithReviewsLimited(String category, int teacherLimit, int reviewLimit, boolean isBest){
        String queryTeacher = isBest ? "SELECT * FROM teacher WHERE category = :cat ORDER BY average_rating DESC LIMIT :tLimit"
                : "SELECT * FROM teacher WHERE category = :cat ORDER BY average_rating ASC LIMIT :tLimit";
        String queryReview = "SELECT * FROM review WHERE teacher_id = :tId LIMIT :rLimit";
        List<Teacher> teachers = jdbcTemplate.query(queryTeacher, Map.of("tLimit", teacherLimit, "cat", category), teacherRowMapper);
        teachers.forEach(teacher -> {
            jdbcTemplate.query(queryReview, Map.of("tId", teacher.getId(), "rLimit", reviewLimit), reviewRowMapper)
                    .forEach(review -> teacher.addReview(review));
        });
        return teachers;
    }

    @Override
    public List<Teacher> fetchWorstRatedTeachersLimited(int limit){
        String query = "SELECT * FROM teacher ORDER BY average_rating ASC LIMIT :limit";
        return jdbcTemplate.query(query, Map.of("limit", limit), teacherRowMapper);
    }

    @Override
    public Teacher fetchTeacherByIdWithReviews(int teacherId){
        String queryTeacher = "SELECT * FROM teacher WHERE teacher_id = :tId";
        String queryReviews = "SELECT * FROM review WHERE teacher_id = :tId";
        Map<String, Integer> map = Map.of("tId", teacherId);
        Teacher teacher = jdbcTemplate.queryForObject(queryTeacher, map, teacherRowMapper);
        jdbcTemplate.query(queryReviews, map, reviewRowMapper).forEach(review -> teacher.addReview(review));
        return teacher;
    }

    @Override
    public Teacher fetchTeacherByIdWithLimitedReviews(int teacherId, int limit){
        String queryTeacher = "SELECT * FROM teacher WHERE teacher_id = :tId";
        String queryReviews = "SELECT * FROM review WHERE teacher_id = :tId LIMIT :limit";
        Teacher teacher = jdbcTemplate.queryForObject(queryTeacher, Map.of("tId", teacherId), teacherRowMapper);
        jdbcTemplate.query(queryReviews, Map.of("tId", teacherId, "limit", limit), reviewRowMapper).forEach(review -> teacher.addReview(review));
        return teacher;
    }

    @Override
    public Teacher fetchTeacherByFullNameWithReviews(String firstName, String lastName){
        String queryTeacher = "SELECT * FROM teacher WHERE full_name LIKE :fn AND full_name LIKE :ln";
        //String queryTeacher = "SELECT * FROM teacher WHERE first_name = :fn AND last_name = :ln";
        String queryReviews = "SELECT * FROM review WHERE teacher_id = :tId";
        Teacher teacher = jdbcTemplate.queryForObject(queryTeacher, Map.of("fn", "%" + firstName + "%", "ln", "%" + lastName + "%"), teacherRowMapper);
        jdbcTemplate.query(queryReviews, Map.of("tId", teacher.getId()), reviewRowMapper).forEach(review -> teacher.addReview(review));
        return teacher;
    }

    @Override
    public Teacher fetchTeacherByFullNameWithLimitedReviews(String firstName, String lastName, int limit){
        String queryTeacher = "SELECT * FROM teacher WHERE full_name LIKE :fn AND full_name LIKE :ln";
        //        String queryTeacher = "SELECT * FROM teacher WHERE first_name = :fn AND last_name = :ln";
        String queryReviews = "SELECT * FROM review WHERE teacher_id = :tId LIMIT :limit";
        Teacher teacher = jdbcTemplate.queryForObject(queryTeacher, Map.of("fn", "%" + firstName + "%", "ln", "%" + lastName + "%"), teacherRowMapper);
        jdbcTemplate.query(queryReviews, Map.of("tId", teacher.getId(), "limit", limit), reviewRowMapper).forEach(review -> teacher.addReview(review));
        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByCategory(String category) {
        String query = "SELECT * FROM teacher WHERE category = :cat";
        List<Teacher> teachers = jdbcTemplate.query(query, Map.of("cat", category), teacherRowMapper);
        return teachers;
    }

    @Override
    public List<Teacher> getTeachersRankedByCategory(String category, boolean isAscending) {
        String query = isAscending ? "SELECT * FROM teacher WHERE category = :cat ORDER BY average_rating ASC"
                : "SELECT * FROM teacher WHERE category = :cat ORDER BY average_rating DESC";

        List<Teacher> teachers = jdbcTemplate.query(query, Map.of("cat", category), teacherRowMapper);
        return teachers;
    }

    @Override
    public List<Teacher> getTeachersRankedByCategoryLimited(String category, int limit, boolean isAscending) {
        String query = isAscending ? "SELECT * FROM teacher WHERE category = :cat ORDER BY average_rating ASC LIMIT :limit"
                : "SELECT * FROM teacher WHERE category = :cat ORDER BY average_rating DESC LIMIT :limit";
        List<Teacher> teachers = jdbcTemplate.query(query, Map.of("cat", category, "limit", limit), teacherRowMapper);
        return teachers;
    }

}
