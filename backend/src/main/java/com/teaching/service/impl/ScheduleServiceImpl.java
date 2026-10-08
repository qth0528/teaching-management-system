package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.Result;
import com.teaching.dto.BatchScheduleRequest;
import com.teaching.dto.ScheduleRequest;
import com.teaching.entity.Schedule;
import com.teaching.mapper.ClassMapper;
import com.teaching.mapper.ScheduleMapper;
import com.teaching.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final String[] DAY_NAMES = {"", "周一", "周二", "周三", "周四", "周五"};

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private ClassMapper classMapper;

    @Override
    public Result<Map<String, Object>> weekly(Long classId, String semester) {
        com.teaching.entity.Clazz clazz = classMapper.selectById(classId);
        if (clazz == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        List<Schedule> schedules = scheduleMapper.selectByClassAndSemester(classId, semester);

        Map<String, Object> data = new HashMap<>();
        data.put("classId", classId);
        data.put("className", clazz.getClassName());
        data.put("semester", semester);
        data.put("list", schedules);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> create(ScheduleRequest request) {
        checkConflict(request.getClassId(), request.getTeacherId(), request.getRoomNumber(),
                request.getDayOfWeek(), request.getPeriod(), request.getSemester(), null);

        Schedule schedule = buildSchedule(request);
        scheduleMapper.insert(schedule);

        Map<String, Object> data = new HashMap<>();
        data.put("schedule", schedule);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> update(Long id, ScheduleRequest request) {
        Schedule exist = scheduleMapper.selectById(id);
        if (exist == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "排课记录不存在");
        }

        checkConflict(request.getClassId(), request.getTeacherId(), request.getRoomNumber(),
                request.getDayOfWeek(), request.getPeriod(), request.getSemester(), id);

        exist.setCourseId(request.getCourseId());
        exist.setTeacherId(request.getTeacherId());
        exist.setRoomNumber(request.getRoomNumber());
        scheduleMapper.update(exist);

        Map<String, Object> data = new HashMap<>();
        data.put("schedule", exist);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> delete(Long id) {
        Schedule exist = scheduleMapper.selectById(id);
        if (exist == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "排课记录不存在");
        }
        scheduleMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> batch(BatchScheduleRequest request) {
        // 检查目标班级是否存在
        if (classMapper.selectById(request.getTargetClassId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "目标班级不存在");
        }

        // 查询源班级的排课
        List<Schedule> sourceSchedules = scheduleMapper.selectByClassSemesterDays(
                request.getSourceClassId(), request.getSemester(), request.getDayOfWeeks());

        if (sourceSchedules.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "源班级无排课记录");
        }

        // 构建目标排课列表，只检查班级和教室冲突（教师冲突由用户后续调整）
        List<Schedule> targetSchedules = new ArrayList<>();
        for (Schedule src : sourceSchedules) {
            checkBatchConflict(request.getTargetClassId(),
                    src.getDayOfWeek(), src.getPeriod(), request.getSemester());

            Schedule target = new Schedule();
            target.setClassId(request.getTargetClassId());
            target.setCourseId(src.getCourseId());
            target.setTeacherId(src.getTeacherId());
            target.setDayOfWeek(src.getDayOfWeek());
            target.setPeriod(src.getPeriod());
            target.setRoomNumber(src.getRoomNumber());
            target.setSemester(request.getSemester());
            targetSchedules.add(target);
        }

        if (!targetSchedules.isEmpty()) {
            scheduleMapper.batchInsert(targetSchedules);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("count", targetSchedules.size());
        return Result.success(data);
    }

    private Schedule buildSchedule(ScheduleRequest request) {
        Schedule schedule = new Schedule();
        schedule.setClassId(request.getClassId());
        schedule.setCourseId(request.getCourseId());
        schedule.setTeacherId(request.getTeacherId());
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setPeriod(request.getPeriod());
        schedule.setRoomNumber(request.getRoomNumber());
        schedule.setSemester(request.getSemester());
        return schedule;
    }

    private void checkBatchConflict(Long classId, Integer dayOfWeek, Integer period, String semester) {
        int classConflict = scheduleMapper.countClassConflict(classId, dayOfWeek, period, semester, null);
        if (classConflict > 0) {
            throw new BusinessException(ErrorCode.CONFLICT,
                    DAY_NAMES[dayOfWeek] + "第" + period + "节目标班级已有课程");
        }
    }

    private void checkConflict(Long classId, Long teacherId, String roomNumber,
                               Integer dayOfWeek, Integer period, String semester, Long excludeId) {
        // 1. 班级冲突
        int classConflict = scheduleMapper.countClassConflict(classId, dayOfWeek, period, semester, excludeId);
        if (classConflict > 0) {
            String msg = DAY_NAMES[dayOfWeek] + "第" + period + "节该班级已有课程";
            throw new BusinessException(ErrorCode.CONFLICT, msg);
        }

        // 2. 教师冲突
        int teacherConflict = scheduleMapper.countTeacherConflict(teacherId, dayOfWeek, period, semester, excludeId);
        if (teacherConflict > 0) {
            String msg = "该教师在" + DAY_NAMES[dayOfWeek] + "第" + period + "节已有课程";
            throw new BusinessException(ErrorCode.CONFLICT, msg);
        }

        // 3. 教室冲突（仅当指定了教室时检查）
        if (roomNumber != null && !roomNumber.isEmpty()) {
            int roomConflict = scheduleMapper.countRoomConflict(roomNumber, dayOfWeek, period, semester, excludeId);
            if (roomConflict > 0) {
                String msg = roomNumber + "在" + DAY_NAMES[dayOfWeek] + "第" + period + "节已被占用";
                throw new BusinessException(ErrorCode.CONFLICT, msg);
            }
        }
    }
}
