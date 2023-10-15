package dev.wms.pwrapi.dao.forum;

import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {
    List<Review> getReviewsByTeacherId(Long teacherId, Pageable pageable);
}