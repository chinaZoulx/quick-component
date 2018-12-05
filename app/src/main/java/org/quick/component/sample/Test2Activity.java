package org.quick.component.sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quick.component.http.HttpService;
import org.quick.component.http.callback.OnRequestListener;

import java.io.IOException;

public class Test2Activity {

    private void test() {

        new HttpService.Builder("1232132132132132132121313").getWithJava(new OnRequestListener<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(@NotNull IOException e, boolean isNetworkError) {

            }

            @Override
            public void onResponse(@Nullable String value) {

            }

            @Override
            public void onEnd() {

            }
        });
    }

    private void prise() {

    }
//
//    /**
//     * 将json解析成java对象
//     *
//     * @return
//     */
//    public static <T> T parseFromJson(String json) {
//        try {
//            return (T) new Gson().fromJson(json, Object.class);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

    public static <T> T parseFromJson(String json) {

        try {
            TypeToken typeToken = new TypeToken<T>() {
            };
            return (T) new Gson().fromJson(json, typeToken.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
