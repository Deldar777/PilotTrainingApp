package nl.shekho.videoplayer.models

enum class RoleType(val type: String) {
    SUPERADMIN("Super admin"),
    ADMIN("Admin"),
    INSTRUCTOR("Instructor"),
    STUDENT("Student"),
}