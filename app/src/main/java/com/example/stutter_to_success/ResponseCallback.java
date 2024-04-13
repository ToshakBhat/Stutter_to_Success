package com.example.stutter_to_success;

public interface ResponseCallback {
    void onResponse(String response);

    void onError(Throwable throwable);
}
