package com.teaching.common;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> TEACHER_ID = new ThreadLocal<>();

    public static void set(Long userId, Long teacherId) {
        USER_ID.set(userId);
        TEACHER_ID.set(teacherId);
    }

    public static Long getCurrentUserId() {
        return USER_ID.get();
    }

    public static Long getCurrentTeacherId() {
        return TEACHER_ID.get();
    }

    public static void clear() {
        USER_ID.remove();
        TEACHER_ID.remove();
    }
}
