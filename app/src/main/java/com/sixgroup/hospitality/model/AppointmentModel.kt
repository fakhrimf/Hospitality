package com.sixgroup.hospitality.model

import java.util.Date

data class AppointmentModel(
    var idAppointment: String,
    var appointmentDate: Date,
    var complaint: String,
    var status: String,
    var dokter: String,
    var pasien: String
) {
    // getter dan setter sudah via property access kotlin
}
