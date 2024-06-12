package domain.course.dto


data class CourseResponse (
    val title: String,
    val tutor: String,
    val category: Category,
    val description: String,
    val lectures: List<Lecture>,
    val reviews: List<Leview>,
    val isBookMarked: Boolean,
    val isSubcribed: Boolean
)