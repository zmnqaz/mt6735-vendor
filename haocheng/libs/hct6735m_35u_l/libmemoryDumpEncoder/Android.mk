LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := libmemoryDumpEncoder
LOCAL_SRC_FILES := libmemoryDumpEncoder.so
LOCAL_SHARED_LIBRARIES := libstdc++
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_MODULE_SUFFIX := .so
include $(BUILD_PREBUILT)
