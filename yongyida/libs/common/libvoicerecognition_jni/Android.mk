LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := libvoicerecognition_jni
LOCAL_SRC_FILES_32 := arm/libvoicerecognition_jni.so
LOCAL_SHARED_LIBRARIES := libandroid_runtime libbinder libmedia libmedia_jni libnativehelper libstdc++ libui libvoicerecognition
LOCAL_MULTILIB := 32
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_MODULE_SUFFIX := .so
include $(BUILD_PREBUILT)
