package com.mpp_poc;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MPP extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;
    HashMap<String, Object> hashMap = new HashMap<String, Object>();

    public MPP(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "MPP";
    }

    @ReactMethod
    public void initialiseMPP(ReadableMap readableMap) {
        Log.d("#initialiseMPP=>", String.valueOf(readableMap));
        hashMap = readableMap.toHashMap();
        if (this.reactContext.getCurrentActivity() != null) {
            this.reactContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FlutterEngine flutterEngine = new FlutterEngine(MPP.this.reactContext.getCurrentActivity());
                        flutterEngine.getDartExecutor()
                                .executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());

                        FlutterEngineCache.getInstance().put("100", flutterEngine);

                        MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor(), "mpp_with_ui_sdk");

                        channel.setMethodCallHandler(
                                new MethodChannel.MethodCallHandler() {
                                    @Override
                                    public void onMethodCall(@NonNull MethodCall call,
                                            @NonNull MethodChannel.Result result) {
                                        String url = call.argument("url");
                                        Log.d("#006=>", call.method);
                                        if (call.method.equals("sdkParam")) {
                                            result.success(hashMap);
                                        } else if (call.method.equals("sendStatusToNative")) {
                                            String text = call.argument("text");
                                            Log.d("#007=>", text);
                                            Log.d("#008=>", "Flutter View  has been dismissed");
                                        } else {
                                            result.notImplemented();
                                        }
                                    }

                                });

                        MPP.this.reactContext.getCurrentActivity().startActivity(FlutterActivity.withCachedEngine("100")
                                .build(MPP.this.reactContext.getCurrentActivity()));
                    } catch (Exception e) {
                        Log.d("#Exception=>", e.toString());
                    }
                }
            });
        }
    }
}
