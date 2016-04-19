package com.myapps.quizify.quizifyclient.net.quizifyapp.net;

public interface APIObjectResponseListener<T, Z> {
    public void getResult(T error, Z result);
}
