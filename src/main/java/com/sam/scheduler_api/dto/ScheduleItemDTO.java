package com.sam.scheduler_api.dto;

import com.sam.scheduler_api.model.Section;

public record ScheduleItemDTO(
        String courseName,
        String professorName,
        String dayOfWeek,
        String timeOfDay,
        String room // We can hard-code this or add it to Section later
) {
    public static ScheduleItemDTO fromEntity(Section section) {
        return new ScheduleItemDTO(
                section.getCourse().getName(),
                section.getInstructor().getName(),
                section.getDayOfWeek(),
                section.getTimeOfDay(),
                "Room 100"
        );
    }
}
