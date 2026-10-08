package com.teaching.mapper;

import com.teaching.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    List<Schedule> selectByClassAndSemester(@Param("classId") Long classId,
                                            @Param("semester") String semester);

    Schedule selectById(@Param("id") Long id);

    int countClassConflict(@Param("classId") Long classId,
                           @Param("dayOfWeek") Integer dayOfWeek,
                           @Param("period") Integer period,
                           @Param("semester") String semester,
                           @Param("excludeId") Long excludeId);

    int countTeacherConflict(@Param("teacherId") Long teacherId,
                             @Param("dayOfWeek") Integer dayOfWeek,
                             @Param("period") Integer period,
                             @Param("semester") String semester,
                             @Param("excludeId") Long excludeId);

    int countRoomConflict(@Param("roomNumber") String roomNumber,
                          @Param("dayOfWeek") Integer dayOfWeek,
                          @Param("period") Integer period,
                          @Param("semester") String semester,
                          @Param("excludeId") Long excludeId);

    int insert(Schedule schedule);

    int update(Schedule schedule);

    int deleteById(@Param("id") Long id);

    List<Schedule> selectByClassSemesterDays(@Param("classId") Long classId,
                                              @Param("semester") String semester,
                                              @Param("dayOfWeeks") List<Integer> dayOfWeeks);

    int batchInsert(@Param("list") List<Schedule> schedules);
}
