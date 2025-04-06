package com.osmosis.backend.mapper;

import com.osmosis.backend.entity.ClassHourRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassHourRecordMapper {
    
    /**
     * 获取学生的课时记录
     */
    @Select("SELECT chr.*, c.course_name as courseName, s.username as studentName, t.username as teacherName " +
            "FROM class_hour_records chr " +
            "JOIN courses c ON chr.course_id = c.id " +
            "JOIN users s ON chr.student_id = s.id " +
            "JOIN users t ON chr.teacher_id = t.id " +
            "WHERE chr.student_id = #{studentId} " +
            "ORDER BY chr.class_date DESC")
    List<ClassHourRecord> findRecordsByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * 获取学生在特定课程的课时记录
     */
    @Select("SELECT chr.*, c.course_name as courseName, s.username as studentName, t.username as teacherName " +
            "FROM class_hour_records chr " +
            "JOIN courses c ON chr.course_id = c.id " +
            "JOIN users s ON chr.student_id = s.id " +
            "JOIN users t ON chr.teacher_id = t.id " +
            "WHERE chr.student_id = #{studentId} AND chr.course_id = #{courseId} " +
            "ORDER BY chr.class_date DESC")
    List<ClassHourRecord> findRecordsByStudentIdAndCourseId(
            @Param("studentId") Integer studentId, 
            @Param("courseId") Integer courseId);
    
    /**
     * 插入新的课时记录
     */
    @Insert("INSERT INTO class_hour_records(student_id, course_id, class_date, start_time, end_time, " +
            "theme, content, status, learning_progress, homework, next_class_goal, teacher_id) " +
            "VALUES(#{studentId}, #{courseId}, #{classDate}, #{startTime}, #{endTime}, " +
            "#{theme}, #{content}, #{status}, #{learningProgress}, #{homework}, #{nextClassGoal}, #{teacherId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertClassHourRecord(ClassHourRecord record);
    
    /**
     * 更新课时记录
     */
    @Update("UPDATE class_hour_records SET " +
            "student_id = #{studentId}, course_id = #{courseId}, class_date = #{classDate}, " +
            "start_time = #{startTime}, end_time = #{endTime}, theme = #{theme}, " +
            "content = #{content}, status = #{status}, learning_progress = #{learningProgress}, " +
            "homework = #{homework}, next_class_goal = #{nextClassGoal}, teacher_id = #{teacherId} " +
            "WHERE id = #{id}")
    int updateClassHourRecord(ClassHourRecord record);
    
    /**
     * 删除课时记录
     */
    @Delete("DELETE FROM class_hour_records WHERE id = #{id}")
    int deleteClassHourRecord(@Param("id") Integer id);
    
    /**
     * 根据id查找课时记录
     */
    @Select("SELECT chr.*, c.course_name as courseName, s.username as studentName, t.username as teacherName " +
            "FROM class_hour_records chr " +
            "JOIN courses c ON chr.course_id = c.id " +
            "JOIN users s ON chr.student_id = s.id " +
            "JOIN users t ON chr.teacher_id = t.id " +
            "WHERE chr.id = #{id}")
    ClassHourRecord findRecordById(@Param("id") Integer id);
} 