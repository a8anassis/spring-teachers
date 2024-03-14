package gr.aueb.cf.springteacher5.repository;

import gr.aueb.cf.springteacher5.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByLastnameStartingWith(String lastname);
    Teacher findTeacherById(Long id);
}
