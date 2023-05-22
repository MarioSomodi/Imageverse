package com.msomodi.imageverse.util

import java.util.regex.Pattern

fun String.isValidPassword() =
    Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#\$%^&+!=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{6,}" +               //at least 6 characters
            "$").matcher(this).matches()
