package com.example.serenitycars

data class UserData(
    val id: String? = null,
    val fullName: String? = null,
    val mobileNumber: Long? = null,
    val email: String? = null,
    val password: String? = null
) {
    init {
        require(mobileNumber == null || mobileNumber >= 0 && mobileNumber.toString().length == 10) {
            "Mobile number must be exactly 10 digits"
        }
    }
}
