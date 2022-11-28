package com.sixgroup.hospitality.model

import java.util.Date

data class AppointmentModel(
    private val idAppointment: String,
    private val appointmentDate: Date,
    private val complaint: String,
    private val status: String,
    private val dokter: DokterModel,
    private val pasien: PasienModel
) {
    // getter dan setter sudah via property access kotlin
}
