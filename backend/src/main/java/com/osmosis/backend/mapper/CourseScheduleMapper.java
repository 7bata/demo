package com.osmosis.backend.mapper;

import com.osmosis.backend.entity.CourseSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseScheduleMapper {
    
    /**
     * 根据课程ID查询课程安排
     */
    @Select("SELECT cs.*, c.course_name as courseName, c.course_code as courseCode, u.username as teacherName " +
            "FROM course_schedules cs " +
            "JOIN courses c ON cs.course_id = c.id " +
            "LEFT JOIN users u ON c.teacher_id = u.id " +
            "WHERE cs.course_id = #{courseId}")
    List<CourseSchedule> findSchedulesByCourseId(@Param("courseId") Integer courseId);
    
    /**
     * 获取学生的所有课程安排
     */
    @Select("SELECT cs.*, c.course_name as courseName, c.course_code as courseCode, u.username as teacherName " +
            "FROM course_schedules cs " +
            "JOIN courses c ON cs.course_id = c.id " +
            "JOIN student_courses sc ON c.id = sc.course_id " +
            "LEFT JOIN users u ON c.teacher_id = u.id " +
            "WHERE sc.student_id = #{studentId} AND sc.status = 'enrolled'")
    List<CourseSchedule> findSchedulesByStudentId(@Param("studentId") Integer studentId);

    /**
     * 获取教师的所有课程安排
     */
    @Select("SELECT cs.*, c.course_name as courseName, c.course_code as courseCode " +
            "FROM course_schedules cs " +
            "JOIN courses c ON cs.course_id = c.id " +
            "WHERE c.teacher_id = #{teacherId}")
    List<CourseSchedule> findSchedulesByTeacherId(@Param("teacherId") Integer teacherId);
} 