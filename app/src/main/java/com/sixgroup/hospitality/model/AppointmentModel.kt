package com.sixgroup.hospitality.model

data class AppointmentModel(
    var idAppointment: String? = "",
    var appointmentDate: String? = "",
    var complaint: String? = "",
    var status: String? = "",
    var dokter: String? = "",
    var pasien: String? = ""
) {
    // getter dan setter sudah via property access kotlin
}
