package com.teaching.mapper;

import com.teaching.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface AttendanceMapper {

    List<Attendance> selectByClassAndDate(@Param("classId") Long classId,
                                          @Param("attendanceDate") LocalDate attendanceDate);

    Attendance selectByStudentAndDate(@Param("studentId") Long studentId,
                                      @Param("attendanceDate") LocalDate attendanceDate);

    int insert(Attendance attendance);

    int update(Attendance attendance);

    int insertOrUpdate(Attendance attendance);

    int batchInsertPresent(@Param("classId") Long classId,
                           @Param("attendanceDate") LocalDate attendanceDate);

    Map<String, Object> selectStats(@Param("classId") Long classId,
                                    @Param("attendanceDate") LocalDate attendanceDate);

    Map<String, Object> selectDailyStats(@Param("attendanceDate") LocalDate attendanceDate);
}
