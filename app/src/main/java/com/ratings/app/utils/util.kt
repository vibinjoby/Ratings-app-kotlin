package com.ratings.app.utils

import com.afollestad.vvalidator.field.FieldError

fun isErrors(errors: List<FieldError>): Boolean {
    println(errors)
    return errors.isNotEmpty()
}