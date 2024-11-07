package com.movieflix.dto;

public record ChangePasswordRequest(String password, String repeatPassword) {
}
