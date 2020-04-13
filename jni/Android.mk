LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := api_keys
LOCAL_SRC_FILES := api_keys.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)