package com.ratings.app.helper

import com.afollestad.vvalidator.field.FieldError

fun isErrors(errors: List<FieldError>): Boolean {
    return errors.isNotEmpty()
}